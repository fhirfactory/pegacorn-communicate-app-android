package im.vector.chat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import im.vector.R
import im.vector.health.directory.shared.BaseDirectoryFragment
import im.vector.health.directory.RoomClickListener
import kotlinx.android.synthetic.main.fragment_create_chat.*

abstract class BaseMemberSelectionFragment : BaseTitleFragment(), RoomClickListener {
    abstract val fragments: List<BaseDirectoryFragment>

    override fun getLayoutResId() = R.layout.fragment_create_chat

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        pager.adapter = CreateChatTabAdapter(childFragmentManager, resources.getStringArray(R.array.create_chat_tabs))
        tabLayout.setupWithViewPager(pager)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                onFilter(s.toString(),null)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //TODO("Not yet implemented")
            }
        })

        fragments.forEach { fragment ->
            fragment.roomClickListener = this
        }
    }

    override fun onFilter(pattern: String?, listener: OnFilterListener?) {
        fragments.forEach { fragment ->
            fragment.applyFilter(pattern)
        }
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