package com.afee.recuritingapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;



class CandidateAdapter(options: FirebaseRecyclerOptions<Candidates>) : FirebaseRecyclerAdapter<Candidates, CandidateAdapter.MyViewHolder>(options){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Candidates){
        holder.nameTextView.text = model.name
        holder.titleTextView.text = model.title
        Glide.with(holder.candidateImg.context)
            .load(model.photo)
            .into(holder.candidateImg)

    }

    class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.candidate_row_layout, parent, false)){
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val candidateImg: ImageView = itemView.findViewById(R.id.candidateImageView)



    }
}
