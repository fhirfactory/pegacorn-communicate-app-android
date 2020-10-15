package im.vector.chat

import android.view.View.VISIBLE
import im.vector.R
import im.vector.directory.people.model.TemporaryRoom
import kotlinx.android.synthetic.main.activity_create_chat.*

class ActChatGroupActivity : BaseChatActivity() {
    lateinit var selectedRoomAdapter: SelectedRoomAdapter

    override fun initUiAndData() {
        super.initUiAndData()
        selectedUserRecyclerView.visibility = VISIBLE

        selectedRoomAdapter = SelectedRoomAdapter(this)
        selectedUserRecyclerView.adapter = selectedRoomAdapter
    }

    override fun onRoomClick(temporaryRoom: TemporaryRoom) {
        selectedRoomAdapter.addRoom(temporaryRoom)
    }
}