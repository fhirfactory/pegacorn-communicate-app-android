package im.vector.calls.recent

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class DummyRecentCallItem(val id: Int, val name: String) : Parcelable

