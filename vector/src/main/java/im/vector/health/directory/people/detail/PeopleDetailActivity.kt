package im.vector.health.directory.people.detail

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.MXCActionBarActivity
import im.vector.activity.VectorMemberDetailsActivity
import im.vector.activity.VectorRoomActivity
import im.vector.health.directory.role.RoleClickListener
import im.vector.health.directory.role.detail.RoleDetailActivity
import im.vector.health.directory.role.model.PractitionerRoleItem
import im.vector.health.microservices.Interfaces.IPractitioner
import im.vector.util.VectorUtils
import kotlinx.android.synthetic.main.activity_people_detail.*
import org.matrix.androidsdk.core.Log
import org.matrix.androidsdk.core.callback.ApiCallback
import org.matrix.androidsdk.core.model.MatrixError
import java.util.*

class PeopleDetailActivity : MXCActionBarActivity(), FragmentManager.OnBackStackChangedListener, RoleClickListener {
    private lateinit var peopleDetailAdapter: PeopleDetailAdapter

    override fun getLayoutRes(): Int = R.layout.activity_people_detail

    // direct message
    /**
     * callback for the creation of the direct message room
     */
    private val mCreateDirectMessageCallBack: ApiCallback<String> = object : ApiCallback<String> {
        override fun onSuccess(roomId: String) {
            val params: MutableMap<String, Any> = HashMap()
            params[VectorRoomActivity.EXTRA_MATRIX_ID] = mSession.myUserId
            params[VectorRoomActivity.EXTRA_ROOM_ID] = roomId
            params[VectorRoomActivity.EXTRA_EXPAND_ROOM_HEADER] = true
            CommonActivityUtils.goToRoomPage(this@PeopleDetailActivity, mSession, params)
        }

        override fun onMatrixError(e: MatrixError) {

        }

        override fun onNetworkError(e: Exception) {

        }

        override fun onUnexpectedError(e: Exception) {

        }
    }

    override fun initUiAndData() {
        configureToolbar()
        mSession = Matrix.getInstance(this).defaultSession

        val people = intent.getParcelableExtra<IPractitioner>(PEOPLE_EXTRA)
        val roleClickable = intent.getBooleanExtra(ROLE_CLICKABLE, false)
        supportActionBar?.let {
            it.title = people.GetName()
        }
        VectorUtils.loadRoomAvatar(this, session, avatar, people)

        jobTitle.text = "Job title"
        organisation.text = "Organization"
        businessUnit.text = "Business Unit"

        peopleDetailAdapter = PeopleDetailAdapter(this, if (roleClickable) this else null)
        peopleRecyclerview.layoutManager = LinearLayoutManager(this)
        peopleRecyclerview.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        peopleRecyclerview.adapter = peopleDetailAdapter
        peopleDetailAdapter.setData(people)

        people.GetRoles{roles ->
            peopleDetailAdapter.setData(roles.map { PractitionerRoleItem(it) })
        }


        callIcon.setOnClickListener { }
        chatIcon.setOnClickListener {
            //enableProgressBarView(CommonActivityUtils.UTILS_DISPLAY_PROGRESS_BAR)
            mSession.createDirectMessageRoom(people.GetMatrixID(), mCreateDirectMessageCallBack)
        }
        videoCallIcon.setOnClickListener { }
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
        private const val PEOPLE_EXTRA = "PEOPLE_EXTRA"
        private const val ROLE_CLICKABLE = "ROLE_CLICKABLE"
        fun intent(context: Context, directoryPeople: IPractitioner, roleClickable: Boolean = false): Intent {
            return Intent(context, PeopleDetailActivity::class.java).also {
                it.putExtra(PEOPLE_EXTRA, directoryPeople)
                it.putExtra(ROLE_CLICKABLE, roleClickable)
            }
        }
    }

    override fun onRoleClick(role: PractitionerRoleItem, forRemove: Boolean) {
        val newIntent = RoleDetailActivity.intent(this, role, intent.getBooleanExtra(ROLE_CLICKABLE, false))
        startActivity(newIntent)
    }
}