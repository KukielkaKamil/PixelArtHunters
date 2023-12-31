package net.kteam.pixelarthunter

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import net.kteam.pixelarthunter.ui.theme.PixelArtHunterTheme
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_form)

        val loginInput = findViewById<EditText>(R.id.loginInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val responseText = findViewById<TextView>(R.id.responseText)

        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val url ="http://192.168.2.190:8000/api/login"
            val loginData = JSONObject()
            loginData.put("email", loginInput.text)
            loginData.put("password",passwordInput.text)

            val arr = JSONArray()
            arr.put(loginData)
            val loginRequest = JsonArrayRequest(Request.Method.POST,url,arr,
                { response ->
                    responseText.text = response.getJSONObject(1).getString("token")
                },
                { error ->
                    responseText.text = error.toString()
                })
            ApiRequestQueue.getInstance(this).addToRequestQueue(loginRequest)
        }
    }
}

