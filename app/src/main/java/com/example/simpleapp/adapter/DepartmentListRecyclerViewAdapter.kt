package com.example.simpleapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleapp.R
import com.example.simpleapp.model.Doctor
import com.example.simpleapp.model.DoctorDepartmentRequest
import com.example.simpleapp.model.DoctorNameClass
import com.example.simpleapp.model.DoctorService
import com.example.simpleapp.model.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DepartmentListRecyclerViewAdapter(val departments: List<String>, val context: Context): RecyclerView.Adapter<DepartmentListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartmentListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val list_item = layoutInflater.inflate(R.layout.department_list_layout, parent, false)
        return DepartmentListViewHolder(list_item, context)
    }

    override fun getItemCount(): Int {
        return departments.size
    }

    override fun onBindViewHolder(holder: DepartmentListViewHolder, position: Int) {
        holder.bind(departments[position], context)
    }
}

class DepartmentListViewHolder(val view: View, val context: Context): RecyclerView.ViewHolder(view){
    fun bind(department: String, context: Context){
        var tvDepartmentName = view.findViewById<TextView>(R.id.tvDepartmentName)
        tvDepartmentName.text = department

        var rvDoctors = view.findViewById<RecyclerView>(R.id.rvDoctors)
        rvDoctors.visibility = GONE

        tvDepartmentName.setOnClickListener {
            rvDoctors.visibility = VISIBLE

            var retrofitDoctor = RetrofitInstance.getRetrofitInstance(context).create(DoctorService::class.java)
            var retrodata = retrofitDoctor.searchDoctor2(DoctorDepartmentRequest(department))



            retrodata.enqueue(object: Callback<List<DoctorNameClass>>{
                override fun onResponse(call: Call<List<DoctorNameClass>>, response: Response<List<DoctorNameClass>>) {
//                Log.d("MYTAG", "response body is:- ${response.body().toString()}")
                    val doctors = response.body()!!
                    rvDoctors.layoutManager = LinearLayoutManager(context)
                    rvDoctors.adapter = DoctorNameRecyclerViewAdapter(doctors, context)
                }

                override fun onFailure(call: Call<List<DoctorNameClass>>, t: Throwable) {
                    Log.d("MYTAG", "getPosts() throwable is: $t")
                }
            })
        }

    }

}