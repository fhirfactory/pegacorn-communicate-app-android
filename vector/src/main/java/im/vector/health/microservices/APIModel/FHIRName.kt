package im.vector.health.microservices.APIModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FHIRName (
        @JvmField
        var nameUse: String,
        @JvmField
        var displayName: String,
        @JvmField
        var familyName: String,
        @JvmField
        var givenNames: List<String>,
        @JvmField
        var preferredGivenName: String,
        @JvmField
        var prefixes: List<String>,
        @JvmField
        var suffixes: List<String>,
        @JvmField
        var period: FHIRTimePeriod
) : Parcelable