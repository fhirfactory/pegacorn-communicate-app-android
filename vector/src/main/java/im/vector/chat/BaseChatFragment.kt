package im.vector.chat

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProviders
import im.vector.R
import im.vector.directory.RoomClickListener
import im.vector.directory.people.DirectoryPeopleFragment
import im.vector.directory.role.DirectoryRoleFragment
import im.vector.home.BaseActFragment
import kotlinx.android.synthetic.main.fragment_create_chat.*

abstract class BaseChatFragment : BaseActFragment(), RoomClickListener {
    lateinit var viewModel: ChatViewModel
    val fragments = listOf(DirectoryRoleFragment.newInstance(true), DirectoryPeopleFragment.newInstance(true))

    override fun getLayoutResId() = R.layout.fragment_create_chat

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        } ?: throw Throwable("invalid activity")

        pager.adapter = CreateChatTabAdapter(childFragmentManager, resources.getStringArray(R.array.create_chat_tabs))
        tabLayout.setupWithViewPager(pager)

        fragments.forEach { fragment ->
            fragment.roomClickListener = this
        }
    }

    override fun onFilter(pattern: String?, listener: OnFilterListener?) {
        // TODO("Not yet implemented")
    }

    override fun onResetFilter() {
        // TODO("Not yet implemented")
    }

    inner class CreateChatTabAdapter(fm: FragmentManager, val titles: Array<String>) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int) = fragments[position]

        override fun getCount(): Int = titles.size

        override fun getPageTitle(position: Int): CharSequence {
            return titles[position]
        }
    }
}