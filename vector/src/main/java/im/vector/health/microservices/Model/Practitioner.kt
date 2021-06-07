package im.vector.health.microservices.Model

import android.os.Parcelable
import im.vector.health.microservices.APIModel.FHIRPractitioner
import im.vector.health.microservices.APIModel.FHIRPractitionerRole
import im.vector.health.microservices.DirectoryConnector
import im.vector.health.microservices.DirectoryServicesSingleton
import im.vector.health.microservices.Interfaces.IPractitioner
import im.vector.health.microservices.Interfaces.IPractitionerRole
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Practitioner(var innerPractitioner: FHIRPractitioner): IPractitioner, Parcelable {

    override fun GetName(): String = innerPractitioner.displayName
    override fun GetRoles(callback: (List<IPractitionerRole>) -> Unit) {
        FetchRoles{practitionerRoles ->
            val roles = practitionerRoles.map { it }
            callback(roles)
        }
    }

    fun FetchRoles(callback: (ArrayList<IPractitionerRole>) -> Unit){
        FetchRolesRecursive(callback, ArrayList(),0)
    }
    private fun FetchRolesRecursive(callback: (ArrayList<IPractitionerRole>) -> Unit, list: ArrayList<IPractitionerRole>, index: Int) {
        if (index >= innerPractitioner.currentPractitionerRoles.count()) {
            callback(list)
            return
        }
        DirectoryServicesSingleton.Instance().GetPractitionerRole(innerPractitioner.currentPractitionerRoles[index].role) {
            it?.let {
                list.add(it)
            }
            FetchRolesRecursive(callback, list, index + 1)
        }
    }

    override fun GetID(): String = innerPractitioner.simplifiedID
    override fun GetMatrixID(): String = innerPractitioner.matrixId
}