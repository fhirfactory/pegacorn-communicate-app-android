package im.vector.chat.group

import android.view.MenuItem
import im.vector.R
import im.vector.activity.MXCActionBarActivity
import org.matrix.androidsdk.core.Log

class GroupChatDetailFragment : MXCActionBarActivity(){
    override fun getLayoutRes() = R.layout.fragment_group_chat_create_detail

    override fun getMenuRes() = R.menu.create

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.ic_action_create -> {
                Log.d("zzzz", "Next")
                true
            }
            else -> false
        }
}