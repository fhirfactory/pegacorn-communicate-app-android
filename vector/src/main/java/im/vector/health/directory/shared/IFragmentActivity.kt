package im.vector.health.directory.shared

import android.content.Intent
import androidx.fragment.app.FragmentActivity

interface IFragmentActivity: ILocalisationProvider {
    fun getActivity(): FragmentActivity
    fun startActivity(intent: Intent)
}