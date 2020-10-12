package im.vector.directory

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.annotation.StringRes
import im.vector.home.BaseActFragment

abstract class BaseDirectoryFragment : BaseActFragment() {
    abstract fun onFavorite(enable: Boolean)

    open fun setHeader(headingView: TextView, @StringRes title: Int, count: Int) {
        val titleText = resources.getString(title)
        val str = SpannableStringBuilder(titleText)
        str.setSpan(StyleSpan(Typeface.BOLD), 0, titleText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        str.append(" ").append(count.toString())
        headingView.text = str
    }
}