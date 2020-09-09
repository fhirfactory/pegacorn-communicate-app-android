package im.vector.invite

import androidx.lifecycle.ViewModelProviders
import im.vector.R
import im.vector.activity.SimpleFragmentActivity
import im.vector.home.InviteRoomFragment

class InviteActivity : SimpleFragmentActivity() {
    override fun getTitleRes() = R.string.invite_title

    private lateinit var viewModel: InviteActivityViewModel

    override fun initUiAndData() {
        super.initUiAndData()
        viewModel = ViewModelProviders.of(this).get(InviteActivityViewModel::class.java)
        viewModel.initSession(mSession)

        if (supportFragmentManager.fragments.isEmpty()) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, InviteRoomFragment())
                    .commitNow()
        }
    }
}