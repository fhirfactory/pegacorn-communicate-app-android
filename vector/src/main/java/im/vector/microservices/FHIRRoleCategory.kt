package im.vector.microservices

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FHIRRoleCategory(
        @JvmField
        var simplifiedId: String,
        @JvmField
        var identifiers: List<FHIRIdentifier>,
        @JvmField
        var description: String?,
        @JvmField
        var displayName: String,
        @JvmField
        var roles: List<String>
): Parcelable