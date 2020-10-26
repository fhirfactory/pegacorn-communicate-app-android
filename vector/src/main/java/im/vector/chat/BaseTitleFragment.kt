package im.vector.chat

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import im.vector.home.BaseActFragment

/**
 * This fragment is to set the title
 * in the toolbar
 */
abstract class BaseTitleFragment : BaseActFragment() {
    lateinit var viewModel: TitleViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(TitleViewModel::class.java)
        } ?: throw Throwable("invalid activity")
    }
}