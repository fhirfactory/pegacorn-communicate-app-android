package im.vector.code

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
import im.vector.directory.role.model.CodeEvent
import im.vector.ui.themes.ThemeUtils.getColor
import im.vector.util.VectorUtils
import im.vector.view.VectorCircularImageView
import org.matrix.androidsdk.MXSession


class CodeEventsAdapter(val context: Context, private val onClickListener: CodeEventClickListener) :
        RecyclerView.Adapter<CodeEventViewHolder>(), OnDataSetChange {
    private val codes = mutableListOf<CodeEvent>()
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


    fun setData(codes: MutableList<CodeEvent>) {
        this.codes.clear()
        this.codes.addAll(codes)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CodeEventViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_directory_role, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return CodeEventViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: CodeEventViewHolder, position: Int) {
        holder.bind(codes[position])
        holder.itemView.setOnClickListener {
            onClickListener.onCodeClick(codes[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = codes.size

    override fun onDataChange(position: Int) {
        notifyItemChanged(position)
    }
}

interface CodeEventClickListener {
    fun onCodeClick(code: CodeEvent)
}

interface OnDataSetChange {
    fun onDataChange(position: Int)
}

class CodeEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

    fun bind( code: CodeEvent) {

    }
}