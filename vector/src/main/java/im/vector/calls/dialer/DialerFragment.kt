package im.vector.calls.dialer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import im.vector.R
import im.vector.calls.recent.RecentCallFragmentViewModel
import im.vector.databinding.FragmentDialerBinding
import im.vector.fragments.VectorBaseFragment
import im.vector.ui.themes.ThemeUtils
import kotlinx.android.synthetic.main.fragment_dialer.*

class DialerFragment : VectorBaseFragment() {
    private lateinit var viewModel: DialerFragmentViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProviders.of(this).get(DialerFragmentViewModel::class.java)
        return FragmentDialerBinding.inflate(
                inflater,
                container,
                false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }.root
    }

    override fun getLayoutResId() = R.layout.fragment_dialer

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        keyZero.setOnLongClickListener {
            viewModel.keyClick("+")
            true
        }
    }
}