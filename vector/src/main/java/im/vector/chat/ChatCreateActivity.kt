package im.vector.chat

import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.SimpleFragmentActivity


class ChatCreateActivity : SimpleFragmentActivity() {
    var chatType: CHAT_TYPE? = null
    private lateinit var viewModel: TitleViewModel
    private lateinit var navController: NavController
    private var appBarConfiguration: AppBarConfiguration? = null
    override fun getLayoutRes() = R.layout.activity_with_nav_fragment

    override fun initUiAndData() {
        super.initUiAndData()
        configureToolbar()
        viewModel = ViewModelProviders.of(this).get(TitleViewModel::class.java)
        viewModel.title.observe(this, Observer {
            supportActionBar?.title = it
        })

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


        navController = findNavController(R.id.chat_nav_host_fragment)
        navController.setGraph(when (chatType) {
            im.vector.chat.CHAT_TYPE.ONE_TO_ONE -> R.navigation.one_to_one_chat_nav
            im.vector.chat.CHAT_TYPE.GROUP -> R.navigation.group_chat_nav
            null -> R.navigation.one_to_one_chat_nav
        })
        appBarConfiguration = AppBarConfiguration.Builder().build()
        toolbar.setupWithNavController(navController, appBarConfiguration!!)

        toolbar.setNavigationOnClickListener {
            if (navController.graph.startDestination == navController.currentDestination?.id) {
                finish()
            } else {
                navController.navigateUp()
            }
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

enum class CHAT_TYPE {
    ONE_TO_ONE,
    GROUP
}