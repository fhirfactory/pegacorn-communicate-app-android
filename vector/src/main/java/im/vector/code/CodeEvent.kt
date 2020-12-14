package im.vector.code

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CodeEvent(val codeName:String, val location:String, val date: String, val codeColor:String, val active: Boolean): Parcelable
