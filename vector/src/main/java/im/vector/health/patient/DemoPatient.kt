package im.vector.health.patient

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DemoPatient(val name: String, val urn:String, val dob: String) : Parcelable