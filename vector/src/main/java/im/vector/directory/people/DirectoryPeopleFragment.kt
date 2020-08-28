package im.vector.directory.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import im.vector.R
import im.vector.directory.DirectoryFragment
import im.vector.directory.people.detail.PeopleDetailActivity
import im.vector.directory.people.model.DirectoryPeople
import im.vector.home.BaseCommunicateHomeFragment
import kotlinx.android.synthetic.main.fragment_directory_people.*

class DirectoryPeopleFragment : DirectoryFragment(), PeopleClickListener {
    private lateinit var peopleDirectoryAdapter: PeopleDirectoryAdapter

    override fun onFilter(pattern: String?, listener: OnFilterListener?) {
        TODO("Not yet implemented")
    }

    override fun onResetFilter() {
        TODO("Not yet implemented")
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_directory_people
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        peopleDirectoryAdapter = PeopleDirectoryAdapter(requireContext(), this)
        peopleRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        peopleRecyclerview.adapter = peopleDirectoryAdapter

        //test data
        val testPeopleData = mutableListOf<DirectoryPeople>()
        testPeopleData.add(DirectoryPeople("1", "Liam", "Software Developer", null, "Emergency Department", "Hospital Department"))
        testPeopleData.add(DirectoryPeople("2", "Noah", "Business Analyst", null, "Emergency Department", "Hospital Department"))
        testPeopleData.add(DirectoryPeople("3", "William", "Scrum Master", null, "Emergency Department", "Hospital Department"))
        testPeopleData.add(DirectoryPeople("4", "Oliver", "Designer", null, "Emergency Department", "Hospital Department"))
        testPeopleData.add(DirectoryPeople("5", "James", "Test Analyst", null, "Emergency Department", "Hospital Department"))

        peopleDirectoryAdapter.setData(testPeopleData)
    }


    override fun onPeopleClick(directoryPeople: DirectoryPeople) {
        startActivity(PeopleDetailActivity.intent(requireContext(), directoryPeople))
    }

    override fun onPeopleFavorite(directoryPeople: DirectoryPeople) {

    }
}