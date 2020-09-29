package im.vector.directory.service

import android.view.Menu
import im.vector.R
import im.vector.directory.BaseDirectoryFragment
import org.matrix.androidsdk.data.Room


class DirectoryServiceFragment : BaseDirectoryFragment() {
    override fun getLayoutResId() = R.layout.fragment_directory_service

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.ic_action_advanced_search)?.isVisible = false
    }

    override fun onFavorite(enable: Boolean) {
    }

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