package im.vector.chat.group

import android.os.Bundle
import android.view.MenuItem
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import im.vector.R
import im.vector.chat.BaseChatFragment
import im.vector.directory.RoomClickListener
import im.vector.directory.people.DirectoryPeopleFragment
import im.vector.directory.people.model.TemporaryRoom
import im.vector.directory.role.DirectoryRoleFragment
import kotlinx.android.synthetic.main.fragment_create_chat.*

class ActChatGroupFragment : BaseChatFragment() {
    lateinit var selectedChatViewModel: SelectedChatViewModel
    lateinit var selectedRoomAdapter: SelectedRoomAdapter
    override fun getMenuRes() = R.menu.next

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.updateActionBarTitle(getString(R.string.room_recents_create_room))
        selectedChatViewModel = ViewModelProviders.of(this).get(SelectedChatViewModel::class.java)

        selectedUserRecyclerView.visibility = VISIBLE

        selectedRoomAdapter = SelectedRoomAdapter(requireContext(), object: RoomClickListener {
            override fun onRoomClick(temporaryRoom: TemporaryRoom, forRemove: Boolean) {
                if(temporaryRoom.role != null){
                    (fragments[0] as DirectoryRoleFragment).unSelect(temporaryRoom.role)
                } else if(temporaryRoom.people != null){
                    (fragments[1] as DirectoryPeopleFragment).unSelect(temporaryRoom.people)
                }
            }
        })
        selectedUserRecyclerView.adapter = selectedRoomAdapter
        subscribeUI()
    }

    fun subscribeUI(){
        selectedChatViewModel.selectedLiveItems.observe(viewLifecycleOwner, Observer{ rooms ->
            selectedRoomAdapter.setData(rooms)
        })
    }

    override fun onRoomClick(temporaryRoom: TemporaryRoom, forRemove: Boolean) {
        if(forRemove) {
            selectedChatViewModel.removeRoom(temporaryRoom)
        } else {
            selectedChatViewModel.addRoom(temporaryRoom)
        }
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
}