package im.vector.health.directory.role.detail

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import im.vector.Matrix
import im.vector.R
import im.vector.health.directory.people.PeopleClickListener
import im.vector.health.directory.people.model.PractitionerItem
import im.vector.health.directory.shared.ILocalisationProvider
import im.vector.health.microservices.interfaces.IPractitionerRole
import im.vector.util.VectorUtils
import im.vector.view.VectorCircularImageView
import kotlinx.android.synthetic.main.activity_role_detail.view.*
import kotlinx.android.synthetic.main.item_role_detail_category1.view.heading
import kotlinx.android.synthetic.main.item_role_detail_category1.view.officialName
import kotlinx.android.synthetic.main.item_role_detail_category1.view.secondaryName
import kotlinx.android.synthetic.main.item_role_detail_category2.view.*
import kotlinx.android.synthetic.main.item_role_detail_category2.view.avatar
import org.matrix.androidsdk.MXSession


class RolesDetailAdapter(val context: Context, private val onClickListener: PeopleClickListener?, private val l11n: ILocalisationProvider) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val adapterModels = mutableListOf<AdapterModel>()
    private val TYPE_ROLE = 1
    private val TYPE_ORGANISATION_UNIT = 2
    private val TYPE_SPECIALITY = 3
    private val TYPE_LOCATION = 4
    private val TYPE_PRACTITIONER_IN_ROLE = 5
    private val TYPE_NO_PRACTITIONERS = 6

    var mSession: MXSession? = null

    init {
        mSession = Matrix.getInstance(context).defaultSession
    }

    inner class RoleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var heading: TextView? = null
        var officialName: TextView? = null
        var secondaryName: TextView? = null

        init {
            heading = itemView.heading
            officialName = itemView.officialName
            secondaryName = itemView.secondaryName
        }

        fun bind(context: Context, session: MXSession?, adapterModel: AdapterModel, showHeader: Boolean) {
            heading?.visibility = if (showHeader) VISIBLE else GONE
            heading?.text = adapterModel.title
            officialName?.text = adapterModel.primaryText
            if (adapterModel.rowType == TYPE_NO_PRACTITIONERS) officialName?.setTextColor(Color.RED)
            if (adapterModel.secondaryText == null) {
                secondaryName?.visibility = GONE
            } else {
                secondaryName?.visibility = VISIBLE
                secondaryName?.text = adapterModel.secondaryText
            }
        }
    }

    inner class PractitionerInRoleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avatar: VectorCircularImageView? = null
        var heading: TextView? = null
        var officialName: TextView? = null
        var secondaryName: TextView? = null

        init {
            avatar = itemView.avatar
            heading = itemView.heading
            officialName = itemView.officialName
            secondaryName = itemView.secondaryName
        }

        fun bind(context: Context, session: MXSession?, adapterModel: AdapterModel, showHeader: Boolean) {
            VectorUtils.loadRoomAvatar(context, session, avatar, adapterModel.people)
            officialName?.text = adapterModel.people?.GetName()
            secondaryName?.text = adapterModel.people?.GetJobTitle()
            heading?.text = adapterModel.title
            heading?.visibility = if (showHeader) VISIBLE else GONE
            itemView.setOnClickListener {
                adapterModel.people?.let { people ->
                    onClickListener?.onPeopleClick(people)
                }
            }
        }
    }

    fun setData(role: IPractitionerRole) {
        this.adapterModels.clear()

        adapterModels.add(AdapterModel("Role", role.GetShortName(), role.GetRoleCategory(), null, TYPE_ROLE))
        adapterModels.add(AdapterModel("Location", role.GetLocation(), null, null, TYPE_LOCATION))
        adapterModels.add(AdapterModel("Organization Unit", role.GetOrgName(), null, null, TYPE_ORGANISATION_UNIT))

        notifyDataSetChanged()
    }

    fun setData(people: List<PractitionerItem>) {
        for (ppl in people) {
            adapterModels.add(AdapterModel("Practitioners in Role", null, null, ppl, TYPE_PRACTITIONER_IN_ROLE))
        }
        notifyDataSetChanged()
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerView.ViewHolder {
        // create a new view
        return when (viewType) {
            TYPE_PRACTITIONER_IN_ROLE -> PractitionerInRoleViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_role_detail_category2, parent, false))
            else -> RoleViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_role_detail_category1, parent, false))
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == adapterModels.count()){
            (holder as RoleViewHolder).bind(context, mSession, AdapterModel("Practitioners in Role","None",null,null,TYPE_NO_PRACTITIONERS), showHeader(position))
            return
        }
        when (adapterModels[position].rowType) {
            TYPE_ROLE, TYPE_ORGANISATION_UNIT, TYPE_SPECIALITY, TYPE_LOCATION -> (holder as RoleViewHolder).bind(context, mSession, adapterModels[position], showHeader(position))
            TYPE_PRACTITIONER_IN_ROLE -> (holder as PractitionerInRoleViewHolder).bind(context, mSession, adapterModels[position], showHeader(position))
        }
    }

    private fun showHeader(position: Int) = position == adapterModels.size || position == 0 || adapterModels[position - 1].title != adapterModels[position].title

    override fun getItemViewType(position: Int): Int {
        if (position == adapterModels.count()) return TYPE_NO_PRACTITIONERS
        return adapterModels[position].rowType
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = if (adapterModels.last().rowType == TYPE_PRACTITIONER_IN_ROLE) adapterModels.size else adapterModels.size + 1
}

data class AdapterModel(val title: String, val primaryText: String?, val secondaryText: String?, val people: PractitionerItem?,
                        val rowType: Int)