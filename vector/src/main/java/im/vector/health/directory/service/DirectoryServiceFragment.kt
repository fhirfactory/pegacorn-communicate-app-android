package im.vector.health.directory.service

import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import im.vector.R
import im.vector.extensions.withArgs
import im.vector.health.directory.BaseDirectoryFragment
import im.vector.health.directory.role.DirectoryRoleFragment
import im.vector.health.directory.role.RolesDirectoryAdapter
import im.vector.health.directory.role.model.PractitionerRoleItem
import im.vector.health.directory.service.DirectoryServiceFragment.Companion.SELECTABLE
import im.vector.health.directory.service.detail.ServiceDetailActivity
import im.vector.health.directory.service.model.HealthcareServiceItem
import im.vector.health.microservices.DirectoryServicesSingleton
import kotlinx.android.synthetic.main.fragment_directory_role.*
import kotlinx.android.synthetic.main.fragment_directory_service.*
import kotlinx.android.synthetic.main.fragment_directory_service.header
import org.matrix.androidsdk.data.Room


class DirectoryServiceFragment : BaseDirectoryFragment(), ServiceClickListener {
    companion object {
        private const val SELECTABLE = "SELECTABLE"

        fun newInstance(selectable: Boolean = false): DirectoryServiceFragment {
            return DirectoryServiceFragment().withArgs {
                putBoolean(SELECTABLE, selectable)
            }
        }
    }

    private lateinit var viewModel: DirectoryServiceViewModel
    private lateinit var serviceAdapter: ServiceDirectoryAdapter
    private var filter: String? = null

    override fun getLayoutResId() = R.layout.fragment_directory_service

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.ic_action_advanced_search)?.isVisible = false
    }

    override fun onFavorite(enable: Boolean) {
        //Temporary
        setHeader(header, if (enable) R.string.total_number_of_favourite_service else R.string.total_number_of_services, if (enable) 4 else 10)
    }

    override fun filter(with: String?) {
        //TODO("Not yet implemented")
    }

    override fun getRooms(): MutableList<Room> {
        TODO("Not yet implemented")
    }

    override fun onFilter(pattern: String?, listener: OnFilterListener?) {
        TODO("Not yet implemented")
    }

    override fun onResetFilter() {
        TODO("Not yet implemented")
    }

    fun initializeList() {
        val selectable = arguments?.getBoolean(SELECTABLE, false)
        serviceAdapter = ServiceDirectoryAdapter(requireContext(), this, selectable ?: false)
        (serviceRecyclerview.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        serviceRecyclerview.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        serviceRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        serviceRecyclerview.adapter = serviceAdapter
        serviceRecyclerview.setHasFixedSize(true)

        serviceRecyclerview.scrollToPosition(0)

        loading = false
        page = -1
        paginate()
        setupScrollListener()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DirectoryServiceViewModel::class.java)

        initializeList()


    }

    var page = -1;
    var loading = false;
    var pageSize = 20;

    fun paginate() {
        if (!loading) {

            val idx = serviceRecyclerview.getChildAdapterPosition(serviceRecyclerview.getChildAt(0))
            val overHalfway: Boolean = page < 0 || (idx >= (page * pageSize) / 2) || (idx == -1)

            if (overHalfway || serviceRecyclerview.height == 0) {
                page += 1
                loading = true;
                context?.let {
                    DirectoryServicesSingleton.Instance().GetHealthcareServices(filter, page, pageSize){ res, count ->
                        setHeader(header, R.string.total_number_of_services, count)
                        res?.let { roles ->
                            serviceAdapter.addPage(roles.map { HealthcareServiceItem(it, false) })
                            loading = false
                        }
                    }
                }
            }
        }
    }

    fun setupScrollListener() {
        serviceRecyclerview.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                paginate()
            }
        })
    }

    override fun onServiceClick(service: HealthcareServiceItem) {
        startActivity(ServiceDetailActivity.intent(requireContext(), service))
    }

    override fun onServiceFavourite(service: HealthcareServiceItem) {
        TODO("Not yet implemented")
    }
}