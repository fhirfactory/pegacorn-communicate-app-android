package im.vector.health.directory.shared

import androidx.annotation.StringRes

interface ILocalisationProvider {
    fun getStringRes(@StringRes resId: Int): String?
}