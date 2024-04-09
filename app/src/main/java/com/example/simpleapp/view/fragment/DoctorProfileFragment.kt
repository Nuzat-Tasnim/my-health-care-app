package com.example.simpleapp.view.fragment

import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.example.simpleapp.R
import com.example.simpleapp.model.Doctor
import com.example.simpleapp.model.DoctorService
import com.example.simpleapp.model.RetrofitInstance
import com.example.simpleapp.model.ScheduleCreateRequest
import com.example.simpleapp.model.ScheduleService
import com.example.simpleapp.model.User
import com.example.simpleapp.model.UserService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Locale

class DoctorProfileFragment : Fragment() {
    private lateinit var sf: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    lateinit var loggedInUser: User
    lateinit var loggedInDoctor: Doctor
    lateinit var token: String
    lateinit var fromTimeButton: Button
    lateinit var toTimeButton: Button

    lateinit var retrofitSchedule: ScheduleService
    lateinit var retrofitDoctor: DoctorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = requireContext()
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_doctor_profile, container, false)

        retrofitSchedule = RetrofitInstance
            .getRetrofitInstance(context)
            .create(ScheduleService::class.java)

        retrofitDoctor = RetrofitInstance
            .getRetrofitInstance(context)
            .create(DoctorService::class.java)

        val shared_preferences_key = context.resources.getString(R.string.Shared_Preferences_Key)
        sf = context.getSharedPreferences(shared_preferences_key, Context.MODE_PRIVATE)
        val user = sf.getString("loggedInUser", null)
        editor  = sf.edit()
//        sf.getString("", null)
        loggedInUser = Gson().fromJson(user, User::class.java)
        token = sf.getString("jwtToken", null)!!


        val doctorProfileLayout = view.findViewById<LinearLayout>(R.id.doctorProfileLayout)
        val tvDoctorName = view.findViewById<TextView>(R.id.tvDoctorName)
        val tvDoctorDepartment = view.findViewById<TextView>(R.id.tvDoctorDepartment)
        val tvWeekdays = view.findViewById<TextView>(R.id.tvWeekdays)

        val editButton = view.findViewById<ImageButton>(R.id.editButton)
        val editDoctorProfileLayout = view.findViewById<LinearLayout>(R.id.editDoctorProfileLayout)

        val selectedDepartmentItems = view.findViewById<TextView>(R.id.selectedDepartmentItems)
        val checkboxContainer1 = view.findViewById<LinearLayout>(R.id.checkboxContainer1)

        val maxAppointment = view.findViewById<EditText>(R.id.etMaxAppointment)

        val selectedItemsTextView = view.findViewById<TextView>(R.id.selectedItemsTextView)
        val checkboxContainer = view.findViewById<LinearLayout>(R.id.checkboxContainer)

        val saveDoctorProfile = view.findViewById<Button>(R.id.saveDoctorProfile)
        val cancelEditDoctorProfile = view.findViewById<Button>(R.id.cancelEditDoctorProfile)

        fromTimeButton = view.findViewById<Button>(R.id.fromTimeButton)
        toTimeButton = view.findViewById<Button>(R.id.toTimeButton)
        fromTimeButton.setOnClickListener {
            showTimePickerDialog(true)
        }

        toTimeButton.setOnClickListener {
            showTimePickerDialog(false)
        }

        tvDoctorName.text = loggedInUser.name
        tvDoctorDepartment.text = loggedInDoctor.areaOfExpertise
//        tvWeekdays.text =

        val departments = context.resources.getStringArray(R.array.doctortypes)
        for (item in departments) {
            val checkBox = CheckBox(context)
            checkBox.text = item
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedDepartmentItems.append("$item\n")
                } else {
                    val text = selectedItemsTextView.text.toString()
                    selectedDepartmentItems.text = text.replace("$item\n", "")
                }
            }
            checkboxContainer1.addView(checkBox)
        }


        val weekdays = context.resources.getStringArray(R.array.weekdays)
        for (item in weekdays) {
            val checkBox = CheckBox(context)
            checkBox.text = item
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedItemsTextView.append("$item\n")
                } else {
                    val text = selectedItemsTextView.text.toString()
                    selectedItemsTextView.text = text.replace("$item\n", "")
                }
            }
            checkboxContainer.addView(checkBox)
        }

        editButton.setOnClickListener {
            doctorProfileLayout.visibility = GONE
            editDoctorProfileLayout.visibility = VISIBLE
        }

        saveDoctorProfile.setOnClickListener {
            Log.d("MYTAG", "${fromTimeButton.text}")
            Log.d("MYTAG", "${toTimeButton.text}")
            Log.d("MYTAG", "${selectedDepartmentItems.text}")
            Log.d("MYTAG", "${selectedItemsTextView.text}")
        }

        cancelEditDoctorProfile.setOnClickListener {
            editDoctorProfileLayout.visibility = GONE
            doctorProfileLayout.visibility = VISIBLE
        }

        return view
    }

    private fun showTimePickerDialog(isFrom: Boolean) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                // Do something with the selected time
                // For example, update TextView with the selected time
                val time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
                if (isFrom) {
                    fromTimeButton.text = time
                } else {
                    toTimeButton.text = time
                }
            },
            hour,
            minute,
            false
        )

        timePickerDialog.show()


    }

//    private fun retrieveDoctor(){
//        var retroData = retrofitDoctor.getDoctor(token, loggedInUser.doctorid)
//        retroData.enqueue(object: Callback<Doctor>{
//            override fun onResponse(call: Call<Doctor>, response: Response<Doctor>) {
//                loggedInDoctor = response.body()!!
//                Log.d("MYTAG", "$loggedInDoctor")
//            }
//
//            override fun onFailure(call: Call<Doctor>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//
//        })
//    }

    companion object {

        @JvmStatic
        fun newInstance() =
            DoctorProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}