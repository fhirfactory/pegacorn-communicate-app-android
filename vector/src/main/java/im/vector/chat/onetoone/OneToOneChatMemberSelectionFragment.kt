package im.vector.chat.onetoone

import android.os.Bundle
import im.vector.R
import im.vector.chat.BaseMemberSelectionFragment
import im.vector.health.TemporaryRoom
import im.vector.health.directory.people.DirectoryPeopleFragment
import im.vector.health.directory.role.DirectoryRoleFragment

class OneToOneChatMemberSelectionFragment : BaseMemberSelectionFragment() {
    override val fragments = listOf(DirectoryRoleFragment.newInstance(false), DirectoryPeopleFragment.newInstance(false))
    override fun getLayoutResId() = R.layout.fragment_create_chat

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.updateActionBarTitle(getString(R.string.room_recents_start_chat))
    }

    override fun onRoomClick(temporaryRoom: TemporaryRoom, forRemove: Boolean) {

    }
}