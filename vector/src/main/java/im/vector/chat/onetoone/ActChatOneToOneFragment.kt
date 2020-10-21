package im.vector.chat.onetoone

import android.os.Bundle
import im.vector.R
import im.vector.chat.BaseChatFragment
import im.vector.directory.people.model.TemporaryRoom

class ActChatOneToOneFragment : BaseChatFragment() {

    override fun getLayoutResId() = R.layout.fragment_create_chat

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.updateActionBarTitle(getString(R.string.room_recents_start_chat))
    }

    override fun onRoomClick(temporaryRoom: TemporaryRoom, forRemove: Boolean) {

    }
}