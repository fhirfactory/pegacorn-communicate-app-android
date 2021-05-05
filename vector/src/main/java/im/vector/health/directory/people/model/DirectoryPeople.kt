package im.vector.health.directory.people.model

import android.content.Context
import android.os.Parcelable
import android.text.SpannableStringBuilder
import android.text.Spanned
import im.vector.health.directory.RoundedBackgroundSpan
import im.vector.health.directory.role.model.DummyRole
import im.vector.health.microservices.DirectoryConnector
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DirectoryPeople(val id: String, val officialName: String, val jobTitle: String, val avatarUrl: String?, val organisations: String, val businessUnits: String, val rolesUnloaded: List<String>) : Parcelable {
    var expanded = false
    fun getSpannableStringBuilder(spanTextBackgroundColor: Int, spanTextColor: Int, textSize: Float, title: String, value: String): SpannableStringBuilder {
        val stringBuilder = SpannableStringBuilder()
        stringBuilder.append(title).append(": ").append(value).append(" ")
        val tagSpan = RoundedBackgroundSpan(spanTextBackgroundColor, spanTextColor, textSize)
        stringBuilder.setSpan(tagSpan, 0, stringBuilder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return stringBuilder
    }
    fun FetchRoles(context: Context, callback: (ArrayList<DummyRole>) -> Unit){
        FetchRolesRecursive(callback, ArrayList(),0, context)
    }
    private fun FetchRolesRecursive(callback: (ArrayList<DummyRole>) -> Unit, list: ArrayList<DummyRole>, index: Int, context: Context) {
        if (index >= rolesUnloaded.count()) {
            callback(list)
            return
        }
        DirectoryConnector.getPractitionerRole(rolesUnloaded[index],context) {
            list.add(DirectoryConnector.convertPractitionerRole(it))
            FetchRolesRecursive(callback, list, index + 1, context)
        }
    }
}

@Parcelize
data class TemporaryRoom(val people: DirectoryPeople?, val role: DummyRole?) : Parcelable