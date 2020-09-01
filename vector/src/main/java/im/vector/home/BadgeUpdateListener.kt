package im.vector.home

interface BadgeUpdateListener {
    fun onBadgeUpdate(count: Int)
}

interface CommunicateTabBadgeUpdateListener {
    fun onBadgeUpdate(count: Int, roomFragmentType: CommunicateHomeFragment.ROOM_FRAGMENTS)
}