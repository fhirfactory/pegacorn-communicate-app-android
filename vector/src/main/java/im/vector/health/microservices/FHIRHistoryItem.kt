package im.vector.health.microservices

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class FHIRHistoryItem(
        @JvmField
        var identifier: String,
        @JvmField
        var createdDate: Long,
        @JvmField
        var endDate: Long?
) : Parcelable {
}