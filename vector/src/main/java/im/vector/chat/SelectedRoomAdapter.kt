package im.vector.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import im.vector.Matrix
import im.vector.R
import im.vector.directory.people.model.TemporaryRoom
import im.vector.util.VectorUtils
import im.vector.view.VectorCircularImageView
import kotlinx.android.synthetic.main.item_directory_people.view.avatar
import kotlinx.android.synthetic.main.item_selectable_room.view.*
import org.matrix.androidsdk.MXSession


class SelectedRoomAdapter(val context: Context) :
        RecyclerView.Adapter<SelectedRoomAdapter.SelectableRoomViewHolder>() {
    private val rooms = mutableListOf<TemporaryRoom>()
    var mSession: MXSession? = null

    init {
        mSession = Matrix.getInstance(context).defaultSession
    }

    inner class SelectableRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avatar: VectorCircularImageView? = null
        var closeButton: ImageView? = null


        init {
            avatar = itemView.avatar
            closeButton = itemView.closeButton
        }
    }

    fun setData(rooms: MutableList<TemporaryRoom>) {
        this.rooms.clear()
        this.rooms.addAll(rooms)
    }

    fun addRoom(room:TemporaryRoom){
        this.rooms.add(room)
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
        if (rooms[position].people == null)
            VectorUtils.loadRoomAvatar(context, mSession, holder.itemView.avatar, rooms[position].role)
        else
            VectorUtils.loadRoomAvatar(context, mSession, holder.itemView.avatar, rooms[position].people)

        holder.itemView.closeButton.setOnClickListener {
            rooms.removeAt(position)
            notifyDataSetChanged()
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = rooms.size
}
