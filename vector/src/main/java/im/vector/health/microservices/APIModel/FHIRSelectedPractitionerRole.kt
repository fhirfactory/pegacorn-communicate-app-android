package im.vector.health.microservices.APIModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FHIRSelectedPractitionerRole(
    @JvmField
    val role: String,
    @JvmField
    val roleCategory: String
) : Parcelable