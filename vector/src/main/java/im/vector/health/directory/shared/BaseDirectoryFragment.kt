package im.vector.health.directory.shared

import im.vector.health.directory.MemberClickListener
import im.vector.home.BaseActFragment

abstract class BaseDirectoryFragment : BaseActFragment() {
    var memberClickListener: MemberClickListener? = null

    abstract fun onFavorite(enable: Boolean)
    abstract fun filter(with: String?)
}