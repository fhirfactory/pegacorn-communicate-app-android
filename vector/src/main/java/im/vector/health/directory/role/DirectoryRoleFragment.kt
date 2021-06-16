package im.vector.health.directory.role

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import im.vector.R
import im.vector.health.directory.role.detail.RoleDetailActivity
import im.vector.health.directory.role.model.*
import im.vector.extensions.withArgs
import im.vector.health.TemporaryRoom
import im.vector.health.directory.MemberClickListener
import im.vector.health.directory.shared.HandlesAPIErrors
import im.vector.health.directory.shared.IMatrixDirectorySelectionFragment
import im.vector.health.directory.shared.IProvidesMatrixItems
import im.vector.health.directory.shared.StandardDirectoryFragment
import im.vector.health.microservices.DirectoryServicesSingleton
import im.vector.health.microservices.interfaces.IPractitioner
import im.vector.health.microservices.interfaces.IPractitionerRole
import im.vector.health.microservices.interfaces.MatrixItem


class DirectoryRoleFragment: StandardDirectoryFragment<RolesDirectoryAdapter, RoleViewHolder, PractitionerRoleItem>(), IMatrixDirectorySelectionFragment<IPractitionerRole>, HandlesAPIErrors {
    companion object {
        fun newInstance(selectable: Boolean = false): DirectoryRoleFragment {
            return DirectoryRoleFragment().withArgs {
                putBoolean(SELECTABLE, selectable)
            }
        }
    }

    fun selectRole(role: IPractitionerRole) {
        listItemAdapter.addToSelectedRoles(role.GetID())
    }

    override fun getHeaderText(count: Int, favourites: Boolean): String = (if (favourites) getString(R.string.total_number_of_favourite_roles) else getString(R.string.total_number_of_roles)) + " " + count.toString()

    override fun constructAdapter(context: Context, selectable: Boolean): RolesDirectoryAdapter {
        return RolesDirectoryAdapter(context,object: RoleClickListener {
            override fun onRoleClick(role: PractitionerRoleItem, forRemove: Boolean) {
                if (memberClickListener == null) {
                    startActivity(RoleDetailActivity.intent(requireContext(), role, true))
                } else {
                    memberClickListener?.onMemberClick(role.practitionerRole, forRemove)
                }
            }
        }, selectable)
    }

    override fun getData(forPage: Int, withPageSize: Int, query: String?, addItem: (List<PractitionerRoleItem>?, Int) -> Unit) {
        DirectoryServicesSingleton.Instance().GetPractitionerRoles(query, page, pageSize, { res, count ->
            addItem(res?.map { PractitionerRoleItem(it) }, count)
        }){
            displayError(it)
        }
    }

    override fun getDataFavourites(forPage: Int, withPageSize: Int, query: String?, addItem: (List<PractitionerRoleItem>?, Int) -> Unit) {
        DirectoryServicesSingleton.Instance().GetPractitionerRoleFavourites(query, page, pageSize, { res, count ->
            addItem(res?.map { PractitionerRoleItem(it) }, count)
        }){
            displayError(it)
        }
    }

    override fun selectItem(item: IPractitionerRole) {
        listItemAdapter.addToSelectedRoles(item.GetID())
    }

    override fun deselectItem(item: IPractitionerRole) {
        listItemAdapter.removeFromSelectedRoles(item.GetID())
    }

    override fun provideMemberClickListener(listener: MemberClickListener) {
        memberClickListener = listener
    }

    override fun getFragment(): Fragment = this

    override fun receivesItem(item: MatrixItem): Boolean = item is IPractitionerRole

    override fun getSelectionTitleResource(): Int = R.string.create_chat_roles

    override fun getCurrentContext(): Context = requireContext()
}