package im.vector.directory.service

import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import im.vector.R
import im.vector.directory.BaseDirectoryFragment
import im.vector.directory.service.detail.ServiceDetailActivity
import kotlinx.android.synthetic.main.fragment_directory_service.*
import org.matrix.androidsdk.data.Room


class DirectoryServiceFragment : BaseDirectoryFragment(), ServiceClickListener {
    private lateinit var viewModel: DirectoryServiceViewModel
    private lateinit var serviceAdapter: ServiceDirectoryAdapter

    override fun getLayoutResId() = R.layout.fragment_directory_service

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.ic_action_advanced_search)?.isVisible = false
    }

    override fun onFavorite(enable: Boolean) {
        //Temporary
        setHeader(header, if (enable) R.string.total_number_of_favourite_service else R.string.total_number_of_service, if (enable) 4 else 10)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DirectoryServiceViewModel::class.java)

        serviceAdapter = ServiceDirectoryAdapter(requireContext(), this)
        (serviceRecyclerview.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        serviceRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        serviceRecyclerview.adapter = serviceAdapter
        serviceRecyclerview.setHasFixedSize(true)

        val testServiceData = mutableListOf<DummyService>()
        testServiceData.add(DummyService("1", "Service name 1", "Service Telecom 1", "Service Address 1", "Service Organization 1", true));
        testServiceData.add(DummyService("2", "Service name 2", "Service Telecom 1", "Service Address 1", "Service Organization 1", true));
        testServiceData.add(DummyService("3", "Service name 3", "Service Telecom 1", "Service Address 1", "Service Organization 1", false));
        testServiceData.add(DummyService("4", "Service name 4", "Service Telecom 1", "Service Address 1", "Service Organization 1", false));
        testServiceData.add(DummyService("5", "Service name 5", "Service Telecom 1", "Service Address 1", "Service Organization 1", false));
        testServiceData.add(DummyService("6", "Service name 6", "Service Telecom 1", "Service Address 1", "Service Organization 1", true));
        testServiceData.add(DummyService("7", "Service name 7", "Service Telecom 1", "Service Address 1", "Service Organization 1", false));

        serviceAdapter.setData(testServiceData)
        setHeader(header, R.string.total_number_of_roles,10)
    }

    override fun onServiceClick(service: DummyService) {
        startActivity(ServiceDetailActivity.intent(requireContext(), service))
    }
}