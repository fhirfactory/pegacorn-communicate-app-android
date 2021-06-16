package im.vector.health.directory.shared

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.facebook.react.bridge.UiThreadUtil
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorCallViewActivity
import im.vector.activity.VectorRoomActivity
import im.vector.util.PERMISSION_REQUEST_CODE_AUDIO_CALL
import im.vector.util.PERMISSION_REQUEST_CODE_VIDEO_CALL
import im.vector.util.RoomUtils
import im.vector.util.checkPermissions
import org.matrix.androidsdk.call.IMXCall
import org.matrix.androidsdk.core.Log
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import org.matrix.androidsdk.crypto.MXCryptoError
import org.matrix.androidsdk.crypto.data.MXDeviceInfo
import org.matrix.androidsdk.crypto.data.MXUsersDevicesMap
import org.matrix.androidsdk.data.Room
import java.util.HashMap

interface MessagingSupport: IMatrixActivity, MatrixChatActionHandler {
    /**
     * Start a call in a dedicated room
     *
     * @param isVideo true if the call is a video call
     */
    fun startCall(isVideo: Boolean, matrixID: String) {
        val room = findCallableRoom(matrixID)
        if (!currentSession.isAlive || room == null) {
            Log.e(this@MessagingSupport.javaClass.simpleName, "startCall : the session is not anymore valid, or no room was found")
            return
        }

        // create the call object
        currentSession.mCallsManager.createCallInRoom(room?.roomId, isVideo, object : ApiCallback<IMXCall?> {
            override fun onSuccess(call: IMXCall?) {
                call?.let { mxCall ->
                    UiThreadUtil.runOnUiThread {
                        val intent = Intent(this@MessagingSupport.getActivity(), VectorCallViewActivity::class.java)
                        intent.putExtra(VectorCallViewActivity.EXTRA_MATRIX_ID, currentSession.credentials.userId)
                        intent.putExtra(VectorCallViewActivity.EXTRA_CALL_ID, mxCall.callId)
                        UiThreadUtil.runOnUiThread { startActivity(intent) }
                    }
                }

            }

            override fun onNetworkError(e: Exception) {
                Toast.makeText(this@MessagingSupport.getActivity(), e.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.e(this@MessagingSupport.javaClass.simpleName, "## startCall() failed " + e.message, e)
            }

            override fun onMatrixError(e: MatrixError) {
                if (e is MXCryptoError) {
                    if (MXCryptoError.UNKNOWN_DEVICES_CODE == e.errcode) {
                        CommonActivityUtils.displayUnknownDevicesDialog(currentSession,
                                this@MessagingSupport.getActivity(),
                                e.mExceptionData as MXUsersDevicesMap<MXDeviceInfo?>,
                                true
                        ) { startCall(isVideo,matrixID) }
                        return
                    }
                }
                Toast.makeText(this@MessagingSupport.getActivity(), e.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.e(this@MessagingSupport.javaClass.simpleName, "## startCall() failed " + e.message)
            }

            override fun onUnexpectedError(e: Exception) {
                Toast.makeText(this@MessagingSupport.getActivity(), e.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.e(this@MessagingSupport.javaClass.simpleName, "## startCall() failed " + e.message, e)
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
    private fun startCheckCallPermissions(aIsVideoCall: Boolean, matrixID: String) {
        val requestCode: Int
        if (aIsVideoCall) {
            requestCode = PERMISSION_REQUEST_CODE_VIDEO_CALL
        } else {
            requestCode = PERMISSION_REQUEST_CODE_AUDIO_CALL
        }
        if (checkPermissions(requestCode, this.getActivity(), requestCode)) {
            startCall(aIsVideoCall,matrixID)
        }
    }

    private fun findCallableRoom(matrixID: String): Room? {
        currentSession.dataHandler.store?.let { store ->
            for (room in store.rooms) {
                if (room.isDirect && room.numberOfMembers == 2 && room.getMember(matrixID) != null) {
                    return room
                }
            }
        }
        return null
    }

    override fun call(aIsVideoCall: Boolean, matrixID: String) {
        startCheckCallPermissions(aIsVideoCall,matrixID)
    }

    /**
     * callback for the creation of the direct message room
     */
    fun getCreateMessageCallback(): ApiCallback<String> = object : ApiCallback<String> {
        override fun onSuccess(roomId: String) {
            hideProgressBar()
            val params: MutableMap<String, Any> = HashMap()
            params[VectorRoomActivity.EXTRA_MATRIX_ID] = currentSession.myUserId
            params[VectorRoomActivity.EXTRA_ROOM_ID] = roomId
            params[VectorRoomActivity.EXTRA_EXPAND_ROOM_HEADER] = true
            this@MessagingSupport.getActivity().let { activity ->
                CommonActivityUtils.goToRoomPage(activity, currentSession, params)
            }

        }

        override fun onMatrixError(e: MatrixError) {
            hideProgressBar()
        }

        override fun onNetworkError(e: Exception) {
            hideProgressBar()
        }

        override fun onUnexpectedError(e: Exception) {
            hideProgressBar()
        }
    }

    override fun startChat(matrixID: String) {

        currentSession.dataHandler.store?.let { store ->
            for (room in store.rooms) {
                if (matrixID == currentSession.myUserId
                        && room.numberOfMembers == 1
                        && room.accountData.hasRoomTags()
                        && room.accountData.roomTag("note") != null) {
                    val params: MutableMap<String, Any> = HashMap()
                    params[VectorRoomActivity.EXTRA_MATRIX_ID] = currentSession.myUserId
                    params[VectorRoomActivity.EXTRA_ROOM_ID] = room.roomId
                    params[VectorRoomActivity.EXTRA_EXPAND_ROOM_HEADER] = true
                    CommonActivityUtils.goToRoomPage(this@MessagingSupport.getActivity() as Activity, currentSession, params)
                    return
                } else if (currentSession.myUserId != matrixID && room.isDirect && room.numberOfMembers == 2 && room.getMember(matrixID) != null) {
                    val params: MutableMap<String, Any> = HashMap()
                    params[VectorRoomActivity.EXTRA_MATRIX_ID] = currentSession.myUserId
                    params[VectorRoomActivity.EXTRA_ROOM_ID] = room.roomId
                    params[VectorRoomActivity.EXTRA_EXPAND_ROOM_HEADER] = true
                    CommonActivityUtils.goToRoomPage(this@MessagingSupport.getActivity() as Activity, currentSession, params)
                    return
                }
            }
        }
        if (matrixID == currentSession.myUserId) {
            showProgressBar()
            currentSession.createRoom(getStringRes(R.string.notes_room_name),getStringRes(R.string.notes_room_topic),null, object: ApiCallback<String> {
                override fun onSuccess(info: String?) {
                    info?.let{ roomID ->
                        RoomUtils.updateRoomTag(currentSession,roomID,2.0,"note", object: ApiCallback<Void> {
                            override fun onSuccess(info: Void?) {
                                hideProgressBar()
                                val params: MutableMap<String, Any> = HashMap()
                                params[VectorRoomActivity.EXTRA_MATRIX_ID] = currentSession.myUserId
                                params[VectorRoomActivity.EXTRA_ROOM_ID] = roomID
                                params[VectorRoomActivity.EXTRA_EXPAND_ROOM_HEADER] = true
                                CommonActivityUtils.goToRoomPage(this@MessagingSupport.getActivity() as Activity, currentSession, params)
                            }

                            override fun onUnexpectedError(e: java.lang.Exception?) {
                                hideProgressBar()
                            }

                            override fun onNetworkError(e: java.lang.Exception?) {
                                hideProgressBar()
                            }

                            override fun onMatrixError(e: MatrixError?) {
                                hideProgressBar()
                            }

                        })
                    }
                }

                override fun onUnexpectedError(e: java.lang.Exception?) {
                    hideProgressBar()
                }

                override fun onNetworkError(e: java.lang.Exception?) {
                    hideProgressBar()
                }

                override fun onMatrixError(e: MatrixError?) {
                    hideProgressBar()
                }

            })
            return
        } else {
            showProgressBar()
            currentSession.createDirectMessageRoom(matrixID, getCreateMessageCallback())
        }
    }
}