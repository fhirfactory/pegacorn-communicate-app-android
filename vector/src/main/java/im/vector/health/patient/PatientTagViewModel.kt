package im.vector.health.patient

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import im.vector.health.directory.role.DropDownItem
import im.vector.health.microservices.Interfaces.IPatient
import im.vector.health.microservices.Mock.MockPatient
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.RoomMediaMessage
import org.matrix.androidsdk.rest.model.Event
import java.util.*
import java.util.Date
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
    private val fakePatients = mutableListOf<IPatient>()
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

    fun filterPatient(text: String) {
        val filterList = fakePatients.filter { it.GetName().contains(text, true) }
        patients.postValue(filterList as MutableList<IPatient>?)
    }


    fun getDesignations(){
        designationList.add(DropDownItem(1, "Aboroginal Liason"))
        designationList.add(DropDownItem(1, "After Hours Hospital Manager"))
        designations.postValue(designationList)
    }

    fun prepareFakePatientData() {
        fakePatients.add(MockPatient("Test Patient", "12345678", Date()))
        fakePatients.add(MockPatient("Rafi Sadat", "12345678", Date()))
        fakePatients.add(MockPatient("Mark Hunter", "12345678", Date()))
        fakePatients.add(MockPatient("Emma Mcdonald", "12345678", Date()))
        fakePatients.add(MockPatient("Craig Mcdonald", "12345678", Date()))
        fakePatients.add(MockPatient("Lance Christie", "12345678", Date()))
    }
}