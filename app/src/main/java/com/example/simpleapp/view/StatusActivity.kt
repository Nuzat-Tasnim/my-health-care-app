package com.example.simpleapp.view

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleapp.R
import com.example.simpleapp.ViewModel.TreatmentViewModel
import com.example.simpleapp.adapter.PostRecyclerViewAdapter
import com.example.simpleapp.adapter.StatusTreatmentRecycleViewAdapter
import com.example.simpleapp.model.Patient
import com.example.simpleapp.model.PatientService
import com.example.simpleapp.model.Post
import com.example.simpleapp.model.PostService
import com.example.simpleapp.model.RetrofitInstance
import com.example.simpleapp.model.Treatment
import com.example.simpleapp.model.TreatmentService
import com.example.simpleapp.model.User
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class StatusActivity : AppCompatActivity() {
    private lateinit var sf: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var loggedInUser: User
    private lateinit var token: String

    private lateinit var retrofitPatient: PatientService
    private lateinit var retrofitTreatment: TreatmentService
    private lateinit var retrofit: PostService
    private lateinit var patient: Patient
    private lateinit var treatments: List<Treatment>
    private lateinit var rvStatus: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

//        val treatmentViewModel: TreatmentViewModel()

        val shared_preferences_key = applicationContext.resources.getString(R.string.Shared_Preferences_Key)
        sf = getSharedPreferences(shared_preferences_key, MODE_PRIVATE)
        token = sf.getString("jwtToken", null)!!
        val user = sf.getString("loggedInUser", null)
        editor  = sf.edit()
        loggedInUser = Gson().fromJson(user, User::class.java)

        var patientid: String
        var intentvalue = intent.getStringExtra("patientid")
        if(intentvalue==loggedInUser.patientid) patientid = loggedInUser.patientid
        else patientid = intentvalue!!

        rvStatus = findViewById<RecyclerView>(R.id.rvStatus)

        retrofitPatient = RetrofitInstance
            .getRetrofitInstance(this)
            .create(PatientService::class.java)

        retrofitTreatment = RetrofitInstance
            .getRetrofitInstance(this)
            .create(TreatmentService::class.java)

        retrofit = RetrofitInstance
            .getRetrofitInstance(this)
            .create(PostService::class.java)


        CoroutineScope(Dispatchers.Main).launch {
            val patientDeferred = async { retrievePatient2(patientid) }
            val treatmentsDeferred = async { retrieveTreatments2(patientid) }

            val patient = patientDeferred.await()
            val treatments = treatmentsDeferred.await()

            val trearmentList = TreatmentViewModel.addBlankObjectatTop(treatments)

            Log.d("MYTAG", "treatments are retrieved $treatments")

            rvStatus.layoutManager = LinearLayoutManager(this@StatusActivity)
            val adapter = StatusTreatmentRecycleViewAdapter(patient, trearmentList)
            rvStatus.adapter = adapter
            Log.d("MYTAG", "${rvStatus.layoutManager} and ${rvStatus.adapter}" )
        }

    }
    private suspend fun retrievePatient2(patientid: String): Patient {
        return suspendCoroutine { continuation ->
            val retroData = retrofitPatient.getPatient(token, patientid)
            retroData.enqueue(object : Callback<Patient> {
                override fun onResponse(call: Call<Patient>, response: Response<Patient>) {
                    Log.d("MYTAG","patient response body is:- ${response.body().toString()}")
                    val patient = response.body()
                    continuation.resume(patient!!)
                }

                override fun onFailure(call: Call<Patient>, t: Throwable) {
                    Log.d("MYTAG", "getPosts() throwable is: $t")
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    private suspend fun retrieveTreatments2(patientid: String): List<Treatment> {
        return suspendCoroutine { continuation ->
            val retroData = retrofitTreatment.getTreatmentsOfPatient(token, patientid)
            retroData.enqueue(object : Callback<List<Treatment>> {
                override fun onResponse(call: Call<List<Treatment>>, response: Response<List<Treatment>>) {
                    Log.d("MYTAG", "treatment response body is:- ${response.body().toString()}")
                    val treatments = response.body()
                    continuation.resume(treatments!!)
                }

                override fun onFailure(call: Call<List<Treatment>>, t: Throwable) {
                    Log.d("MYTAG", "getPosts() throwable is: $t")
                    continuation.resumeWithException(t)
                }
            })
        }
    }



    private suspend fun retrieveTreatments(){
        var retroData = retrofitTreatment.getTreatmentsOfPatient(token, patient.id)
        retroData.enqueue(object: Callback<List<Treatment>> {
            override fun onResponse(call: Call<List<Treatment>>, response: Response<List<Treatment>>) {
                Log.d("MYTAG", "treatment response body is:- ${response.body().toString()}")
                treatments = response.body()!!

                rvStatus.layoutManager = LinearLayoutManager(this@StatusActivity)
                rvStatus.adapter = StatusTreatmentRecycleViewAdapter(patient, treatments)
            }

            override fun onFailure(call: Call<List<Treatment>>, t: Throwable) {
                Log.d("MYTAG", "getPosts() throwable is: $t")
            }
        })

    }

    private suspend fun retrievePatient(): Patient{
        var retroData = retrofitPatient.getPatient(token, loggedInUser.patientid)
        retroData.enqueue(object: Callback<Patient> {
            override fun onResponse(call: Call<Patient>, response: Response<Patient>) {
                Log.d("MYTAG","patient response body is:- ${response.body().toString()}")
                patient = response.body()!!
            }

            override fun onFailure(call: Call<Patient>, t: Throwable) {
                Log.d("MYTAG", "getPosts() throwable is: $t")
            }
        })
        Log.d("MYTAG", "After retrodata.enqueue() and $patient")
        return patient

    }

}