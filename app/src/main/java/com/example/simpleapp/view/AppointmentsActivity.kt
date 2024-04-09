package com.example.simpleapp.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleapp.R
import com.example.simpleapp.adapter.AppointmentsRecyclerViewAdapter
import com.example.simpleapp.model.Appointment
import com.example.simpleapp.model.AppointmentService
import com.example.simpleapp.model.AppointmentWithSchedule
import com.example.simpleapp.model.RetrofitInstance
import com.example.simpleapp.model.ScheduleService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentsActivity : AppCompatActivity() {
    lateinit var token: String
    lateinit var retrofitAppointment: AppointmentService
    lateinit var rvAppointmentsfeed: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments)

        val context = this
        val shared_preferences_key = context.resources.getString(R.string.Shared_Preferences_Key)
        val sf = context.getSharedPreferences(shared_preferences_key, Context.MODE_PRIVATE)
        token = sf.getString("jwtToken", null)!!

        retrofitAppointment = RetrofitInstance
            .getRetrofitInstance(context)
            .create(AppointmentService::class.java)

        var doctorid = intent.getStringExtra("doctorid")
        var patientid = intent.getStringExtra("patientid")

        if(doctorid!=null) {
            CoroutineScope(Dispatchers.IO).launch {
                retrieveDoctorAppointments(doctorid)
            }
        }

        if(patientid!=null){
            CoroutineScope(Dispatchers.IO).launch {
                retrievePatientAppointments(patientid)
            }
        }

        rvAppointmentsfeed = findViewById<RecyclerView>(R.id.rvAppointmentsfeed)

        rvAppointmentsfeed.layoutManager = LinearLayoutManager(this)

    }

    fun retrieveDoctorAppointments(doctorid: String){
        var retrodata = retrofitAppointment.getDoctorAppointments(token, doctorid)
        retrodata.enqueue(object: Callback<List<AppointmentWithSchedule>>{
            override fun onResponse(
                call: Call<List<AppointmentWithSchedule>>,
                response: Response<List<AppointmentWithSchedule>>
            ) {
                rvAppointmentsfeed.adapter = AppointmentsRecyclerViewAdapter(response.body()!!, true, this@AppointmentsActivity)
                Log.d("MYTAG", response.body().toString())
            }

            override fun onFailure(call: Call<List<AppointmentWithSchedule>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun retrievePatientAppointments(patientid: String){
        var retrodata = retrofitAppointment.getPatientAppointments(token, patientid)
        retrodata.enqueue(object: Callback<List<AppointmentWithSchedule>>{
            override fun onResponse(
                call: Call<List<AppointmentWithSchedule>>,
                response: Response<List<AppointmentWithSchedule>>
            ) {
                rvAppointmentsfeed.adapter = AppointmentsRecyclerViewAdapter(response.body()!!, false, this@AppointmentsActivity)
            }

            override fun onFailure(call: Call<List<AppointmentWithSchedule>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}