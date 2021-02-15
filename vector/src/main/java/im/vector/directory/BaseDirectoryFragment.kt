package im.vector.directory

import im.vector.home.BaseActFragment

abstract class BaseDirectoryFragment : BaseActFragment() {
    var roomClickListener: RoomClickListener? = null

    abstract fun onFavorite(enable: Boolean)
}