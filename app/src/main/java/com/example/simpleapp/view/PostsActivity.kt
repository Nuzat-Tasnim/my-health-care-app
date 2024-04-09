package com.example.simpleapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleapp.R
import com.example.simpleapp.adapter.PostRecyclerViewAdapter
import com.example.simpleapp.model.Post
import com.example.simpleapp.model.PostService
import com.example.simpleapp.model.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostsActivity : AppCompatActivity() {
    private lateinit var retrofit: PostService
    private lateinit var posts: List<Post>
    private lateinit var rvPosts: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)
        rvPosts = findViewById<RecyclerView>(R.id.rvMyPostfeed)

        retrofit = RetrofitInstance
            .getRetrofitInstance(this)
            .create(PostService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            retrieveMyPosts()
        }

    }
    private fun retrieveMyPosts(){
        var retroData = retrofit.getPosts()
        retroData.enqueue(object: Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                Log.d("MYTAG", "response body is:- ${response.body().toString()}")
                posts = response.body()!!
                rvPosts.layoutManager = LinearLayoutManager(this@PostsActivity)
                rvPosts.adapter = PostRecyclerViewAdapter(posts)
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Log.d("MYTAG", "getPosts() throwable is: $t")
            }
        })

    }

}