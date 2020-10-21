package im.vector.directory.people

import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import im.vector.R
import im.vector.directory.BaseDirectoryFragment
import im.vector.directory.people.detail.PeopleDetailActivity
import im.vector.directory.people.model.DirectoryPeople
import im.vector.directory.people.model.TemporaryRoom
import im.vector.extensions.withArgs
import kotlinx.android.synthetic.main.fragment_directory_people.*
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

    override fun onFilter(pattern: String?, listener: OnFilterListener?) {
        // TODO("Not yet implemented")
    }

    override fun onResetFilter() {
        TODO("Not yet implemented")
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
        peopleRecyclerview.adapter = peopleDirectoryAdapter

        //test data
        val testPeopleData = mutableListOf<DirectoryPeople>()
        testPeopleData.add(DirectoryPeople("1", "Liam", "Registrar", null, "Emergency Department", "Hospital Department"))
        testPeopleData.add(DirectoryPeople("2", "Noah", "Ward Nurse", null, "Emergency Department", "Hospital Department"))
        testPeopleData.add(DirectoryPeople("3", "William", "Nursing Team Leader", null, "Emergency Department", "Hospital Department"))
        testPeopleData.add(DirectoryPeople("4", "Oliver", "Emergency Admitting Officer", null, "Emergency Department", "Hospital Department"))
        testPeopleData.add(DirectoryPeople("5", "James", "Consultant", null, "Emergency Department", "Hospital Department"))

        peopleDirectoryAdapter.setData(testPeopleData)
        setHeader(header, R.string.total_number_of_people, testPeopleData.size)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.ic_action_advanced_search)?.isVisible = false
    }

    override fun onPeopleClick(directoryPeople: DirectoryPeople, forRemove: Boolean) {
        if(roomClickListener==null) {
            startActivity(PeopleDetailActivity.intent(requireContext(), directoryPeople, true))
        } else {
            roomClickListener?.onRoomClick(TemporaryRoom(directoryPeople, null), forRemove)
        }
    }

    override fun onPeopleFavorite(directoryPeople: DirectoryPeople) {

    }

    fun unSelectPeople(people: DirectoryPeople){
        peopleDirectoryAdapter.removeFromSelectedPeople(people.id)
    }

    fun selectPeople(people: DirectoryPeople){
        peopleDirectoryAdapter.addToSelectedPeople(people.id)
    }
}