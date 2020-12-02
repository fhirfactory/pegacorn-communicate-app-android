package im.vector.calls.recent

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import im.vector.R
import im.vector.fragments.AbsHomeFragment
import im.vector.ui.themes.ThemeUtils
import kotlinx.android.synthetic.main.fragment_recent_calls.*
import org.matrix.androidsdk.data.Room

class RecentCallFragment : AbsHomeFragment(), RecentCallItemClickListener {

    private lateinit var recentCallAdapter: RecentCallAdapter
    private lateinit var viewModel: RecentCallFragmentViewModel
    override fun getLayoutResId() = R.layout.fragment_recent_calls

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let { activity ->
            mPrimaryColor = ThemeUtils.getColor(activity, R.attr.vctr_tab_home)
            mSecondaryColor = ThemeUtils.getColor(activity, R.attr.vctr_tab_home_secondary)

            mFabColor = ContextCompat.getColor(activity, R.color.tab_people)
            mFabPressedColor = ContextCompat.getColor(activity, R.color.tab_people_secondary)
        }

        viewModel = ViewModelProviders.of(this).get(RecentCallFragmentViewModel::class.java)
        subscribeUI()

        recentCallAdapter = RecentCallAdapter(requireContext(), this)
        (recentCallRecyclerview.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        recentCallRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        recentCallRecyclerview.adapter = recentCallAdapter
        recentCallRecyclerview.setHasFixedSize(true)
    }

    private fun subscribeUI() {
        viewModel.recentCalls.observe(viewLifecycleOwner, Observer {
            recentCallAdapter.setData(it)
        })
        viewModel.getData()
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

    override fun onRoleClick(recentCallItem: DummyRecentCallItem) {

    }
}