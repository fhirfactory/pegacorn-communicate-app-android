package im.vector.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import im.vector.R
import im.vector.adapters.HomeRoomAdapter
import im.vector.ui.themes.ThemeUtils.setTabLayoutTheme
import im.vector.util.HomeRoomsViewModel
import im.vector.util.PreferencesManager
import im.vector.util.RoomUtils
import kotlinx.android.synthetic.main.fragment_view_pager_tab.*
import org.matrix.androidsdk.data.Room
import org.matrix.androidsdk.data.RoomTag

class CommunicateHomeFragment : BaseCommunicateHomeFragment(), HomeRoomAdapter.OnSelectRoomListener, RegisterListener, CommunicateTabBadgeUpdateListener {
    private val dataUpdateListeners = mutableMapOf<ROOM_FRAGMENTS, UpDateListener?>(ROOM_FRAGMENTS.INVITE to null, ROOM_FRAGMENTS.FAVORITE to null, ROOM_FRAGMENTS.NORMAL to null, ROOM_FRAGMENTS.LOW_PRIORITY to null)
    private var result: HomeRoomsViewModel.Result? = null
    private var roomPositionMap = mutableMapOf(ROOM_FRAGMENTS.INVITE to -1, ROOM_FRAGMENTS.FAVORITE to -1, ROOM_FRAGMENTS.NORMAL to -1, ROOM_FRAGMENTS.LOW_PRIORITY to -1)

    override fun getLayoutResId(): Int {
        return R.layout.fragment_view_pager_tab
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pager.offscreenPageLimit = 3
        pager.adapter = HomePagerAdapter(childFragmentManager)
        tabLayout.setupWithViewPager(pager)
        activity?.let { activity ->
            setTabLayoutTheme(activity, tabLayout)
        }
    }

    override fun onFilter(pattern: String?, listener: OnFilterListener?) {
        dataUpdateListeners.forEach { listenerMap ->
            listenerMap.value?.onFilter(pattern, listener)
        }
    }

    override fun onResetFilter() {
        dataUpdateListeners.forEach { listenerMap ->
            listenerMap.value?.onFilter("", null)
        }
    }

    override fun onRoomResultUpdated(result: HomeRoomsViewModel.Result?) {
        if (isResumed) {
            refreshData(result)
        }
    }

    private fun calculateRoomPosition() {
        var count = 0
        result?.let { result ->
            roomPositionMap[ROOM_FRAGMENTS.INVITE] = if (mActivity.roomInvitations.isEmpty()) -1 else count++
            roomPositionMap[ROOM_FRAGMENTS.FAVORITE] = if (result.favourites.isEmpty()) -1 else count++
            roomPositionMap[ROOM_FRAGMENTS.NORMAL] = count++
            roomPositionMap[ROOM_FRAGMENTS.LOW_PRIORITY] = if (result.lowPriorities.isEmpty()) -1 else count++
        }
    }

    override fun onBadgeUpdate(count: Int, roomFragmentType: ROOM_FRAGMENTS) {
        roomPositionMap[roomFragmentType]?.let {position ->
            if(position!=-1 && pager.adapter?.count ?: 0 > position)
            tabLayout.getTabAt(position)?.apply {
                if(count > 0) {
                    orCreateBadge
                    badge?.isVisible = true
                    badge?.number = count
                }else{
                    removeBadge()
                }
            }
        }
    }

    /*
     * *********************************************************************************************
     * Data management
     * *********************************************************************************************
     */
    /**
     * Init the rooms data
     */
    private fun refreshData(result: HomeRoomsViewModel.Result?) {
        this.result = result
        val pinMissedNotifications = PreferencesManager.pinMissedNotifications(activity)
        val pinUnreadMessages = PreferencesManager.pinUnreadMessages(activity)
        val notificationComparator = RoomUtils.getNotifCountRoomsComparator(mSession, pinMissedNotifications, pinUnreadMessages)
        calculateRoomPosition()
        pager.adapter?.notifyDataSetChanged()
        dataUpdateListeners.forEach { listenerMap ->
            when (listenerMap.key) {
                ROOM_FRAGMENTS.INVITE -> {
                    listenerMap.value?.onUpdate(mActivity.roomInvitations, notificationComparator)
                }
                ROOM_FRAGMENTS.FAVORITE -> {
                    listenerMap.value?.onUpdate(result?.favourites, notificationComparator)
                }
                ROOM_FRAGMENTS.NORMAL -> {
                    listenerMap.value?.onUpdate(result?.otherRooms?.plus(result.directChats), notificationComparator)
                }
                ROOM_FRAGMENTS.LOW_PRIORITY -> {
                    listenerMap.value?.onUpdate(result?.lowPriorities, notificationComparator)
                }
            }
        }
        mActivity.hideWaitingView()
    }

    override fun onLongClickRoom(v: View?, room: Room?, position: Int) {
        // User clicked on the "more actions" area
        val tags = room!!.accountData.keys
        val isFavorite = tags != null && tags.contains(RoomTag.ROOM_TAG_FAVOURITE)
        val isLowPriority = tags != null && tags.contains(RoomTag.ROOM_TAG_LOW_PRIORITY)
        RoomUtils.displayPopupMenu(activity, mSession, room, v, isFavorite, isLowPriority, this)
    }

    override fun onSelectRoom(room: Room?, position: Int) {
        openRoom(room)
    }

    override fun onRegister(fragmentType: ROOM_FRAGMENTS, listener: UpDateListener) {
        dataUpdateListeners[fragmentType] = listener
    }

    override fun onUnregister(fragmentType: ROOM_FRAGMENTS) {
        dataUpdateListeners[fragmentType] = null
    }

    inner class HomePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        private val pinMissedNotifications = PreferencesManager.pinMissedNotifications(activity)
        private val pinUnreadMessages = PreferencesManager.pinUnreadMessages(activity)
        private val notificationComparator = RoomUtils.getNotifCountRoomsComparator(mSession, pinMissedNotifications, pinUnreadMessages)

        private fun getFragmentForNormal() = NormalRoomFragment().also { fragment ->
            fragment.onUpdate(result?.directChats?.let { result?.otherRooms?.plus(it) }, notificationComparator)
            fragment.addListener(this@CommunicateHomeFragment, this@CommunicateHomeFragment, null, this@CommunicateHomeFragment, this@CommunicateHomeFragment)
        }

        private fun getFragmentForFavorite() = FavoriteRoomFragment().also { fragment ->
            fragment.onUpdate(result?.favourites, notificationComparator)
            fragment.addListener(this@CommunicateHomeFragment, this@CommunicateHomeFragment, null, this@CommunicateHomeFragment, this@CommunicateHomeFragment)
        }

        private fun getFragmentForLowPriority() = LowPriorityRoomFragment().also { fragment ->
            fragment.onUpdate(result?.lowPriorities, notificationComparator)
            fragment.addListener(this@CommunicateHomeFragment, this@CommunicateHomeFragment, null, this@CommunicateHomeFragment, this@CommunicateHomeFragment)
        }

        private fun getFragmentForInvitation() = InviteRoomFragment().also { fragment ->
            fragment.onUpdate(mActivity.roomInvitations, notificationComparator)
            fragment.addListener(this@CommunicateHomeFragment, this@CommunicateHomeFragment, this@CommunicateHomeFragment, null, this@CommunicateHomeFragment)
        }

        private fun getFragment(roomFragment: ROOM_FRAGMENTS): Fragment {
            return when (roomFragment) {
                ROOM_FRAGMENTS.INVITE -> getFragmentForInvitation()
                ROOM_FRAGMENTS.FAVORITE -> getFragmentForFavorite()
                ROOM_FRAGMENTS.NORMAL -> getFragmentForNormal()
                ROOM_FRAGMENTS.LOW_PRIORITY -> getFragmentForLowPriority()
            }
        }

        override fun getItem(position: Int): Fragment {
            for ((key, value) in roomPositionMap) {
                if (value != -1 && value == position) {
                    return getFragment(key)
                }
            }
            throw IllegalArgumentException("No fragment is found for that position.")
        }

        override fun getItemPosition(`object`: Any): Int {
            return POSITION_NONE
        }

        override fun getCount(): Int {
            var count = 0
            for ((_, value) in roomPositionMap) {
                if (value != -1) {
                    count++
                }
            }
            return count
        }

        override fun getPageTitle(position: Int): CharSequence {
            for ((key, value) in roomPositionMap) {
                if (value != -1 && value == position) {
                    return key.title
                }
            }
            throw IllegalArgumentException("No title is found for that position.")
        }
    }

    enum class ROOM_FRAGMENTS(val title: String) {
        INVITE("Invite"),
        FAVORITE("Favourite"),
        NORMAL("Normal"),
        LOW_PRIORITY("Low Priority")
    }
}

