package im.vector.patient

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import im.vector.directory.role.model.DropDownItem
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.RoomMediaMessage

class PatientTagViewModel : ViewModel() {
    var session: MXSession? = null
    var fileLocation: String? = null
    var selectedDesignation :DropDownItem? = null
    var mediaMessageArray: ArrayList<RoomMediaMessage>? = null
    val patients = MutableLiveData<MutableList<DemoPatient>>()
    val designations = MutableLiveData<MutableList<DropDownItem>>()
    val selectedPatient = MutableLiveData<DemoPatient?>()
    private val fakePatients = mutableListOf<DemoPatient>()
    private val designationList = mutableListOf<DropDownItem>()

    fun initSession(session: MXSession) {
        if (this.session == null) {
            this.session = session
        }
    }

    fun removeSelectedPatient() {
        selectedPatient.postValue(null)
    }

    fun addSelectedPatient(patient: DemoPatient) {
        selectedPatient.postValue(patient)
    }

    fun filterPatient(text: String) {
        val filterList = fakePatients.filter { it.name.contains(text, true) }
        patients.postValue(filterList as MutableList<DemoPatient>?)
    }


    fun getDesignations(){
        designationList.add(DropDownItem(1, "Aboroginal Liason"))
        designationList.add(DropDownItem(1, "After Hours Hospital Manager"))
        designations.postValue(designationList)
    }

    fun prepareFakePatientData() {
        fakePatients.add(DemoPatient("James Bond", "12345678", "01-Jan-1900"))
        fakePatients.add(DemoPatient("Rafi Sadat", "12345678", "01-Jan-1900"))
        fakePatients.add(DemoPatient("Mark Hunter", "12345678", "01-Jan-1900"))
        fakePatients.add(DemoPatient("Emma Mcdonald", "12345678", "01-Jan-1900"))
        fakePatients.add(DemoPatient("Craig Mcdonald", "12345678", "01-Jan-1900"))
        fakePatients.add(DemoPatient("Lance Christie", "12345678", "01-Jan-1900"))
    }
}