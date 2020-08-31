package im.vector.home

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import im.vector.R
import kotlinx.android.synthetic.main.fragment_home_individual.*

class InviteRoomFragment : BaseCommunicateHomeIndividualFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sectionView.setupRoomRecyclerView(LinearLayoutManager(activity, RecyclerView.VERTICAL, false),
                R.layout.adapter_item_room_invite, false, onSelectRoomListener, invitationListener, moreActionListener)
        sectionView.setRooms(localRooms)
    }

    override fun getRoomFragmentType(): CommunicateHomeFragment.ROOM_FRAGMENTS {
        return CommunicateHomeFragment.ROOM_FRAGMENTS.INVITE
    }
}