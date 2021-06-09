package im.vector.health.directory.patient

import android.content.Context
import im.vector.health.directory.shared.StandardDirectoryFragment
import im.vector.health.microservices.DirectoryServicesSingleton
import im.vector.health.microservices.Interfaces.IPatient
import im.vector.health.patient.PatientAdapter
import im.vector.health.patient.PatientClickListener
import im.vector.health.patient.PatientItem
import im.vector.health.patient.PatientViewHolder

class DirectoryPatientFragment: StandardDirectoryFragment<PatientAdapter,PatientViewHolder,PatientItem>() {
    override fun constructAdapter(context: Context, selectable: Boolean): PatientAdapter {
        return PatientAdapter(object: PatientClickListener{
            override fun onPatientClick(patient: IPatient) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun getData(forPage: Int, withPageSize: Int, query: String?, addItem: (List<PatientItem>?, Int) -> Unit) {
        DirectoryServicesSingleton.Instance().GetPatients(query, page, pageSize){ res, count ->
            addItem(res?.map { PatientItem(it) }, count)
        }
    }

    override fun getDataFavourites(forPage: Int, withPageSize: Int, query: String?, addItem: (List<PatientItem>?, Int) -> Unit) {
        DirectoryServicesSingleton.Instance().GetPatients(query, page, pageSize){ res, count ->
            addItem(res?.map { PatientItem(it) }, count)
        }
    }
}