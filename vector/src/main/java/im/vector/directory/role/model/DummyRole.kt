package im.vector.directory.role.model

import android.content.Context
import android.os.Parcelable
import im.vector.directory.people.model.DirectoryPeople
import im.vector.microservices.DirectoryConnector
import im.vector.microservices.FHIRPractitionerRole
import kotlinx.android.parcel.Parcelize


@Parcelize
data class DummyRole(val id: String, val officialName: String, val secondaryName: String, val avatarUrl: String?, val organizationUnit: String, val fhirPractitionerRole: FHIRPractitionerRole) : Parcelable {
    var expanded = false
    var type: Int = 1
    var filled = false
    fun fetchPractitioners(context: Context, callback: (ArrayList<DirectoryPeople>) -> Unit) = fhirPractitionerRole.fetchPractitioners(context,callback)
    fun fetchDetailedRoleInfo(context: Context, callback: (DummyRole) -> Unit) {

    }
}

@Parcelize
data class Role(val id: String, val name: String, val category: String, val description: String?) : Parcelable {
    override fun toString(): String {
        return name
    }
}

@Parcelize
data class Speciality(val id: String, val name: String) : Parcelable {
    override fun toString(): String {
        return name
    }
}

@Parcelize
data class DummyLocation(val id: String, val name: String) : Parcelable {
    override fun toString(): String {
        return name
    }
}

@Parcelize
data class Team(val id: String, val name: String) : Parcelable
