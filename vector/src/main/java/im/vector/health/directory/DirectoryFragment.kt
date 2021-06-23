package im.vector.health.directory

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import im.vector.R
import im.vector.health.directory.patient.DirectoryPatientFragment
import im.vector.health.directory.people.DirectoryPeopleFragment
import im.vector.health.directory.role.DirectoryRoleFragment
import im.vector.health.directory.service.DirectoryServiceFragment
import im.vector.home.BaseActFragment
import im.vector.ui.themes.ThemeUtils
import kotlinx.android.synthetic.main.fragment_view_pager_tab.*


class DirectoryFragment : BaseActFragment() {
    var favouriteEnable = false
    var changeQueryText: ((id: Int, id2: Int)->Unit)? = null
    var reloadQueryText: (()->Unit)? = null
    private val fragments = mutableMapOf(DIRECTORY_FRAGMENTS.ROLE to DirectoryRoleFragment(), DIRECTORY_FRAGMENTS.PEOPLE to DirectoryPeopleFragment(), DIRECTORY_FRAGMENTS.SERVICE to DirectoryServiceFragment(), DIRECTORY_FRAGMENTS.PATIENTS to DirectoryPatientFragment())

    override fun getLayoutResId() = R.layout.fragment_view_pager_tab

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pager.offscreenPageLimit = fragments.size - 1
        pager.adapter = DirectoryPagerAdapter(childFragmentManager)
        tabLayout.setupWithViewPager(pager)
        activity?.let { activity ->
            ThemeUtils.setTabLayoutTheme(activity, tabLayout)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_directory, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favourite -> {
                favouriteEnable = !favouriteEnable
                item.setIcon(if (favouriteEnable) R.drawable.filled_star else R.drawable.outline_star)
                fragments.forEach { fragment ->
                    fragment.value.onFavorite(favouriteEnable)
                }
                return true
            }
        }
        return false
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.ic_action_global_search)?.isVisible = false
        menu.findItem(R.id.ic_action_historical)?.isVisible = false
        menu.findItem(R.id.ic_action_mark_all_as_read)?.isVisible = false
        menu.findItem(R.id.action_favourite).setIcon(if (favouriteEnable) R.drawable.filled_star else R.drawable.outline_star)
    }

    override fun onFilter(pattern: String?, listener: OnFilterListener?) {
        //TODO("Not yet implemented")
        fragments[DIRECTORY_FRAGMENTS.values()[pager.currentItem]]?.filter(pattern)
        listener?.onFilterDone(0)
    }

    public override fun onResetFilter() {
        //TODO("Not yet implemented")
        fragments.forEach { fragment ->
            fragment.value.filter(null)
        }
    }

    inner class DirectoryPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int) = fragments[DIRECTORY_FRAGMENTS.values()[position]]!!

        override fun getCount() = DIRECTORY_FRAGMENTS.values().size

        override fun getPageTitle(position: Int) = DIRECTORY_FRAGMENTS.values()[position].title

        var lastItem = -1

        override fun finishUpdate(container: ViewGroup) {
            super.finishUpdate(container)
            changeQueryText?.let {
                when (DIRECTORY_FRAGMENTS.values()[pager.currentItem]) {
                    DIRECTORY_FRAGMENTS.ROLE -> it(R.string.home_filter_placeholder_people, R.string.search_directory_roles)
                    DIRECTORY_FRAGMENTS.PEOPLE -> it(R.string.home_filter_placeholder_people, R.string.search_directory_people)
                    DIRECTORY_FRAGMENTS.SERVICE -> it(R.string.home_filter_placeholder_roles, R.string.search_directory_services)
                    DIRECTORY_FRAGMENTS.PATIENTS -> it(R.string.home_filter_placeholder_patients, R.string.search_directory_patients)
                }
            }
            if (pager.currentItem != lastItem) {
                reloadQueryText?.let { it() }
                lastItem = pager.currentItem
            }
        }
    }

    enum class DIRECTORY_FRAGMENTS(val title: String) {
        ROLE("Roles"),
        PEOPLE("People"),
        SERVICE("Services"),
        PATIENTS("Patients")
    }
}