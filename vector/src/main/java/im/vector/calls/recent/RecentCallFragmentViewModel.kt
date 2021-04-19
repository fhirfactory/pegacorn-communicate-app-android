package im.vector.calls.recent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecentCallFragmentViewModel : ViewModel() {
    val recentCalls = MutableLiveData<MutableList<DummyRecentCallItem>>()

    fun getData() {
        val items = mutableListOf<DummyRecentCallItem>()
        items.add(DummyRecentCallItem(1, "something"))
        items.add(DummyRecentCallItem(2, "something"))
        items.add(DummyRecentCallItem(3, "something"))
        items.add(DummyRecentCallItem(4, "something"))
        items.add(DummyRecentCallItem(5, "something"))
        items.add(DummyRecentCallItem(6, "something"))
        items.add(DummyRecentCallItem(7, "something"))
        items.add(DummyRecentCallItem(8, "something"))
        items.add(DummyRecentCallItem(9, "something"))
        recentCalls.postValue(items)
    }
}