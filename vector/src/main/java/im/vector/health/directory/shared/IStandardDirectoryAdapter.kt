package im.vector.health.directory.shared

interface IStandardDirectoryAdapter<T> {
    fun setData(items: List<T>)
    fun addPage(items: List<T>)
}