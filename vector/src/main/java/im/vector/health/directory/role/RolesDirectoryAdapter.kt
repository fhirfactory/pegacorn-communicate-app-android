package im.vector.health.directory.role

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import im.vector.Matrix
import im.vector.R
import im.vector.health.directory.role.model.PractitionerRoleItem
import im.vector.health.directory.shared.IStandardDirectoryAdapter
import im.vector.health.microservices.DirectoryConnector
import im.vector.health.microservices.APIModel.FavouriteTypes
import im.vector.health.microservices.DirectoryServicesSingleton
import im.vector.health.microservices.Interfaces.IPractitionerRole
import im.vector.util.VectorUtils
import im.vector.view.VectorCircularImageView
import kotlinx.android.synthetic.main.item_directory_people.view.favoriteIcon
import org.matrix.androidsdk.MXSession


class RolesDirectoryAdapter(val context: Context, private val onClickListener: RoleClickListener, private val selectable: Boolean = false) :
        RecyclerView.Adapter<RoleViewHolder>(), OnDataSetChange, IStandardDirectoryAdapter<PractitionerRoleItem> {
    private val roles = mutableListOf<PractitionerRoleItem>()
    var mSession: MXSession? = null
    var textSize: Float = 0.0F
    var selectedIds: MutableSet<String>? = null

    init {
        mSession = Matrix.getInstance(context).defaultSession

        if (selectable) {
            selectedIds = mutableSetOf()
        }
    }

    override fun setData(items: List<PractitionerRoleItem>) {
        this.roles.clear()
        this.roles.addAll(items)
        notifyDataSetChanged()
    }

    override fun addPage(items: List<PractitionerRoleItem>) {
        this.roles.addAll(items)
        notifyDataSetChanged()
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RoleViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_directory_role, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return RoleViewHolder(view)
    }

    private fun checkSelection(role: PractitionerRoleItem): Boolean? {
        if (!selectable) return null
        selectedIds?.forEach { id ->
            if (id == role.GetID())
                return true
        }
        return false
    }

    fun addToSelectedRoles(id: String) {
        if (selectedIds?.add(id) == true) {
            notifyDataSetChanged()
        }
    }

    fun removeFromSelectedRoles(id: String) {
        selectedIds?.remove(id)
        notifyDataSetChanged()
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RoleViewHolder, position: Int) {
        holder.bind(context, mSession, roles[position], this, position, onClickListener, false, checkSelection(roles[position]))
        holder.selectionRadioImageView?.visibility = if (selectable) VISIBLE else GONE
        holder.itemView.setOnClickListener {
            if (selectable) {
                val added = selectedIds?.add(roles[position].GetID())
                if (added == false) {
                    selectedIds?.remove(roles[position].GetID())
                }
                notifyItemChanged(position)
                onClickListener.onRoleClick(roles[position], !(added ?: true))
            } else {
                onClickListener.onRoleClick(roles[position])
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = roles.size

    override fun onDataChange(position: Int) {
        notifyItemChanged(position)
    }
}

class RoleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var avatar: VectorCircularImageView? = null
    var expandableIcon: ImageView? = null
    var selectionRadioImageView: ImageView? = null
    var officialName: TextView? = null
    var secondaryName: TextView? = null
    var roleText: TextView? = null
    var categoryText: TextView? = null
    var orgUnitText: TextView? = null
    var locationText: TextView? = null
    var heading: TextView? = null
    var favouriteButton: ImageView? = null
    var roleFilledTextView: TextView? = null
    var favourite: Boolean = false

    init {
        heading = itemView.findViewById(R.id.heading)
        avatar = itemView.findViewById(R.id.avatar)
        expandableIcon = itemView.findViewById(R.id.expandableIcon)
        selectionRadioImageView = itemView.findViewById(R.id.selected)
        officialName = itemView.findViewById(R.id.officialName)
        secondaryName = itemView.findViewById(R.id.secondaryName)
        roleText = itemView.findViewById(R.id.roleText)
        categoryText = itemView.findViewById(R.id.categoryText)
        orgUnitText = itemView.findViewById(R.id.orgUnitText)
        locationText = itemView.findViewById(R.id.locationText)
        favouriteButton = itemView.favoriteIcon
        roleFilledTextView = itemView.findViewById(R.id.roleFilledTextView)
        favourite = false
    }

    fun updateFavourite() {
        if (favourite) {
            favouriteButton?.setImageResource(R.drawable.ic_material_star_black)
        } else {
            favouriteButton?.setImageResource(R.drawable.ic_material_star_border_black)
        }
    }

    fun bind(context: Context, session: MXSession?, role: PractitionerRoleItem, onDataSetChange: OnDataSetChange, position: Int, onClickListener: RoleClickListener?, showHeader: Boolean = false, selection: Boolean? = null) {
        VectorUtils.loadRoomAvatar(context, session, avatar, role)
        heading?.visibility = if (showHeader) VISIBLE else GONE
        officialName?.text = role.GetLongName()
        secondaryName?.text = role.GetShortName()
        if(selection==null){
            selectionRadioImageView?.visibility = GONE
        } else {
            selectionRadioImageView?.visibility = VISIBLE
            selectionRadioImageView?.setImageResource(if (selection == true) R.drawable.ic_radio_button_checked else R.drawable.ic_radio_button_unchecked)
        }
        if (role.expanded) {
            expandableIcon?.animate()?.setDuration(200)?.rotation(180F)
            roleText?.visibility = VISIBLE
            orgUnitText?.visibility = VISIBLE
            categoryText?.visibility = VISIBLE
            locationText?.visibility = VISIBLE
        } else {
            expandableIcon?.animate()?.setDuration(200)?.rotation(0F)
            roleText?.visibility = GONE
            orgUnitText?.visibility = GONE
            categoryText?.visibility = GONE
            locationText?.visibility = GONE
        }
        if (role.GetActive()) {
            //roleFilledTextView?.text = context.getText(R.string.role_filled)
            //stealing the default colour from another text view is like, the best viable way of doing this, apparently
            //otherwise, the color has to come from resource values -- so would be theme dependent (as it true of the use of vector_warning_color)
            //roleFilledTextView?.setTextColor(officialName?.textColors)
        } else {
            roleFilledTextView?.text = context.getText(R.string.role_unfilled)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                roleFilledTextView?.setTextColor(context.getColor(R.color.vector_warning_color))
            } else {
                roleFilledTextView?.setTextColor(context.applicationContext.resources.getColor(R.color.vector_warning_color))
            }
        }

        DirectoryServicesSingleton.Instance().CheckFavourite(FavouriteTypes.PractitionerRoles,role.GetID()) {
            this.favourite = it
            this.updateFavourite()
        }

        favouriteButton?.setOnClickListener {
            this.favourite = !this.favourite
            this.updateFavourite()
            if (this.favourite) {
                DirectoryServicesSingleton.Instance().AddFavourite(FavouriteTypes.PractitionerRoles,role.GetID())
            } else {
                DirectoryServicesSingleton.Instance().RemoveFavourite(FavouriteTypes.PractitionerRoles,role.GetID())
            }
        }

        roleText?.text = "Role: ${role.GetRoleName()}"
        orgUnitText?.text = "Org Unit: ${role.GetOrgName()}"
        categoryText?.text = "Category: ${role.GetRoleCategory()}"
        locationText?.text = "Location: ${role.GetLocation()}"

        expandableIcon?.setOnClickListener {
            role.expanded = !role.expanded
            onDataSetChange.onDataChange(position)
        }
    }
}

interface RoleClickListener {
    fun onRoleClick(role: PractitionerRoleItem, forRemove: Boolean = false)
}

interface OnDataSetChange {
    fun onDataChange(position: Int)
}

