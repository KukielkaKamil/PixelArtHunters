package net.kteam.pixelarthunter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class Register : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val loginField = findViewById<EditText>(R.id.loginField)
        val emailField = findViewById<EditText>(R.id.emailField)
        val passwordField = findViewById<EditText>(R.id.passField)
        val responseText = findViewById<TextView>(R.id.responseText)

        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            val url = "http://192.168.2.190:8000/api/login"

            val att = JSONObject()
            att.put("name",loginField.text)
            att.put("email", emailField.text)
            att.put("password", passwordField.text)

            val registerRequest = JsonObjectRequest(
                Request.Method.POST,url,att,
                { response ->
                    responseText.text = response.getString("token")
                },
                { error ->
                    responseText.text = error.toString()
                })

            ApiRequestQueue.getInstance(this)
        }
    }
}