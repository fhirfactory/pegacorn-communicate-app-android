package im.vector.health.directory.service

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import im.vector.R
import im.vector.health.directory.service.model.HealthcareServiceItem
import im.vector.health.directory.shared.HandlesAPIErrors
import im.vector.health.directory.shared.IStandardDirectoryAdapter
import im.vector.health.microservices.APIModel.FavouriteTypes
import im.vector.health.microservices.DirectoryServicesSingleton
import kotlinx.android.synthetic.main.item_directory_service.view.*


class ServiceDirectoryAdapter(val context: Context, private val onClickListener: ServiceClickListener, private val selectable: Boolean = false) :
        RecyclerView.Adapter<ServiceViewHolder>(), OnDataSetChange, IStandardDirectoryAdapter<HealthcareServiceItem> {
    private val services = mutableListOf<HealthcareServiceItem>()

    override fun setData(items: List<HealthcareServiceItem>) {
        this.services.clear()
        this.services.addAll(items)
    }

    override fun addPage(items: List<HealthcareServiceItem>) {
        this.services.addAll(items)
        notifyDataSetChanged()
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ServiceViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_directory_service, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return ServiceViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(context, services[position], onClickListener)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = services.size

    override fun onDataChange(position: Int) {
        notifyItemChanged(position)
    }
}

interface OnDataSetChange {
    fun onDataChange(position: Int)
}

class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), HandlesAPIErrors {
    var officialName: TextView? = null
    var locationCode: TextView? = null
    var serviceOrgUnit: TextView? = null
    var favoriteIcon: ImageView? = null
    var favourite: Boolean = false
    lateinit var context: Context

    fun updateFavourite() {
        if (favourite) {
            favoriteIcon?.setImageResource(R.drawable.ic_material_star_black)
        } else {
            favoriteIcon?.setImageResource(R.drawable.ic_material_star_border_black)
        }
    }

    init {
        officialName = itemView.officialName
        locationCode = itemView.locationCode
        serviceOrgUnit = itemView.locationDetail
        favoriteIcon = itemView.favoriteIcon
    }

    fun bind(context: Context, service: HealthcareServiceItem, onClickListener: ServiceClickListener?) {
        this.context = context
        favoriteIcon?.setOnClickListener {
            onClickListener?.onServiceFavourite(service)
        }
        DirectoryServicesSingleton.Instance().CheckFavourite(FavouriteTypes.Service,service.GetID(), {
            this.favourite = it
            this.updateFavourite()
        }){

        }

        updateFavourite()
        favoriteIcon?.setOnClickListener {
            this.favourite = !this.favourite
            this.updateFavourite()
            if (this.favourite) {
                DirectoryServicesSingleton.Instance().AddFavourite(FavouriteTypes.Service,service.GetID()){
                    displayError(it)
                }
            } else {
                DirectoryServicesSingleton.Instance().RemoveFavourite(FavouriteTypes.Service,service.GetID()){
                    displayError(it)
                }
            }
        }

        officialName?.text = service.GetLongName()
        serviceOrgUnit?.text = service.GetOrganisationUnit()

        itemView.setOnClickListener {
            onClickListener?.onServiceClick(service)
        }

    }

    override fun getCurrentContext(): Context = context
}
interface ServiceClickListener {
    fun onServiceClick(service: HealthcareServiceItem)
    fun onServiceFavourite(service: HealthcareServiceItem)
}