package im.vector.directory.people

import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import im.vector.R
import im.vector.adapters.ParticipantAdapterItem
import im.vector.adapters.VectorParticipantsAdapter
import im.vector.directory.BaseDirectoryFragment
import im.vector.directory.people.detail.PeopleDetailActivity
import im.vector.directory.people.model.DirectoryPeople
import im.vector.directory.people.model.TemporaryRoom
import im.vector.extensions.withArgs
import im.vector.microservices.DirectoryConnector
import kotlinx.android.synthetic.main.fragment_directory_people.*
import kotlinx.android.synthetic.main.fragment_directory_people.header
import org.matrix.androidsdk.data.Room

class DirectoryPeopleFragment : BaseDirectoryFragment(), PeopleClickListener {
    companion object {
        private const val SELECTABLE = "SELECTABLE"

        fun newInstance(selectable: Boolean = false): DirectoryPeopleFragment {
            return DirectoryPeopleFragment().withArgs {
                putBoolean(SELECTABLE, selectable)
            }
        }
    }

    private lateinit var peopleDirectoryAdapter: PeopleDirectoryAdapter

    override fun filter(with: String?) {
        //TODO("Not yet implemented")
    }

    override fun onFilter(pattern: String?, listener: OnFilterListener?) {
        // TODO("Not yet implemented")
        val participantsAdapter: VectorParticipantsAdapter = VectorParticipantsAdapter(context,
                R.layout.adapter_item_vector_add_participants,
                R.layout.adapter_item_vector_people_header,
                mSession, null, true)
        val participantAdapterItem = ParticipantAdapterItem(null,null,null,false)
        participantsAdapter.setSearchedPattern(pattern,participantAdapterItem) {count ->
            peopleDirectoryAdapter.setData(participantsAdapter)
            listener?.onFilterDone(count)
        }
    }

    override fun onResetFilter() {
        // TODO("Not yet implemented")
    }

    override fun getRooms(): MutableList<Room> {
        TODO("Not yet implemented")
    }

    override fun onFavorite(enable: Boolean) {
        //Temporary
        setHeader(header, if (enable) R.string.total_number_of_favourite_people else R.string.total_number_of_people, if (enable) 4 else 10)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_directory_people
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val selectable = arguments?.getBoolean(SELECTABLE, false)

        peopleDirectoryAdapter = PeopleDirectoryAdapter(requireContext(), this, selectable ?: false)
        peopleRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        peopleRecyclerview.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        peopleRecyclerview.adapter = peopleDirectoryAdapter


        //test data
//        val testPeopleData = mutableListOf<DirectoryPeople>()
//        peopleDirectoryAdapter.setData(testPeopleData)
//        setHeader(header, R.string.total_number_of_people, testPeopleData.size)
        initializeList()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.ic_action_advanced_search)?.isVisible = false
    }

    fun initializeList() {
        val selectable = arguments?.getBoolean(SELECTABLE, false)
        peopleDirectoryAdapter = PeopleDirectoryAdapter(requireContext(), this, selectable ?: false)
        (peopleRecyclerview.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        peopleRecyclerview.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        peopleRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        peopleRecyclerview.adapter = peopleDirectoryAdapter
        peopleRecyclerview.setHasFixedSize(true)

        loading = false
        page = -1
        paginate()
        setupScrollListener()
    }

    var page = -1;
    var loading = false;
    var pageSize = 20;

    fun paginate() {
        if (!loading) {

            val idx = peopleRecyclerview.getChildAdapterPosition(peopleRecyclerview.getChildAt(0))
            val overHalfway: Boolean = page < 0 || (idx >= (page * pageSize) / 2) || (idx == -1)

            if (overHalfway || peopleRecyclerview.height == 0) {
                page += 1
                loading = true;
                context?.let {
                    DirectoryConnector.getPractitioners(page, pageSize, it){
                        it?.let {
                            peopleDirectoryAdapter.addPage(it)
                            loading = false
                        }
                    }
                }
            }
        }
    }

    fun setupScrollListener() {
        peopleRecyclerview.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                paginate()
            }
        })
    }

    override fun onPeopleClick(directoryPeople: DirectoryPeople, forRemove: Boolean) {
        if (roomClickListener == null) {
            startActivity(PeopleDetailActivity.intent(requireContext(), directoryPeople, true))
        } else {
            roomClickListener?.onRoomClick(TemporaryRoom(directoryPeople, null), forRemove)
        }
    }

    override fun onPeopleFavorite(directoryPeople: DirectoryPeople) {

    }

    fun unSelectPeople(people: DirectoryPeople) {
        peopleDirectoryAdapter.removeFromSelectedPeople(people.id)
    }

    fun selectPeople(people: DirectoryPeople) {
        peopleDirectoryAdapter.addToSelectedPeople(people.id)
    }
}