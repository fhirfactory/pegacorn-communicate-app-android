package im.vector.chat.onetoone

import im.vector.R
import im.vector.chat.BaseChatFragment
import im.vector.directory.people.model.TemporaryRoom

class ActChatOneToOneFragment : BaseChatFragment() {
    override fun getLayoutResId() = R.layout.fragment_create_chat

    override fun onRoomClick(temporaryRoom: TemporaryRoom) {

    }
}