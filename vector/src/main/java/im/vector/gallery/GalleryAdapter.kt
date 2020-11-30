package im.vector.gallery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import im.vector.R


class GalleryAdapter(val context: Context, private val onClickListener: GalleryItemClickListener) :
        RecyclerView.Adapter<GalleryViewHolder>() {
    private val galleryItems = mutableListOf<DummyGalleryItem>()

    fun setData(galleryItems: MutableList<DummyGalleryItem>) {
        this.galleryItems.clear()
        this.galleryItems.addAll(galleryItems)
        notifyDataSetChanged()
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): GalleryViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gallery, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return GalleryViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(galleryItems[position])
        holder.itemView.setOnClickListener {
            onClickListener.onRoleClick(galleryItems[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = galleryItems.size
}

interface GalleryItemClickListener {
    fun onRoleClick(galleryItem: DummyGalleryItem)
}

class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(galleryItem: DummyGalleryItem) {

    }
}