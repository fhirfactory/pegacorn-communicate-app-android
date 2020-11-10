package im.vector.patient

import android.content.Context
import android.content.Intent
import im.vector.R
import im.vector.activity.SimpleFragmentActivity

class PatientTagActivity : SimpleFragmentActivity() {
    override fun getTitleRes() = R.string.patient_tag_title

    override fun initUiAndData() {
        super.initUiAndData()
        if (supportFragmentManager.fragments.isEmpty()) {
            val fileLocation = intent.getStringExtra(FILE_LOCATION__EXTRA)
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, PatientTagFragment.newInstance(fileLocation))
                    .commitNow()
        }
    }

    companion object {
        private const val FILE_LOCATION__EXTRA = "FILE_LOCATION_EXTRA"
        fun intent(context: Context, fileLocation: String): Intent {
            return Intent(context, PatientTagActivity::class.java).also {
                it.putExtra(FILE_LOCATION__EXTRA, fileLocation)
            }
        }
    }
}