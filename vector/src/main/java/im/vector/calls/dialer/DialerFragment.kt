package im.vector.calls.dialer

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import im.vector.R
import im.vector.calls.recent.RecentCallFragmentViewModel
import im.vector.fragments.VectorBaseFragment
import im.vector.ui.themes.ThemeUtils

class DialerFragment : VectorBaseFragment() {
    private lateinit var viewModel: DialerFragmentViewModel

    override fun getLayoutResId() = R.layout.fragment_dialer

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(DialerFragmentViewModel::class.java)
        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.pressedKeys.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()){

            } else {

            }
        })
    }


}