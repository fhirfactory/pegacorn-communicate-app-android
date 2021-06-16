package im.vector.health.directory.people

import android.content.Context
import androidx.fragment.app.Fragment
import im.vector.R
import im.vector.health.directory.people.detail.PeopleDetailActivity
import im.vector.extensions.withArgs
import im.vector.health.TemporaryRoom
import im.vector.health.directory.MemberClickListener
import im.vector.health.directory.people.model.PractitionerItem
import im.vector.health.directory.shared.*
import im.vector.health.microservices.DirectoryServicesSingleton
import im.vector.health.microservices.interfaces.IPractitioner
import im.vector.health.microservices.interfaces.MatrixItem

class DirectoryPeopleFragment: StandardDirectoryFragment<PeopleDirectoryAdapter, PeopleDirectoryAdapter.PeopleViewHolder, PractitionerItem>(), MessagingSupport, IMatrixDirectorySelectionFragment<IPractitioner>, HandlesAPIErrors {
    companion object {
        fun newInstance(selectable: Boolean = false): DirectoryPeopleFragment {
            return DirectoryPeopleFragment().withArgs {
                putBoolean(SELECTABLE, selectable)
            }
        }
    }

    override fun constructAdapter(context: Context, selectable: Boolean): PeopleDirectoryAdapter {
        return PeopleDirectoryAdapter(context, object : PeopleClickListener {
            override fun onPeopleClick(directoryPeople: PractitionerItem, forRemove: Boolean) {
                if (memberClickListener == null) {
                    startActivity(PeopleDetailActivity.intent(requireContext(), directoryPeople, true))
                } else {
                    memberClickListener?.onMemberClick(directoryPeople.practitioner, forRemove)
                }
            }

            override fun onPeopleFavorite(directoryPeople: PractitionerItem) {
                //TODO("Not yet implemented")
            }
        }, selectable, this, this)
    }

    override fun getHeaderText(count: Int, favourites: Boolean): String = (if (favourites) getString(R.string.total_number_of_favourite_people) else getString(R.string.total_number_of_people)) + " " + count.toString()

    override fun getData(forPage: Int, withPageSize: Int, query: String?, addItem: (List<PractitionerItem>?, Int) -> Unit) {
        DirectoryServicesSingleton.Instance().GetPractitioners(query, page, pageSize, { practitioners, count ->
            addItem(practitioners?.map { PractitionerItem(it,false) }, count)
        }){
            displayError(it)
        }
    }

    override fun getDataFavourites(forPage: Int, withPageSize: Int, query: String?, addItem: (List<PractitionerItem>?, Int) -> Unit) {
        DirectoryServicesSingleton.Instance().GetPractitionerFavourites(query, page, pageSize, { practitioners, count ->
            addItem(practitioners?.map { PractitionerItem(it,false) }, count)
        }){
            displayError(it)
        }
    }

    override fun selectItem(item: IPractitioner) {
        listItemAdapter.addToSelectedPeople(item.GetID())
    }

    override fun deselectItem(item: IPractitioner) {
        listItemAdapter.removeFromSelectedPeople(item.GetID())
    }

    override fun provideMemberClickListener(listener: MemberClickListener) {
        memberClickListener = listener
    }

    override fun getFragment(): Fragment = this

    override fun receivesItem(item: MatrixItem): Boolean = item is IPractitioner

    override fun getSelectionTitleResource(): Int = R.string.create_chat_people

    override fun getCurrentContext(): Context = requireContext()
}