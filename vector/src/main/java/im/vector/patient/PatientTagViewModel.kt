package im.vector.patient

import androidx.lifecycle.ViewModel
import im.vector.util.RoomUtils
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.Room
import java.util.*

class PatientTagViewModel : ViewModel() {
    var session: MXSession? = null
    var fileLocation: String? = null

    fun initSession(session: MXSession) {
        if (this.session == null) {
            this.session = session
        }
    }
}