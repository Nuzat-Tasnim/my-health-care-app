package com.example.simpleapp.view

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.simpleapp.R
import com.example.simpleapp.model.ApplyRequestService
import com.example.simpleapp.model.EditUserRequestBody
import com.example.simpleapp.model.NewRequestBody
import com.example.simpleapp.model.RetrofitInstance
import com.example.simpleapp.model.User
import com.example.simpleapp.model.UserService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class ProfileActivity : AppCompatActivity() {
    private lateinit var sf: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var loggedInUser: User
    private lateinit var thisUser: User
    private lateinit var token: String

    lateinit var retrofitUser: UserService
    lateinit var retrofitApplyRequest: ApplyRequestService

    lateinit var checkboxContainer1: LinearLayout

    lateinit var ibEditProfile: ImageButton
    lateinit var tvApplyDoctor : TextView
    lateinit var tvApplyNurse : TextView
    lateinit var tvApplyAdmin : TextView

    val selectedDepts = mutableListOf<String>()

    var selectedDay = 0
    var selectedYear = 0
    var selectedMonth = 0

    private val userDataLiveData: MutableLiveData<User> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val profileLayout = findViewById<LinearLayout>(R.id.profileLayout)
        val editProfileLayout = findViewById<LinearLayout>(R.id.editProfileLayout)

        val tvName = findViewById<TextView>(R.id.tvUserName)
        val tvGender = findViewById<TextView>(R.id.tvGender)
        val tvAddress = findViewById<TextView>(R.id.tvAddress)
        val tvBirthdate = findViewById<TextView>(R.id.tvBirthdate)
        val tvContact = findViewById<TextView>(R.id.tvContact)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        ibEditProfile = findViewById<ImageButton>(R.id.editButton)
        val btnCancelEditProfile = findViewById<Button>(R.id.cancelEditProfile)

        val etName = findViewById<EditText>(R.id.etName)
        val etGender = findViewById<EditText>(R.id.etGender)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val dateEdt = findViewById<ImageButton>(R.id.ibBirthdate)
        val tvSelectedBirthdate = findViewById<TextView>(R.id.tvSelectedBirthdate)
        val etContact = findViewById<EditText>(R.id.etContact)

        val btnSave = findViewById<Button>(R.id.saveProfile)

        tvApplyDoctor = findViewById<TextView>(R.id.tvDoctorApply)
        tvApplyNurse = findViewById<TextView>(R.id.tvNurseApply)
        tvApplyAdmin = findViewById<TextView>(R.id.tvAdminApply)

        val layoutApplyDoctor = findViewById<LinearLayout>(R.id.layoutDoctorApply)
        val layoutApplyNurse = findViewById<LinearLayout>(R.id.layoutNurseApply)
        val layoutApplyAdmin = findViewById<LinearLayout>(R.id.layoutAdminApply)

        val btnApplyDoctor = findViewById<Button>(R.id.btnDoctorApply)
        val btnApplyNurse = findViewById<Button>(R.id.btnNurseApply)
        val btnApplyAdmin = findViewById<Button>(R.id.btnAdminApply)

        checkboxContainer1 = findViewById<LinearLayout>(R.id.checkboxContainerDepts)

        retrofitUser = RetrofitInstance
            .getRetrofitInstance(this)
            .create(UserService::class.java)
        retrofitApplyRequest = RetrofitInstance
            .getRetrofitInstance(this)
            .create(ApplyRequestService::class.java)

        val shared_preferences_key = applicationContext.resources.getString(R.string.Shared_Preferences_Key)
        sf = getSharedPreferences(shared_preferences_key, MODE_PRIVATE)
        val user = sf.getString("loggedInUser", null)
        token = sf.getString("jwtToken", null)!!

        editor  = sf.edit()
        loggedInUser = Gson().fromJson(user, User::class.java)

        val userid = intent.getStringExtra("userid")
        if(userid==loggedInUser.id) {
            thisUser = loggedInUser
            userDataLiveData.value = thisUser
            updateLayouts(true)
        }
        else{
            Log.d("MYTAG", "diving in $userid")
            updateLayouts(false)
            CoroutineScope(Dispatchers.IO).launch{
                retrieveUser(userid!!)
            }
        }
        editProfileLayout.visibility = GONE

        userDataLiveData.observe(this, Observer { user ->
            tvName.text = user.name
            tvGender.text = user.gender
            tvAddress.text = user.address
            tvBirthdate.text = user.birthdate
            tvContact.text = user.contact
            tvEmail.text = user.email

            etName.setText(user.name)
            etGender.setText(user.gender)
            etAddress.setText(user.address)
            etContact.setText(user.contact)
            tvSelectedBirthdate.text = user.birthdate
        })

        if(loggedInUser.id!=userid) { ibEditProfile.visibility = GONE }
        ibEditProfile.setOnClickListener{
            editProfileLayout.visibility = VISIBLE
            profileLayout.visibility = GONE

            dateEdt.setOnClickListener {

                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    // on below line we are passing context.
                    this,
                    { view, year, monthOfYear, dayOfMonth ->
                        selectedDay = dayOfMonth
                        selectedMonth = monthOfYear+1
                        selectedYear = year
                        val dateString = ((monthOfYear+1).toString() + "/" + dayOfMonth.toString() + "/" + year.toString() )
                        tvSelectedBirthdate.text = dateString
                    }, year,month,day)
                datePickerDialog.show()

            }
        }
        btnSave.setOnClickListener{
            val editUser = EditUserRequestBody(etName.text.toString(), etGender.text.toString(), selectedYear, selectedMonth, selectedDay, etAddress.text.toString(), etContact.text.toString())
            CoroutineScope(Dispatchers.IO).launch{
                saveEditedUserProfile(editUser)
            }
            editProfileLayout.visibility = GONE
            profileLayout.visibility = VISIBLE

        }
        btnCancelEditProfile.setOnClickListener {
            editProfileLayout.visibility = VISIBLE
            profileLayout.visibility = GONE
        }


        if(loggedInUser.id==userid){
            updateLayouts(true)
        }

        tvApplyDoctor.setOnClickListener { layoutApplyDoctor.visibility = VISIBLE }
        tvApplyNurse.setOnClickListener { layoutApplyNurse.visibility = VISIBLE }
        tvApplyAdmin.setOnClickListener { layoutApplyAdmin.visibility = VISIBLE }

        checkboxForDepartments()

        btnApplyDoctor.setOnClickListener {
            if (selectedDepts.isEmpty()) {
                Toast.makeText(this, "Please select the department you specialize in to apply.", Toast.LENGTH_LONG).show()
            }
            else {
                CoroutineScope(Dispatchers.IO).launch {
                    apply(
                        NewRequestBody(
                            loggedInUser.id,
                            "Doctor",
                            selectedDepts.joinToString(separator = ", ")
                        )
                    )

                }
            }
        }
        btnApplyNurse.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                apply(NewRequestBody(loggedInUser.id,"Nurse",""))
            }
        }
        btnApplyAdmin.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                apply(NewRequestBody(loggedInUser.id,"Admin",""))
            }
        }

    }

    private fun saveEditedUserProfile(editUser: EditUserRequestBody){
        val token = sf.getString("jwtToken", null)!!
        var retroData = retrofitUser.editProfile(token, loggedInUser.id, editUser)
        retroData.enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Log.d("MYTAG", "saveEditedUserProfile -> Response ${response.body()}")
                loggedInUser = response.body()!!
                userDataLiveData.value = loggedInUser
                storeSessionData()
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }
    private fun retrieveUser(userid: String){
        val token = sf.getString("jwtToken", null)!!
        var retroData = retrofitUser.getUser(token, userid)
        retroData.enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                thisUser = response.body()!!
                userDataLiveData.value = thisUser
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun apply(newRequestBody: NewRequestBody){
        var retrodata = retrofitApplyRequest.newRequest(token, newRequestBody)
        retrodata.enqueue(object: Callback<Any>{
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                TODO("Not yet implemented")
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun storeSessionData(){
        editor.apply {
            putString("loggedInUser", Gson().toJson(loggedInUser))
            commit()
        }
    }

    private fun checkboxForDepartments(){
        selectedDepts.clear()
        checkboxContainer1.removeAllViews()

        val context = this@ProfileActivity
        val departments = context.resources.getStringArray(R.array.doctortypes)
        for (item in departments) {
            val checkBox = CheckBox(context)
            checkBox.text = item
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedDepts.add(item)
                } else {
                    selectedDepts.remove(item)
                }
            }
            checkboxContainer1.addView(checkBox)
        }
    }

    private fun updateLayouts(myProfile: Boolean){
        if(myProfile){
            ibEditProfile.visibility = VISIBLE
            if (loggedInUser.doctorid == null) tvApplyDoctor.visibility = VISIBLE
            if (loggedInUser.nurseid == null) tvApplyNurse.visibility = VISIBLE
            if (loggedInUser.adminid == null) tvApplyAdmin.visibility = VISIBLE
        }
        else{
            ibEditProfile.visibility = GONE
            tvApplyDoctor.visibility = GONE
            tvApplyNurse.visibility = GONE
            tvApplyAdmin.visibility = GONE
        }

    }
}