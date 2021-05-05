package im.vector.health.directory.service

import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import im.vector.R
import im.vector.health.directory.BaseDirectoryFragment
import im.vector.health.directory.service.detail.ServiceDetailActivity
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DirectoryServiceViewModel::class.java)

        serviceAdapter = ServiceDirectoryAdapter(requireContext(), this)
        (serviceRecyclerview.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        serviceRecyclerview.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        serviceRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        serviceRecyclerview.adapter = serviceAdapter
        serviceRecyclerview.setHasFixedSize(true)

        val testServiceData = mutableListOf<DummyService>()
        testServiceData.add(DummyService("1", "Service name 1", "Service Telecom 1", "Service Address 1", "Service Organization 1", true));
        testServiceData.add(DummyService("2", "Service name 2", "Service Telecom 2", "Service Address 2", "Service Organization 2", true));
        testServiceData.add(DummyService("3", "Service name 3", "Service Telecom 3", "Service Address 3", "Service Organization 3", false));
        testServiceData.add(DummyService("4", "Service name 4", "Service Telecom 4", "Service Address 4", "Service Organization 4", false));
        testServiceData.add(DummyService("5", "Service name 5", "Service Telecom 5", "Service Address 5", "Service Organization 5", false));
        testServiceData.add(DummyService("6", "Service name 6", "Service Telecom 6", "Service Address 6", "Service Organization 6", true));
        testServiceData.add(DummyService("7", "Service name 7", "Service Telecom 7", "Service Address 7", "Service Organization 7", false));

        serviceAdapter.setData(testServiceData)
        setHeader(header, R.string.total_number_of_services, 10)
    }

    override fun onServiceClick(service: DummyService) {
        startActivity(ServiceDetailActivity.intent(requireContext(), service))
    }

    override fun onServiceFavourite(service: DummyService) {
        TODO("Not yet implemented")
    }
}