package im.vector.home

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import im.vector.R
import kotlinx.android.synthetic.main.fragment_home_individual.*

class NormalRoomFragment : BaseCommunicateHomeIndividualFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sectionView.setTitle(R.string.total_notification)
        sectionView.setPlaceholders(null, getString(R.string.no_result_placeholder))
        sectionView.setupRoomRecyclerView(LinearLayoutManager(activity, RecyclerView.VERTICAL, false),
                R.layout.adapter_item_room_view, true, onSelectRoomListener, invitationListener, moreActionListener)
        sectionView.setRooms(localRooms)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        registerListener?.onRegister(CommunicateHomeFragment.ROOM_FRAGMENTS.NORMAL, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        registerListener?.onUnregister(CommunicateHomeFragment.ROOM_FRAGMENTS.NORMAL)
    }
}