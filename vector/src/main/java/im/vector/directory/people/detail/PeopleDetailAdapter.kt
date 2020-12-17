package im.vector.directory.people.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import im.vector.Matrix
import im.vector.R
import im.vector.directory.people.model.DirectoryPeople
import im.vector.directory.role.OnDataSetChange
import im.vector.directory.role.RoleClickListener
import im.vector.directory.role.RoleViewHolder
import im.vector.directory.role.model.DummyRole
import im.vector.ui.themes.ThemeUtils
import kotlinx.android.synthetic.main.item_role_detail_category1.view.*
import org.matrix.androidsdk.MXSession


class PeopleDetailAdapter(val context: Context, private val onClickListener: RoleClickListener?) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(), OnDataSetChange {
    private val models = mutableListOf<PeopleDetailAdapterModel>()
    private val TYPE_EMAIL = 1
    private val TYPE_PHONE = 2
    private val TYPE_ROLE = 3

    var mSession: MXSession? = null
    var textSize: Float = 0.0F
    var spanTextBackgroundColor: Int
    var spanTextColor: Int

    init {
        mSession = Matrix.getInstance(context).defaultSession
        textSize = 12 * context.resources.displayMetrics.scaledDensity // sp to px
        spanTextBackgroundColor = ThemeUtils.getColor(context, R.attr.vctr_text_spanable_text_background_color)
        spanTextColor = ThemeUtils.getColor(context, R.attr.vctr_text_reverse_color)
    }

    inner class EmailPhoneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var heading: TextView? = null
        var officialName: TextView? = null
        var secondaryName: TextView? = null

        init {
            heading = itemView.heading
            officialName = itemView.officialName
            secondaryName = itemView.secondaryName
        }

        fun bind(peopleDetailAdapterModel: PeopleDetailAdapterModel, showHeader: Boolean = false) {
            heading?.visibility = if (showHeader) VISIBLE else GONE
            heading?.text = when (peopleDetailAdapterModel.type) {
                TYPE_PHONE -> "Phone"
                TYPE_EMAIL -> "Email"
                else -> ""
            }
            officialName?.text = peopleDetailAdapterModel.value
            secondaryName?.visibility = GONE
        }
    }

    fun setData(people: DirectoryPeople) {
        this.models.clear()
        this.models.add(PeopleDetailAdapterModel("something@act.gov.au", null, TYPE_EMAIL))
        this.models.add(PeopleDetailAdapterModel("0455552522", null, TYPE_PHONE))
        notifyDataSetChanged()
    }

    fun setData(roles: MutableList<DummyRole>) {
        for (role in roles) {
            this.models.add(PeopleDetailAdapterModel(null, role, TYPE_ROLE))
        }
        notifyDataSetChanged()
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerView.ViewHolder {
        // create a new view
        return when (viewType) {
            TYPE_ROLE -> RoleViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_directory_role, parent, false))
            // TYPE_EMAIL, TYPE_PHONE
            else -> EmailPhoneViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_role_detail_category1, parent, false))
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (models[position].type) {
            TYPE_ROLE -> (holder as RoleViewHolder).bind(context, mSession, models[position].role!!,  this, position, onClickListener, showHeader(position))
            TYPE_EMAIL, TYPE_PHONE -> (holder as EmailPhoneViewHolder).bind(models[position], showHeader(position))
        }
    }

    private fun showHeader(position: Int) = position == 0 || models[position - 1].type != models[position].type

    override fun getItemViewType(position: Int): Int {
        return models[position].type
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = models.size
    override fun onDataChange(position: Int) {
        notifyItemChanged(position)
    }
}

data class PeopleDetailAdapterModel(val value: String?, val role: DummyRole?, val type: Int)