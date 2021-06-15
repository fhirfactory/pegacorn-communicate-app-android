package im.vector.health.microservices.APIModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FHIRHealthcareService(
    val displayName: String,
    val systemManaged: Boolean,
    val identifiers: List<FHIRIdentifier>,
    val simplifiedID: String,
    val organisationalUnit: String
) : Parcelable