package im.vector.code

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.SimpleItemAnimator
import im.vector.R
import im.vector.directory.role.model.CodeEvent
import kotlinx.android.synthetic.main.fragment_code_event.*


class CodeEventFragment : Fragment(), CodeEventClickListener {
    private lateinit var viewModel: CodeEventFragmentViewModel
    private lateinit var codeAdapter: CodeEventsAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_code_event, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CodeEventFragmentViewModel::class.java)
        subscribeUI()

        codeAdapter = CodeEventsAdapter(requireContext(), this)
        (codeEventRecyclerview.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        codeEventRecyclerview.adapter = codeAdapter
        codeEventRecyclerview.setHasFixedSize(true)
    }

    private fun subscribeUI() {

    }

    override fun onCodeClick(code: CodeEvent) {

    }
}