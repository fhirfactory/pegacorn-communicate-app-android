package im.vector.chat

import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.VectorBaseSearchActivity

class ActChatOneToOneActivity : VectorBaseSearchActivity(){
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
    }
}