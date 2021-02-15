package im.vector.calls.recent

import android.os.Parcelable
import android.text.SpannableStringBuilder
import android.text.Spanned
import im.vector.directory.RoundedBackgroundSpan
import kotlinx.android.parcel.Parcelize


@Parcelize
data class DummyRecentCallItem(val id: Int, val name: String) : Parcelable

