package im.vector.directory.service.detail

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import im.vector.Matrix
import im.vector.R
import im.vector.activity.MXCActionBarActivity
import im.vector.directory.people.PeopleClickListener
import im.vector.directory.people.detail.PeopleDetailActivity
import im.vector.directory.people.model.DirectoryPeople
import im.vector.directory.role.model.DummyRole
import im.vector.directory.service.DummyService
import im.vector.util.VectorUtils
import kotlinx.android.synthetic.main.activity_role_detail.*

class ServiceDetailActivity : MXCActionBarActivity(), FragmentManager.OnBackStackChangedListener {

    override fun getLayoutRes(): Int = R.layout.activity_service_detail
    override fun getTitleRes() = R.string.title_activity_service_detail

    override fun initUiAndData() {
        configureToolbar()
        mSession = Matrix.getInstance(this).defaultSession

        val service = intent.getParcelableExtra<DummyService>(SERVICE_EXTRA)
        supportActionBar?.let {
            it.title = service.name
        }
        VectorUtils.loadRoomAvatar(this, session, avatar, service)
    }

    override fun onDestroy() {
        supportFragmentManager.removeOnBackStackChangedListener(this)
        super.onDestroy()
    }

    override fun onBackStackChanged() {
        if (0 == supportFragmentManager.backStackEntryCount) {
            supportActionBar?.title = getString(getTitleRes())
        }
    }

    companion object {
        private const val SERVICE_EXTRA = "SERVICE_EXTRA"
        fun intent(context: Context, dummyService: DummyService): Intent {
            return Intent(context, ServiceDetailActivity::class.java).also {
                it.putExtra(SERVICE_EXTRA, dummyService)
            }
        }
    }
}