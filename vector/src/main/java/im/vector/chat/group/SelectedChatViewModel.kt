package im.vector.chat.group

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import im.vector.directory.people.model.TemporaryRoom
import im.vector.extensions.notifyObserver

class SelectedChatViewModel: ViewModel(){
    private val rooms = mutableListOf<TemporaryRoom>()
    val selectedLiveItems = MutableLiveData<MutableList<TemporaryRoom>>()

    init {
        selectedLiveItems.value = rooms
    }

    fun addRoom(room: TemporaryRoom) {
        selectedLiveItems.value?.add(room)
        selectedLiveItems.notifyObserver()
    }

    fun removeRoom(room: TemporaryRoom) {
        selectedLiveItems.value?.remove(room)
        selectedLiveItems.notifyObserver()
    }
}