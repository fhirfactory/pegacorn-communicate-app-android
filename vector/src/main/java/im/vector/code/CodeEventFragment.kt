package im.vector.code

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.SimpleItemAnimator
import im.vector.R
import im.vector.code.detail.CodeEventDetailActivity
import im.vector.fragments.AbsHomeFragment
import im.vector.ui.themes.ThemeUtils.getColor
import kotlinx.android.synthetic.main.fragment_code_event.*
import org.matrix.androidsdk.data.Room


class CodeEventFragment : AbsHomeFragment(), CodeEventClickListener {
    private lateinit var viewModel: CodeEventFragmentViewModel
    private lateinit var codeAdapter: CodeEventsAdapter
    override fun getLayoutResId(): Int = R.layout.fragment_code_event

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CodeEventFragmentViewModel::class.java)

        activity?.let { activity ->
            mPrimaryColor = getColor(activity, R.attr.vctr_tab_home)
            mSecondaryColor = getColor(activity, R.attr.vctr_tab_home_secondary)

            mFabColor = ContextCompat.getColor(activity, R.color.tab_people)
            mFabPressedColor = ContextCompat.getColor(activity, R.color.tab_people_secondary)
        }

        codeAdapter = CodeEventsAdapter(requireContext(), this)
        (codeEventRecyclerview.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        codeEventRecyclerview.adapter = codeAdapter
        codeEventRecyclerview.setHasFixedSize(true)
        codeEventRecyclerview.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        subscribeUI()
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

    private fun subscribeUI() {
        viewModel.codeList.observe(viewLifecycleOwner, Observer {
            codeAdapter.setData(it)
        })
        viewModel.getCodeEvents()
    }

    override fun onCodeClick(code: CodeEvent) {
        startActivity(CodeEventDetailActivity.intent(requireContext(), code))
    }
}