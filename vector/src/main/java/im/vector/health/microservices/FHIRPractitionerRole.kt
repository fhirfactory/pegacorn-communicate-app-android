package im.vector.health.microservices

import android.content.Context
import android.os.Parcelable
import im.vector.health.directory.people.model.DirectoryPeople
import kotlinx.android.parcel.Parcelize

@Parcelize
class FHIRPractitionerRole(
        @JvmField
        var simplifiedID: String,
        @JvmField
        var identifiers: List<FHIRIdentifier>,
        @JvmField
        var displayName: String,
        @JvmField
        var description: String,
        @JvmField
        var systemManaged: Boolean,
        @JvmField
        var primaryOrganizationID: String,
        @JvmField
        var primaryLocationID: String,
        @JvmField
        var primaryRoleID: String,
        @JvmField
        var contactPoints: List<FHIRContactPoint>,
        @JvmField
        var primaryRoleCategoryID: String,
        @JvmField
        var roleHistory: FHIRRoleHistory?,
        @JvmField
        var activePractitionerSet: List<String>
) : Parcelable {
        val assignedPractitioners: List<String>
        get(){
                if (activePractitionerSet.count() > 0)
                        return activePractitionerSet
                roleHistory?.let {return it.roleHistories.filter { x -> x.endDate == null }.map { x -> x.identifier } }
                return ArrayList()
        }

        fun fetchPractitioners(context: Context, callback: (ArrayList<DirectoryPeople>) -> Unit){
                fetchPractitionersRecursive(callback, ArrayList(),0, context)
        }
        private fun fetchPractitionersRecursive(callback: (ArrayList<DirectoryPeople>) -> Unit, list: ArrayList<DirectoryPeople>, index: Int, context: Context) {
                if (index >= assignedPractitioners.count()) {
                        callback(list)
                        return
                }
                DirectoryConnector.getPractitioner(assignedPractitioners[index],context) {
                        list.add(DirectoryConnector.convertPractitioner(it))
                        fetchPractitionersRecursive(callback, list, index + 1, context)
                }
        }
}