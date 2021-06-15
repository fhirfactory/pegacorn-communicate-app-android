package im.vector.health.directory.role

import android.content.Context
import im.vector.R
import im.vector.health.directory.role.detail.RoleDetailActivity
import im.vector.health.directory.role.model.*
import im.vector.extensions.withArgs
import im.vector.health.TemporaryRoom
import im.vector.health.directory.shared.StandardDirectoryFragment
import im.vector.health.microservices.DirectoryServicesSingleton
import im.vector.health.microservices.interfaces.IPractitionerRole


class DirectoryRoleFragment: StandardDirectoryFragment<RolesDirectoryAdapter, RoleViewHolder, PractitionerRoleItem>() {
    companion object {
        fun newInstance(selectable: Boolean = false): DirectoryRoleFragment {
            return DirectoryRoleFragment().withArgs {
                putBoolean(SELECTABLE, selectable)
            }
        }
    }

    fun unSelectRole(role: IPractitionerRole) {
        listItemAdapter.removeFromSelectedRoles(role.GetID())
    }

    fun selectRole(role: IPractitionerRole) {
        listItemAdapter.addToSelectedRoles(role.GetID())
    }

    override fun getHeaderText(count: Int, favourites: Boolean): String = (if (favourites) getString(R.string.total_number_of_favourite_roles) else getString(R.string.total_number_of_roles)) + " " + count.toString()

    override fun constructAdapter(context: Context, selectable: Boolean): RolesDirectoryAdapter {
        return RolesDirectoryAdapter(context,object: RoleClickListener {
            override fun onRoleClick(role: PractitionerRoleItem, forRemove: Boolean) {
                if (roomClickListener == null) {
                    startActivity(RoleDetailActivity.intent(requireContext(), role, true))
                } else {
                    roomClickListener?.onRoomClick(TemporaryRoom(null, role), forRemove)
                }
            }
        }, selectable)
    }

    override fun getData(forPage: Int, withPageSize: Int, query: String?, addItem: (List<PractitionerRoleItem>?, Int) -> Unit) {
        DirectoryServicesSingleton.Instance().GetPractitionerRoles(query, page, pageSize){ res, count ->
            addItem(res?.map { PractitionerRoleItem(it) }, count)
        }
    }

    override fun getDataFavourites(forPage: Int, withPageSize: Int, query: String?, addItem: (List<PractitionerRoleItem>?, Int) -> Unit) {
        DirectoryServicesSingleton.Instance().GetPractitionerRoleFavourites(query, page, pageSize){ res, count ->
            addItem(res?.map { PractitionerRoleItem(it) }, count)
        }
    }
}