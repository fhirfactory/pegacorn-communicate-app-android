package im.vector.calls.dialer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import im.vector.calls.recent.DummyRecentCallItem

class DialerFragmentViewModel : ViewModel() {
    val pressedKeys = MutableLiveData<String>()

}