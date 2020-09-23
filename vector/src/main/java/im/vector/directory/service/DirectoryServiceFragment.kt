package im.vector.directory.service

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.transition.TransitionManager
import im.vector.R
import im.vector.directory.DirectoryFragment
import im.vector.directory.role.detail.RoleDetailActivity
import im.vector.directory.role.model.*
import im.vector.fragments.AbsHomeFragment
import im.vector.home.BaseCommunicateHomeFragment
import kotlinx.android.synthetic.main.fragment_directory_role.*
import org.matrix.androidsdk.data.Room


class DirectoryServiceFragment : BaseCommunicateHomeFragment() {
    override fun getLayoutResId() = R.layout.fragment_directory_role

    override fun getRooms(): MutableList<Room> {
        TODO("Not yet implemented")
    }

    override fun onFilter(pattern: String?, listener: OnFilterListener?) {
        TODO("Not yet implemented")
    }

    override fun onResetFilter() {
        TODO("Not yet implemented")
    }
}