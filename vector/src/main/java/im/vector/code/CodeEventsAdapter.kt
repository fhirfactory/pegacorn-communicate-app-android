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
import im.vector.ui.themes.ThemeUtils.getColor
import im.vector.util.VectorUtils
import im.vector.view.VectorCircularImageView
import kotlinx.android.synthetic.main.item_code_event.view.*
import org.matrix.androidsdk.MXSession


class CodeEventsAdapter(val context: Context, private val onClickListener: CodeEventClickListener) :
        RecyclerView.Adapter<CodeEventViewHolder>(), OnDataSetChange {
    private val codes = mutableListOf<CodeEvent>()

    fun setData(codes: MutableList<CodeEvent>) {
        this.codes.clear()
        this.codes.addAll(codes)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CodeEventViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_code_event, parent, false)
        return CodeEventViewHolder(view)
    }

    override fun onBindViewHolder(holder: CodeEventViewHolder, position: Int) {
        /*holder.bind(codes[position])
        holder.itemView.setOnClickListener {
            onClickListener.onCodeClick(codes[position])
        }*/
    }

    override fun getItemCount() = 10 //codes.size

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
    var codeImageView: ImageView? = null
    var codeName: TextView? = null
    var codeLocation: TextView? = null
    var codeDate: TextView? = null
    var statusTextView: TextView? = null

    init {
        codeImageView = itemView.codeImageView
        codeName = itemView.codeName
        codeLocation = itemView.codeLocation
        codeDate = itemView.codeDate
        statusTextView = itemView.statusTextView
    }

    fun bind( code: CodeEvent) {

    }
}