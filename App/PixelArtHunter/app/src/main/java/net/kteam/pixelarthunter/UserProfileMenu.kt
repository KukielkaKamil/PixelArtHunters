package net.kteam.pixelarthunter

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import java.util.HashMap

class UserProfileMenu() : AppCompatActivity() {

    private val apiHandler =ApiRequestQueue.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getIntExtra("id",0)
        setContentView(R.layout.activity_profile_menu)
        val usernameDP = findViewById<TextView>(R.id.usernameDisplay)
        val descDP = findViewById<TextView>(R.id.descriptionDisplay)



        val imageRecycleView = findViewById<RecyclerView>(R.id.userImagesDisplay)
        imageRecycleView.layoutManager = LinearLayoutManager(this)
        val adapter = OtherUserDataAdapter(this,id,usernameDP,descDP)
        adapter.fetchDataAndInitializeAdapter()
        imageRecycleView.adapter = adapter

//        usernameDP.text = adapter.userJson.getString("name")
//        descDP.text = adapter.userJson.getString("description")
//        scoreDP.text = adapter.userJson.getInt("score").toString()
    }


}