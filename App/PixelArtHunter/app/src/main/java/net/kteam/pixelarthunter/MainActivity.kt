package net.kteam.pixelarthunter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import net.kteam.pixelarthunter.ui.theme.PixelArtHunterTheme
import org.json.JSONObject

class MainActivity : ComponentActivity() {

    val ApiHandler = ApiRequestQueue.getInstance(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.login_form)

        val loginField = findViewById<EditText>(R.id.loginField)
        val passwordField = findViewById<EditText>(R.id.passwordField)
        val responseText = findViewById<TextView>(R.id.responseText)

//        val currUser = User()

        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val url = "http://192.168.2.190:8000/api/login"

            val att = JSONObject()
            att.put("email", loginField.text)
            att.put("password", passwordField.text)
            val loginRequest = JsonObjectRequest(
                Request.Method.GET,url,att,
                { response ->
                    val loadinDialog = LoadinDialog(this)
                    loadinDialog.showLoadingDialog()
//                    currUser.setId(response.getInt("id"))
//                    currUser.name = response.getString("name")
//                    currUser.email = response.getString("email")
//                    currUser.score = response.getInt("score")
                    ApiHandler.setToken(this,response.getString("token"))

                    responseText.text = response.getString("token")
                    loadinDialog.hideLoadingDialog()

                    val mainMenuIntent = Intent(this,DrawMenu::class.java)
                    startActivity(mainMenuIntent)
                },
                { error ->
                    if (error.hashCode() == 401){
                        responseText.text = "401 RESPONSE CODE"
                    }
                    responseText.text = error.toString()
                })

            ApiHandler.addToRequestQueue(loginRequest)
        }

        val registerButton = findViewById<Button>(R.id.registerActivityButton)

        registerButton.setOnClickListener {
            val intent = Intent(this,DrawMenu::class.java)
//            intent.putExtra("user", currUser)

            startActivity(intent)
        }
    }
}
