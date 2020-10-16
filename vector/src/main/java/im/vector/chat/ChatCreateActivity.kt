package im.vector.chat

import android.content.Context
import android.content.Intent
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.SimpleFragmentActivity
import im.vector.chat.group.ActChatGroupFragment
import im.vector.chat.onetoone.ActChatOneToOneFragment

class ChatCreateActivity : SimpleFragmentActivity() {
    var chatType: CHAT_TYPE? = null
    override fun getTitleRes() = R.string.invite_title

    override fun initUiAndData() {
        super.initUiAndData()
        configureToolbar()
        mSession = getSession(intent)
        if (mSession == null) {
            finish()
            return
        }

        if (CommonActivityUtils.shouldRestartApp(this)) {
            CommonActivityUtils.restartApp(this)
            return
        }

        if (intent.hasExtra(CHAT_TYPE)) {
            chatType = intent.getSerializableExtra(CHAT_TYPE) as CHAT_TYPE?
        }

        if (supportFragmentManager.fragments.isEmpty()) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, if(chatType==im.vector.chat.CHAT_TYPE.GROUP) ActChatGroupFragment() else ActChatOneToOneFragment())
                    .commitNow()
        }
    }

    companion object {
        const val CHAT_TYPE = "CHAT_TYPE"
        fun intent(context: Context, chatType: CHAT_TYPE): Intent {
            return Intent(context, ChatCreateActivity::class.java).also { intent ->
                intent.putExtra(CHAT_TYPE, chatType)
            }
        }
    }
}

enum class CHAT_TYPE{
    ONE_TO_ONE,
    GROUP
}