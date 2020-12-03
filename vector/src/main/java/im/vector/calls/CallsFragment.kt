package im.vector.calls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import im.vector.R
import im.vector.calls.dialer.DialerFragment
import im.vector.calls.recent.RecentCallFragment
import im.vector.directory.DirectoryFragment
import im.vector.directory.people.DirectoryPeopleFragment
import im.vector.directory.role.DirectoryRoleFragment
import im.vector.fragments.AbsHomeFragment
import im.vector.ui.themes.ThemeUtils.getColor
import im.vector.ui.themes.ThemeUtils.setTabLayoutTheme
import kotlinx.android.synthetic.main.fragment_view_pager_tab.*
import org.matrix.androidsdk.data.Room

class CallsFragment : AbsHomeFragment() {
    override fun getLayoutResId() =  R.layout.fragment_view_pager_tab

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pager.adapter = DemoCollectionPagerAdapter(childFragmentManager, resources.getStringArray(R.array.call_tabs))
        tabLayout.setupWithViewPager(pager)
        activity?.let { activity ->
            setTabLayoutTheme(activity, tabLayout)
            mPrimaryColor = getColor(activity, R.attr.vctr_tab_home)
            mSecondaryColor = getColor(activity, R.attr.vctr_tab_home_secondary)

            mFabColor = ContextCompat.getColor(activity, R.color.tab_people)
            mFabPressedColor = ContextCompat.getColor(activity, R.color.tab_people_secondary)
        }
    }

    override fun getRooms(): MutableList<Room> {
        TODO("Not yet implemented")
    }

    override fun onFilter(pattern: String?, listener: OnFilterListener?) {
        TODO("Not yet implemented")
    }

    override fun onResetFilter() {
        TODO("Not yet implemented")
    }

    class DemoCollectionPagerAdapter(fm: FragmentManager, val titles: Array<String>) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> RecentCallFragment()
                1 -> DialerFragment()
                else -> DialerFragment()
            }
        }

        override fun getCount(): Int = titles.size

        override fun getPageTitle(position: Int): CharSequence {
            return titles[position]
        }
    }
}