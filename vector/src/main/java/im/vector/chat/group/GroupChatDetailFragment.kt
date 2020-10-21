package im.vector.chat.group

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import im.vector.R
import im.vector.chat.ChatViewModel
import im.vector.directory.RoomClickListener
import im.vector.directory.people.model.TemporaryRoom
import im.vector.home.BaseActFragment
import kotlinx.android.synthetic.main.fragment_create_chat.*
import org.matrix.androidsdk.core.Log

class GroupChatDetailFragment : BaseActFragment() {
    lateinit var viewModel: ChatViewModel
    lateinit var selectedChatViewModel: SelectedChatViewModel
    lateinit var selectedRoomAdapter: SelectedRoomAdapter
    val safeArgs: GroupChatDetailFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        } ?: throw Throwable("invalid activity")
        viewModel.updateActionBarTitle(getString(R.string.room_recents_create_room))
        selectedChatViewModel = ViewModelProviders.of(this).get(SelectedChatViewModel::class.java)

        selectedRoomAdapter = SelectedRoomAdapter(requireContext(), object : RoomClickListener {
            override fun onRoomClick(temporaryRoom: TemporaryRoom, forRemove: Boolean) {
                selectedChatViewModel.removeRoom(temporaryRoom)
            }
        })
        selectedUserRecyclerView.adapter = selectedRoomAdapter
        subscribeUI()
        safeArgs.rooms?.let { rooms ->
            selectedChatViewModel.selectedLiveItems.postValue(rooms.toMutableList())
        }
    }

    fun subscribeUI() {
        selectedChatViewModel.selectedLiveItems.observe(viewLifecycleOwner, Observer { rooms ->
            selectedRoomAdapter.setData(rooms)
        })
    }

    override fun getMenuRes() = R.menu.create

    override fun getLayoutResId() = R.layout.fragment_group_chat_create_detail

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.ic_action_create -> {
                    Log.d("zzzz", "Next")
                    true
                }
                else -> false
            }

    override fun onFilter(pattern: String?, listener: OnFilterListener?) {
        TODO("Not yet implemented")
    }

    override fun onResetFilter() {
        TODO("Not yet implemented")
    }
}