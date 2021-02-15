package im.vector.calls.recent

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import im.vector.R


class RecentCallAdapter(val context: Context, private val onClickListener: RecentCallItemClickListener) :
        RecyclerView.Adapter<RecentCallViewHolder>() {
    private val recentCallItems = mutableListOf<DummyRecentCallItem>()

    fun setData(recentCallItems: MutableList<DummyRecentCallItem>) {
        this.recentCallItems.clear()
        this.recentCallItems.addAll(recentCallItems)
        notifyDataSetChanged()
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecentCallViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recent_calls, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return RecentCallViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecentCallViewHolder, position: Int) {
        holder.bind(recentCallItems[position])
        holder.itemView.setOnClickListener {
            onClickListener.onRoleClick(recentCallItems[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = recentCallItems.size
}

interface RecentCallItemClickListener {
    fun onRoleClick(recentCallItem: DummyRecentCallItem)
}

class RecentCallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(recentCallItem: DummyRecentCallItem) {

    }
}