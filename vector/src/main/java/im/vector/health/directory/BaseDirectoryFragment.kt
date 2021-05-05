package im.vector.health.directory

import im.vector.home.BaseActFragment

abstract class BaseDirectoryFragment : BaseActFragment() {
    var roomClickListener: RoomClickListener? = null

    abstract fun onFavorite(enable: Boolean)
    abstract fun filter(with: String?)
}