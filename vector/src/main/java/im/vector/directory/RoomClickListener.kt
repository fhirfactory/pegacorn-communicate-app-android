package im.vector.directory

import im.vector.directory.people.model.TemporaryRoom

interface RoomClickListener{
    fun onRoomClick(temporaryRoom: TemporaryRoom, forRemove: Boolean = false)
}