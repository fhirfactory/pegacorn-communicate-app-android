package im.vector.invite

import androidx.lifecycle.ViewModel
import im.vector.util.RoomUtils
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.Room
import java.util.*

class InviteRoomViewModel : ViewModel() {
    var session: MXSession? = null

    fun initSession(session: MXSession) {
        if (this.session == null) {
            this.session = session
        }
    }

    /*
     * *********************************************************************************************
     * Room invitation management
     * *********************************************************************************************
     */
    fun getRoomInvitations(): List<Room?>? {
        val directChatInvitations: MutableList<Room> = ArrayList()
        val roomInvitations: MutableList<Room> = ArrayList()
        if (null == session?.dataHandler?.store) {
            return ArrayList()
        }
        val roomSummaries = session?.dataHandler?.store?.summaries
        if (roomSummaries != null) {
            for (roomSummary in roomSummaries) {
                // reported by rageshake
                // i don't see how it is possible to have a null roomSummary
                if (null != roomSummary) {
                    val roomSummaryId = roomSummary.roomId
                    val room: Room? = session?.dataHandler?.store?.getRoom(roomSummaryId)

                    // check if the room exists
                    // the user conference rooms are not displayed.
                    if (room != null && !room.isConferenceUserRoom && room.isInvited) {
                        if (room.isDirectChatInvitation) {
                            directChatInvitations.add(room)
                        } else {
                            roomInvitations.add(room)
                        }
                    }
                }
            }
        }

        // the invitations are sorted from the oldest to the more recent one
        val invitationComparator = RoomUtils.getRoomsDateComparator(session, true)
        Collections.sort(directChatInvitations, invitationComparator)
        Collections.sort(roomInvitations, invitationComparator)
        val roomInvites: MutableList<Room?> = ArrayList()

        roomInvites.addAll(directChatInvitations)
        roomInvites.addAll(roomInvitations)
        Collections.sort(roomInvites, invitationComparator)

        return roomInvites
    }
}