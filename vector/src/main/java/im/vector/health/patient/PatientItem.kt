package im.vector.health.patient

import kotlinx.android.parcel.Parcelize
import im.vector.health.microservices.interfaces.IPatient
import java.util.*

@Parcelize
class PatientItem(val embeddedPatient: IPatient) : IPatient {
    override fun GetName(): String = embeddedPatient.GetName()

    override fun GetDOB(): Date = embeddedPatient.GetDOB()

    override fun GetURN(): String = embeddedPatient.GetURN()

}