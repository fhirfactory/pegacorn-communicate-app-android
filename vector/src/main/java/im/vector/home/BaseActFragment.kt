package im.vector.home

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import im.vector.R
import im.vector.fragments.AbsHomeFragment
import im.vector.ui.themes.ThemeUtils
import org.matrix.androidsdk.data.Room

abstract class BaseActFragment : AbsHomeFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let { activity ->
            mPrimaryColor = ThemeUtils.getColor(activity, R.attr.vctr_tab_home)
            mSecondaryColor = ThemeUtils.getColor(activity, R.attr.vctr_tab_home_secondary)
            mFabColor = ContextCompat.getColor(activity, R.color.tab_rooms)
            mFabPressedColor = ContextCompat.getColor(activity, R.color.tab_rooms_secondary)
        }
    }

    open fun setHeader(headingView: TextView, @StringRes title: Int, count: Int) {
        val titleText = resources.getString(title)
        val str = SpannableStringBuilder(titleText)
        str.setSpan(StyleSpan(Typeface.BOLD), 0, titleText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        str.append(" ").append(count.toString())
        headingView.text = str
    }

    override fun getRooms(): MutableList<Room> {
        return ArrayList(mSession.dataHandler.store?.rooms)
    }
}