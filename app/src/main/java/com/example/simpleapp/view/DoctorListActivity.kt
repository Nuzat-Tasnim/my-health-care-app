package com.example.simpleapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleapp.R
import com.example.simpleapp.adapter.DepartmentListRecyclerViewAdapter

class DoctorListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_list)

        var rvDepartmentList = findViewById<RecyclerView>(R.id.rvDepartmentList)

        rvDepartmentList.layoutManager = LinearLayoutManager(this)
        rvDepartmentList.adapter = DepartmentListRecyclerViewAdapter(this.resources.getStringArray(R.array.doctortypes).toList(), this)
    }
}