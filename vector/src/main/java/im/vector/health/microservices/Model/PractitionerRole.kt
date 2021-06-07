package im.vector.health.microservices.Model

import android.os.Parcelable
import im.vector.health.microservices.APIModel.FHIRPractitioner
import im.vector.health.microservices.APIModel.FHIRPractitionerRole
import im.vector.health.microservices.APIModel.FHIRTools
import im.vector.health.microservices.DirectoryConnector
import im.vector.health.microservices.DirectoryServicesSingleton
import im.vector.health.microservices.Interfaces.IPractitioner
import im.vector.health.microservices.Interfaces.IPractitionerRole
import kotlinx.android.parcel.Parcelize

@Parcelize
open class PractitionerRole(var practitionerRole: FHIRPractitionerRole) : IPractitionerRole, Parcelable {

    override fun GetLongName(): String = FHIRTools.getNameForIdentifier(practitionerRole.identifiers, "LongName")?:practitionerRole.displayName

    override fun GetShortName(): String = practitionerRole.displayName

    override fun GetOrgName(): String = practitionerRole.primaryOrganizationID

    override fun GetRoleName(): String = practitionerRole.primaryRoleID

    override fun GetRoleCategory(): String = practitionerRole.primaryRoleCategoryID

    override fun GetLocation(): String = practitionerRole.primaryLocationID

    override fun GetPractitioners(callback: (List<IPractitioner>) -> Unit) {
        fetchPractitioners { fhirPractitioners ->
            val practitioners = fhirPractitioners.map { it }
            callback(practitioners)
        }
    }

    fun fetchPractitioners(callback: (ArrayList<IPractitioner>) -> Unit){
        fetchPractitionersRecursive(callback, ArrayList(),0)
    }
    private fun fetchPractitionersRecursive(callback: (ArrayList<IPractitioner>) -> Unit, list: ArrayList<IPractitioner>, index: Int) {
        if (index >= practitionerRole.activePractitionerSet.count()) {
            callback(list)
            return
        }
        DirectoryServicesSingleton.Instance().GetPractitioner(practitionerRole.activePractitionerSet[index]) {
            it?.let {
                list.add(it)
            }
            fetchPractitionersRecursive(callback, list, index + 1)
        }
    }

    override fun GetID(): String = practitionerRole.simplifiedID

    override fun GetActive(): Boolean = practitionerRole.activePractitionerSet.any()
}