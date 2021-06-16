package im.vector.chat.group

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import im.vector.extensions.notifyObserver
import im.vector.health.microservices.interfaces.MatrixItem

class SelectedChatViewModel : ViewModel() {
    private val members = mutableListOf<MatrixItem>()
    val selectedLiveItems = MutableLiveData<MutableList<MatrixItem>>()

    init {
        selectedLiveItems.value = members
    }

    fun addMember(item: MatrixItem) {
        selectedLiveItems.value?.add(item)
        selectedLiveItems.notifyObserver()
    }

    fun removeMember(item: MatrixItem) {
        selectedLiveItems.value?.remove(item)
        selectedLiveItems.notifyObserver()
    }
}