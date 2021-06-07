package im.vector.health.microservices.APIModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FHIRRole(
        @JvmField
        var simplifiedID: String,
        @JvmField
        var displayName: String,
        @JvmField
        var roleCategory: String,
        @JvmField
        var description: String?
) : Parcelable