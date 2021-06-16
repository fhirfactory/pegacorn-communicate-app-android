package im.vector.chat.group

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import im.vector.Matrix
import im.vector.R
import im.vector.health.directory.MemberClickListener
import im.vector.health.microservices.interfaces.MatrixItem
import im.vector.util.VectorUtils
import im.vector.view.VectorCircularImageView
import kotlinx.android.synthetic.main.item_directory_people.view.avatar
import kotlinx.android.synthetic.main.item_selectable_room.view.*
import org.matrix.androidsdk.MXSession


class SelectedMemberAdapter(val context: Context, val removeListener: MemberClickListener) :
        RecyclerView.Adapter<SelectedMemberAdapter.SelectableRoomViewHolder>() {
    private val members = mutableListOf<MatrixItem>()
    var mSession: MXSession? = null

    init {
        mSession = Matrix.getInstance(context).defaultSession
    }

    inner class SelectableRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avatar: VectorCircularImageView? = null
        var closeButton: ImageView? = null
        var name: TextView? = null

        init {
            avatar = itemView.avatar
            closeButton = itemView.closeButton
            name = itemView.name
        }
    }

    fun setData(rooms: MutableList<MatrixItem>) {
        this.members.clear()
        this.members.addAll(rooms)
        notifyDataSetChanged()
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): SelectableRoomViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_selectable_room, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return SelectableRoomViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: SelectableRoomViewHolder, position: Int) {
        VectorUtils.loadRoomAvatar(context, mSession, holder.itemView.avatar, members[position])
        holder.itemView.name.text = members[position].GetDisplayName()

        holder.itemView.closeButton.setOnClickListener {
            removeListener.onMemberClick(members[position], true)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = members.size
}
