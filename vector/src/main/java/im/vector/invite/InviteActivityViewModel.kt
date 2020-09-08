package im.vector.invite

import androidx.lifecycle.ViewModel
import org.matrix.androidsdk.MXSession

class InviteActivityViewModel : ViewModel(){
    var session: MXSession? = null

    fun initSession(session: MXSession) {
        if (this.session == null) {
            this.session = session
        }
    }
}