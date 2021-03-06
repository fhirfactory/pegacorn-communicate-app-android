package im.vector.directory.role

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import im.vector.Matrix
import im.vector.R
import im.vector.directory.role.model.DummyRole
import im.vector.ui.themes.ThemeUtils.getColor
import im.vector.util.VectorUtils
import im.vector.view.VectorCircularImageView
import org.matrix.androidsdk.MXSession


class RolesDirectoryAdapter(val context: Context, private val onClickListener: RoleClickListener) :
        RecyclerView.Adapter<RoleViewHolder>(), OnDataSetChange {
    private val roles = mutableListOf<DummyRole>()
    var mSession: MXSession? = null
    var textSize: Float = 0.0F
    var spanTextBackgroundColor: Int
    var spanTextColor: Int

    init {
        mSession = Matrix.getInstance(context).defaultSession
        textSize = 12 * context.resources.displayMetrics.scaledDensity // sp to px
        spanTextBackgroundColor = getColor(context, R.attr.vctr_text_spanable_text_background_color)
        spanTextColor = getColor(context, R.attr.vctr_text_reverse_color)
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
        holder.bind(context, mSession, roles[position], spanTextBackgroundColor, spanTextColor, textSize, this, position)
        holder.itemView.setOnClickListener {
            onClickListener.onRoleClick(roles[position])
        }
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
    var description: TextView? = null

    init {
        avatar = itemView.findViewById(R.id.avatar)
        expandableIcon = itemView.findViewById(R.id.expandableIcon)
        officialName = itemView.findViewById(R.id.officialName)
        secondaryName = itemView.findViewById(R.id.secondaryName)
        description = itemView.findViewById(R.id.description)
    }

    fun bind(context: Context, session: MXSession?, role: DummyRole, spanTextBackgroundColor: Int, spanTextColor: Int, textSize: Float, onDataSetChange: OnDataSetChange, position: Int) {
        VectorUtils.loadRoomAvatar(context, session, avatar, role)
        officialName?.text = role.officialName
        secondaryName?.text = role.secondaryName
        if (role.expanded) {
            expandableIcon?.animate()?.setDuration(200)?.rotation(180F)
            description?.visibility = View.VISIBLE
        } else {
            expandableIcon?.animate()?.setDuration(200)?.rotation(0F)
            description?.visibility = View.GONE
        }
        val stringBuilder = SpannableStringBuilder()
        for (rl in role.roles) {
            stringBuilder.append(rl.getSpannableStringBuilder(spanTextBackgroundColor, spanTextColor, textSize)).append(" ")
        }
        for (sp in role.speciality) {
            stringBuilder.append(sp.getSpannableStringBuilder(spanTextBackgroundColor, spanTextColor, textSize)).append(" ")
        }
        for (lc in role.location) {
            stringBuilder.append(lc.getSpannableStringBuilder(spanTextBackgroundColor, spanTextColor, textSize))
        }
        description?.text = stringBuilder
        expandableIcon?.setOnClickListener {
            role.expanded = !role.expanded
            onDataSetChange.onDataChange(position)
        }
    }
}