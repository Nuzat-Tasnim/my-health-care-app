package com.example.simpleapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleapp.R
import com.example.simpleapp.model.ApplyRequestService
import com.example.simpleapp.model.ApplyRequestWithName
import com.example.simpleapp.model.ApproveRequestBody
import com.example.simpleapp.model.RetrofitInstance
import com.example.simpleapp.view.ProfileActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UnapprovedListRecyclerViewAdapter(val unapprovedList: List<ApplyRequestWithName>,
                                        val context: Context,
                                        val token : String,
                                        val adminid: String
    ): RecyclerView.Adapter<UnapprovedListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnapprovedListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val list_item = layoutInflater.inflate(R.layout.unapproved_list_layout, parent, false)
        return UnapprovedListViewHolder(list_item, context, token, adminid)
    }

    override fun getItemCount(): Int {
        return unapprovedList.size
    }

    override fun onBindViewHolder(holder: UnapprovedListViewHolder, position: Int) {
        holder.bind(unapprovedList[position])
    }

}

class UnapprovedListViewHolder(val view: View,
                               val context: Context,
                               val token: String,
                               val adminid: String
    ): RecyclerView.ViewHolder(view){

    val retrofit = RetrofitInstance
                    .getRetrofitInstance(context)
                    .create(ApplyRequestService::class.java)
    fun bind(applyRequest: ApplyRequestWithName){
        val unapprovedLayout = view.findViewById<LinearLayout>(R.id.unapprovedLayout)
        val unapprovedButtonLayout = view.findViewById<LinearLayout>(R.id.unapprovedButtonLayout)
        val tvName = view.findViewById<TextView>(R.id.tvUnapprovedName)
        val btnApprove = view.findViewById<Button>(R.id.btnApprove)
        val btnReject = view.findViewById<Button>(R.id.btnReject)

        tvName.text = applyRequest.userInfo.name
        tvName.setPadding(10, 10, 10, 10)
        tvName.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra("userid", applyRequest.userInfo.id)
            context.startActivity(intent)
        }

        btnApprove.setOnClickListener {
            unapprovedLayout.visibility = GONE
            var retrodata = retrofit.approveRequest(token, ApproveRequestBody(applyRequest.id, adminid))
            retrodata.enqueue(object: Callback<Any>{
                override fun onResponse(call: Call<Any>, response: Response<Any>) {}
                override fun onFailure(call: Call<Any>, t: Throwable) {}
            })

        }
        btnReject.setOnClickListener {
            unapprovedLayout.visibility = GONE
            var retrodata = retrofit.rejectRequest(token, ApproveRequestBody(applyRequest.id, adminid))
            retrodata.enqueue(object: Callback<Any>{
                override fun onResponse(call: Call<Any>, response: Response<Any>) {}
                override fun onFailure(call: Call<Any>, t: Throwable) {}
            })
        }
    }

}