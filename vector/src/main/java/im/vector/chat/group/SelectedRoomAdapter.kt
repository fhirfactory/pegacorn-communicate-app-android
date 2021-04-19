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
import im.vector.directory.RoomClickListener
import im.vector.directory.people.model.TemporaryRoom
import im.vector.util.VectorUtils
import im.vector.view.VectorCircularImageView
import kotlinx.android.synthetic.main.item_directory_people.view.avatar
import kotlinx.android.synthetic.main.item_selectable_room.view.*
import org.matrix.androidsdk.MXSession


class SelectedRoomAdapter(val context: Context, val removeListener: RoomClickListener) :
        RecyclerView.Adapter<SelectedRoomAdapter.SelectableRoomViewHolder>() {
    private val rooms = mutableListOf<TemporaryRoom>()
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

    fun setData(rooms: MutableList<TemporaryRoom>) {
        this.rooms.clear()
        this.rooms.addAll(rooms)
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
        if (rooms[position].people == null) {
            VectorUtils.loadRoomAvatar(context, mSession, holder.itemView.avatar, rooms[position].role)
            holder.itemView.name.text = rooms[position].role?.officialName
        } else {
            VectorUtils.loadRoomAvatar(context, mSession, holder.itemView.avatar, rooms[position].people)
            holder.itemView.name.text = rooms[position].people?.officialName
        }

        holder.itemView.closeButton.setOnClickListener {
            removeListener.onRoomClick(rooms[position], true)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = rooms.size
}
