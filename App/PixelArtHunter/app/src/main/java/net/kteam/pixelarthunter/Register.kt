package net.kteam.pixelarthunter

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class Register : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginField = findViewById<EditText>(R.id.username_input)
        val emailField = findViewById<EditText>(R.id.email_input)
        val passwordField = findViewById<EditText>(R.id.password_input)
        val apiHandler = ApiRequestQueue.getInstance(this)
        val loadingDialog = LoadinDialog(this)

        val registerButton = findViewById<Button>(R.id.makeacount_btn)

        registerButton.setOnClickListener {
            loadingDialog.showLoadingDialog()
            val url = "http://192.168.50.8:8000/api/register"

            val att = JSONObject()
            att.put("name",loginField.text)
            att.put("email", emailField.text)
            att.put("password", passwordField.text)

            val registerRequest = JsonObjectRequest(
                Request.Method.POST,url,att,
                { _ ->
                    Toast.makeText(this@Register, "Successful Registration", Toast.LENGTH_LONG).show()
                    finish()

                },
                { error ->
                    loadingDialog.hideLoadingDialog()
                    val message = if (error.hashCode() == 401){
                        error.message.toString()
                    } else{
                        error.toString()
                    }
                    Toast.makeText(this@Register, "An error occurred: $message", Toast.LENGTH_LONG).show()
                })

            apiHandler.addToRequestQueue(registerRequest)
        }
    }
}