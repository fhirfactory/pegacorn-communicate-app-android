package im.vector.patient

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import im.vector.R
import im.vector.activity.SimpleFragmentActivity
import im.vector.activity.SimpleFragmentActivityListener
import im.vector.fragments.VectorMessageListFragment
import im.vector.home.BaseCommunicateHomeFragment
import kotlinx.android.synthetic.main.fragment_patient_tag.*
import org.matrix.androidsdk.fragments.MatrixMessageListFragment

class PatientTagFragment : BaseCommunicateHomeFragment() {
    private var LOG_TAG = "PatientTagFragment"
    private lateinit var viewModel: PatientTagViewModel
    private var simpleFragmentActivityListener: SimpleFragmentActivityListener? = null
    override fun getLayoutResId(): Int = R.layout.fragment_patient_tag
    override fun getMenuRes() = R.menu.menu_done

    companion object {
        private const val FILE_LOCATION__EXTRA = "FILE_LOCATION_EXTRA"
        fun newInstance(fileLocation: String): PatientTagFragment {
            val f = PatientTagFragment()
            val args = Bundle()
            args.putString(FILE_LOCATION__EXTRA, fileLocation)
            f.arguments = args
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

        viewModel.fileLocation = arguments?.getString(FILE_LOCATION__EXTRA)
        Glide.with(this).load(viewModel.fileLocation).into(imageView)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_done->

            return true
        }
        return super.onOptionsItemSelected(item)
    }
}