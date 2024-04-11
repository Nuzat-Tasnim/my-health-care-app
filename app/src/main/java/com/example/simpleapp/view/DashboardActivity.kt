package com.example.simpleapp.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleapp.MainActivity
import com.example.simpleapp.R
import com.example.simpleapp.adapter.PostRecyclerViewAdapter
import com.example.simpleapp.model.Post
import com.example.simpleapp.model.PostService
import com.example.simpleapp.model.RetrofitInstance
import com.example.simpleapp.model.User
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {
    private lateinit var sf: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    lateinit var loggedInUser: User

    private lateinit var retrofit: PostService
    private lateinit var posts: List<Post>
    private lateinit var rvPosts: RecyclerView
    private lateinit var bloodPostLayout: LinearLayout
    private lateinit var topBarLayout: LinearLayout
    private lateinit var dashboardLayout: LinearLayout
    private lateinit var dashboardDoctorLayout: LinearLayout
    private lateinit var dashboardNurseLayout: LinearLayout
    private lateinit var dashboardUserLayout: LinearLayout
    private lateinit var dashboardAdminLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val context = this
        val shared_preferences_key = context.resources.getString(R.string.Shared_Preferences_Key)
        sf = context.getSharedPreferences(shared_preferences_key, Context.MODE_PRIVATE)
        val user = sf.getString("loggedInUser", null)
        editor  = sf.edit()
//        sf.getString("", null)
        loggedInUser = Gson().fromJson(user, User::class.java)


        topBarLayout = findViewById<LinearLayout>(R.id.topBarLayout)
        dashboardLayout = findViewById(R.id.dashboardLayout)
        dashboardUserLayout = findViewById<LinearLayout>(R.id.dashboardUserLayout)
        dashboardDoctorLayout = findViewById<LinearLayout>(R.id.dashboardDoctorLayout)
        dashboardNurseLayout = findViewById<LinearLayout>(R.id.dashboardNurseLayout)
        dashboardAdminLayout = findViewById<LinearLayout>(R.id.dashboardAdminLayout)

        bloodPostLayout = findViewById<LinearLayout>(R.id.bloodPostLayout)

        if(loggedInUser.doctorid==null) dashboardDoctorLayout.visibility = GONE
        if(loggedInUser.nurseid==null) dashboardNurseLayout.visibility = GONE
        if(loggedInUser.adminid==null) dashboardAdminLayout.visibility = GONE


        rvPosts = findViewById<RecyclerView>(R.id.rvPostfeed)
        val cancelPost = findViewById<Button>(R.id.cancelPost)

        val tvBloodPost = findViewById<TextView>(R.id.tvBloodPost)

        val spBloodtype = findViewById<Spinner>(R.id.spBloodtype)

        val btnProfile = findViewById<ImageButton>(R.id.ibProfile)
        btnProfile.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("userid", loggedInUser.id)
            startActivity(intent)
        }

        val btnLogout = findViewById<ImageButton>(R.id.ibLogout)
        btnLogout.setOnClickListener {
            clearSessionData()
            val user = sf.getString("loggedInUser", null)
            Log.d("MYTAG", "cleared -> $user")
            startActivity(Intent(this, MainActivity::class.java))
        }

        val btnStatus = findViewById<ImageButton>(R.id.ibStatus)
        btnStatus.setOnClickListener{
            startActivity(Intent(this, StatusActivity::class.java))
        }

        val btnPatientAppointments = findViewById<ImageButton>(R.id.ibPatientAppoinements)
        btnPatientAppointments.setOnClickListener{
            val intent = Intent(this, AppointmentsActivity::class.java)
            intent.putExtra("patientid", loggedInUser.patientid)
            Log.d("MYTAG", "right?")
            startActivity(intent)
        }

        val btnBookAppointment = findViewById<ImageButton>(R.id.ibBookAppoinement)
        btnBookAppointment.setOnClickListener{
            startActivity(Intent(this, DoctorListActivity::class.java))
        }

        val btnPosts = findViewById<ImageButton>(R.id.ibPosts)
        btnPosts.setOnClickListener{
            Log.d("MYTAG", "posts button clicked just fine")
            startActivity(Intent(this, PostsActivity::class.java))
        }

        val doctorProfile = findViewById<ImageButton>(R.id.ibDoctorProfile)
        doctorProfile.setOnClickListener{
            val intent = Intent(this, DoctorProfileActivity::class.java)
            startActivity(intent)
        }

        val btnDoctorAppointments = findViewById<ImageButton>(R.id.ibDoctorAppoinements)
        btnDoctorAppointments.setOnClickListener{
            val intent = Intent(this, AppointmentsActivity::class.java)
            intent.putExtra("doctorid", loggedInUser.doctorid)
//            intent.putExtra("patientid", null)
            startActivity(intent)        }

        val ibAssignedToMe = findViewById<ImageButton>(R.id.ibAssignedToMe)
        ibAssignedToMe.setOnClickListener {  }

        val ibLogs = findViewById<ImageButton>(R.id.ibLogs)
        ibLogs.setOnClickListener {  }

        val ibUnapprovedDoctor = findViewById<ImageButton>(R.id.ibUnapprovedDoctor)
        ibUnapprovedDoctor.setOnClickListener {
                val intent = Intent(this, UnapprovedListActivity::class.java)
                intent.putExtra("listType", "Doctor")
                startActivity(intent)
            }
        val ibUnapprovedNurse = findViewById<ImageButton>(R.id.ibUnapprovedNurse)
        ibUnapprovedNurse.setOnClickListener {
                val intent = Intent(this, UnapprovedListActivity::class.java)
                intent.putExtra("listType", "Nurse")
                startActivity(intent)
            }
        val ibUnapprovedAdmin = findViewById<ImageButton>(R.id.ibUnapprovedAdmin)
        ibUnapprovedAdmin.setOnClickListener {
            val intent = Intent(this, UnapprovedListActivity::class.java)
            intent.putExtra("listType", "Admin")
            startActivity(intent)
        }



        spBloodtype.adapter = ArrayAdapter<String>(
            this,
            R.layout.simple_view,
            R.id.tvSelected,
            resources.getStringArray(R.array.bloodtype))

        retrofit = RetrofitInstance
            .getRetrofitInstance(this)
            .create(PostService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            retrievePosts()
        }


        tvBloodPost.setOnClickListener {
            bloodPostLayout.visibility = VISIBLE

            topBarLayout.visibility= GONE
//            dashboardUserLayout.visibility = GONE
//            dashboardDoctorLayout.visibility = GONE
//            dashboardNurseLayout.visibility = GONE
//            dashboardAdminLayout.visibility = GONE
            dashboardLayout.visibility = GONE
            rvPosts.visibility = GONE

            tvBloodPost.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            tvBloodPost.setTextColor(ContextCompat.getColor(this, R.color.white))

//            updateLayouts()
        }

        cancelPost.setOnClickListener {
            tvBloodPost.setBackgroundColor(ContextCompat.getColor(this, R.color.lightGray))
            tvBloodPost.setTextColor(ContextCompat.getColor(this, R.color.black))

            bloodPostLayout.visibility = GONE

            topBarLayout.visibility = VISIBLE
            dashboardUserLayout.visibility= VISIBLE
            dashboardDoctorLayout.visibility= VISIBLE
            dashboardNurseLayout.visibility= VISIBLE
            dashboardAdminLayout.visibility= VISIBLE
            rvPosts.visibility = VISIBLE

//            updateLayouts()
        }

    }

    private fun retrievePosts(){
        var retroData = retrofit.getPosts()
        retroData.enqueue(object: Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
//                Log.d("MYTAG", "response body is:- ${response.body().toString()}")
                posts = response.body()!!
                rvPosts.layoutManager = LinearLayoutManager(this@DashboardActivity)
                rvPosts.adapter = PostRecyclerViewAdapter(posts)
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Log.d("MYTAG", "getPosts() throwable is: $t")
            }
        })

    }

    private fun updateLayouts(){
        bloodPostLayout.requestLayout()
        topBarLayout.requestLayout()
        dashboardUserLayout.requestLayout()
        dashboardDoctorLayout.requestLayout()
        dashboardNurseLayout.requestLayout()
        dashboardAdminLayout.requestLayout()
    }

    private fun clearSessionData(){
        editor.clear()
        editor.commit()
    }

}