package im.vector.chat.group

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import im.vector.R
import im.vector.activity.VectorMediaPickerActivity
import im.vector.chat.BaseTitleFragment
import im.vector.chat.TitleViewModel
import im.vector.directory.RoomClickListener
import im.vector.directory.people.model.TemporaryRoom
import im.vector.home.BaseActFragment
import im.vector.util.PERMISSIONS_FOR_TAKING_PHOTO
import im.vector.util.PERMISSION_REQUEST_CODE_LAUNCH_CAMERA
import im.vector.util.VectorUtils
import im.vector.util.checkPermissions
import kotlinx.android.synthetic.main.fragment_create_chat.selectedUserRecyclerView
import kotlinx.android.synthetic.main.fragment_group_chat_create_detail.*
import org.matrix.androidsdk.core.Log
import java.io.FileNotFoundException


class GroupChatDetailFragment : BaseTitleFragment() {
    lateinit var selectedChatViewModel: SelectedChatViewModel
    lateinit var selectedRoomAdapter: SelectedRoomAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.updateActionBarTitle(getString(R.string.room_recents_create_room))
        activity?.run {
            selectedChatViewModel = ViewModelProviders.of(this).get(SelectedChatViewModel::class.java)
        } ?: throw Throwable("invalid activity")

        selectedRoomAdapter = SelectedRoomAdapter(requireContext(), object : RoomClickListener {
            override fun onRoomClick(temporaryRoom: TemporaryRoom, forRemove: Boolean) {
                selectedChatViewModel.removeRoom(temporaryRoom)
            }
        })
        selectedUserRecyclerView.adapter = selectedRoomAdapter

        avatar.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_material_camera))
        avatar.setOnClickListener {
            if (checkPermissions(PERMISSIONS_FOR_TAKING_PHOTO, this, PERMISSION_REQUEST_CODE_LAUNCH_CAMERA)) {
                changeAvatar()
            }
        }
        subscribeUI()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item: MenuItem = menu.findItem(R.id.ic_action_create)
        item.isVisible = selectedChatViewModel.selectedLiveItems.value?.size ?: 0 > 0
    }

    private fun changeAvatar() {
        val intent = Intent(activity, VectorMediaPickerActivity::class.java)
        intent.putExtra(VectorMediaPickerActivity.EXTRA_AVATAR_MODE, true)
        startActivityForResult(intent, VectorUtils.TAKE_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                VectorUtils.TAKE_IMAGE -> {
                    val thumbnailUri = VectorUtils.getThumbnailUriFromIntent(activity, data, mSession.mediaCache)

                    if (null != thumbnailUri) {
                        try {
                            val inputStream = activity?.contentResolver?.openInputStream(thumbnailUri)
                            avatar.setImageDrawable(Drawable.createFromStream(inputStream, thumbnailUri.toString()))
                        } catch (e: FileNotFoundException) {
                            avatar.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_material_camera))
                        }
                    }
                }
            }
        }
    }

    fun subscribeUI() {
        selectedChatViewModel.selectedLiveItems.observe(viewLifecycleOwner, Observer { rooms ->
            selectedRoomAdapter.setData(rooms)
            rooms?.size?.let { setHeader(header, R.string.total_number_of_member, it) }
            activity?.invalidateOptionsMenu()
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