package im.vector.patient

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.RoomMediaMessage

class PatientTagViewModel : ViewModel() {
    var session: MXSession? = null
    var fileLocation: String? = null
    var mediaMessageArray: ArrayList<RoomMediaMessage>? = null
    val patients = MutableLiveData<MutableList<DemoPatient>>()
    val selectedPatient = MutableLiveData<DemoPatient?>()
    private val fakePatients = mutableListOf<DemoPatient>()

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

    fun getPatientData() {
        fakePatients.add(DemoPatient("James Bond", "12345678", "01-Jan-1900"))
        fakePatients.add(DemoPatient("Rafi Sadat", "12345678", "01-Jan-1900"))
        fakePatients.add(DemoPatient("Mark Hunter", "12345678", "01-Jan-1900"))
        fakePatients.add(DemoPatient("Emma Mcdonald", "12345678", "01-Jan-1900"))
        fakePatients.add(DemoPatient("Craig Mcdonald", "12345678", "01-Jan-1900"))
        fakePatients.add(DemoPatient("Lance Christie", "12345678", "01-Jan-1900"))
        patients.postValue(fakePatients)
    }
}