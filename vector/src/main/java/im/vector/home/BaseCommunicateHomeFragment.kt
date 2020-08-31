package im.vector.home

import android.os.Bundle
import androidx.core.content.ContextCompat
import im.vector.R
import im.vector.fragments.AbsHomeFragment
import im.vector.ui.themes.ThemeUtils
import org.matrix.androidsdk.data.Room

abstract class BaseCommunicateHomeFragment : AbsHomeFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let { activity ->
            mPrimaryColor = ThemeUtils.getColor(activity, R.attr.vctr_tab_home)
            mSecondaryColor = ThemeUtils.getColor(activity, R.attr.vctr_tab_home_secondary)
            mFabColor = ContextCompat.getColor(activity, R.color.tab_rooms)
            mFabPressedColor = ContextCompat.getColor(activity, R.color.tab_rooms_secondary)
        }
    }

    override fun getRooms(): MutableList<Room> {
        return ArrayList(mSession.dataHandler.store?.rooms)
    }
}