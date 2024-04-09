package com.example.simpleapp.view.fragment

import android.app.DatePickerDialog
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.simpleapp.R
import com.example.simpleapp.model.EditUserRequestBody
import com.example.simpleapp.model.RetrofitInstance
import com.example.simpleapp.model.User
import com.example.simpleapp.model.UserService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class UserProfileFragment : Fragment() {
    private lateinit var sf: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    lateinit var loggedInUser: User
    lateinit var retrofit: UserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = requireContext()
        val view =  inflater.inflate(R.layout.fragment_user_profile, container, false)

        retrofit = RetrofitInstance
            .getRetrofitInstance(context)
            .create(UserService::class.java)

        val shared_preferences_key = context.resources.getString(R.string.Shared_Preferences_Key)
        sf = context.getSharedPreferences(shared_preferences_key, MODE_PRIVATE)
        val user = sf.getString("loggedInUser", null)
        editor  = sf.edit()
//        sf.getString("", null)
        loggedInUser = Gson().fromJson(user, User::class.java)

        val profileLayout = view.findViewById<LinearLayout>(R.id.doctorProfileLayout)
        val editProfileLayout = view.findViewById<LinearLayout>(R.id.editDoctorProfileLayout)

        val tvName = view.findViewById<TextView>(R.id.tvDoctorName)
        val tvGender = view.findViewById<TextView>(R.id.tvDoctorDepartment)
        val tvAddress = view.findViewById<TextView>(R.id.tvAddress)
        val tvBirthdate = view.findViewById<TextView>(R.id.tvBirthdate)
        val tvContact = view.findViewById<TextView>(R.id.tvContact)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
        val ibEditProfile = view.findViewById<ImageButton>(R.id.editButton)
        val btnCancelEditProfile = view.findViewById<Button>(R.id.cancelEditProfile)

        val etName = view.findViewById<EditText>(R.id.etName)
        val etGender = view.findViewById<EditText>(R.id.etGender)
        val etAddress = view.findViewById<EditText>(R.id.etAddress)
        val dateEdt = view.findViewById<EditText>(R.id.etBirthdate)
        val etContact = view.findViewById<EditText>(R.id.etContact)

        editProfileLayout.visibility = GONE

        tvName.text = loggedInUser.name
        tvGender.text = loggedInUser.gender
        tvAddress.text = loggedInUser.address
        tvBirthdate.text = loggedInUser.birthdate.substring(0, 10)
        tvContact.text = loggedInUser.contact
        tvEmail.text = loggedInUser.email

        etName.setText(loggedInUser.name)
        etGender.setText(loggedInUser.gender)
        etAddress.setText(loggedInUser.address)
        etContact.setText(loggedInUser.contact)

        ibEditProfile.setOnClickListener {
            editProfileLayout.visibility = View.VISIBLE
            profileLayout.visibility = View.GONE
        }

        val btnSave = view.findViewById<Button>(R.id.saveProfile)

        dateEdt.setOnClickListener {

            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, monthOfYear, dayOfMonth ->
                    val dateString = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    dateEdt.setText(dateString)
                },
                year,month,day)

            datePickerDialog.show()
        }

//        btnSave.setOnClickListener{
//            Log.d("MYTAG", "profile save button clicked")
//            val editUser = EditUserRequestBody(etName.text.toString(), etGender.text.toString(), dateEdt.text.toString(), etAddress.text.toString(), etContact.text.toString())
//            Log.d("MYTAG", "edit user created - $editUser")
//            saveEditedUserProfile(editUser)
//
//            Toast.makeText(context, "", Toast.LENGTH_LONG).show()
//
//            editProfileLayout.visibility = View.GONE
//            profileLayout.visibility = View.VISIBLE
//
//        }

        btnCancelEditProfile.setOnClickListener {
            editProfileLayout.visibility = View.VISIBLE
            profileLayout.visibility = View.GONE
        }

        return view
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            UserProfileFragment().apply {
                arguments = Bundle().apply {}
            }
    }

private fun saveEditedUserProfile(editUser: EditUserRequestBody){
    Log.d("MYTAG", "inside saveEditedUserProfile")
    val token = sf.getString("jwtToken", null)!!
    var retroData = retrofit.editProfile(token, loggedInUser.id, editUser)
    retroData.enqueue(object : Callback<User> {
        override fun onResponse(call: Call<User>, response: Response<User>) {
            Log.d("MYTAG", "saveEditedUserProfile -> Response $response")
            loggedInUser = response.body()!!
            storeSessionData()
        }

        override fun onFailure(call: Call<User>, t: Throwable) {
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
}