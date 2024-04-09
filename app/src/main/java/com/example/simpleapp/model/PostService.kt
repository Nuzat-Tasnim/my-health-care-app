package com.example.simpleapp.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PostService {
    @GET("/posts/feed")
    fun getPosts(): Call<List<Post>>

    @POST("/posts/send")
    fun sendPost(
        @Header("x-auth-token") token: String,
        @Body postSendRequest: PostSendRequest
    ): Call<Post>

    @GET("/posts/{postId}")
    fun getPost(
        @Header("x-auth-token") token: String,
        @Path("postId") postId: String
    ): Call<Post>

    @DELETE("/posts/remove/{postid}")
    fun deletePost(
        @Header("x-auth-token") token: String,
        @Path("postId") postId: String
    ): Call<Post>
}