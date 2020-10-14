package im.vector.chat

import android.view.View.VISIBLE
import im.vector.R
import kotlinx.android.synthetic.main.activity_create_chat.*

class ActChatGroupActivity : BaseChatActivity() {

    override fun initUiAndData() {
        super.initUiAndData()
        selectedUserRecyclerView.visibility = VISIBLE
    }
}