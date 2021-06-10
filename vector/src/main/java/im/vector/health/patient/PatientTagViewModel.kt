package im.vector.health.patient

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import im.vector.health.directory.role.DropDownItem
import im.vector.health.microservices.Interfaces.IPatient
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.RoomMediaMessage
import org.matrix.androidsdk.rest.model.Event
import kotlin.collections.ArrayList

class PatientTagViewModel : ViewModel() {
    var session: MXSession? = null
    var fileLocation: String? = null
    var selectedDesignation :DropDownItem? = null
    var mediaMessageArray: ArrayList<RoomMediaMessage>? = null
    var event: Event? = null
    val patients = MutableLiveData<MutableList<IPatient>>()
    val designations = MutableLiveData<MutableList<DropDownItem>>()
    val selectedPatient = MutableLiveData<IPatient?>()
    private val designationList = mutableListOf<DropDownItem>()

    fun initSession(session: MXSession) {
        if (this.session == null) {
            this.session = session
        }
    }

    fun removeSelectedPatient() {
        selectedPatient.postValue(null)
    }

    fun addSelectedPatient(patient: IPatient) {
        selectedPatient.postValue(patient)
    }


    fun getDesignations(){
        designationList.add(DropDownItem(1, "Aboroginal Liason"))
        designationList.add(DropDownItem(1, "After Hours Hospital Manager"))
        designations.postValue(designationList)
    }
}