package im.vector.health.microservices.model

import android.os.Parcelable
import im.vector.health.microservices.APIModel.FHIRPractitioner
import im.vector.health.microservices.interfaces.IPractitioner
import im.vector.health.microservices.interfaces.IPractitionerRole
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Practitioner(var innerPractitioner: FHIRPractitioner): IPractitioner, Parcelable {
    override fun GetName(): String = innerPractitioner.displayName
    override fun GetRoles(callback: (List<IPractitionerRole>) -> Unit) = callback(innerPractitioner.currentPractitionerRoles.map { PractitionerRole(it) })
    override fun GetJobTitle(): String = innerPractitioner.mainJobTitle ?: ""
    override fun GetBusinessUnit(): String {
        if (innerPractitioner.organisationStructure.count() > 4) return innerPractitioner.organisationStructure[4].value
        return ""
    }

    override fun GetOrganization(): String {
        if (innerPractitioner.organisationStructure.count() > 4) return innerPractitioner.organisationStructure[1].value
        return ""
    }
    override fun GetID(): String = innerPractitioner.simplifiedID
    override fun GetMatrixID(): String = innerPractitioner.matrixId
    override fun GetEmailAddress(): String? = innerPractitioner.identifiers.find { x -> x.type == "EmailAddress" }?.value
    override fun GetPhoneNumber(): String? = innerPractitioner.contactPoints.find { x -> x.type == "MOBILE" }?.value
    override fun GetOnlineStatus(): Boolean = innerPractitioner.practitionerStatus.loggedIn
    override fun GetActiveStatus(): Boolean = innerPractitioner.practitionerStatus.active
}