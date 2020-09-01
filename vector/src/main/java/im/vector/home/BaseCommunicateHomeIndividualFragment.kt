package im.vector.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import im.vector.R
import im.vector.adapters.AbsAdapter
import im.vector.adapters.HomeRoomAdapter
import im.vector.fragments.AbsHomeFragment
import kotlinx.android.synthetic.main.fragment_home_individual.*
import org.matrix.androidsdk.data.Room
import java.util.*
import kotlin.collections.ArrayList


abstract class BaseCommunicateHomeIndividualFragment : BaseCommunicateHomeFragment(), UpDateListener, BadgeUpdateListener {
    private val LOG_TAG = BaseCommunicateHomeIndividualFragment::class.java.simpleName

    var registerListener: RegisterListener? = null
    var onSelectRoomListener: HomeRoomAdapter.OnSelectRoomListener? = null
    var invitationListener: AbsAdapter.RoomInvitationListener? = null
    var moreActionListener: AbsAdapter.MoreRoomActionListener? = null
    var communicateTabBadgeUpdateListener: CommunicateTabBadgeUpdateListener? = null

    val localRooms = ArrayList<Room>()

    abstract fun getRoomFragmentType(): CommunicateHomeFragment.ROOM_FRAGMENTS

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home_individual
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sectionView.mHeader.visibility = GONE
        sectionView.mBadge.visibility = GONE
        sectionView.setHideIfEmpty(true)
        sectionView.setBadgeUpdateListener(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        registerListener?.onRegister(getRoomFragmentType(), this)
    }

    override fun onDestroy() {
        super.onDestroy()
        registerListener?.onUnregister(getRoomFragmentType())
    }

    override fun onUpdate(rooms: List<Room>?, comparator: Comparator<Room>) {
        Log.d(LOG_TAG, "called" + rooms?.size)
        localRooms.clear()
        try {
            Collections.sort(rooms, comparator)
        } catch (e: Exception) {
            org.matrix.androidsdk.core.Log.e(LOG_TAG, "## sortAndDisplay() failed " + e.message, e)
        }
        rooms?.let {
            localRooms.addAll(rooms)
            if (sectionView != null) {
                sectionView.setRooms(localRooms)
            }
        }
    }

    override fun onFilter(pattern: String?, listener: OnFilterListener?) {
        sectionView.onFilter(pattern, listener)
    }

    override fun onResetFilter() {
        sectionView.onFilter("", null)
    }

    fun addListener(registerListener: RegisterListener?, selectionListener: HomeRoomAdapter.OnSelectRoomListener?, invitationListener: AbsAdapter.RoomInvitationListener?, moreActionListener: AbsAdapter.MoreRoomActionListener?, communicateTabBadgeUpdateListener: CommunicateTabBadgeUpdateListener) {
        this.registerListener = registerListener
        this.onSelectRoomListener = selectionListener
        this.invitationListener = invitationListener
        this.moreActionListener = moreActionListener
        this.communicateTabBadgeUpdateListener = communicateTabBadgeUpdateListener
    }
}

interface UpDateListener {
    fun onUpdate(rooms: List<Room>?, comparator: Comparator<Room>)
    fun onFilter(pattern: String?, listener: AbsHomeFragment.OnFilterListener?)
    fun onResetFilter()
}

interface RegisterListener {
    fun onRegister(fragmentType: CommunicateHomeFragment.ROOM_FRAGMENTS, listener: UpDateListener)
    fun onUnregister(fragmentType: CommunicateHomeFragment.ROOM_FRAGMENTS)
}