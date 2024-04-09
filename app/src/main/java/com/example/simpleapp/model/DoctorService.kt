package com.example.simpleapp.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DoctorService {

    @GET("/doctors/{doctorid}")
    fun getDoctor(
        @Header("x-auth-token") token: String,
        @Path("doctorid") doctorid: String
    ): Call<DoctorNameClass>

    @POST("/doctors/create")
    fun createDoctor(
        @Header("x-auth-token") token: String,
        @Body createRequest: DoctorCreateRequest
    ): Call<Doctor>

    @PUT("/doctors/edit")
    fun editDoctor(
        @Header("x-auth-token") token: String,
        @Body doctorEditRequest: DoctorEditRequest
    ): Call<DoctorNameClass>

    @POST("/doctors/search")
    fun searchDoctor(
        @Body searchRequest: DoctorSearchRequest
    ): Call<List<Doctor>>

    @POST("/doctors/seek")
    fun searchDoctor2(
        @Body departmentRequest: DoctorDepartmentRequest
    ): Call<List<DoctorNameClass>>

    @POST("/doctors/rate/{doctorid}")
    fun rateDoctor(
        @Header("x-auth-token") token: String,
        @Path("doctorid") doctorid: String,
        @Body rateRequest: DoctorRateRequest
    )

    @POST("/doctors/addSchedule")
    fun addSchedule(
        @Header("x-auth-token") token: String,
        @Body doctorAddScheduleRequest: DoctorAddScheduleRequest
        ): Call<Schedule>

    @GET("/doctors/unapproved")
    fun getUnapprovedDoctors(
        @Header("x-auth-token") token: String
    ): Call<List<Doctor>>

    @PUT("/doctors/approve")
    fun approveDoctor(
        @Header("x-auth-token") token: String,
        @Body doctorApproveRequest: DoctorApproveRequest
    ): Call<Doctor>

    @GET("/doctors/getDoctorsByDepartment")
    fun getDoctorsByDepartment():Call<Any>
}
