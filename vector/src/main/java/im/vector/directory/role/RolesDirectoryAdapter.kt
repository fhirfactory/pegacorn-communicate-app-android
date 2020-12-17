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


class RolesDirectoryAdapter(val context: Context, private val onClickListener: RoleClickListener) :
        RecyclerView.Adapter<RoleViewHolder>(), OnDataSetChange {
    private val roles = mutableListOf<DummyRole>()
    var mSession: MXSession? = null

    init {
        mSession = Matrix.getInstance(context).defaultSession
    }


    fun setData(roles: MutableList<DummyRole>) {
        this.roles.clear()
        this.roles.addAll(roles)
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

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RoleViewHolder, position: Int) {
        holder.bind(context, mSession, roles[position], this, position, onClickListener)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = roles.size

    override fun onDataChange(position: Int) {
        notifyItemChanged(position)
    }
}

interface RoleClickListener {
    fun onRoleClick(role: DummyRole)
}

interface OnDataSetChange {
    fun onDataChange(position: Int)
}

class RoleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var avatar: VectorCircularImageView? = null
    var expandableIcon: ImageView? = null
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
        officialName = itemView.findViewById(R.id.officialName)
        secondaryName = itemView.findViewById(R.id.secondaryName)
        roleText = itemView.findViewById(R.id.roleText)
        categoryText = itemView.findViewById(R.id.categoryText)
        orgUnitText = itemView.findViewById(R.id.orgUnitText)
        locationText = itemView.findViewById(R.id.locationText)
        favouriteButton = itemView.favoriteIcon
    }

    fun bind(context: Context, session: MXSession?, role: DummyRole, onDataSetChange: OnDataSetChange, position: Int, onClickListener: RoleClickListener?, showHeader: Boolean = false) {
        VectorUtils.loadRoomAvatar(context, session, avatar, role)
        heading?.visibility = if (showHeader) VISIBLE else GONE
        officialName?.text = role.officialName
        secondaryName?.text = role.secondaryName
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
        itemView.setOnClickListener {
            onClickListener?.onRoleClick(role)
        }
    }
}