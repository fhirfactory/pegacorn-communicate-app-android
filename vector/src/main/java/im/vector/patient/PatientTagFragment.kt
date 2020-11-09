package im.vector.patient

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import im.vector.R
import im.vector.activity.SimpleFragmentActivity
import im.vector.activity.SimpleFragmentActivityListener
import im.vector.home.BaseCommunicateHomeFragment

class PatientTagFragment : BaseCommunicateHomeFragment() {
    private var LOG_TAG = "PatientTagFragment"
    private lateinit var viewModel: PatientTagViewModel
    private var simpleFragmentActivityListener: SimpleFragmentActivityListener? = null
    override fun getLayoutResId(): Int = R.layout.fragment_patient_tag

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
    }
}