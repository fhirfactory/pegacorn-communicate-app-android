package im.vector.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TitleViewModel : ViewModel() {
    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    fun updateActionBarTitle(title: String) = _title.postValue(title)
}