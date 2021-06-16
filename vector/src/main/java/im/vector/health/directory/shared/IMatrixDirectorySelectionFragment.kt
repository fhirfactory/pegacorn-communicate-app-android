package im.vector.health.directory.shared

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import im.vector.health.directory.MemberClickListener
import im.vector.health.microservices.interfaces.MatrixItem

interface IMatrixDirectorySelectionFragment<T: MatrixItem>: IProvidesMatrixItems<T>, IFilterable {
    fun provideMemberClickListener(listener: MemberClickListener)
    fun getFragment(): Fragment
    @StringRes
    fun getSelectionTitleResource(): Int
}