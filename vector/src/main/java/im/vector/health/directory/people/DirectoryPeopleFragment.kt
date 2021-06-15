package im.vector.health.directory.people

import android.content.Context
import im.vector.R
import im.vector.health.directory.people.detail.PeopleDetailActivity
import im.vector.extensions.withArgs
import im.vector.health.TemporaryRoom
import im.vector.health.directory.people.model.PractitionerItem
import im.vector.health.directory.shared.MessagingSupport
import im.vector.health.directory.shared.StandardDirectoryFragment
import im.vector.health.microservices.DirectoryServicesSingleton
import im.vector.health.microservices.interfaces.IPractitioner

class DirectoryPeopleFragment: StandardDirectoryFragment<PeopleDirectoryAdapter, PeopleDirectoryAdapter.PeopleViewHolder, PractitionerItem>(), MessagingSupport {
    companion object {
        fun newInstance(selectable: Boolean = false): DirectoryPeopleFragment {
            return DirectoryPeopleFragment().withArgs {
                putBoolean(SELECTABLE, selectable)
            }
        }
    }

    fun unSelectPeople(people: IPractitioner) {
        listItemAdapter.removeFromSelectedPeople(people.GetID())
    }

    fun selectPeople(people: IPractitioner) {
        listItemAdapter.addToSelectedPeople(people.GetID())
    }

    override fun constructAdapter(context: Context, selectable: Boolean): PeopleDirectoryAdapter {
        return PeopleDirectoryAdapter(context, object : PeopleClickListener {
            override fun onPeopleClick(directoryPeople: PractitionerItem, forRemove: Boolean) {
                if (roomClickListener == null) {
                    startActivity(PeopleDetailActivity.intent(requireContext(), directoryPeople, true))
                } else {
                    roomClickListener?.onRoomClick(TemporaryRoom(directoryPeople.practitioner, null), forRemove)
                }
            }

            override fun onPeopleFavorite(directoryPeople: PractitionerItem) {
                //TODO("Not yet implemented")
            }
        }, selectable, this)
    }

    override fun getHeaderText(count: Int, favourites: Boolean): String = (if (favourites) getString(R.string.total_number_of_favourite_people) else getString(R.string.total_number_of_people)) + " " + count.toString()

    override fun getData(forPage: Int, withPageSize: Int, query: String?, addItem: (List<PractitionerItem>?, Int) -> Unit) {
        DirectoryServicesSingleton.Instance().GetPractitioners(query, page, pageSize){ practitioners, count ->
            addItem(practitioners?.map { PractitionerItem(it,false) }, count)
        }
    }

    override fun getDataFavourites(forPage: Int, withPageSize: Int, query: String?, addItem: (List<PractitionerItem>?, Int) -> Unit) {
        DirectoryServicesSingleton.Instance().GetPractitionerFavourites(query, page, pageSize){ practitioners, count ->
            addItem(practitioners?.map { PractitionerItem(it,false) }, count)
        }
    }
}