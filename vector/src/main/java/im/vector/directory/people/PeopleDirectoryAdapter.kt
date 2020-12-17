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
import im.vector.ui.themes.ThemeUtils.getColor
import im.vector.util.VectorUtils
import im.vector.view.VectorCircularImageView
import kotlinx.android.synthetic.main.item_directory_people.view.*
import org.matrix.androidsdk.MXSession


class PeopleDirectoryAdapter(val context: Context, private val onClickListener: PeopleClickListener) :
        RecyclerView.Adapter<PeopleDirectoryAdapter.PeopleViewHolder>(), OnDataSetChange {
    private val people = mutableListOf<DirectoryPeople>()
    var mSession: MXSession? = null


    init {
        mSession = Matrix.getInstance(context).defaultSession
    }

    inner class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avatar: VectorCircularImageView? = null
        var favouriteButton: ImageView? = null
        var expandableIcon: ImageView? = null
        var officialName: TextView? = null
        var jobTitle: TextView? = null
        var organisationText: TextView? = null
        var businessUnitText: TextView? = null
        var statusText: TextView? = null

        init {
            avatar = itemView.avatar
            favouriteButton = itemView.favoriteIcon
            expandableIcon = itemView.expandableIcon
            officialName = itemView.officialName
            jobTitle = itemView.jobTitle
            organisationText = itemView.organisationText
            businessUnitText = itemView.businessUnitText
            statusText = itemView.statusText
        }

        fun bind(context: Context, session: MXSession?, people: DirectoryPeople, onDataSetChange: OnDataSetChange, position: Int) {
            VectorUtils.loadRoomAvatar(context, session, avatar, people)
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
        holder.bind(context, mSession, people[position], this, position)
        holder.itemView.setOnClickListener {
            onClickListener.onPeopleClick(people[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = people.size

    override fun onDataChange(position: Int) {
        notifyItemChanged(position)
    }
}

interface PeopleClickListener {
    fun onPeopleClick(directoryPeople: DirectoryPeople)
    fun onPeopleFavorite(directoryPeople: DirectoryPeople)
}