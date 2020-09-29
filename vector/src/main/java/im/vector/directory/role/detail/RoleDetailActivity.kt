package im.vector.directory.role.detail

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
import im.vector.util.VectorUtils
import kotlinx.android.synthetic.main.activity_role_detail.*

class RoleDetailActivity : MXCActionBarActivity(), FragmentManager.OnBackStackChangedListener, PeopleClickListener {
    private lateinit var roleAdapter: RolesDetailAdapter

    override fun getLayoutRes(): Int = R.layout.activity_role_detail
    override fun getTitleRes() = R.string.title_activity_role_detail

    override fun initUiAndData() {
        configureToolbar()
        mSession = Matrix.getInstance(this).defaultSession

        val role = intent.getParcelableExtra<DummyRole>(ROLE_EXTRA)
        val peopleClickable = intent.getBooleanExtra(PEOPLE_CLICKABLE, false)
        VectorUtils.loadRoomAvatar(this, session, avatar, role)
        officialName.text = role.officialName
        secondaryName.text = role.secondaryName

        roleAdapter = RolesDetailAdapter(this, if (peopleClickable) this else null)
        roleRecyclerview.layoutManager = LinearLayoutManager(this)
        roleRecyclerview.adapter = roleAdapter
        roleAdapter.setData(role)

        callIcon.setOnClickListener { }
        chatIcon.setOnClickListener { }
        videoCallIcon.setOnClickListener { }

        //test data
        val testPeopleData = mutableListOf<DirectoryPeople>()
        testPeopleData.add(DirectoryPeople("1", "Liam", "Software Developer", null, "Emergency Department", "Hospital Department"))
        testPeopleData.add(DirectoryPeople("2", "Noah", "Business Analyst", null, "Emergency Department", "Hospital Department"))
        testPeopleData.add(DirectoryPeople("3", "William", "Scrum Master", null, "Emergency Department", "Hospital Department"))
        testPeopleData.add(DirectoryPeople("4", "Oliver", "Designer", null, "Emergency Department", "Hospital Department"))
        testPeopleData.add(DirectoryPeople("5", "James", "Test Analyst", null, "Emergency Department", "Hospital Department"))

        roleAdapter.setData(testPeopleData)
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
        private const val ROLE_EXTRA = "ROLE_EXTRA"
        private const val PEOPLE_CLICKABLE = "PEOPLE_CLICKABLE"
        fun intent(context: Context, dummyRole: DummyRole, peopleClickable: Boolean = false): Intent {
            return Intent(context, RoleDetailActivity::class.java).also {
                it.putExtra(ROLE_EXTRA, dummyRole)
                it.putExtra(PEOPLE_CLICKABLE, peopleClickable)
            }
        }
    }

    override fun onPeopleClick(directoryPeople: DirectoryPeople) {
        startActivity(PeopleDetailActivity.intent(this, directoryPeople, false))
    }

    override fun onPeopleFavorite(directoryPeople: DirectoryPeople) {
        //TODO("Not yet implemented")
    }
}