package com.example.simpleapp.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleapp.R
import com.example.simpleapp.adapter.UnapprovedListRecyclerViewAdapter
import com.example.simpleapp.model.ApplyRequestService
import com.example.simpleapp.model.ApplyRequestWithName
import com.example.simpleapp.model.FindRequestBody
import com.example.simpleapp.model.RetrofitInstance
import com.example.simpleapp.model.User
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UnapprovedListActivity : AppCompatActivity() {
    lateinit var token: String
    lateinit var loggedInUser: User
    lateinit var retrofitApplyRequest: ApplyRequestService
    lateinit var rvUnapproved: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unapproved_list)

        val context = this
        val shared_preferences_key = context.resources.getString(R.string.Shared_Preferences_Key)
        val sf = context.getSharedPreferences(shared_preferences_key, Context.MODE_PRIVATE)
        val user = sf.getString("loggedInUser", null)
        loggedInUser = Gson().fromJson(user, User::class.java)
        token = sf.getString("jwtToken", null)!!

        val unapprovedListType = intent.getStringExtra("listType")
        Log.d("MYTAG", "unapprovedListType $unapprovedListType")

        retrofitApplyRequest = RetrofitInstance
            .getRetrofitInstance(context)
            .create(ApplyRequestService::class.java)

        rvUnapproved = findViewById<RecyclerView>(R.id.rvUnapproved)
        rvUnapproved.layoutManager = LinearLayoutManager(this)

        CoroutineScope(Dispatchers.IO).launch {
            retrieveUnapprovedList(FindRequestBody(unapprovedListType!!))
        }
    }

    fun retrieveUnapprovedList(findRequestBody: FindRequestBody){
        var retrodata = retrofitApplyRequest.findRequest(token, findRequestBody)
        retrodata.enqueue(object: Callback<List<ApplyRequestWithName>>{
            override fun onResponse(
                call: Call<List<ApplyRequestWithName>>,
                response: Response<List<ApplyRequestWithName>>
            ) {
                val listy = response.body()
                Log.d("MYTAG", "listy $listy")
                rvUnapproved.adapter = UnapprovedListRecyclerViewAdapter(listy!!, this@UnapprovedListActivity, token, loggedInUser.adminid)
            }

            override fun onFailure(call: Call<List<ApplyRequestWithName>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}