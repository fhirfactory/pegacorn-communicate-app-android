package im.vector.chat.group

import android.os.Bundle
import android.view.MenuItem
import android.view.View.VISIBLE
import im.vector.R
import im.vector.chat.BaseChatFragment
import im.vector.directory.people.model.TemporaryRoom
import kotlinx.android.synthetic.main.fragment_create_chat.*
import org.matrix.androidsdk.core.Log

class ActChatGroupFragment : BaseChatFragment() {
    lateinit var selectedRoomAdapter: SelectedRoomAdapter
    override fun getMenuRes() = R.menu.next

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.updateActionBarTitle(getString(R.string.room_recents_create_room))

        selectedUserRecyclerView.visibility = VISIBLE

        selectedRoomAdapter = SelectedRoomAdapter(requireContext())
        selectedUserRecyclerView.adapter = selectedRoomAdapter
    }

    override fun onRoomClick(temporaryRoom: TemporaryRoom) {
        selectedRoomAdapter.addRoom(temporaryRoom)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_action_next -> {
                Log.d("zzzz", "Next")
                return true
            }
        }
        return false
    }
}