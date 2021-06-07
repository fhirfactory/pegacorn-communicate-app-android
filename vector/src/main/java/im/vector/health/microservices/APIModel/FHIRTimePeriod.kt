package im.vector.health.microservices.APIModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FHIRTimePeriod (
        @JvmField
        var startDate: Long,
        @JvmField
        var endDate: Long?
) : Parcelable