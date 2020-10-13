package im.vector.chat

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorBaseSearchActivity
import im.vector.directory.people.DirectoryPeopleFragment
import im.vector.directory.role.DirectoryRoleFragment
import kotlinx.android.synthetic.main.fragment_view_pager_tab.*

class ActChatOneToOneActivity : VectorBaseSearchActivity() {
    private var mMatrixId: String? = null

    override fun getLayoutRes() = R.layout.activity_view_pager_tab

    override fun initUiAndData() {
        super.initUiAndData()
        configureToolbar()

        if (CommonActivityUtils.shouldRestartApp(this)) {
            CommonActivityUtils.restartApp(this)
            return
        }

        mSession = getSession(intent)
        if (mSession == null) {
            finish()
            return
        }

        if (intent.hasExtra(EXTRA_MATRIX_ID)) {
            mMatrixId = intent.getStringExtra(EXTRA_MATRIX_ID)
        }

        // get current session
        mSession = Matrix.getInstance(applicationContext).getSession(mMatrixId)
        if (null == mSession || !mSession.isAlive) {
            finish()
            return
        }

        // the user defines a
        if (null != mPatternToSearchEditText) {
            mPatternToSearchEditText.setHint(R.string.one_to_one_room_search_hint)
        }

        pager.adapter = CreateChatTabAdapter(supportFragmentManager, resources.getStringArray(R.array.create_chat_tabs))
        tabLayout.setupWithViewPager(pager)
    }

    /**
     * The search pattern has been updated
     */
    override fun onPatternUpdate(isTypingUpdate: Boolean) {
        val pattern = mPatternToSearchEditText.text.toString()
        Log.d("zzz", pattern)
        //TODO 
    }

    class CreateChatTabAdapter(fm: FragmentManager, val titles: Array<String>) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> DirectoryRoleFragment()
                1 -> DirectoryPeopleFragment()
                else -> DirectoryRoleFragment()
            }
        }

        override fun getCount(): Int = titles.size

        override fun getPageTitle(position: Int): CharSequence {
            return titles[position]
        }
    }
}