package im.vector.health.directory.service

import android.content.Context
import im.vector.R
import im.vector.extensions.withArgs
import im.vector.health.directory.service.detail.ServiceDetailActivity
import im.vector.health.directory.service.model.HealthcareServiceItem
import im.vector.health.directory.shared.StandardDirectoryFragment
import im.vector.health.microservices.DirectoryServicesSingleton

class DirectoryServiceFragment: StandardDirectoryFragment<ServiceDirectoryAdapter,ServiceViewHolder,HealthcareServiceItem>(){
    companion object {
        fun newInstance(selectable: Boolean = false): DirectoryServiceFragment {
            return DirectoryServiceFragment().withArgs {
                putBoolean(SELECTABLE, selectable)
            }
        }
    }

    override fun constructAdapter(context: Context, selectable: Boolean): ServiceDirectoryAdapter {
        return ServiceDirectoryAdapter(context,object : ServiceClickListener {
            override fun onServiceClick(service: HealthcareServiceItem) {
                startActivity(ServiceDetailActivity.intent(requireContext(), service))
            }

            override fun onServiceFavourite(service: HealthcareServiceItem) {
                //TODO("Not yet implemented")
            }

        },selectable)
    }

    override fun getHeaderText(count: Int, favourites: Boolean): String = (if (favourites) getString(R.string.total_number_of_favourite_service) else getString(R.string.total_number_of_services)) + " " + count.toString()

    override fun getData(forPage: Int, withPageSize: Int, query: String?, addItem: (List<HealthcareServiceItem>?, Int) -> Unit) {
        DirectoryServicesSingleton.Instance().GetHealthcareServices(query, page, pageSize){ res, count ->
            addItem(res?.map { HealthcareServiceItem(it,false) }, count)
        }
    }

    override fun getDataFavourites(forPage: Int, withPageSize: Int, query: String?, addItem: (List<HealthcareServiceItem>?, Int) -> Unit) {
        DirectoryServicesSingleton.Instance().GetHealthcareServiceFavourites(query, page, pageSize){ res, count ->
            addItem(res?.map { HealthcareServiceItem(it,false) }, count)
        }
    }
}