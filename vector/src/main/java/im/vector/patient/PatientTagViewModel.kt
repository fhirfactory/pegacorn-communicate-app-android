package im.vector.patient

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.matrix.androidsdk.MXSession

class PatientTagViewModel : ViewModel() {
    var session: MXSession? = null
    var fileLocation: String? = null
    val patients = MutableLiveData<MutableList<DemoPatient>>()

    fun initSession(session: MXSession) {
        if (this.session == null) {
            this.session = session
        }
    }

    fun getPatientData() {
        val fakePatients = mutableListOf<DemoPatient>()
        fakePatients.add(DemoPatient("Rafi Sadat", "12345678", "01-Jan-1900"))
        fakePatients.add(DemoPatient("Rafi Sadat", "12345678", "01-Jan-1900"))
        fakePatients.add(DemoPatient("Emma Mcdonald", "12345678", "01-Jan-1900"))
        fakePatients.add(DemoPatient("Craig Mcdonald", "12345678", "01-Jan-1900"))
        fakePatients.add(DemoPatient("Lance Christie", "12345678", "01-Jan-1900"))
        patients.postValue(fakePatients)
    }
}