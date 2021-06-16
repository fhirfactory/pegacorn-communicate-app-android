package im.vector.health.directory.shared

interface IFilterable {
    fun applyFilter(query: String?)
}