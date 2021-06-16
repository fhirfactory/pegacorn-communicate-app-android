package im.vector.health.directory.shared

import im.vector.health.microservices.interfaces.MatrixItem

interface IProvidesMatrixItems<T: MatrixItem> {
    fun selectItem(item: T)
    fun deselectItem(item: T)

    //dirty hacks to avoid the consequences of type erasure
    fun unsafeSelectItem(item: MatrixItem) {
        selectItem(item as T)
    }
    fun unsafeDeselectItem(item: MatrixItem) {
        deselectItem(item as T)
    }
    fun receivesItem(item: MatrixItem): Boolean
}