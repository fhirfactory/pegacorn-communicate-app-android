package im.vector.health.microservices.model

import im.vector.health.microservices.APIModel.FHIRHealthcareService
import im.vector.health.microservices.APIModel.FHIRTools
import im.vector.health.microservices.interfaces.IHealthcareService
import kotlinx.android.parcel.Parcelize

@Parcelize
class HealthcareService (val innerHealthcareService: FHIRHealthcareService): IHealthcareService {
    override fun GetLongName(): String  = FHIRTools.getNameForIdentifier(innerHealthcareService.identifiers,"LongName") ?: innerHealthcareService.displayName

    override fun GetShortName(): String = FHIRTools.getNameForIdentifier(innerHealthcareService.identifiers,"ShortName") ?: innerHealthcareService.displayName

    override fun GetID(): String = innerHealthcareService.simplifiedID

    override fun GetDescription(): String = innerHealthcareService.description

}