package im.vector.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import im.vector.R
import im.vector.adapters.AbsAdapter
import im.vector.adapters.HomeRoomAdapter
import im.vector.adapters.model.NotificationCounter
import im.vector.fragments.AbsHomeFragment
import kotlinx.android.synthetic.main.fragment_home_individual.*
import org.matrix.androidsdk.data.Room
import java.util.*
import kotlin.collections.ArrayList


abstract class BaseActIndividualFragment(private var fragmentType: CommunicateHomeFragment.ROOM_FRAGMENTS) : BaseActFragment(), UpDateListener, BadgeUpdateListener {
    private val LOG_TAG = BaseActIndividualFragment::class.java.simpleName

    var registerListener: RegisterListener? = null
    var onSelectRoomListener: HomeRoomAdapter.OnSelectRoomListener? = null
    var invitationListener: AbsAdapter.RoomInvitationListener? = null
    var moreActionListener: AbsAdapter.MoreRoomActionListener? = null
    var communicateTabBadgeUpdateListener: CommunicateTabBadgeUpdateListener? = null

    val localRooms = ArrayList<Room>()

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home_individual
    }

    override fun onBadgeUpdate(notificationCounter: NotificationCounter) {
        communicateTabBadgeUpdateListener?.onBadgeUpdate(notificationCounter.unreadRoomCount, fragmentType)
        when (fragmentType) {
            CommunicateHomeFragment.ROOM_FRAGMENTS.FAVORITE, CommunicateHomeFragment.ROOM_FRAGMENTS.CHAT, CommunicateHomeFragment.ROOM_FRAGMENTS.LOW_PRIORITY -> {
                sectionView.setTitle(R.string.total_number_of_room, notificationCounter.totalRoomCount)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sectionView.setupRoomRecyclerView(LinearLayoutManager(activity, RecyclerView.VERTICAL, false),
                R.layout.adapter_item_room_view, true, onSelectRoomListener, invitationListener, moreActionListener)
        sectionView.setRooms(localRooms)
        sectionView.mBadge.visibility = GONE
        sectionView.setHideIfEmpty(true)
        sectionView.setBadgeUpdateListener(this)
        sectionView.updateRoomCounts()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        registerListener?.onRegister(fragmentType, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        registerListener?.onUnregister(fragmentType)
    }

    override fun onUpdate(rooms: List<Room>?, comparator: Comparator<Room>) {
        Log.d(LOG_TAG, "called" + rooms?.size)
        localRooms.clear()
        try {
            Collections.sort(rooms, comparator)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "## sortAndDisplay() failed " + e.message, e)
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