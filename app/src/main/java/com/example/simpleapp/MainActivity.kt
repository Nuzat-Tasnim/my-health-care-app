package com.example.simpleapp

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.simpleapp.model.Admin
import com.example.simpleapp.model.AdminService
import com.example.simpleapp.model.AuthService
import com.example.simpleapp.model.Doctor
import com.example.simpleapp.model.DoctorDepartmentRequest
import com.example.simpleapp.model.DoctorNameClass
import com.example.simpleapp.model.DoctorSearchRequest
import com.example.simpleapp.model.DoctorService
import com.example.simpleapp.model.LoginRequestBody
import com.example.simpleapp.model.Nurse
import com.example.simpleapp.model.NurseService
import com.example.simpleapp.model.Patient
import com.example.simpleapp.model.PatientService
import com.example.simpleapp.model.RegisterRequestBody
import com.example.simpleapp.model.RetrofitInstance
import com.example.simpleapp.model.User
import com.example.simpleapp.view.DashboardActivity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var sf: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var retrofit: AuthService
    private lateinit var retrofitAdmin: AdminService
    private lateinit var retrofitDoctor: DoctorService
    private lateinit var retrofitNurse: NurseService
    private lateinit var retrofitPatient: PatientService
    private lateinit var loggedInUser: User
    private lateinit var token: String
    private lateinit var tokenKey: String

    var selectedDay = 0
    var selectedYear = 0
    var selectedMonth = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val hardToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MmEzYTcxOGFkNThjZWFmYTJiYzAyNjkiLCJkb2N0b3JpZCI6IjYyYTNhZDRlMGNjZDkxZjcwZjI1MjJlOSIsIm51cnNlaWQiOm51bGwsInBhdGllbnRpZCI6bnVsbCwiYWRtaW5pZCI6bnVsbCwicm9sZXMiOlsiRG9jdG9yIl0sImlhdCI6MTcxMDIyODY4Nn0.R53aBooJZNIPcvdCVaqZVP9lLrFBkw7-SP9jQ6KaOc4"

        val shared_preferences_key = resources.getString(R.string.Shared_Preferences_Key)
        sf = getSharedPreferences(shared_preferences_key, MODE_PRIVATE)
        editor = sf.edit()
        tokenKey = resources.getString(R.string.Token_Key)

        val user = sf.getString("loggedInUser", null)

        if (user != null) {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        val tvRegister = findViewById<TextView>(R.id.tvRegister)
        val tvLogin = findViewById<TextView>(R.id.tvLogin)

        var registerLayout = findViewById<LinearLayout>(R.id.registerLayout)
        var loginLayout = findViewById<LinearLayout>(R.id.loginLayout)

        val etName = findViewById<EditText>(R.id.etName)
        val etBirthdate = findViewById<EditText>(R.id.etBirthdate)
        val spGender = findViewById<Spinner>(R.id.spGender)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etEmail2 = findViewById<EditText>(R.id.etEmail2)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etPassword2 = findViewById<EditText>(R.id.etPassword2)
        val etPassword3 = findViewById<EditText>(R.id.etPassword3)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)


        spGender.adapter = ArrayAdapter<String>(
            this,
            R.layout.simple_view,
            R.id.tvSelected,
            resources.getStringArray(R.array.genderList))


        retrofit = RetrofitInstance
            .getRetrofitInstance(this)
            .create(AuthService::class.java)

        // Initially
        tvLogin.setTextColor(ContextCompat.getColor(this, R.color.black))
        tvLogin.setBackgroundColor(ContextCompat.getColor(this, R.color.lightGray))
        tvRegister.setTextColor(ContextCompat.getColor(this, R.color.gray))
        tvRegister.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
//        registerLayout.layoutParams.height = 0
        registerLayout.visibility = GONE


        etBirthdate.setOnClickListener {
            // the instance of our calendar.
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // setting date to our edit text.
                    selectedDay = dayOfMonth
                    selectedMonth = monthOfYear
                    selectedYear = year
                    val dateString = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    etBirthdate.setText(dateString)
                },
                // passing year, month and day for the selected date in our date picker.
                year,month,day)
            // calling show to display our date picker dialog.
            datePickerDialog.show()
        }

        tvRegister.setOnClickListener {
            registerLayout.visibility = VISIBLE
            loginLayout.visibility = GONE

            registerLayout.requestLayout() // Request layout update
            tvRegister.setTextColor(ContextCompat.getColor(this, R.color.black))
            tvRegister.setBackgroundColor(ContextCompat.getColor(this, R.color.lightGray))
            tvLogin.setTextColor(ContextCompat.getColor(this, R.color.gray))
            tvLogin.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            loginLayout.requestLayout()
            registerLayout.requestLayout()
        }

        tvLogin.setOnClickListener {
            registerLayout.visibility = GONE
            loginLayout.visibility = VISIBLE

            tvLogin.setTextColor(ContextCompat.getColor(this, R.color.black))
            tvLogin.setBackgroundColor(ContextCompat.getColor(this, R.color.lightGray))
            tvRegister.setTextColor(ContextCompat.getColor(this, R.color.gray))
            tvRegister.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            loginLayout.requestLayout()
            registerLayout.requestLayout()
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val pass = etPassword.text.toString()
            if(email!="" && pass!=""){
                CoroutineScope(Dispatchers.IO).launch {
                    login(email, pass)
//                    login("doctor2@gmail.com", "password")
                }
            }
            else{
                Toast.makeText(this, "Please fill the fields to login", Toast.LENGTH_LONG).show()
            }
        }

        btnRegister.setOnClickListener {
            val name = etName.text.toString()
            val gender = spGender.selectedItem.toString()
            val birthdate = etBirthdate.text.toString()
            val email = etEmail2.text.toString()
            val pass1 = etPassword2.text.toString()
            val pass2 = etPassword3.text.toString()
            if(email!="" && pass1!=""){
                if (pass1!=pass2){
                    Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_LONG).show()
                }
                else{
                    CoroutineScope(Dispatchers.IO).launch {
                        register(name, gender, email, pass1)
                    }
                }
            }
            else{
                Toast.makeText(this, "Please fill the fields to sign up", Toast.LENGTH_LONG).show()
            }
        }

    }

    fun goToDashboardActivity(){
            val intent = Intent(this, DashboardActivity::class.java)
            Log.d("MYTAG", "intent done, profile activity starting")
            startActivity(intent)
    }

    private fun login(email: String, password: String){
        var retroData = retrofit.login(LoginRequestBody(email, password))
        Log.d("MYTAG", "logging in --> $retroData")

        retroData.enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Log.d("MYTAG", "response body is:- ${response.body().toString()}")
                token = response.headers().get(tokenKey).toString()
                loggedInUser = response.body()!!

                storeSessionData()
                goToDashboardActivity()
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("MYTAG", "login() throwable is: $t")
            }
        })
    }

    private fun register(name: String, gender: String, email: String, password: String){
        var retroData = retrofit.register(RegisterRequestBody(name, selectedDay, selectedMonth, selectedYear,  gender, email, password) )
        Log.d("MYTAG", "registering")

        retroData.enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Log.d("MYTAG", "response body is:- ${response.body().toString()}")
                token = response.headers().get(tokenKey).toString()
                loggedInUser = response.body()!!

                storeSessionData()
                goToDashboardActivity()
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("MYTAG", "login() throwable is: $t")
            }
        })
    }

    fun storeSessionData(){
        editor.apply {
            putString("loggedInUser", Gson().toJson(loggedInUser))
            putString("jwtToken", Gson().toJson(token))
            commit()
        }
    }

}