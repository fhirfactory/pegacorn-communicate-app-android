package im.vector.microservices

import android.content.Context
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import im.vector.directory.people.model.DirectoryPeople
import kotlinx.android.parcel.Parcelize

@Parcelize
class FHIRPractitionerRole(
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
//        @JvmField
//        var activePractitionerSet: List<String>,
        @JvmField
        var roleHistory: FHIRRoleHistory
) : Parcelable {
        val activePractitionerSet: List<String>
        get() = roleHistory.roleHistories.filter { x -> x.endDate == null }.map { x -> x.identifier }

        fun fetchPractitioners(context: Context, callback: (ArrayList<DirectoryPeople>) -> Unit){
                fetchPractitionersRecursive(callback, ArrayList(),0, context)
        }
        private fun fetchPractitionersRecursive(callback: (ArrayList<DirectoryPeople>) -> Unit, list: ArrayList<DirectoryPeople>, index: Int, context: Context) {
                if (index >= activePractitionerSet.count()) {
                        callback(list)
                        return
                }
                DirectoryConnector.getPractitioner(activePractitionerSet[index],context) {
                        list.add(DirectoryConnector.convertPractitioner(it))
                        fetchPractitionersRecursive(callback, list, index + 1, context)
                }
        }
}