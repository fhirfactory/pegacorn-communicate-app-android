package im.vector.health.directory.service.model

import android.os.Parcelable
import im.vector.health.microservices.Interfaces.IHealthcareService
import kotlinx.android.parcel.Parcelize

@Parcelize
class HealthcareServiceItem(val innerService: IHealthcareService, var expanded: Boolean) : IHealthcareService {
    override fun GetLongName(): String = innerService.GetLongName()
    override fun GetShortName(): String = innerService.GetShortName()
    override fun GetID(): String = innerService.GetID()
    override fun GetDescription(): String = innerService.GetDescription()
}