package im.vector.health.directory.role

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import im.vector.R
import im.vector.health.directory.BaseDirectoryFragment
import im.vector.health.directory.role.detail.RoleDetailActivity
import im.vector.health.directory.role.model.*
import im.vector.extensions.withArgs
import im.vector.health.TemporaryRoom
import im.vector.health.microservices.DirectoryServicesSingleton
import im.vector.health.microservices.Interfaces.IPractitionerRole
import kotlinx.android.synthetic.main.fragment_directory_role.*
import kotlinx.android.synthetic.main.fragment_directory_role.header
import kotlinx.android.synthetic.main.fragment_directory_service.*
import org.matrix.androidsdk.data.Room


class DirectoryRoleFragment : BaseDirectoryFragment(), RoleClickListener {
    companion object {
        private const val SELECTABLE = "SELECTABLE"

        fun newInstance(selectable: Boolean = false): DirectoryRoleFragment {
            return DirectoryRoleFragment().withArgs {
                putBoolean(SELECTABLE, selectable)
            }
        }
    }

    private lateinit var viewModel: DirectoryRoleViewModel
    private lateinit var categoryAdapter: DropDownAdapter
    private lateinit var organisationUnitAdapter: DropDownAdapter
    private lateinit var specialityAdapter: DropDownAdapter
    private lateinit var locationAdapter: DropDownAdapter
    private lateinit var roleAdapter: RolesDirectoryAdapter
    private val constraintCollapsed = ConstraintSet()
    private val constraintExpanded = ConstraintSet()
    private var filter: String? = null

    override fun getLayoutResId(): Int {
        return R.layout.fragment_directory_role
    }

    override fun filter(with: String?) {
        //TODO("Not yet implemented")
        if (filter != with) {
            filter = with
            initializeList()
        }
    }

    override fun onFilter(pattern: String?, listener: OnFilterListener?) {
        // TODO("Not yet implemented")
        println(pattern)
    }

    override fun onResetFilter() {
        // TODO("Not yet implemented")
    }

    override fun getRooms(): MutableList<Room> {
        TODO("Not yet implemented")
    }

    fun initializeList() {
        val selectable = arguments?.getBoolean(SELECTABLE, false)
        roleAdapter = RolesDirectoryAdapter(requireContext(), this, selectable ?: false)
        (roleRecyclerview.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        roleRecyclerview.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        roleRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        roleRecyclerview.adapter = roleAdapter
        roleRecyclerview.setHasFixedSize(true)

        roleRecyclerview.scrollToPosition(0)

        loading = false
        page = -1
        paginate()
        setupScrollListener()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DirectoryRoleViewModel::class.java)

        subscribeUI()

        setHeader(header, R.string.total_number_of_roles, 0)
    }

    override fun onResume() {
        super.onResume()
        initializeList()
    }

    var page = -1;
    var loading = false;
    var pageSize = 20;

    fun paginate() {
        if (!loading) {

            val idx = roleRecyclerview.getChildAdapterPosition(roleRecyclerview.getChildAt(0))
            val overHalfway: Boolean = page < 0 || (idx >= (page * pageSize) / 2) || (idx == -1)

            if (overHalfway || roleRecyclerview.height == 0) {
                page += 1
                loading = true;
                context?.let {
                    DirectoryServicesSingleton.Instance().GetPractitionerRoles(filter, page, pageSize){ res, count ->
                        setHeader(header, R.string.total_number_of_roles, count)
                        res?.let { roles ->
                            roleAdapter.addPage(roles.map { PractitionerRoleItem(it) })
                            loading = false
                        }
                    }
                }
            }
        }
    }

    fun setupScrollListener() {
        roleRecyclerview.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                paginate()
            }
        })
    }

    override fun onFavorite(enable: Boolean) {
        //Temporary
        setHeader(header, if (enable) R.string.total_number_of_favourite_roles else R.string.total_number_of_roles, if (enable) 4 else 10)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_action_advanced_search -> {
                viewModel.toggleSearchView()
                return true
            }
        }
        return false
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.ic_action_advanced_search)?.isVisible = false
    }

    private fun subscribeUI() {
        viewModel.advancedSearchVisibility.observe(viewLifecycleOwner, Observer {
            val constraint = if (it) constraintExpanded else constraintCollapsed
        })
    }

    override fun onRoleClick(role: PractitionerRoleItem, forRemove: Boolean) {
        if (roomClickListener == null) {
            startActivity(RoleDetailActivity.intent(requireContext(), role, true))
        } else {
            roomClickListener?.onRoomClick(TemporaryRoom(null, role), forRemove)
        }
    }

    fun unSelectRole(role: IPractitionerRole) {
        roleAdapter.removeFromSelectedRoles(role.GetID())
    }

    fun selectRole(role: IPractitionerRole) {
        roleAdapter.addToSelectedRoles(role.GetID())
    }
}