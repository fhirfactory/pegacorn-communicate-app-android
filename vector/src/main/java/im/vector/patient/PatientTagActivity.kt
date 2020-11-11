package im.vector.patient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import im.vector.R
import im.vector.activity.SimpleFragmentActivity

class PatientTagActivity : SimpleFragmentActivity() {
    override fun getTitleRes() = R.string.patient_tag_title

    override fun initUiAndData() {
        super.initUiAndData()
        if (supportFragmentManager.fragments.isEmpty()) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, PatientTagFragment.newInstance(intent.extras))
                    .commitNow()
        }
    }

    companion object {
        const val FILE_LOCATION_EXTRA = "FILE_LOCATION_EXTRA"
        const val BUNDLE_EXTRA = "BUNDLE_EXTRA"
        fun intent(context: Context, fileLocation: String): Intent {
            return Intent(context, PatientTagActivity::class.java).also {
                it.putExtra(FILE_LOCATION_EXTRA, fileLocation)
            }
        }
    }
}