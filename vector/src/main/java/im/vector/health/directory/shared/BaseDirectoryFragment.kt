package im.vector.health.directory.shared

import im.vector.health.directory.RoomClickListener
import im.vector.home.BaseActFragment

abstract class BaseDirectoryFragment : BaseActFragment() {
    var roomClickListener: RoomClickListener? = null

    abstract fun onFavorite(enable: Boolean)
    abstract fun filter(with: String?)
}