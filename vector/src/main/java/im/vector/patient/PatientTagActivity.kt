package im.vector.patient

import im.vector.R
import im.vector.activity.SimpleFragmentActivity

class PatientTagActivity : SimpleFragmentActivity() {
    override fun getTitleRes() = R.string.invite_title

    override fun initUiAndData() {
        super.initUiAndData()
        if (supportFragmentManager.fragments.isEmpty()) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, PatientTagFragment())
                    .commitNow()
        }
    }
}