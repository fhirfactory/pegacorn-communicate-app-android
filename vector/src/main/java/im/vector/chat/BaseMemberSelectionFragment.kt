package im.vector.chat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import im.vector.R
import im.vector.health.directory.MemberClickListener
import im.vector.health.directory.shared.IMatrixDirectorySelectionFragment
import kotlinx.android.synthetic.main.fragment_create_chat.*

abstract class BaseMemberSelectionFragment : BaseTitleFragment(), MemberClickListener {
    abstract val selectionFragments: List<IMatrixDirectorySelectionFragment<*>>

    override fun getLayoutResId() = R.layout.fragment_create_chat

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        pager.adapter = CreateChatTabAdapter(childFragmentManager)
        
        if (selectionFragments.size <= 1) {
            tabLayout.visibility = View.GONE
        }

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

        selectionFragments.forEach { fragment ->
            fragment.provideMemberClickListener(this)
        }
    }

    override fun onFilter(pattern: String?, listener: OnFilterListener?) {
        selectionFragments.forEach { fragment ->
            fragment.applyFilter(pattern)
        }
    }

    override fun onResetFilter() {
        selectionFragments.forEach { fragment ->
            fragment.applyFilter("")
        }
    }

    inner class CreateChatTabAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int) = selectionFragments[position].getFragment()

        override fun getCount(): Int = selectionFragments.size

        override fun getPageTitle(position: Int): CharSequence {
            return getString(selectionFragments[position].getSelectionTitleResource())
        }
    }
}