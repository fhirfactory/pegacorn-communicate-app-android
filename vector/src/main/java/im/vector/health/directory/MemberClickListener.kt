package im.vector.health.directory

import im.vector.health.microservices.interfaces.MatrixItem


interface MemberClickListener {
    fun onMemberClick(member: MatrixItem, forRemove: Boolean = false)
}