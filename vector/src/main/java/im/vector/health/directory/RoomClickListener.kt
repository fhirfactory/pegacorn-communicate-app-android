package im.vector.health.directory

import im.vector.health.TemporaryRoom


interface RoomClickListener {
    fun onRoomClick(temporaryRoom: TemporaryRoom, forRemove: Boolean = false)
}