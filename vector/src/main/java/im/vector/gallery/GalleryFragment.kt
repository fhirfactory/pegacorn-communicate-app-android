package im.vector.gallery

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import im.vector.Matrix
import im.vector.R
import im.vector.dialogs.DialogListItem
import im.vector.dialogs.DialogListItem.*
import im.vector.dialogs.DialogSendItemAdapter
import im.vector.fragments.AbsHomeFragment
import im.vector.fragments.VectorSearchRoomFilesListFragment
import im.vector.patient.DemoPatient
import im.vector.patient.PatientTagActivity
import im.vector.patient.PatientTagActivity.Companion.intent
import im.vector.patient.PatientTagFragment
import im.vector.ui.themes.ThemeUtils
import im.vector.util.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.matrix.androidsdk.data.Room
import org.matrix.androidsdk.data.RoomMediaMessage
import org.matrix.androidsdk.fragments.MatrixMessageListFragment


class GalleryFragment : AbsHomeFragment() {

    private lateinit var galleryAdapter: VectorSearchRoomFilesListFragment
    override fun getLayoutResId() = R.layout.fragment_gallery
    companion object {
        private val TAKE_IMAGE_REQUEST_CODE = 0
        private val PATIENT_TAG_REQUEST_CODE = 1

        private val VIDEO_VALUE_TITLE = "video"
        private val IMAGE_VALUE_TITLE = "photo"
    }

    private lateinit var mVectorRoomMediasSender: VectorRoomMediasSender
    private var mLatestTakePictureCameraUri: String? = null // has to be String not Uri because of Serializable


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let { activity ->
            mPrimaryColor = ThemeUtils.getColor(activity, R.attr.vctr_tab_home)
            mSecondaryColor = ThemeUtils.getColor(activity, R.attr.vctr_tab_home_secondary)

            mFabColor = ContextCompat.getColor(activity, R.color.tab_people)
            mFabPressedColor = ContextCompat.getColor(activity, R.color.tab_people_secondary)
        }

        galleryAdapter = VectorSearchRoomFilesListFragment.newInstance(mSession.myUserId,"!HdSQgdQYAGKNMuYUPk:matrix.org",R.layout.fragment_matrix_message_list_fragment);
        mVectorRoomMediasSender = VectorRoomMediasSender(requireActivity(),galleryAdapter, Matrix.getInstance(requireContext()).mediaCache)
        childFragmentManager.beginTransaction().apply {
            add(R.id.filesListContainer, galleryAdapter)
            commit()
        }

        takePhoto.setOnClickListener {
            chooseMediaSource()
        }

    }

    private fun chooseMediaSource() {
        // hide the header room
        val items: MutableList<DialogListItem> = ArrayList()

        // Camera
        items.add(TakePhoto)
        items.add(TakeVideo)


        // if one item it must be DialogListItem.TakePhotoVideo.INSTANCE
        // hence not showing the dialog
        if (items.size == 1) {
            onSendChoiceClicked(TakePhotoVideo)
        } else {
            AlertDialog.Builder(requireContext())
                    .setAdapter(DialogSendItemAdapter(requireContext(), items)) { dialog, which -> onSendChoiceClicked(items[which]) }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
        }
    }

    private fun onSendChoiceClicked(dialogListItem: DialogListItem) {
        if (dialogListItem is TakePhoto) {
            if (checkPermissions(PERMISSIONS_FOR_TAKING_PHOTO,
                            this, PERMISSION_REQUEST_CODE_LAUNCH_NATIVE_CAMERA)) {
                launchNativeCamera()
            }
        } else if (dialogListItem is TakeVideo) {
            if (checkPermissions(PERMISSIONS_FOR_TAKING_PHOTO,
                            this, PERMISSION_REQUEST_CODE_LAUNCH_NATIVE_VIDEO_CAMERA)) {
                launchNativeVideoRecorder()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                TAKE_IMAGE_REQUEST_CODE -> getPatientData(data)
                PATIENT_TAG_REQUEST_CODE -> sendPatientMediasIntent(data)
            }
        }
    }

    private fun getPatientData(intent: Intent?) {
        // sanity check
        if (null == intent && null == mLatestTakePictureCameraUri) {
            return
        }
        var sharedDataItems = ArrayList<RoomMediaMessage>()
        if (null != intent) {
            sharedDataItems = ArrayList(RoomMediaMessage.listRoomMediaMessages(intent))
        }
        if (sharedDataItems.size > 0) {
            startActivityForResult(intent(requireContext(), sharedDataItems), PATIENT_TAG_REQUEST_CODE)
        } else if (mLatestTakePictureCameraUri != null) {
            startActivityForResult(intent(requireContext(), mLatestTakePictureCameraUri!!), PATIENT_TAG_REQUEST_CODE)
        } else {
            Toast.makeText(requireContext(), R.string.no_image, Toast.LENGTH_LONG).show()
        }
    }

    private fun sendPatientMediasIntent(intent: Intent?) {
        if (null != intent) {
            val bundle = intent.extras
            // sanity checks
            if (null != bundle) {
                if (bundle.containsKey(PatientTagFragment.PATIENT_EXTRA)) {
                    val patient: DemoPatient? = bundle.getParcelable(PatientTagFragment.PATIENT_EXTRA)
                    Toast.makeText(requireContext(), patient?.name ?: "No Patient Tagged", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "No Patient Tagged", Toast.LENGTH_LONG).show()
                }
                if (bundle.containsKey(PatientTagActivity.ROOM_MEDIA_MESSAGE_ARRAY_EXTRA)) {
                    val sharedDataItems: ArrayList<RoomMediaMessage> = bundle.getParcelableArrayList(PatientTagActivity.ROOM_MEDIA_MESSAGE_ARRAY_EXTRA) ?: ArrayList()
                    if (sharedDataItems.isEmpty()) {
                        sharedDataItems.add(RoomMediaMessage(Uri.parse(mLatestTakePictureCameraUri)))
                    }
                    mVectorRoomMediasSender.sendMedias(sharedDataItems)
                    galleryAdapter.refresh()
                    galleryAdapter.messageAdapter.clear()

                    galleryAdapter.backPaginate(true)
                }
            }
        }
    }

    /**
     * Launch the camera
     */
    private fun launchNativeVideoRecorder() {
        openCameraWithoutSavingVideoToGallery(this, VIDEO_VALUE_TITLE, TAKE_IMAGE_REQUEST_CODE);
    }

    /**
     * Launch the camera
     */
    private fun launchNativeCamera() {
        mLatestTakePictureCameraUri = openCameraWithoutSavingImageToGallery(this, IMAGE_VALUE_TITLE, TAKE_IMAGE_REQUEST_CODE);
    }

    private fun subscribeUI() {
        TODO("Not yet implemented")
    }

    override fun getRooms(): MutableList<Room> {
        TODO("Not yet implemented")
    }

    override fun onFilter(pattern: String?, listener: OnFilterListener?) {
        galleryAdapter.searchPattern(pattern, true, object: MatrixMessageListFragment.OnSearchResultListener{
            override fun onSearchSucceed(nbrMessages: Int) {
                //TODO("Not yet implemented")
            }

            override fun onSearchFailed() {
                //TODO("Not yet implemented")
            }

        })
    }

    override fun onResetFilter() {
        TODO("Not yet implemented")
    }
}