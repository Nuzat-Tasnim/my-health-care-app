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
import com.example.simpleapp.model.DoctorNameClass
import com.example.simpleapp.view.DoctorProfileActivity

class DoctorNameRecyclerViewAdapter(val doctors: List<DoctorNameClass>, val context: Context): RecyclerView.Adapter<DoctorNameViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorNameViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val list_item = layoutInflater.inflate(R.layout.doctor_list_layout, parent, false)
        return DoctorNameViewHolder(list_item, context)
    }

    override fun getItemCount(): Int {
        return doctors.size
    }

    override fun onBindViewHolder(holder: DoctorNameViewHolder, position: Int) {
        holder.bind(doctors[position], context)
    }
}

class DoctorNameViewHolder(val view: View, val context: Context): RecyclerView.ViewHolder(view){
    fun bind(doctor:DoctorNameClass, context:Context){
        val tvListDoctorName = view.findViewById<TextView>(R.id.tvListDoctorName)
        tvListDoctorName.text = doctor.userInfo.name
        tvListDoctorName.setOnClickListener {
            Log.d("MYTAG", "clicked on doctor name")
            val intent = Intent(context, DoctorProfileActivity::class.java)
            intent.putExtra("doctorid", doctor.id)
            context.startActivity(intent)
        }
    }

}