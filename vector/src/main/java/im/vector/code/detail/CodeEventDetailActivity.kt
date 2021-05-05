package im.vector.code.detail

import android.content.Context
import android.content.Intent
import im.vector.R
import im.vector.activity.MXCActionBarActivity
import im.vector.code.CodeEvent

class CodeEventDetailActivity : MXCActionBarActivity(){

    override fun getLayoutRes(): Int = R.layout.activity_code_detail
    override fun getTitleRes() = R.string.title_activity_code_detail

    override fun initUiAndData() {
        configureToolbar()
    }

    companion object {
        private const val CODE_EXTRA = "CODE_EXTRA"
        fun intent(context: Context, codeEvent: CodeEvent): Intent {
            return Intent(context, CodeEventDetailActivity::class.java).also {
                it.putExtra(CODE_EXTRA, codeEvent)
            }
        }
    }
}