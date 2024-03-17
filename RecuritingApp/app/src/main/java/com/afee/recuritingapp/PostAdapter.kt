package com.afee.recuritingapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions



class PostAdapter(options: FirebaseRecyclerOptions<Post>) : FirebaseRecyclerAdapter<Post, PostAdapter.MyViewHolder>(options){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Post){
        holder.nameTextView.text = model.name
        holder.postDescTextView.text = model.post_desc
        holder.postTitleTextView.text = model.post_title
        holder.postTime.text = model.date


    }

    class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.posts_row_layout, parent, false)){
        val nameTextView: TextView = itemView.findViewById(R.id.postNameTextView)
        val postTitleTextView: TextView = itemView.findViewById(R.id.postsTitle)
        val postTime: TextView = itemView.findViewById(R.id.postsTimePosted)
        val postDescTextView: TextView = itemView.findViewById(R.id.postsDescription)




    }
}
