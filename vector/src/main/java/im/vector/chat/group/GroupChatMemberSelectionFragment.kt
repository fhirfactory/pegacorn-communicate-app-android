package im.vector.chat.group

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import im.vector.R
import im.vector.chat.BaseMemberSelectionFragment
import im.vector.health.directory.MemberClickListener
import im.vector.health.directory.people.DirectoryPeopleFragment
import im.vector.health.directory.role.DirectoryRoleFragment
import im.vector.health.directory.shared.IMatrixDirectorySelectionFragment
import im.vector.health.microservices.interfaces.MatrixItem
import kotlinx.android.synthetic.main.fragment_create_chat.*


class GroupChatMemberSelectionFragment : BaseMemberSelectionFragment() {
    override val selectionFragments = listOf(DirectoryPeopleFragment.newInstance(true) as IMatrixDirectorySelectionFragment<*>)//listOf(DirectoryRoleFragment.newInstance(true) as IMatrixDirectorySelectionFragment<*>, DirectoryPeopleFragment.newInstance(true) as IMatrixDirectorySelectionFragment<*>)
    lateinit var selectedChatViewModel: SelectedChatViewModel
    lateinit var selectedMemberAdapter: SelectedMemberAdapter
    override fun getMenuRes() = R.menu.next

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.updateActionBarTitle(getString(R.string.room_recents_create_room))

        activity?.run {
            selectedChatViewModel = ViewModelProviders.of(this).get(SelectedChatViewModel::class.java)
        } ?: throw Throwable("invalid activity")

        selectedUserRecyclerView.visibility = VISIBLE

        selectedMemberAdapter = SelectedMemberAdapter(requireContext(), object : MemberClickListener {
            override fun onMemberClick(member: MatrixItem, forRemove: Boolean) {
                selectedChatViewModel.removeMember(member)
                for (fragment in selectionFragments) {
                    if (fragment.receivesItem(member)) {
                        fragment.unsafeDeselectItem(member)
                        break
                    }
                }
            }
        })
        selectedUserRecyclerView.adapter = selectedMemberAdapter
        subscribeUI()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item: MenuItem = menu.findItem(R.id.ic_action_next)
        item.isVisible = selectedChatViewModel.selectedLiveItems.value?.size ?: 0 > 0
    }

    fun subscribeUI() {
        selectedChatViewModel.selectedLiveItems.observe(viewLifecycleOwner, Observer { rooms ->
            selectedMemberAdapter.setData(rooms)
            rooms.forEach { itm ->
                for (fragment in selectionFragments) {
                    if (fragment.receivesItem(itm)) {
                        fragment.unsafeSelectItem(itm)
                        break
                    }
                }
            }
            activity?.invalidateOptionsMenu()
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_action_next -> {
                view?.findNavController()?.navigate(R.id.action_actChatGroupFragment_to_groupChatDetailFragment)
                return true
            }
        }
        return false
    }

    override fun onMemberClick(member: MatrixItem, forRemove: Boolean) {
        if (forRemove) {
            selectedChatViewModel.removeMember(member)
        } else {
            selectedChatViewModel.addMember(member)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) activity?.finish()
    }
}