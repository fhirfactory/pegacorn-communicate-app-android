package im.vector.patient

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DemoPatient(val name: String, val urn:String, val dob: String) : Parcelable