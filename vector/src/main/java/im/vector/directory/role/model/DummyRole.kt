package im.vector.directory.role.model

import android.os.Parcelable
import android.text.SpannableStringBuilder
import android.text.Spanned
import im.vector.directory.RoundedBackgroundSpan
import kotlinx.android.parcel.Parcelize


@Parcelize
data class DummyRole(val id: String, val officialName: String, val secondaryName: String, val avatarUrl: String?, val organizationUnit: String, val roles: ArrayList<Role>,
                     val speciality: ArrayList<Speciality>, val location: ArrayList<DummyLocation>, val teams: ArrayList<Team>) : Parcelable {
    var expanded = false
    var type: Int = 1
}

@Parcelize
data class Role(val id: String, val name: String, val category: String) : Parcelable {
    override fun toString(): String {
        return name
    }
}

@Parcelize
data class Speciality(val id: String, val name: String) : Parcelable {
    override fun toString(): String {
        return name
    }
}

@Parcelize
data class DummyLocation(val id: String, val name: String) : Parcelable {
    override fun toString(): String {
        return name
    }
}

@Parcelize
data class Team(val id: String, val name: String) : Parcelable
