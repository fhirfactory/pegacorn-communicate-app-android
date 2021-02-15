package im.vector.activity

import im.vector.activity.util.WaitingViewData

interface SimpleFragmentActivityListener {
    fun showWaitingView()
    fun hideWaitingView()
    fun updateWaitingView(data: WaitingViewData?)
    fun hideKeyboard()
}