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
import com.android.volley.toolbox.JsonObjectRequest
import net.kteam.pixelarthunter.ui.theme.PixelArtHunterTheme
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_form)

        val loginField = findViewById<EditText>(R.id.loginField)
        val passwordField = findViewById<EditText>(R.id.passwordField)
        val responseText = findViewById<TextView>(R.id.responseText)

        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val url = "http://192.168.2.190:8000/api/login"

            val att = JSONObject()
            att.put("email", loginField.text)
            att.put("password", passwordField.text)
            val loginRequest = JsonObjectRequest(Request.Method.POST,url,att,
                { response ->
                    responseText.text ="GREAT SUCCESS"
                },
                { error ->
                    responseText.text = error.toString()
                })
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PixelArtHunterTheme {
        Greeting("Android")
    }
}