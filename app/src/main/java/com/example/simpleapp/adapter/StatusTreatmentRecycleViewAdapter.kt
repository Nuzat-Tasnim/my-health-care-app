package com.example.simpleapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleapp.R
import com.example.simpleapp.model.Patient
import com.example.simpleapp.model.Post
import com.example.simpleapp.model.Treatment
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class StatusTreatmentRecycleViewAdapter(val patient: Patient, val treatments:List<Treatment>): RecyclerView.Adapter<StatusTreatmentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusTreatmentViewHolder {
        Log.d("MYTAG", "inside status adapter ${treatments}")

        val layoutInflater = LayoutInflater.from(parent.context)
        val list_item = layoutInflater.inflate(R.layout.status_item_layout, parent, false)
        return StatusTreatmentViewHolder(list_item)
    }

    override fun getItemCount(): Int {
        return treatments.size
    }


    override fun onBindViewHolder(holder: StatusTreatmentViewHolder, position: Int) {
        holder.bind(position, patient, treatments[position])
    }

}

class StatusTreatmentViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    fun bind(position: Int, patient: Patient, treatment: Treatment){
        val statusLayout = view.findViewById<LinearLayout>(R.id.statusLayout)
        val treatmentLayout = view.findViewById<LinearLayout>(R.id.treatmentLayout)

        val tvStatusBloodtype = view.findViewById<TextView>(R.id.tvStatusBloodtype)
        val tvStatusAllergies = view.findViewById<TextView>(R.id.tvStatusAllergies)
        val tvStatusMedicalHistory = view.findViewById<TextView>(R.id.tvStatusMedicalHistory)


        val tvcheckupDate = view.findViewById<TextView>(R.id.tvcheckupDate)
        val tvStatusSymptoms = view.findViewById<TextView>(R.id.tvStatusSymptoms)
        val tvStatusSicknessDuration = view.findViewById<TextView>(R.id.tvStatusSicknessDuration)
        val tvStatusWeight = view.findViewById<TextView>(R.id.tvStatusWeight)
        val tvStatusPressure = view.findViewById<TextView>(R.id.tvStatusPressure)
        val tvStatusSugarLevel = view.findViewById<TextView>(R.id.tvStatusSugarLevel)
        val tvStatusAssessment = view.findViewById<TextView>(R.id.tvStatusAssessment)

        tvStatusBloodtype.text = patient.bloodType
        tvStatusAllergies.text = patient.allergies
        tvStatusMedicalHistory.text = patient.medicalHistories

        tvcheckupDate.text = treatment.date
        tvStatusSymptoms.text = treatment.symptom
        tvStatusSicknessDuration.text = treatment.duration
        tvStatusWeight.text = treatment.weight.toString()
        tvStatusPressure.text = "${treatment.pressureHigh.toString()}~${treatment.pressureLow.toString()}"
        tvStatusSugarLevel.text = treatment.sugarLevel.toString()
        tvStatusAssessment.text = treatment.assessment

        if(position==0){
            statusLayout.visibility = VISIBLE
            treatmentLayout.visibility = GONE
        }
        else{
            statusLayout.visibility = GONE
            treatmentLayout.visibility = VISIBLE
        }

    }

}