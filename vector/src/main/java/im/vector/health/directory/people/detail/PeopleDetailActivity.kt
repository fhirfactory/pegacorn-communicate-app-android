package im.vector.health.directory.people.detail

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import im.vector.Matrix
import im.vector.R
import im.vector.activity.CommonActivityUtils
import im.vector.activity.MXCActionBarActivity
import im.vector.health.directory.role.RoleClickListener
import im.vector.health.directory.role.detail.RoleDetailActivity
import im.vector.health.directory.role.model.PractitionerRoleItem
import im.vector.health.directory.shared.HandlesAPIErrors
import im.vector.health.directory.shared.MessagingSupport
import im.vector.health.microservices.DirectoryServicesSingleton
import im.vector.health.microservices.interfaces.IPractitioner
import im.vector.util.VectorUtils
import kotlinx.android.synthetic.main.activity_people_detail.*
import org.matrix.androidsdk.MXSession

class PeopleDetailActivity : MXCActionBarActivity(), MessagingSupport, FragmentManager.OnBackStackChangedListener, RoleClickListener, HandlesAPIErrors {
    private lateinit var peopleDetailAdapter: PeopleDetailAdapter

    override fun getLayoutRes(): Int = R.layout.activity_people_detail

    override fun initUiAndData() {
        configureToolbar()
        mSession = Matrix.getInstance(this).defaultSession

        val people = intent.getParcelableExtra<IPractitioner>(PEOPLE_EXTRA)
        val roleClickable = intent.getBooleanExtra(ROLE_CLICKABLE, false)
        supportActionBar?.let {
            it.title = people.GetName()
        }
        VectorUtils.loadRoomAvatar(this, session, avatar, people)

        jobTitle.text = people.GetJobTitle()
        organisation.text = people.GetOrganization()
        businessUnit.text = people.GetBusinessUnit()

        peopleDetailAdapter = PeopleDetailAdapter(this, if (roleClickable) this else null)
        peopleRecyclerview.layoutManager = LinearLayoutManager(this)
        peopleRecyclerview.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        peopleRecyclerview.adapter = peopleDetailAdapter
        peopleDetailAdapter.setData(people)

        //we need to get the fully populated person object, rather than the version without practitioner roles
        DirectoryServicesSingleton.Instance().GetPractitioner(people.GetID(), {
            it?.let { person ->
                person.GetRoles{roles ->
                    peopleDetailAdapter.setData(roles.map { role -> PractitionerRoleItem(role) })
                }
            }
        }){
            displayError(it)
        }



        callIcon.setOnClickListener {
            call(false, people.GetMatrixID())
        }
        chatIcon.setOnClickListener {
            startChat(people.GetMatrixID())
        }
        videoCallIcon.setOnClickListener {
            call(true, people.GetMatrixID())
        }
        if (people.GetMatrixID() == mSession?.myUserId) {
            chatIcon?.isEnabled = false
            videoCallIcon?.isEnabled = false
            chatIcon?.visibility = View.INVISIBLE
            videoCallIcon?.visibility = View.INVISIBLE
            callIcon?.setImageResource(R.drawable.ic_material_message_black)
            callIcon.setOnClickListener {
                startChat(people.GetMatrixID())
            }
        }
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

    override var currentSession: MXSession = Matrix.getInstance(this).defaultSession
    override fun getActivity(): FragmentActivity = this
    override fun getCurrentContext(): Context = this
}