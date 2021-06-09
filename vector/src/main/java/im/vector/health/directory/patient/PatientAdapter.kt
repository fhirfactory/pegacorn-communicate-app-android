package im.vector.health.patient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import im.vector.R
import im.vector.health.directory.shared.IStandardDirectoryAdapter
import im.vector.health.microservices.Interfaces.IPatient
import kotlinx.android.synthetic.main.item_patient.view.*


class PatientAdapter(private val onClickListener: PatientClickListener?) :
        RecyclerView.Adapter<PatientViewHolder>(), IStandardDirectoryAdapter<PatientItem> {
    private val patients = mutableListOf<IPatient>()


    override fun setData(items: List<PatientItem>) {
        this.patients.clear()
        this.patients.addAll(items)
        notifyDataSetChanged()
    }

    override fun addPage(items: List<PatientItem>) {
        this.patients.addAll(items)
        notifyDataSetChanged()
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PatientViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_patient, parent, false))


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        holder.bind(patients[position])
        holder.itemView.setOnClickListener {
            onClickListener?.onPatientClick(patients[position])
        }
    }

    override fun getItemCount() = patients.size
}

interface PatientClickListener {
    fun onPatientClick(patient: IPatient)
}

class PatientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var patientNameTextView: TextView? = null
    var patientURNTextView: TextView? = null
    var patientDobTextView: TextView? = null

    init {
        patientNameTextView = itemView.patientNameTextView
        patientURNTextView = itemView.patientURNTextView
        patientDobTextView = itemView.patientDobTextView
    }

    fun bind(patient: IPatient) {
        patientNameTextView?.text = patient.GetName()
        patientURNTextView?.text = patient.GetURN()
        patientDobTextView?.text = patient.GetDOB().toString()
    }
}