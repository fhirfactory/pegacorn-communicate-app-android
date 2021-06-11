package im.vector.health.microservices.APIModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FHIROrganizationItem(
        @JvmField
        var index: Int,
        @JvmField
        var type: String,
        @JvmField
        var value: String
) : Parcelable