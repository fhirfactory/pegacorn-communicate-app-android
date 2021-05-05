package im.vector.health.directory

import im.vector.health.directory.people.model.TemporaryRoom

interface RoomClickListener {
    fun onRoomClick(temporaryRoom: TemporaryRoom, forRemove: Boolean = false)
}