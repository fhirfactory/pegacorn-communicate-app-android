package im.vector.patient

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.res.Resources
import android.graphics.Point
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.coroutineScope
import com.bumptech.glide.Glide
import im.vector.R
import im.vector.activity.SimpleFragmentActivity
import im.vector.activity.SimpleFragmentActivityListener
import im.vector.adapters.VectorMessagesAdapterMediasHelper
import im.vector.directory.role.DropDownAdapter
import im.vector.directory.role.model.DropDownItem
import im.vector.home.BaseCommunicateHomeFragment
import im.vector.patient.PatientTagActivity.Companion.FILE_LOCATION_EXTRA
import im.vector.patient.PatientTagActivity.Companion.ROOM_EVENT_EXTRA
import im.vector.patient.PatientTagActivity.Companion.ROOM_MEDIA_MESSAGE_ARRAY_EXTRA
import kotlinx.android.synthetic.main.fragment_patient_tag.*
import kotlinx.android.synthetic.main.item_patient.*
import kotlinx.android.synthetic.main.layout_designation.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.matrix.androidsdk.core.JsonUtils
import org.matrix.androidsdk.rest.model.Event
import org.matrix.androidsdk.rest.model.message.ImageMessage
import org.matrix.androidsdk.rest.model.message.VideoMessage


class PatientTagFragment : BaseCommunicateHomeFragment(), PatientClickListener {
    private var LOG_TAG = "PatientTagFragment"
    private lateinit var viewModel: PatientTagViewModel
    private var simpleFragmentActivityListener: SimpleFragmentActivityListener? = null
    private lateinit var patientAdapter: PatientAdapter
    private lateinit var designationAdapter: DropDownAdapter
    private var mMediasHelper: VectorMessagesAdapterMediasHelper? = null


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
        initMediaAdapterHelper()

        viewModel = ViewModelProviders.of(this).get(PatientTagViewModel::class.java)
        viewModel.initSession(mSession)
        username.text = mSession.myUser.displayname

        designationAdapter = DropDownAdapter(requireContext(), R.layout.drop_down_item)
        designationEditText.threshold = 1
        designationEditText.setAdapter(designationAdapter)
        designationEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                saveButton.isEnabled = (viewModel.selectedPatient.value != null && p0.toString().isNotEmpty()) || true
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        designationEditText.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ ->
            //locationEditText.setText((locationAdapter.getItem(pos) as Location).name, false)
            viewModel.selectedDesignation = designationAdapter.getItem(pos) as DropDownItem
            saveButton.isEnabled = (viewModel.selectedPatient.value != null && designationEditText.text.toString().isNotEmpty()) || true
        }

        viewModel.fileLocation = arguments?.getString(FILE_LOCATION_EXTRA)
        viewModel.mediaMessageArray = arguments?.getParcelableArrayList(ROOM_MEDIA_MESSAGE_ARRAY_EXTRA)
        viewModel.event = arguments?.getSerializable(ROOM_EVENT_EXTRA) as Event?
        if(viewModel.event!=null){
            saveButton.text = getText(R.string.update)
            val message = JsonUtils.toMessage(viewModel.event?.content)
            mMediasHelper?.managePendingImageVideoDownload(imageView, null, progressBar, viewModel.event, message, -1)
        } else {
            progressBar.visibility = GONE
            Glide.with(this).load(if (viewModel.fileLocation == null) viewModel.mediaMessageArray?.get(0)?.uri else viewModel.fileLocation).into(imageView)
        }
        patientsRecyclerView.setHasFixedSize(true)
        patientAdapter = PatientAdapter(this)
        patientsRecyclerView.adapter = patientAdapter

        subscribeUI()
        viewModel.prepareFakePatientData()
        viewModel.getDesignations()

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
                        if (it.isEmpty()) {
                            //so that nothing comes up, possibly temporary
                            viewModel.filterPatient("###########")
                        } else if (it.length > 2) {
                            viewModel.filterPatient(it)
                        }
                    }
                })
        saveButton.setOnClickListener {
            if (viewModel.selectedPatient.value == null) {
                AlertDialog.Builder(requireContext())
                        .setTitle(R.string.dialog_title_confirmation)
                        .setMessage(getString(R.string.patient_tag_warning))
                        .setPositiveButton(R.string.ok) { _, _ ->
                            finishActivity(Intent().apply {
                                putExtra(ROOM_MEDIA_MESSAGE_ARRAY_EXTRA, viewModel.mediaMessageArray)
                            })
                        }
                        .setNegativeButton(R.string.cancel, null)
                        .show()
            } else {
                sendPatientBackToPreviousActivity()
            }
        }
    }

    fun initMediaAdapterHelper(){
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels

        // helpers
        mMediasHelper = VectorMessagesAdapterMediasHelper(context,
                mSession, (screenWidth * 0.3f).toInt(), (screenHeight * 0.3f).toInt(), -1, -1)
    }

    private fun subscribeUI() {
        viewModel.patients.observe(viewLifecycleOwner, Observer { patientList ->
            patientAdapter.setData(patientList)
        })

        viewModel.designations.observe(viewLifecycleOwner, Observer { designations ->
            designationAdapter.addData(designations)
        })

        viewModel.selectedPatient.observe(viewLifecycleOwner, Observer { patient ->
            if (patient == null) {
                selectedPatient.visibility = GONE
                cross.visibility = GONE
                designationLayout.visibility = GONE
                searchInputLayout.visibility = VISIBLE
                patientsRecyclerView.visibility = VISIBLE
                saveButton.isEnabled = true
                viewModel.selectedDesignation = null
            } else {
                // selectedPatient.setBackgroundColor(Color.LTGRAY)
                simpleFragmentActivityListener?.hideKeyboard()
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
                putExtra(ROOM_MEDIA_MESSAGE_ARRAY_EXTRA, viewModel.mediaMessageArray)
            })
        }
    }

    private fun finishActivity(intent: Intent) {
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