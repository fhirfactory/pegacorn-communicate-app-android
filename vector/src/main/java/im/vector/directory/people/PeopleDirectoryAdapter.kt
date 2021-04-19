package im.vector.directory.people

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import im.vector.Matrix
import im.vector.R
import im.vector.adapters.ParticipantAdapterItem
import im.vector.adapters.VectorParticipantsAdapter
import im.vector.directory.people.model.DirectoryPeople
import im.vector.directory.role.OnDataSetChange
import im.vector.ui.themes.ThemeUtils.getColor
import im.vector.util.VectorUtils
import im.vector.view.VectorCircularImageView
import kotlinx.android.synthetic.main.item_directory_people.view.*
import org.matrix.androidsdk.MXSession


class PeopleDirectoryAdapter(val context: Context, private val onClickListener: PeopleClickListener, private val selectable: Boolean = false) :
        RecyclerView.Adapter<PeopleDirectoryAdapter.PeopleViewHolder>(), OnDataSetChange {
    private val people = mutableListOf<DirectoryPeople>()
    var mSession: MXSession? = null
    var textSize: Float = 0.0F
    var spanTextBackgroundColor: Int
    var spanTextColor: Int
    var selectedIds: MutableSet<String>? = null

    init {
        mSession = Matrix.getInstance(context).defaultSession
        textSize = 12 * context.resources.displayMetrics.scaledDensity // sp to px
        spanTextBackgroundColor = getColor(context, R.attr.vctr_text_spanable_text_background_color)
        spanTextColor = getColor(context, R.attr.vctr_text_reverse_color)
        if (selectable) {
            selectedIds = mutableSetOf()
        }
    }

    inner class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avatar: VectorCircularImageView? = null
        var favouriteButton: ImageView? = null
        var expandableIcon: ImageView? = null
        var officialName: TextView? = null
        var jobTitle: TextView? = null
        var selectionRadioImageView: ImageView? = null
        var organisationText: TextView? = null
        var businessUnitText: TextView? = null
        var statusText: TextView? = null

        init {
            avatar = itemView.avatar
            favouriteButton = itemView.favoriteIcon
            expandableIcon = itemView.expandableIcon
            officialName = itemView.officialName
            jobTitle = itemView.jobTitle
            selectionRadioImageView = itemView.selected
            organisationText = itemView.organisationText
            businessUnitText = itemView.businessUnitText
            statusText = itemView.statusText
        }

        fun bind(context: Context, session: MXSession?, people: DirectoryPeople, onDataSetChange: OnDataSetChange, position: Int, selection: Boolean? = false) {
            VectorUtils.loadRoomAvatar(context, session, avatar, people)
            selectionRadioImageView?.setImageResource(if (selection == true) R.drawable.ic_radio_button_checked else R.drawable.ic_radio_button_unchecked)
            officialName?.text = people.officialName
            jobTitle?.text = people.jobTitle
            organisationText?.text = "Organisation: ${people.organisations}"
            businessUnitText?.text = "Business Unit: ${people.businessUnits}"
            statusText?.text = "online"

            if (people.expanded) {
                expandableIcon?.animate()?.setDuration(200)?.rotation(180F)
                organisationText?.visibility = View.VISIBLE
                businessUnitText?.visibility = View.VISIBLE
            } else {
                expandableIcon?.animate()?.setDuration(200)?.rotation(0F)
                organisationText?.visibility = View.GONE
                businessUnitText?.visibility = View.GONE
            }
            expandableIcon?.setOnClickListener {
                people.expanded = !people.expanded
                onDataSetChange.onDataChange(position)
            }
        }
    }

    fun setData(people: MutableList<DirectoryPeople>) {
        this.people.clear()
        this.people.addAll(people)
    }

    fun setData(participants: VectorParticipantsAdapter){
        this.people.clear()
        for (i in 0 until participants.groupCount) {
            for (j in 0 until participants.getChildrenCount(i)) {
                val child: ParticipantAdapterItem = participants.getChild(i,j) as ParticipantAdapterItem
                if (child.mUserId == null) continue
                this.people.add(this.people.count(), DirectoryPeople(child.mUserId, child.mDisplayName, "Placeholder", child.mAvatarUrl, "Placeholder", "Placeholder"))
            }
        }
        notifyDataSetChanged()
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PeopleViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_directory_people, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return PeopleViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        holder.bind(context, mSession, people[position], this, position, checkSelection(people[position]))
        holder.selectionRadioImageView?.visibility = if (selectable) View.VISIBLE else View.GONE
        holder.itemView.setOnClickListener {
            if (selectable) {
                val added = selectedIds?.add(people[position].id)
                if (added == false) {
                    selectedIds?.remove(people[position].id)
                }
                notifyItemChanged(position)
                onClickListener.onPeopleClick(people[position], !(added ?: true))
            } else {
                onClickListener.onPeopleClick(people[position])
            }
        }
    }

    private fun checkSelection(people: DirectoryPeople): Boolean {
        if (!selectable) return false
        selectedIds?.forEach { id ->
            if (id == people.id)
                return true
        }
        return false
    }

    fun removeFromSelectedPeople(id: String) {
        selectedIds?.remove(id)
        notifyDataSetChanged()
    }

    fun addToSelectedPeople(id: String) {
        if (selectedIds?.add(id) == true) {
            notifyDataSetChanged()
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = people.size

    override fun onDataChange(position: Int) {
        notifyItemChanged(position)
    }
}

interface PeopleClickListener {
    fun onPeopleClick(directoryPeople: DirectoryPeople, forRemove: Boolean = false)
    fun onPeopleFavorite(directoryPeople: DirectoryPeople)
}