package im.vector.health.directory.shared

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import org.matrix.androidsdk.MXSession

interface IMatrixActivity {
    var currentSession: MXSession
    fun getActivity(): FragmentActivity
    fun startActivity(intent: Intent)
}