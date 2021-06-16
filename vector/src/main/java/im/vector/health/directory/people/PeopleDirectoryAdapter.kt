package im.vector.health.directory.people

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import im.vector.Matrix
import im.vector.R
import im.vector.activity.VectorAppCompatActivity
import im.vector.adapters.ParticipantAdapterItem
import im.vector.adapters.VectorParticipantsAdapter
import im.vector.health.directory.people.model.PractitionerItem
import im.vector.health.directory.role.OnDataSetChange
import im.vector.health.directory.shared.*
import im.vector.health.microservices.APIModel.FavouriteTypes
import im.vector.health.microservices.DirectoryServicesSingleton
import im.vector.ui.themes.ThemeUtils.getColor
import im.vector.util.VectorUtils
import im.vector.view.VectorCircularImageView
import kotlinx.android.synthetic.main.item_directory_people.view.*
import kotlinx.android.synthetic.main.item_directory_people.view.avatar
import kotlinx.android.synthetic.main.item_directory_people.view.expandableIcon
import kotlinx.android.synthetic.main.item_directory_people.view.favoriteIcon
import kotlinx.android.synthetic.main.item_directory_people.view.officialName
import kotlinx.android.synthetic.main.item_directory_people.view.selected
import kotlinx.android.synthetic.main.item_directory_role.view.*
import org.matrix.androidsdk.MXSession
import kotlinx.android.synthetic.main.item_directory_people.view.beginChatIcon as beginChatIcon
import kotlinx.android.synthetic.main.item_directory_people.view.voiceCallIcon as beginVoiceCallIcon
import kotlinx.android.synthetic.main.item_directory_people.view.videoCallIcon as beginVideoCallIcon


class PeopleDirectoryAdapter(val context: Context, private val onClickListener: PeopleClickListener, private val selectable: Boolean = false, private val matrixHandler: MatrixChatActionHandler, private val l18n: ILocalisationProvider) :
        RecyclerView.Adapter<PeopleDirectoryAdapter.PeopleViewHolder>(), OnDataSetChange, IStandardDirectoryAdapter<PractitionerItem> {
    private val people = mutableListOf<PractitionerItem>()
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
        var favourite: Boolean = false

        var expandableIcon: ImageView? = null
        var officialName: TextView? = null
        var jobTitle: TextView? = null
        var selectionRadioImageView: ImageView? = null
        var organisationText: TextView? = null
        var businessUnitText: TextView? = null
        var roleText: TextView? = null
        var statusText: TextView? = null

        var chatIcon: ImageView? = null
        var voiceCallIcon: ImageView? = null
        var videoCallIcon: ImageView? = null

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
            roleText = itemView.roleText
            chatIcon = itemView.beginChatIcon
            voiceCallIcon = itemView.beginVoiceCallIcon
            videoCallIcon = itemView.beginVideoCallIcon

        }

        fun updateFavourite() {
            if (favourite) {
                favouriteButton?.setImageResource(R.drawable.ic_material_star_black)
            } else {
                favouriteButton?.setImageResource(R.drawable.ic_material_star_border_black)
            }
        }

        fun bind(context: Context, session: MXSession?, people: PractitionerItem, onDataSetChange: OnDataSetChange, position: Int, selection: Boolean? = false) {
            VectorUtils.loadRoomAvatar(context, session, avatar, people)
            selectionRadioImageView?.setImageResource(if (selection == true) R.drawable.ic_radio_button_checked else R.drawable.ic_radio_button_unchecked)
            officialName?.text = people.GetName()
            jobTitle?.text = people.GetJobTitle()
            organisationText?.text = String.format(l18n.getStringRes(R.string.organisation) ?: "organisationText: %s",people.GetOrganization())
            businessUnitText?.text = String.format(l18n.getStringRes(R.string.business_unit) ?: "businessUnitText: %s", people.GetBusinessUnit())
            //roleText?.text = "Role: some role"
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

                //This is needed, otherwise when we set the visibility to VISIBLE from GONE, Android gets confused and doesn't actually make the TextViews visible.
                organisationText?.visibility = View.INVISIBLE
                businessUnitText?.visibility = View.INVISIBLE

                onDataSetChange.onDataChange(position)
            }

            DirectoryServicesSingleton.Instance().CheckFavourite(FavouriteTypes.Practitioner,people.GetID()) {
                this.favourite = it
                this.updateFavourite()
            }

            chatIcon?.setOnClickListener {
                matrixHandler.startChat(people.GetMatrixID())
            }

            videoCallIcon?.setOnClickListener {
                matrixHandler.call(true,people.GetMatrixID())
            }

            voiceCallIcon?.setOnClickListener {
                matrixHandler.call(false,people.GetMatrixID())
            }

            if (people.GetMatrixID() == mSession?.myUserId) {
                voiceCallIcon?.isEnabled = false
                videoCallIcon?.isEnabled = false
                voiceCallIcon?.visibility = View.GONE
                videoCallIcon?.visibility = View.GONE
            }

            favouriteButton?.setOnClickListener {
                this.favourite = !this.favourite
                this.updateFavourite()
                if (this.favourite) {
                    DirectoryServicesSingleton.Instance().AddFavourite(FavouriteTypes.Practitioner,people.GetID())
                } else {
                    DirectoryServicesSingleton.Instance().RemoveFavourite(FavouriteTypes.Practitioner,people.GetID())
                }
            }
        }
    }

    override fun addPage(items: List<PractitionerItem>) {
        this.people.addAll(items)
        notifyDataSetChanged()
    }

    override fun setData(items: List<PractitionerItem>) {
        this.people.clear()
        this.people.addAll(items)
    }

    fun setData(participants: VectorParticipantsAdapter){
        this.people.clear()
        for (i in 0 until participants.groupCount) {
            for (j in 0 until participants.getChildrenCount(i)) {
                val child: ParticipantAdapterItem = participants.getChild(i,j) as ParticipantAdapterItem
                if (child.mUserId == null) continue
                //this.people.add(this.people.count(), DirectoryPeople(child.mUserId, child.mDisplayName, "Placeholder", child.mAvatarUrl, "Placeholder", "Placeholder", ArrayList()))
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
                val added = selectedIds?.add(people[position].GetID())
                if (added == false) {
                    selectedIds?.remove(people[position].GetID())
                }
                notifyItemChanged(position)
                onClickListener.onPeopleClick(people[position], !(added ?: true))
            } else {
                onClickListener.onPeopleClick(people[position])
            }
        }
    }

    private fun checkSelection(people: PractitionerItem): Boolean {
        if (!selectable) return false
        selectedIds?.forEach { id ->
            if (id == people.GetID())
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
    fun onPeopleClick(directoryPeople: PractitionerItem, forRemove: Boolean = false)
    fun onPeopleFavorite(directoryPeople: PractitionerItem)
}