package im.vector.microservices

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

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