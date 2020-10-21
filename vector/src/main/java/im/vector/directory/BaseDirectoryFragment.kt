package im.vector.directory

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.annotation.StringRes
import im.vector.home.BaseActFragment

abstract class BaseDirectoryFragment : BaseActFragment() {
    var roomClickListener: RoomClickListener? = null

    abstract fun onFavorite(enable: Boolean)
}