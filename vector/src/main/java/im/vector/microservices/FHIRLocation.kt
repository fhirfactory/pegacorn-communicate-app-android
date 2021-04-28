package im.vector.microservices

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FHIRLocation(
        @JvmField
        var simplifiedID: String,
        @JvmField
        var identifiers: List<FHIRIdentifier>,
        @JvmField
        var displayName: String,
        @JvmField
        var description: String
): Parcelable