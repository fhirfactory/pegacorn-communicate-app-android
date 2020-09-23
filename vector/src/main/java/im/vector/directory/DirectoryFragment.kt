package im.vector.directory

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import im.vector.R
import im.vector.directory.people.DirectoryPeopleFragment
import im.vector.directory.role.DirectoryRoleFragment
import im.vector.directory.service.DirectoryServiceFragment
import im.vector.home.BaseCommunicateHomeFragment
import im.vector.ui.themes.ThemeUtils
import kotlinx.android.synthetic.main.fragment_view_pager_tab.*


class DirectoryFragment : BaseCommunicateHomeFragment() {
    override fun getLayoutResId() = R.layout.fragment_view_pager_tab

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pager.adapter = DirectoryPagerAdapter(childFragmentManager)
        tabLayout.setupWithViewPager(pager)
        activity?.let { activity ->
            ThemeUtils.setTabLayoutTheme(activity, tabLayout)
        }
    }

    override fun onFilter(pattern: String?, listener: OnFilterListener?) {
        TODO("Not yet implemented")
    }

    override fun onResetFilter() {
        TODO("Not yet implemented")
    }

    inner class DirectoryPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int) = when (DIRECTORY_FRAGMENTS.values()[position]) {
            DIRECTORY_FRAGMENTS.ROLE -> {
                DirectoryRoleFragment()
            }
            DIRECTORY_FRAGMENTS.PEOPLE -> {
                DirectoryPeopleFragment()
            }
            DIRECTORY_FRAGMENTS.SERVICE -> {
                DirectoryServiceFragment()
            }
        }

        override fun getCount() = DIRECTORY_FRAGMENTS.values().size

        override fun getPageTitle(position: Int) = DIRECTORY_FRAGMENTS.values()[position].title
    }

    enum class DIRECTORY_FRAGMENTS(val title: String) {
        ROLE("Roles"),
        PEOPLE("People"),
        SERVICE("Services")
    }
}