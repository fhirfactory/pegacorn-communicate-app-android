package im.vector.directory.role

import android.content.Context
import android.text.SpannableStringBuilder
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
import im.vector.directory.role.model.DummyRole
import im.vector.util.VectorUtils
import im.vector.view.VectorCircularImageView
import kotlinx.android.synthetic.main.item_directory_people.view.*
import org.matrix.androidsdk.MXSession


class RolesDirectoryAdapter(val context: Context, private val onClickListener: RoleClickListener, private val selectable: Boolean = false) :
        RecyclerView.Adapter<RoleViewHolder>(), OnDataSetChange {
    private val roles = mutableListOf<DummyRole>()
    var mSession: MXSession? = null
    var textSize: Float = 0.0F
    var selectedIds: MutableSet<String>? = null

    init {
        mSession = Matrix.getInstance(context).defaultSession

        if (selectable) {
            selectedIds = mutableSetOf()
        }
    }

    fun setData(roles: MutableList<DummyRole>) {
        this.roles.clear()
        this.roles.addAll(roles)
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

    private fun checkSelection(role: DummyRole): Boolean {
        if (!selectable) return false
        selectedIds?.forEach { id ->
            if (id == role.id)
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
        holder.bind(context, mSession, roles[position], this, position, onClickListener, checkSelection(roles[position]))
        holder.selectionRadioImageView?.visibility = if (selectable) VISIBLE else GONE
        holder.itemView.setOnClickListener {
            if (selectable) {
                val added = selectedIds?.add(roles[position].id)
                if (added == false) {
                    selectedIds?.remove(roles[position].id)
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
    }

    fun bind(context: Context, session: MXSession?, role: DummyRole, onDataSetChange: OnDataSetChange, position: Int, onClickListener: RoleClickListener?, showHeader: Boolean = false, selection: Boolean? = false) {
        VectorUtils.loadRoomAvatar(context, session, avatar, role)
        heading?.visibility = if (showHeader) VISIBLE else GONE
        officialName?.text = role.officialName
        secondaryName?.text = role.secondaryName
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

        roleText?.text = "Role: ${role.roles.joinToString(", ")}"
        orgUnitText?.text = "Org Unit: ${role.organizationUnit}"
        categoryText?.text = "Category: ${role.speciality.joinToString(", ")}"
        locationText?.text = "Location: ${role.location.joinToString(", ")}"

        expandableIcon?.setOnClickListener {
            role.expanded = !role.expanded
            onDataSetChange.onDataChange(position)
        }
    }
}

interface RoleClickListener {
    fun onRoleClick(role: DummyRole, forRemove: Boolean = false)
}

interface OnDataSetChange {
    fun onDataChange(position: Int)
}

