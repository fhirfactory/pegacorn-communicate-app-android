package im.vector.invite

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import im.vector.R
import im.vector.VectorApp
import im.vector.activity.SimpleFragmentActivity
import im.vector.activity.SimpleFragmentActivityListener
import im.vector.activity.VectorHomeActivity
import im.vector.activity.VectorRoomActivity
import im.vector.activity.util.WaitingViewData
import im.vector.adapters.AbsAdapter
import im.vector.home.BaseActFragment
import kotlinx.android.synthetic.main.fragment_home_individual.*
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.core.Log
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import java.util.*

class InviteRoomFragment : BaseActFragment(), AbsAdapter.RoomInvitationListener {
    private var LOG_TAG = "InviteRoomFragment"
    private lateinit var viewModel: InviteRoomViewModel
    private var simpleFragmentActivityListener: SimpleFragmentActivityListener? = null
    override fun getLayoutResId(): Int = R.layout.fragment_home_individual

    //Filter is not part of this view anymore
    override fun onFilter(pattern: String?, listener: OnFilterListener?) {
        TODO("Not yet implemented")
    }

    //Filter is not part of this view anymore
    override fun onResetFilter() {
        TODO("Not yet implemented")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is SimpleFragmentActivity) {
            simpleFragmentActivityListener = activity as SimpleFragmentActivity?
        }

        viewModel = ViewModelProviders.of(this).get(InviteRoomViewModel::class.java)
        viewModel.initSession(mSession)

        sectionView.setupRoomRecyclerView(LinearLayoutManager(activity, RecyclerView.VERTICAL, false),
                R.layout.adapter_item_room_invite, false, null, this, null)
        val rooms = viewModel.getRoomInvitations()
        sectionView.setRooms(rooms)
        rooms?.size?.let {
            if (rooms.isEmpty()) {
                sectionView.setTitle(R.string.no_invites)
            } else {
                sectionView.setTitle(R.string.total_number_of_invite, rooms.size)
            }
        }
    }

    override fun onRejectInvitation(session: MXSession?, roomId: String?) {
        roomId?.let { roomId ->
            val room = mSession?.dataHandler?.getRoom(roomId)

            if (null != room) {
                simpleFragmentActivityListener?.updateWaitingView(WaitingViewData(getString(R.string.rejecting)))
                room.leave(createForgetLeaveCallback(roomId, null))
            }
        }
    }

    override fun onAcceptInvitation(session: MXSession?, roomId: String?) {
        simpleFragmentActivityListener?.updateWaitingView(WaitingViewData(getString(R.string.accepting)))
        mSession.joinRoom(roomId, object : ApiCallback<String?> {
            override fun onSuccess(roomId: String?) {
                simpleFragmentActivityListener?.hideWaitingView()
                val params: MutableMap<String, String?> = HashMap()
                params[VectorRoomActivity.EXTRA_MATRIX_ID] = mSession.myUserId
                params[VectorRoomActivity.EXTRA_ROOM_ID] = roomId

                // clear the activity stack to home activity
                val intent = Intent(activity, VectorHomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

                intent.putExtra(VectorHomeActivity.EXTRA_JUMP_TO_ROOM_PARAMS, params as HashMap<*, *>)
                startActivity(intent)
            }

            private fun onError(errorMessage: String) {
                Log.d(LOG_TAG, "re join failed $errorMessage")
                //Toast.makeText(VectorRoomActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                simpleFragmentActivityListener?.hideWaitingView()
            }

            override fun onNetworkError(e: Exception) {
                onError(e.localizedMessage)
            }

            override fun onMatrixError(e: MatrixError) {
                if (MatrixError.M_CONSENT_NOT_GIVEN == e.errcode) {
                    Log.d(LOG_TAG, "re join failed ${e.localizedMessage}")
                    simpleFragmentActivityListener?.hideWaitingView()
                } else {
                    onError(e.localizedMessage)
                }
            }

            override fun onUnexpectedError(e: Exception) {
                onError(e.localizedMessage)
            }
        })
    }

    private fun createForgetLeaveCallback(roomId: String, onSuccessCallback: ApiCallback<Void?>?): ApiCallback<Void?>? {
        return object : ApiCallback<Void?> {
            override fun onSuccess(info: Void?) {
                // clear any pending notification for this room
                VectorApp.getInstance().notificationDrawerManager.clearMessageEventOfRoom(roomId)
                simpleFragmentActivityListener?.hideWaitingView()
                onSuccessCallback?.onSuccess(null)
                sectionView.setRooms(viewModel.getRoomInvitations())
            }

            private fun onError(message: String) {
                simpleFragmentActivityListener?.hideWaitingView()
                //Toast.makeText(this@VectorHomeActivity, message, Toast.LENGTH_LONG).show()
            }

            override fun onNetworkError(e: java.lang.Exception) {
                onError(e.localizedMessage)
            }

            override fun onMatrixError(e: MatrixError) {
                if (MatrixError.M_CONSENT_NOT_GIVEN == e.errcode) {
                    simpleFragmentActivityListener?.hideWaitingView()
                    //consentNotGivenHelper.displayDialog(e)
                } else {
                    onError(e.localizedMessage)
                }
            }

            override fun onUnexpectedError(e: java.lang.Exception) {
                onError(e.localizedMessage)
            }
        }
    }
}