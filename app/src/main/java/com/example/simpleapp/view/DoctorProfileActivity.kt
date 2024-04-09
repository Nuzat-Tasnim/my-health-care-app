package com.example.simpleapp.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
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
import com.example.simpleapp.R
import com.example.simpleapp.model.Appointment
import com.example.simpleapp.model.AppointmentService
import com.example.simpleapp.model.AppointmentSetRequest
import com.example.simpleapp.model.Doctor
import com.example.simpleapp.model.DoctorEditRequest
import com.example.simpleapp.model.DoctorNameClass
import com.example.simpleapp.model.DoctorService
import com.example.simpleapp.model.RetrofitInstance
import com.example.simpleapp.model.Schedule
import com.example.simpleapp.model.ScheduleService
import com.example.simpleapp.model.User
import com.example.simpleapp.model.UserInfo
import com.example.simpleapp.model.UserService
import com.google.gson.Gson
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DoctorProfileActivity : AppCompatActivity() {
    private lateinit var context: Context
    private lateinit var sf: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    lateinit var loggedInUser: User
    lateinit var thisDoctor: DoctorNameClass
//    lateinit var thisDoctorUser: User
    lateinit var thisDoctorSchedule: Schedule
    lateinit var token: String
    lateinit var fromTimeButton: Button
    lateinit var toTimeButton: Button
    lateinit var tvScheduleFrom: TextView
    lateinit var tvScheduleTo: TextView
    lateinit var editButton: ImageButton

    lateinit var retrofitSchedule: ScheduleService
    lateinit var retrofitDoctor: DoctorService
    lateinit var retrofitAppointment: AppointmentService

    lateinit var tvDoctorName: TextView
    lateinit var tvDoctorDepartment: TextView
    lateinit var tvWeekdays: TextView
    lateinit var tvScheduleTime: TextView

    lateinit var checkboxContainer: LinearLayout
    lateinit var checkboxContainer1: LinearLayout
    lateinit var scheduleLayout: LinearLayout
    lateinit var appointmentLayout: LinearLayout

    val selectedDays = mutableListOf<String>()
    val selectedDepts = mutableListOf<String>()

    var selectedDay = 0
    var selectedYear = 0
    var selectedMonth = 0
    var selectedWeekday = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_profile)

        context = this

        retrofitSchedule = RetrofitInstance
            .getRetrofitInstance(context)
            .create(ScheduleService::class.java)

        retrofitDoctor = RetrofitInstance
            .getRetrofitInstance(context)
            .create(DoctorService::class.java)

        retrofitAppointment = RetrofitInstance
            .getRetrofitInstance(context)
            .create(AppointmentService::class.java)

        thisDoctor = DoctorNameClass("", "", "", 0, "", UserInfo("", ""), 0)
        thisDoctorSchedule = Schedule("", "", "", listOf("") , 0)

        tvDoctorName = findViewById<TextView>(R.id.tvDoctorName)
        tvDoctorDepartment = findViewById<TextView>(R.id.tvDoctorDepartment)
        tvWeekdays = findViewById<TextView>(R.id.tvWeekdays)
        tvScheduleTime = findViewById<TextView>(R.id.tvScheduleTime)

        val shared_preferences_key = context.resources.getString(R.string.Shared_Preferences_Key)
        sf = context.getSharedPreferences(shared_preferences_key, Context.MODE_PRIVATE)
        val user = sf.getString("loggedInUser", null)
        loggedInUser = Gson().fromJson(user, User::class.java)

        token = sf.getString("jwtToken", null)!!


        var doctorid = intent.getStringExtra("doctorid")
        if(doctorid != null){
            CoroutineScope(Dispatchers.IO).launch {
                retrieveDoctor(doctorid)
            }
        }
        else{
            CoroutineScope(Dispatchers.IO).launch {
                retrieveDoctor(loggedInUser.doctorid)
            }
        }

        val doctorProfileLayout = findViewById<LinearLayout>(R.id.doctorProfileLayout)
        scheduleLayout = findViewById<LinearLayout>(R.id.scheduleLayout)

        val editDoctorProfileLayout = findViewById<LinearLayout>(R.id.editDoctorProfileLayout)
        editButton = findViewById<ImageButton>(R.id.editButton)
        checkboxContainer1 = findViewById<LinearLayout>(R.id.checkboxContainer1)
        fromTimeButton = findViewById<Button>(R.id.fromTimeButton)
        toTimeButton = findViewById<Button>(R.id.toTimeButton)
        tvScheduleFrom = findViewById<TextView>(R.id.tvScheduleFrom)
        tvScheduleTo = findViewById<TextView>(R.id.tvScheduleTo)
        val maxAppointment = findViewById<EditText>(R.id.etMaxAppointment)

        checkboxContainer = findViewById<LinearLayout>(R.id.checkboxContainer)

        val saveDoctorProfile = findViewById<Button>(R.id.saveDoctorProfile)
        val cancelEditDoctorProfile = findViewById<Button>(R.id.cancelEditDoctorProfile)

        appointmentLayout = findViewById<LinearLayout>(R.id.appointmentLayout)
        val dateEdt = findViewById<ImageButton>(R.id.ibAppointmentCalender)
        val tvAppointmentDate = findViewById<TextView>(R.id.tvAppointmentDate)


        editDoctorProfileLayout.visibility = GONE
        appointmentLayout.visibility = GONE

        editButton.setOnClickListener{
            doctorProfileLayout.visibility = GONE
            editDoctorProfileLayout.visibility = VISIBLE
        }

        fromTimeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(context,
                { _, hourOfDay, minute ->
                    val time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                    tvScheduleFrom.text = time
                },
                hour,minute,false)
            timePickerDialog.show()
        }
        toTimeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(context,
                { _, hourOfDay, minute ->
                    val time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                    tvScheduleTo.text = time
                },
                hour,minute,false)
            timePickerDialog.show()
        }


        saveDoctorProfile.setOnClickListener {
            val doctorEditRequest = DoctorEditRequest(
                thisDoctor.id,
                selectedDepts.joinToString(separator = ", "),
                tvScheduleFrom.text.toString(),
                tvScheduleTo.text.toString(),
                selectedDays,
                maxAppointment.text.toString().toInt()
            )
            CoroutineScope(Dispatchers.IO).launch {
                saveEditedDoctor(doctorEditRequest)
            }
            editDoctorProfileLayout.visibility = GONE
            doctorProfileLayout.visibility = VISIBLE
        }

        cancelEditDoctorProfile.setOnClickListener {
            editDoctorProfileLayout.visibility = GONE
            doctorProfileLayout.visibility = VISIBLE
        }

        var date = ""
        dateEdt.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val currentDate = c.timeInMillis
            c.add(Calendar.DAY_OF_MONTH, 30) // Add 30 days to the current date
            val maxDate = c.timeInMillis


            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(year, monthOfYear, dayOfMonth)
                    val selectedDate = selectedCalendar.timeInMillis
                    if (selectedDate in currentDate..maxDate) {
                        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                        date = dateFormat.format(selectedCalendar.time)

                        val weekdayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
                        selectedWeekday = weekdayFormat.format(selectedCalendar.time)

                        selectedDay = dayOfMonth
                        selectedMonth = monthOfYear
                        selectedYear = year

                        val dateString = SimpleDateFormat("EEE, dd-MM-yyyy", Locale.getDefault()).format(selectedCalendar.time)
                        tvAppointmentDate.text = dateString
                    }
                    else {
                        // Notify the user that the selected date is out of range
                        Toast.makeText(this, "Please select a date within the next 30 days.", Toast.LENGTH_SHORT).show()
                    }
                },
                year,
                month,
                day
            )
            datePickerDialog.datePicker.minDate = currentDate
            datePickerDialog.datePicker.maxDate = maxDate
            datePickerDialog.show()
        }

        val btnBook = findViewById<Button>(R.id.btnBookAppointment)
        btnBook.setOnClickListener {
            if(weekdayInSchedule()){
                CoroutineScope(Dispatchers.IO).launch{
                    setAppointment(AppointmentSetRequest(thisDoctor.id, selectedYear, selectedMonth+1, selectedDay))
                }
            }
            else{
                Toast.makeText(this, "Please select a day within schedule!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun weekdayInSchedule(): Boolean{
        for (day in thisDoctorSchedule.days) {
            if (day.contains(selectedWeekday, ignoreCase = true)) {
                selectedWeekday = day
                return true
            }
        }
        return false

    }

    private fun updateUIforDoctor(doctor: DoctorNameClass){
        tvDoctorName.text = doctor.userInfo.name
        tvDoctorDepartment.text = doctor.areaOfExpertise
    }
    private fun updateUIforSchedule(schedule: Schedule){
        tvScheduleFrom.text = schedule.from
        tvScheduleTo.text = schedule.to
        tvScheduleTime.text = "${schedule.from} - ${schedule.to}"
        tvWeekdays.text = schedule.days.joinToString(separator = ", ")
    }

    private fun retrieveDoctor(doctorid: String){
        var retroData = retrofitDoctor.getDoctor(token, doctorid)
        retroData.enqueue(object: Callback<DoctorNameClass> {
            override fun onResponse(call: Call<DoctorNameClass>, response: Response<DoctorNameClass>) {
                thisDoctor = response.body()!!
                Log.d("MYTAG", "DOCTOR RETRIEVED $thisDoctor")
                runOnUiThread{
                    updateUIforDoctor(thisDoctor)
                    checkboxForDepartments()
                }
                retrieveSchedule()
            }
            override fun onFailure(call: Call<DoctorNameClass>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun retrieveSchedule(){
        if(thisDoctor.schedule==null){
            scheduleLayout.visibility = GONE
            return
        }
        var retroData = retrofitSchedule.getSchedule(thisDoctor.schedule)
        retroData.enqueue(object : Callback <Schedule>{
            override fun onResponse(call: Call<Schedule>, response: Response<Schedule>) {
                thisDoctorSchedule = response.body()!!
                Log.d("MYTAG", "$thisDoctorSchedule")
                runOnUiThread{
                    updateUIforSchedule(thisDoctorSchedule)
                    checkboxForWeekdays()
                }
            }
            override fun onFailure(call: Call<Schedule>, t: Throwable) {
                Log.d("MYTAG", "Schedule throwable --> $t")
            }

        })
    }

    private fun setAppointment(appointmentSetRequest: AppointmentSetRequest){
        var retrodata = retrofitAppointment.setAppointment(token, appointmentSetRequest)
        retrodata.enqueue(object: Callback<Appointment>{
            override fun onResponse(call: Call<Appointment>, response: Response<Appointment>) {
                Log.d("MYTAG", response.body().toString())
            }

            override fun onFailure(call: Call<Appointment>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun saveEditedDoctor(doctorEditRequest: DoctorEditRequest){
        var retrodata = retrofitDoctor.editDoctor(token, doctorEditRequest)
        retrodata.enqueue(object: Callback<DoctorNameClass>{
            override fun onResponse(
                call: Call<DoctorNameClass>,
                response: Response<DoctorNameClass>
            ) {
                thisDoctor = response.body()!!
                Log.d("MYTAG", thisDoctor.toString())
                updateUIforDoctor(thisDoctor)
                checkboxForDepartments()
                retrieveSchedule()
            }

            override fun onFailure(call: Call<DoctorNameClass>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun checkboxForDepartments(){
        selectedDepts.clear()
        checkboxContainer1.removeAllViews()

        val context = this@DoctorProfileActivity
        val departments = context.resources.getStringArray(R.array.doctortypes)
        for (item in departments) {
            val checkBox = CheckBox(context)
            checkBox.text = item
            if(item in thisDoctor.areaOfExpertise) {
                checkBox.isChecked = true
                selectedDepts.add(item)
            }
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

    private fun checkboxForWeekdays(){
        selectedDays.clear()
        checkboxContainer.removeAllViews()

        val context = this@DoctorProfileActivity
        val weekdays = context.resources.getStringArray(R.array.weekdays)
        for (item in weekdays) {
            val checkBox = CheckBox(context)
            checkBox.text = item
            if(item in thisDoctorSchedule.days) {
                checkBox.isChecked = true
                selectedDays.add(item)
            }
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedDays.add(item)
                } else {
                    selectedDays.remove(item)
                }
            }
            checkboxContainer.addView(checkBox)
        }
    }

}