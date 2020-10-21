package im.vector.directory.service

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class DummyService(val id: String, val name: String, val telecom: String, val address: String?, val organizationUnit: String, val isFavorite: Boolean) : Parcelable

