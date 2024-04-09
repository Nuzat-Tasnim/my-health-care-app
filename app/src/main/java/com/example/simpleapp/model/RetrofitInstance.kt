package com.example.simpleapp.model

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance {
    //    val BASE_URL = context.getString(R.string.BASE_URL)
    val BASE_URL = "https://my-health-care.onrender.com/"

    companion object{

        val interceptor = HttpLoggingInterceptor().apply {
            HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
        }.build()

        val BASE_URL = "https://my-health-care.onrender.com/"

        fun getRetrofitInstance(context: Context): Retrofit {
            return Retrofit.Builder()
//                .baseUrl(context.getString(R.string.BASE_URL))
                .baseUrl("http://10.0.2.2:3000/")
//                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
//                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
        }
    }
}