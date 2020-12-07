package im.vector.calls.dialer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DialerFragmentViewModel : ViewModel() {
    val pressedKeys = MutableLiveData<String>()

    fun keyClick(key: String) {
        pressedKeys.postValue(if (pressedKeys.value==null) key else pressedKeys.value.plus(key))
        Log.d("zzzz", key)
    }

    fun delete() {
        pressedKeys.value?.let {
            pressedKeys.postValue(it.substring(0, it.length-1))
        }
    }
}