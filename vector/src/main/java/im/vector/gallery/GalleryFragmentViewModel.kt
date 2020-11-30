package im.vector.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GalleryFragmentViewModel : ViewModel() {
    val galleryItems = MutableLiveData<MutableList<DummyGalleryItem>>()

    fun getData() {
        val items = mutableListOf<DummyGalleryItem>()
        items.add(DummyGalleryItem(1, "something"))
        items.add(DummyGalleryItem(2, "something"))
        items.add(DummyGalleryItem(3, "something"))
        items.add(DummyGalleryItem(4, "something"))
        items.add(DummyGalleryItem(5, "something"))
        items.add(DummyGalleryItem(6, "something"))
        galleryItems.postValue(items)
    }
}