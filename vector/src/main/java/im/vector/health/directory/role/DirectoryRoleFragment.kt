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
import im.vector.health.directory.people.model.TemporaryRoom
import im.vector.health.directory.role.detail.RoleDetailActivity
import im.vector.health.directory.role.model.*
import im.vector.extensions.withArgs
import im.vector.health.microservices.DirectoryConnector
import kotlinx.android.synthetic.main.fragment_directory_role.*
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

/*        categoryAdapter = DropDownAdapter(requireContext(), R.layout.drop_down_item)
        categoryEditText.threshold = 1
        categoryEditText.setAdapter(categoryAdapter)

        organisationUnitAdapter = DropDownAdapter(requireContext(), R.layout.drop_down_item)
        organisationEditText.threshold = 1
        organisationEditText.setAdapter(organisationUnitAdapter)

        specialityAdapter = DropDownAdapter(requireContext(), R.layout.drop_down_item)
        specialityEditText.threshold = 1
        specialityEditText.setAdapter(specialityAdapter)

        locationAdapter = DropDownAdapter(requireContext(), R.layout.drop_down_item)
        locationEditText.threshold = 1
        locationEditText.setAdapter(locationAdapter)*/



/*
        //test data
        val testDropDownData = mutableListOf<DropDownItem>()
        for (i in 1..5) {
            testDropDownData.add(DropDownItem(i, "Item 1"))
        }
        categoryAdapter.addData(testDropDownData)
        organisationUnitAdapter.addData(testDropDownData)
        specialityAdapter.addData(testDropDownData)
        locationAdapter.addData(testDropDownData)
*/
        /*
        testRoleData.add(DummyRole("1", "ED Acute SRMO", "Emergency Department  Acute Senior Resident Medical Officer Medical Officer", null, "ED {Emergency Department}", arrayListOf(Role("1", "Senior Resident Medical Officer", "Doctor")),
                arrayListOf(Speciality("1", "Emergency")), arrayListOf(DummyLocation("1", "CH {Canberra Hospital}")), arrayListOf(Team("1", "Emergency Department Acute"))))

        testRoleData.add(DummyRole("2", "ED Acute RMO", "Emergency Department  Acute Resident Medical Officer", null, "ED {Emergency Department}", arrayListOf(Role("1", "Resident", "Doctor")),
                arrayListOf(Speciality("1", "Emergency")), arrayListOf(DummyLocation("1", "CH {Canberra Hospital}")), arrayListOf(Team("1", "Emergency Department Acute"))))

        testRoleData.add(DummyRole("3", "ED Acute Intern", "Emergency Department  Acute Intern", null, "ED {Emergency Department}", arrayListOf(Role("1", "Intern", "Doctor")),
                arrayListOf(Speciality("1", "Emergency")), arrayListOf(DummyLocation("1", "CH {Canberra Hospital}")), arrayListOf(Team("1", "Emergency Department Acute"))))

        testRoleData.add(DummyRole("4", "ED Acute Consultant", "Emergency Department  Acute Consultant", null, "ED {Emergency Department}", arrayListOf(Role("1", "Consultant", "Doctor")),
                arrayListOf(Speciality("1", "Emergency")), arrayListOf(DummyLocation("1", "CH {Canberra Hospital}")), arrayListOf(Team("1", "Emergency Department Acute"))))

        testRoleData.add(DummyRole("5", "ED Acute East Nurse", "Emergency Department  Acute East Nurse", null, "ED {Emergency Department}", arrayListOf(Role("1", "Emergency Department Nurse", "Nursing and Midwifery")),
                arrayListOf(Speciality("1", "Emergency")), arrayListOf(DummyLocation("1", "CH {Canberra Hospital}")), arrayListOf(Team("1", "Emergency Department Acute"))))

        testRoleData.add(DummyRole("6", "ED Acute East PL", "Emergency Department  Acute East Pod Leader", null, "ED {Emergency Department}", arrayListOf(Role("1", "Nursing Team Leader", "Nursing and Midwifery")),
                arrayListOf(Speciality("1", "Emergency")), arrayListOf(DummyLocation("1", "CH {Canberra Hospital}")), arrayListOf(Team("1", "Emergency Department Acute"))))

        testRoleData.add(DummyRole("7", "ED Acute North Nurse", "Emergency Department  Acute North Nurse", null, "ED {Emergency Department}", arrayListOf(Role("1", "Emergency Department Nurse", "Nursing and Midwifery")),
                arrayListOf(Speciality("1", "Emergency")), arrayListOf(DummyLocation("1", "CH {Canberra Hospital}")), arrayListOf(Team("1", "Emergency Department Acute"))))

        testRoleData.add(DummyRole("8", "ED Acute North Nurse", "Emergency Department  Acute North Nurse", null, "ED {Emergency Department}", arrayListOf(Role("1", "Emergency Department Nurse", "Nursing and Midwifery")),
                arrayListOf(Speciality("1", "Emergency")), arrayListOf(DummyLocation("1", "CH {Canberra Hospital}")), arrayListOf(Team("1", "Emergency Department Acute"))))
         */

        DirectoryConnector.initializeWithPractitionerId("donald.duck@acme.org")

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
                    DirectoryConnector.getPractitionerRoles(page, pageSize, it, filter){
                        it?.let {
                            roleAdapter.addPage(it)
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

    override fun onRoleClick(role: DummyRole, forRemove: Boolean) {
        if (roomClickListener == null) {
            startActivity(RoleDetailActivity.intent(requireContext(), role, true))
        } else {
            roomClickListener?.onRoomClick(TemporaryRoom(null, role), forRemove)
        }
    }

    fun unSelectRole(role: DummyRole) {
        roleAdapter.removeFromSelectedRoles(role.id)
    }

    fun selectRole(role: DummyRole) {
        roleAdapter.addToSelectedRoles(role.id)
    }
}