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

class ProfileMenu : AppCompatActivity() {

    private val apiHandler =ApiRequestQueue.getInstance(this)
    private lateinit var loadinDialog: LoadinDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_menu)
//        loadinDialog= LoadinDialog(this)
//        loadinDialog.showLoadingDialog()
        val usernameDP = findViewById<TextView>(R.id.usernameDisplay)
        usernameDP.text = apiHandler.user.name
        val descDP = findViewById<TextView>(R.id.descriptionDisplay)
        descDP.text = apiHandler.user.description
        descDP.setOnLongClickListener{
                showInputDialog()
                true
        }

        val imageRecycleView = findViewById<RecyclerView>(R.id.userImagesDisplay)
        imageRecycleView.layoutManager = LinearLayoutManager(this)
        val adapter = UserImagesAdapter(this)
        adapter.fetchDataAndInitializeAdapter()
        imageRecycleView.adapter = adapter

    }

    fun showInputDialog() {
        val editText = EditText(this)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter Text")
        builder.setView(editText)

        builder.setPositiveButton("OK") { _, _ ->
            val enteredText = editText.text.toString()
            // Do something with the entered text
            val descDP = findViewById<TextView>(R.id.descriptionDisplay)
            descDP.text = enteredText
            apiHandler.user.description = enteredText

            val url = "http://192.168.50.8:8000/api/user/desc"
            val att = JSONObject()
            att.put("description", enteredText)

            val updateDescRequest = object: JsonObjectRequest(
                Method.PATCH, url,att,{_ ->},
                {error ->
                    Toast.makeText(this@ProfileMenu, "An error occurred: ${error.message}", Toast.LENGTH_LONG).show()
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val token = apiHandler.getToken(this@ProfileMenu)
                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer $token"
                    return headers
                }
            }
            apiHandler.addToRequestQueue(updateDescRequest)
        }

        builder.setNegativeButton("Cancel") { _, _ ->
            // Handle cancel button click
        }

        builder.show()
    }
}