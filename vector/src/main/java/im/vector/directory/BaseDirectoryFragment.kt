package im.vector.directory

import im.vector.home.BaseActFragment

abstract class BaseDirectoryFragment : BaseActFragment() {
    abstract fun onFavorite(enable: Boolean)
}