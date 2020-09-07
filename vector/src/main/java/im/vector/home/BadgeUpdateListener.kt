package im.vector.home

import im.vector.adapters.model.NotificationCounter

interface BadgeUpdateListener {
    fun onBadgeUpdate(notificationCounter: NotificationCounter)
}

