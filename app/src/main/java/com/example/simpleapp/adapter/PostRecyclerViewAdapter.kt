package com.example.simpleapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleapp.R
import com.example.simpleapp.model.Post
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class PostRecyclerViewAdapter(val posts:List<Post>): RecyclerView.Adapter<PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val list_item = layoutInflater.inflate(R.layout.post_feed, parent, false)
        return PostViewHolder(list_item)
    }

    override fun getItemCount(): Int {
        return posts.size
//        return 1
    }


    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

}

class PostViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    fun bind(post:Post){
        val tvBloodtype = view.findViewById<TextView>(R.id.tvbloodtype)
        val tvPosterName = view.findViewById<TextView>(R.id.tvPosterName)
        val tvPostDate = view.findViewById<TextView>(R.id.tvPostDate)
        val tvPostText = view.findViewById<TextView>(R.id.tvPostText)
        val tvPostContact = view.findViewById<TextView>(R.id.tvPostContact)

        val date = OffsetDateTime.parse(post.date)
            // and extract the date
            .toLocalDate()
        val time = OffsetDateTime.parse(post.date).toLocalTime()

        tvBloodtype.text = post.bloodType + " blood needed"
        tvPostDate.text= parseJavascriptDate(post.date)
        tvPosterName.text = post.username
//        tvPostDate.text = post.date
        tvPostText.text = post.text
        tvPostContact.text = post.contact
    }

    fun parseJavascriptDate(dateString: String): String {
        // Remove unnecessary characters from the date string
        val cleanString = dateString.trim().replace("'", "").replace("new Date(", "").replace(")", "")

        // Parse the string to a LocalDate object
        val dateTime = LocalDateTime.parse(cleanString, DateTimeFormatter.ISO_ZONED_DATE_TIME)
        // Format date and time separately:
        val dateStr = dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM"))
        val timeStr = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        return "$dateStr ~ $timeStr"
    }


}