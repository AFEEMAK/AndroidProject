package com.afee.recuritingapp


import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailActivity : AppCompatActivity() {
    private lateinit var candidateId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        candidateId = intent.getStringExtra("candidateId") ?:""
        Log.i("Id",candidateId)
        val userName : TextView = findViewById(R.id.user_name)
        val userTitle : TextView = findViewById(R.id.user_title)
        val userCompany : TextView = findViewById(R.id.user_company)
        val userDesc : TextView = findViewById(R.id.user_desc)
        val userPhone : TextView = findViewById(R.id.user_phone)
        val userEmail : TextView = findViewById(R.id.user_email)
        val userPhoto : ImageView = findViewById(R.id.user_photo)


            val candidate = FirebaseDatabase.getInstance().reference.child("Candidates").child(candidateId)
            candidate.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val candidate = snapshot.getValue(Candidates::class.java)
                        if (candidate != null) {
                            userName.text = candidate.name
                            userTitle.text = candidate.position
                            userCompany.text = candidate.company
                            userDesc.text = candidate.desc
                            userPhone.text = candidate.phone
                            userEmail.text = candidate.email
                            Glide.with(this@DetailActivity)
                                .load(candidate.photo)
                                .into(userPhoto)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.w("Error", "Failed to read value.", error.toException())
                }
        })

    }


}



