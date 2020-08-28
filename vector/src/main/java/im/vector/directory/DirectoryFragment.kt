package im.vector.directory

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import im.vector.R
import im.vector.home.BaseCommunicateHomeFragment


abstract class DirectoryFragment : BaseCommunicateHomeFragment() {
    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.ic_action_global_search)?.isVisible = false
        menu.findItem(R.id.ic_action_historical)?.isVisible = false
        menu.findItem(R.id.ic_action_mark_all_as_read)?.isVisible = false
    }
}