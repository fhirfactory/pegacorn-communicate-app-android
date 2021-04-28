package im.vector.microservices

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FHIRIdentifier (
    @JvmField
    var type: String,
    @JvmField
    var use: String,
    @JvmField
    var value: String,
    @JvmField
    var leafValue: String
) : Parcelable {
    val fhirIdentifier: String
    get() {
        return "${type}|${use}|${value}"
    }
}