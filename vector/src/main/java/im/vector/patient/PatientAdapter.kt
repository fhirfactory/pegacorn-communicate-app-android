package im.vector.patient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import im.vector.R
import kotlinx.android.synthetic.main.item_patient.view.*


class PatientAdapter(private val onClickListener: PatientClickListener) :
        RecyclerView.Adapter<PatientViewHolder>() {
    private val patients = mutableListOf<DemoPatient>()


    fun setData(patients: MutableList<DemoPatient>) {
        this.patients.clear()
        this.patients.addAll(patients)
        notifyDataSetChanged()
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PatientViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_patient, parent, false))


    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        holder.bind(patients[position])
        holder.itemView.setOnClickListener {
            onClickListener.onPatientClick(patients[position])
        }
    }

    override fun getItemCount() = patients.size
}

interface PatientClickListener {
    fun onPatientClick(patient: DemoPatient)
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

    fun bind(patient: DemoPatient) {
        patientNameTextView?.text = patient.name
        patientURNTextView?.text = patient.urn
        patientDobTextView?.text = patient.dob
    }
}