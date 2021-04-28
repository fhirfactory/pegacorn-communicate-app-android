package im.vector.microservices

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FHIRRoleHistory(
        @JvmField
        var roleHistories: List<FHIRHistoryItem>
) : Parcelable