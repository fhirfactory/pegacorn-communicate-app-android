package im.vector.health.microservices.APIModel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FHIRStatus(
    val loggedIn: Boolean,
    val active: Boolean,
    val typing: Boolean
): Parcelable