package im.vector.directory

import android.view.Menu
import android.view.MenuItem
import im.vector.R
import im.vector.home.BaseCommunicateHomeFragment


abstract class DirectoryFragment : BaseCommunicateHomeFragment() {
    override fun onPrepareOptionsMenu(menu: Menu) {
        hideMenuItem(menu.findItem(R.id.ic_action_global_search))
        hideMenuItem(menu.findItem(R.id.ic_action_historical))
        hideMenuItem(menu.findItem(R.id.ic_action_mark_all_as_read))
    }

    private fun hideMenuItem(item: MenuItem){
        item.isVisible = false
    }
}