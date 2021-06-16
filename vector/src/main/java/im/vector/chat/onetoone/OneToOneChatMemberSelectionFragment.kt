package im.vector.chat.onetoone

import android.os.Bundle
import im.vector.R
import im.vector.chat.BaseMemberSelectionFragment
import im.vector.health.directory.people.DirectoryPeopleFragment
import im.vector.health.directory.role.DirectoryRoleFragment
import im.vector.health.directory.shared.IMatrixDirectorySelectionFragment
import im.vector.health.microservices.interfaces.MatrixItem

class OneToOneChatMemberSelectionFragment : BaseMemberSelectionFragment() {
    override val selectionFragments = listOf(DirectoryRoleFragment.newInstance(false) as IMatrixDirectorySelectionFragment<*>, DirectoryPeopleFragment.newInstance(false) as IMatrixDirectorySelectionFragment<*>)
    override fun getLayoutResId() = R.layout.fragment_create_chat

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.updateActionBarTitle(getString(R.string.room_recents_start_chat))
    }

    override fun onMemberClick(member: MatrixItem, forRemove: Boolean) {

    }
}