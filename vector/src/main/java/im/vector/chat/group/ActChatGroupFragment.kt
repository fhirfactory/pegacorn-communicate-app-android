package im.vector.chat.group

import android.os.Bundle
import android.view.View.VISIBLE
import im.vector.chat.BaseChatFragment
import im.vector.directory.people.model.TemporaryRoom
import kotlinx.android.synthetic.main.fragment_create_chat.*

class ActChatGroupFragment : BaseChatFragment() {
    lateinit var selectedRoomAdapter: SelectedRoomAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        selectedUserRecyclerView.visibility = VISIBLE

        selectedRoomAdapter = SelectedRoomAdapter(requireContext())
        selectedUserRecyclerView.adapter = selectedRoomAdapter
    }

    override fun onRoomClick(temporaryRoom: TemporaryRoom) {
        selectedRoomAdapter.addRoom(temporaryRoom)
    }
}