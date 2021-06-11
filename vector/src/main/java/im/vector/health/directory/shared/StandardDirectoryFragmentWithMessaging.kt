package im.vector.health.directory.shared

import android.content.Intent
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.facebook.react.bridge.UiThreadUtil.runOnUiThread
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorCallViewActivity
import im.vector.activity.VectorMemberDetailsActivity
import im.vector.activity.VectorRoomActivity
import im.vector.util.PERMISSION_REQUEST_CODE_AUDIO_CALL
import im.vector.util.PERMISSION_REQUEST_CODE_VIDEO_CALL
import im.vector.util.checkPermissions
import org.matrix.androidsdk.call.IMXCall
import org.matrix.androidsdk.core.Log
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.callback.SimpleApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.crypto.MXCryptoError
import org.matrix.androidsdk.crypto.data.MXDeviceInfo
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap
import org.matrix.androidsdk.data.Room
import org.matrix.androidsdk.rest.model.RoomMember
import java.util.*

abstract class StandardDirectoryFragmentWithMessaging<Adapter,ViewHolder : RecyclerView.ViewHolder?,Item> : StandardDirectoryFragment<Adapter,ViewHolder,Item>(), MatrixChatActionHandler
        where
        Adapter: RecyclerView.Adapter<ViewHolder>, Adapter: IStandardDirectoryAdapter<Item>
{
    private val LOG_TAG = VectorMemberDetailsActivity::class.java.simpleName
    private var mCallableRoom: Room? = null

    /**
     * Start a call in a dedicated room
     *
     * @param isVideo true if the call is a video call
     */
    internal fun startCall(isVideo: Boolean) {
        if (!mSession.isAlive || mCallableRoom == null) {
            Log.e(LOG_TAG, "startCall : the session is not anymore valid, or no callable room found")
            return
        }

        // create the call object
        mSession.mCallsManager.createCallInRoom(mCallableRoom?.roomId, isVideo, object : ApiCallback<IMXCall?> {
            override fun onSuccess(call: IMXCall?) {
                call?.let { mxCall ->
                    runOnUiThread {
                        val intent = Intent(this@StandardDirectoryFragmentWithMessaging.context, VectorCallViewActivity::class.java)
                        intent.putExtra(VectorCallViewActivity.EXTRA_MATRIX_ID, mSession.credentials.userId)
                        intent.putExtra(VectorCallViewActivity.EXTRA_CALL_ID, mxCall.callId)
                        runOnUiThread { startActivity(intent) }
                    }
                }

            }

            override fun onNetworkError(e: Exception) {
                Toast.makeText(this@StandardDirectoryFragmentWithMessaging.context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.e(LOG_TAG, "## startCall() failed " + e.message, e)
            }

            override fun onMatrixError(e: MatrixError) {
                if (e is MXCryptoError) {
                    if (MXCryptoError.UNKNOWN_DEVICES_CODE == e.errcode) {
                        CommonActivityUtils.displayUnknownDevicesDialog(mSession,
                                this@StandardDirectoryFragmentWithMessaging.activity,
                                e.mExceptionData as MXUsersDevicesMap<MXDeviceInfo?>,
                                true
                        ) { startCall(isVideo) }
                        return
                    }
                }
                Toast.makeText(this@StandardDirectoryFragmentWithMessaging.context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.e(LOG_TAG, "## startCall() failed " + e.message)
            }

            override fun onUnexpectedError(e: Exception) {
                Toast.makeText(this@StandardDirectoryFragmentWithMessaging.context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.e(LOG_TAG, "## startCall() failed " + e.message, e)
            }
        })
    }

    /**
     * Check the permissions to establish an audio/video call.
     * If permissions are already granted, the call is established, otherwise
     * the permissions are checked against the system. Final result is provided in
     * [.onRequestPermissionsResult].
     *
     * @param aIsVideoCall true if video call, false if audio call
     */
    internal fun startCheckCallPermissions(aIsVideoCall: Boolean) {
        val requestCode: Int
        if (aIsVideoCall) {
            requestCode = PERMISSION_REQUEST_CODE_VIDEO_CALL
        } else {
            requestCode = PERMISSION_REQUEST_CODE_AUDIO_CALL
        }
        if (checkPermissions(requestCode, this, requestCode)) {
            startCall(aIsVideoCall)
        }
    }

    /**
     * Search the first callable room with this member
     *
     * @return a valid Room instance, null if no room found
     */
    internal open fun searchCallableRoom(callback: ApiCallback<Room?>, matrixID: String) {
        if (!mSession.isAlive) {
            Log.e(LOG_TAG, "searchCallableRoom : the session is not anymore valid")
            callback.onSuccess(null)
        } else {
            mSession.dataHandler.store?.rooms?.let {rooms ->
                searchCallableRoomRecursive(ArrayList(rooms), 0, callback, matrixID)
            }

        }
    }

    private fun searchCallableRoomRecursive(rooms: List<Room>,
                                                 index: Int,
                                                 callback: ApiCallback<Room?>,
                                                 matrixID: String) {
        if (index >= rooms.size) {
            // Not found
            callback.onSuccess(null)
        } else {
            val room = rooms[index]
            if (room.numberOfMembers == 2 && room.canPerformCall()) {
                room.getMembersAsync(object : SimpleApiCallback<List<RoomMember?>?>() {
                    override fun onSuccess(members: List<RoomMember?>?) {
                        members?.let { memberList ->
                            for (member in memberList) {
                                member?.let { theMember ->
                                    if (theMember.userId == matrixID) {
                                        callback.onSuccess(room)
                                        return
                                    }
                                }

                            }
                        }

                        // Try next one
                        searchCallableRoomRecursive(rooms, index + 1, callback,matrixID)
                    }
                })
            } else {
                // Try next one
                searchCallableRoomRecursive(rooms, index + 1, callback,matrixID)
            }
        }
    }

    override fun call(aIsVideoCall: Boolean, matrixID: String) {
        searchCallableRoom(object : SimpleApiCallback<Room?>() {
            override fun onSuccess(info: Room?) {
                mCallableRoom = info
                startCheckCallPermissions(aIsVideoCall)
            }
        },matrixID)
    }

    /**
     * callback for the creation of the direct message room
     */
    internal val mCreateDirectMessageCallBack: ApiCallback<String> = object : ApiCallback<String> {
        override fun onSuccess(roomId: String) {
            val params: MutableMap<String, Any> = HashMap()
            params[VectorRoomActivity.EXTRA_MATRIX_ID] = mSession.myUserId
            params[VectorRoomActivity.EXTRA_ROOM_ID] = roomId
            params[VectorRoomActivity.EXTRA_EXPAND_ROOM_HEADER] = true
            this@StandardDirectoryFragmentWithMessaging.activity?.let { activity ->
                CommonActivityUtils.goToRoomPage(activity, mSession, params)
            }

        }

        override fun onMatrixError(e: MatrixError) {

        }

        override fun onNetworkError(e: Exception) {

        }

        override fun onUnexpectedError(e: Exception) {

        }
    }

    override fun startChat(matrixID: String) {
        mSession.createDirectMessageRoom(matrixID, mCreateDirectMessageCallBack)
    }
}