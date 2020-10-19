package im.vector.directory.people

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import im.vector.Matrix
import im.vector.R
import im.vector.directory.people.model.DirectoryPeople
import im.vector.directory.role.OnDataSetChange
import im.vector.directory.role.model.DummyRole
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
        if(selectable){
            selectedIds = mutableSetOf()
        }
    }

    inner class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avatar: VectorCircularImageView? = null
        var favouriteButton: ImageView? = null
        var expandableIcon: ImageView? = null
        var officialName: TextView? = null
        var jobTitle: TextView? = null
        var description: TextView? = null
        var selectionRadioImageView: ImageView? = null

        init {
            avatar = itemView.avatar
            favouriteButton = itemView.favoriteIcon
            expandableIcon = itemView.expandableIcon
            officialName = itemView.officialName
            jobTitle = itemView.jobTitle
            description = itemView.description
            selectionRadioImageView = itemView.selected
        }

        fun bind(context: Context, session: MXSession?, people: DirectoryPeople, onDataSetChange: OnDataSetChange, position: Int, selection: Boolean? = false) {
            VectorUtils.loadRoomAvatar(context, session, avatar, people)
            selectionRadioImageView?.setImageResource(if(selection == true) R.drawable.ic_radio_button_checked else R.drawable.ic_radio_button_unchecked)
            officialName?.text = people.officialName
            jobTitle?.text = people.jobTitle
            description?.text = people.getSpannableStringBuilder(spanTextBackgroundColor, spanTextColor, textSize, "Organisation", people.organisations).append(people.getSpannableStringBuilder(spanTextBackgroundColor, spanTextColor, textSize, "Business Unit", people.businessUnits))
            if (people.expanded) {
                expandableIcon?.animate()?.setDuration(200)?.rotation(180F)
                description?.visibility = View.VISIBLE
            } else {
                expandableIcon?.animate()?.setDuration(200)?.rotation(0F)
                description?.visibility = View.GONE
            }
            expandableIcon?.setOnClickListener {
                people.expanded = !people.expanded
                onDataSetChange.onDataChange(position)
            }
        }
    }

    fun setData(roles: MutableList<DirectoryPeople>) {
        this.people.clear()
        this.people.addAll(roles)
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
            if(selectable) {
                val added = selectedIds?.add(people[position].id)
                if(added == false) {
                    selectedIds?.remove(people[position].id)
                }
                notifyItemChanged(position)
                onClickListener.onPeopleClick(people[position], !(added ?: true))
            } else {
                onClickListener.onPeopleClick(people[position])
            }
        }
    }

    private fun checkSelection(people: DirectoryPeople): Boolean{
        if(!selectable) return false
        selectedIds?.forEach { id ->
            if(id == people.id)
                return true
        }
        return false
    }

    fun removePeople(id: String){
        selectedIds?.remove(id)
        notifyDataSetChanged()
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