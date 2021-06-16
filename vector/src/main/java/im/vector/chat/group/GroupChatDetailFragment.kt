package im.vector.chat.group

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import im.vector.R
import im.vector.activity.VectorMediaPickerActivity
import im.vector.chat.BaseTitleFragment
import im.vector.health.directory.MemberClickListener
import im.vector.health.microservices.interfaces.MatrixItem

import im.vector.util.PERMISSIONS_FOR_TAKING_PHOTO
import im.vector.util.PERMISSION_REQUEST_CODE_LAUNCH_CAMERA
import im.vector.util.VectorUtils
import im.vector.util.checkPermissions
import kotlinx.android.synthetic.main.fragment_create_chat.selectedUserRecyclerView
import kotlinx.android.synthetic.main.fragment_group_chat_create_detail.*
import org.matrix.androidsdk.core.callback.SimpleApiCallback
import org.matrix.androidsdk.rest.model.CreateRoomParams
import java.io.FileNotFoundException


class GroupChatDetailFragment : BaseTitleFragment() {
    lateinit var selectedChatViewModel: SelectedChatViewModel
    lateinit var selectedMemberAdapter: SelectedMemberAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.updateActionBarTitle(getString(R.string.room_recents_create_room))
        activity?.run {
            selectedChatViewModel = ViewModelProviders.of(this).get(SelectedChatViewModel::class.java)
        } ?: throw Throwable("invalid activity")

        selectedMemberAdapter = SelectedMemberAdapter(requireContext(), object : MemberClickListener {
            override fun onMemberClick(member: MatrixItem, forRemove: Boolean) {
                selectedChatViewModel.removeMember(member)
            }
        })
        selectedUserRecyclerView.adapter = selectedMemberAdapter

        avatar.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_material_camera))
        avatar.setOnClickListener {
            if (checkPermissions(PERMISSIONS_FOR_TAKING_PHOTO, this, PERMISSION_REQUEST_CODE_LAUNCH_CAMERA)) {
                changeAvatar()
            }
        }

        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                refreshCreateButton()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //TODO("Not yet implemented")
            }
        }
        roomNameEditText.addTextChangedListener(watcher)
        roomTopicEditText.addTextChangedListener(watcher)

        subscribeUI()
    }

    var menuItem: MenuItem? = null

    fun refreshCreateButton() {
        menuItem?.isVisible = roomNameEditText.text.toString() != "" && selectedChatViewModel.selectedLiveItems.value?.size ?: 0 > 0
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item: MenuItem = menu.findItem(R.id.ic_action_create)
        menuItem = item
        refreshCreateButton()
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
            selectedMemberAdapter.setData(rooms)
            rooms?.size?.let { setHeader(header, R.string.total_number_of_member, it) }
            activity?.invalidateOptionsMenu()
        })
    }

    override fun getMenuRes() = R.menu.create

    override fun getLayoutResId() = R.layout.fragment_group_chat_create_detail

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.ic_action_create -> {
                    val roomParams: CreateRoomParams = CreateRoomParams()
                    roomParams.name = roomNameEditText.text.toString()
                    roomParams.topic = roomTopicEditText.text.toString()
                    roomParams.visibility = if (publicSwitch.isActivated) "public" else "private"
                    val roomMembers = selectedChatViewModel.selectedLiveItems.value?.mapNotNull { x ->
                        x.GetMatrixID()
                    }
                    roomParams.invitedUserIds = roomMembers
                    mSession.createRoom(roomParams, object: SimpleApiCallback<String>(activity) {
                        override fun onSuccess(info: String?) {
                            //Room Created
                            finishActivity(Intent())
                        }
                    })
                    true
                }
                else -> false
            }

    private fun finishActivity(intent: Intent) {
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun onFilter(pattern: String?, listener: OnFilterListener?) {
        TODO("Not yet implemented")
    }

    override fun onResetFilter() {
        TODO("Not yet implemented")
    }
}