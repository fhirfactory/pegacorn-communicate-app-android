package im.vector.chat.group

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import im.vector.R
import im.vector.activity.MXCActionBarActivity
import im.vector.chat.BaseChatFragment
import im.vector.chat.ChatViewModel
import im.vector.directory.people.model.TemporaryRoom
import im.vector.home.BaseActFragment
import org.matrix.androidsdk.core.Log

class GroupChatDetailFragment : BaseActFragment(){
    lateinit var viewModel: ChatViewModel
    override fun getMenuRes() = R.menu.create

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        } ?: throw Throwable("invalid activity")
        viewModel.updateActionBarTitle(getString(R.string.room_recents_create_room))
    }

    override fun getLayoutResId() = R.layout.fragment_group_chat_create_detail

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.ic_action_create -> {
                Log.d("zzzz", "Next")
                true
            }
            else -> false
        }

    override fun onFilter(pattern: String?, listener: OnFilterListener?) {
        TODO("Not yet implemented")
    }

    override fun onResetFilter() {
        TODO("Not yet implemented")
    }
}