package im.vector.patient

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.coroutineScope
import com.bumptech.glide.Glide
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.SimpleFragmentActivity
import im.vector.activity.SimpleFragmentActivityListener
import im.vector.activity.VectorMemberDetailsActivity
import im.vector.home.BaseCommunicateHomeFragment
import im.vector.patient.PatientTagActivity.Companion.FILE_LOCATION_EXTRA
import im.vector.patient.PatientTagActivity.Companion.ROOM_MEDIA_MESSAGE_EXTRA
import kotlinx.android.synthetic.main.fragment_patient_tag.*
import kotlinx.android.synthetic.main.item_patient.*
import kotlinx.android.synthetic.main.layout_designation.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.matrix.androidsdk.core.Log


class PatientTagFragment : BaseCommunicateHomeFragment(), PatientClickListener {
    private var LOG_TAG = "PatientTagFragment"
    private lateinit var viewModel: PatientTagViewModel
    private var simpleFragmentActivityListener: SimpleFragmentActivityListener? = null
    private lateinit var patientAdapter: PatientAdapter

    override fun getLayoutResId(): Int = R.layout.fragment_patient_tag

    companion object {
        const val PATIENT_EXTRA = "PATIENT_EXTRA"
        fun newInstance(bundle: Bundle): PatientTagFragment {
            val f = PatientTagFragment()
            f.arguments = bundle
            return f
        }
    }

    //Filter is not part of this view anymore
    override fun onFilter(pattern: String?, listener: OnFilterListener?) {
        TODO("Not yet implemented")
    }

    //Filter is not part of this view anymore
    override fun onResetFilter() {
        TODO("Not yet implemented")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is SimpleFragmentActivity) {
            simpleFragmentActivityListener = activity as SimpleFragmentActivity?
        }

        viewModel = ViewModelProviders.of(this).get(PatientTagViewModel::class.java)
        viewModel.initSession(mSession)
        username.text = mSession.myUser.displayname

        viewModel.fileLocation = arguments?.getString(FILE_LOCATION_EXTRA)
        viewModel.mediaMessageArray = arguments?.getParcelableArrayList(ROOM_MEDIA_MESSAGE_EXTRA)
        Glide.with(this).load(if (viewModel.fileLocation == null) viewModel.mediaMessageArray?.get(0)?.uri else viewModel.fileLocation).into(imageView)

        patientsRecyclerView.setHasFixedSize(true)
        patientAdapter = PatientAdapter(this)
        patientsRecyclerView.adapter = patientAdapter

        subscribeUI()
        viewModel.getPatientData()

        cross.setOnClickListener {
            viewModel.removeSelectedPatient()
        }

        cancelButton.setOnClickListener {
            requireActivity().finish()
        }

        searchEditText.addTextChangedListener(
                DebouncingQueryTextListener(
                        lifecycle
                ) { newText ->
                    newText?.let {
                        viewModel.filterPatient(it)
                    }
                })
        saveButton.setOnClickListener {
            if(viewModel.selectedPatient.value == null) {
                AlertDialog.Builder(requireContext())
                        .setTitle(R.string.dialog_title_confirmation)
                        .setMessage(getString(R.string.start_new_chat_prompt_msg))
                        .setPositiveButton(R.string.ok) { _, _ ->
                            finishActivity(Intent().apply {
                                putExtra(ROOM_MEDIA_MESSAGE_EXTRA, viewModel.mediaMessageArray)
                            })
                        }
                        .setNegativeButton(R.string.cancel, null)
                        .show()
            } else {
                sendPatientBackToPreviousActivity()
            }
        }
    }

    private fun subscribeUI() {
        viewModel.patients.observe(viewLifecycleOwner, Observer { patientList ->
            patientAdapter.setData(patientList)
        })

        viewModel.selectedPatient.observe(viewLifecycleOwner, Observer { patient ->
            if (patient == null) {
                selectedPatient.visibility = GONE
                cross.visibility = GONE
                designationLayout.visibility = GONE
                searchInputLayout.visibility = VISIBLE
                patientsRecyclerView.visibility = VISIBLE
                saveButton.isEnabled = true
            } else {
                // selectedPatient.setBackgroundColor(Color.LTGRAY)
                saveButton.isEnabled = false
                searchInputLayout.visibility = GONE
                patientsRecyclerView.visibility = GONE
                designationLayout.visibility = VISIBLE
                selectedPatient.visibility = VISIBLE
                cross.visibility = VISIBLE
                patientNameTextView?.text = patient.name
                patientURNTextView?.text = patient.urn
                patientDobTextView?.text = patient.dob
            }
        })
    }

    private fun sendPatientBackToPreviousActivity() {
        viewModel.selectedPatient.value?.let {
            finishActivity(Intent().apply {
                putExtra(PATIENT_EXTRA, viewModel.selectedPatient.value)
                putExtra(ROOM_MEDIA_MESSAGE_EXTRA, viewModel.mediaMessageArray)
            })
        }
    }

    private fun finishActivity(intent: Intent){
        activity?.setResult(RESULT_OK, intent)
        activity?.finish()
    }

    override fun onPatientClick(patient: DemoPatient) {
        viewModel.addSelectedPatient(patient)
    }

    internal class DebouncingQueryTextListener(
            lifecycle: Lifecycle,
            private val onDebouncingQueryTextChange: (String?) -> Unit
    ) : TextWatcher {
        var debouncePeriod: Long = 500

        private val coroutineScope = lifecycle.coroutineScope

        private var searchJob: Job? = null

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            // TODO("Not yet implemented")
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            searchJob?.cancel()
            searchJob = coroutineScope.launch {
                p0?.let {
                    delay(debouncePeriod)
                    onDebouncingQueryTextChange(p0.toString())
                }
            }
        }

        override fun afterTextChanged(p0: Editable?) {
            // TODO("Not yet implemented")
        }
    }
}