package im.vector.health.patient

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import im.vector.health.microservices.Interfaces.IPatient
import java.util.*

@Parcelize
class DemoPatient(val embeddedPatient: IPatient) : IPatient {
    override fun GetName(): String = embeddedPatient.GetName()

    override fun GetDOB(): Date = embeddedPatient.GetDOB()

    override fun GetURN(): String = embeddedPatient.GetURN()

}