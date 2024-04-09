package com.example.simpleapp.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleapp.R
import com.example.simpleapp.model.Appointment
import com.example.simpleapp.model.AppointmentWithSchedule
import com.example.simpleapp.view.DoctorProfileActivity
import com.example.simpleapp.view.ProfileActivity
import com.example.simpleapp.view.StatusActivity

class AppointmentsRecyclerViewAdapter(val appointments: List<AppointmentWithSchedule>, val doctorview: Boolean, val context: Context): RecyclerView.Adapter<AppointmentsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val list_item = layoutInflater.inflate(R.layout.appointment_list_layout, parent, false)
        return AppointmentsViewHolder(list_item, doctorview, context)
    }

    override fun getItemCount(): Int {
        return appointments.size
    }

    override fun onBindViewHolder(holder: AppointmentsViewHolder, position: Int) {
        return holder.bind(appointments[position])
    }
}

class AppointmentsViewHolder(val view: View, val doctorview: Boolean, val context: Context): RecyclerView.ViewHolder(view){

    fun bind(appointment: AppointmentWithSchedule){
        val tvAppointmentName = view.findViewById<TextView>(R.id.tvAppointmentName)
        val tvAppointmentDate = view.findViewById<TextView>(R.id.tvAppointmentDate)
        val tvAppointmentTime = view.findViewById<TextView>(R.id.tvAppointmentTime)

        if(doctorview) tvAppointmentName.text = appointment.doctorname
        else tvAppointmentName.text = appointment.patientname

        tvAppointmentDate.text = appointment.date
        tvAppointmentTime.text = "${appointment.scheduleTime.from} - ${appointment.scheduleTime.to}"

        tvAppointmentName.setOnClickListener {
            if(doctorview){
                val intent = Intent(context, DoctorProfileActivity::class.java)
                intent.putExtra("doctorid", appointment.doctor)
                context.startActivity(intent)
            }
            else{
                val intent = Intent(context, StatusActivity::class.java)
                intent.putExtra("patientid", appointment.patient)
                context.startActivity(intent)
            }
        }

    }
}
