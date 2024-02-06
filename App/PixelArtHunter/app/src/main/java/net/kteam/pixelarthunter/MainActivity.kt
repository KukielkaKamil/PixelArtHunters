package net.kteam.pixelarthunter

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class MainActivity : ComponentActivity() {

    private val apiHandler = ApiRequestQueue.getInstance(this)
    private lateinit var loadingDialog: LoadinDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        loadingDialog = LoadinDialog(this)

//        val shakeMenuActivity = Intent(this,DrawMenu::class.java)
//        startActivity(shakeMenuActivity)

        val login = findViewById<EditText>(R.id.username_input).text
        val password= findViewById<EditText>(R.id.password_input).text
//        val responseText = findViewById<TextView>(R.id.responseText)


        val token:String = apiHandler.getToken(this)

        if(token.isNotBlank()){
                loadingDialog.showLoadingDialog()
                val url = "http://192.168.50.8:8000/api/user"
                val getUserRequest = object :JsonObjectRequest(
                    Method.GET,url,null,
                    {response ->
                        val user = getUserFromRequest(response)
                        apiHandler.user = user
                        loadPoi(token)
                    },
                    {error ->
                        loadingDialog.hideLoadingDialog()
                        handleError(error)
                    }
                ){
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Authorization"] = "Bearer $token"
                        return headers
                    }
                }
            apiHandler.addToRequestQueue(getUserRequest)
            }


        val loginButton = findViewById<Button>(R.id.login_btn)

        loginButton.setOnClickListener {
            loadingDialog.showLoadingDialog()
            val url = "http://192.168.50.8:8000/api/login"

            val att = JSONObject()
            att.put("email", login)
            att.put("password", password)
            val loginRequest = JsonObjectRequest(
                Request.Method.POST,url,att,
                { response ->
                    val userData = response.getJSONObject("user")
                    val user = getUserFromRequest(userData)
                    apiHandler.user = user
                    val responseToken = response.getString("token")

                    apiHandler.setToken(this,responseToken)

                    loadPoi(responseToken)

                },
                { error ->
                    loadingDialog.hideLoadingDialog()
                    handleError(error)
                })

            apiHandler.addToRequestQueue(loginRequest)
        }

        val registerButton = findViewById<Button>(R.id.singin_btn)

        registerButton.setOnClickListener {
            val intent = Intent(this,Register::class.java)
            startActivity(intent)
        }
    }

    private fun getUserFromRequest(response: JSONObject): User {
         return User(
             response.getInt("id"),
             response.getString("name"),
             response.getString("email"),
             response.getString("description") ?: "",
             response.getInt("score"),
             response.getInt("pixels"),
             response.getInt("is_admin")
        )
    }

    private fun startMainMenu(){
        val mainMenuIntent = Intent(this,MainMenu::class.java)
        startActivity(mainMenuIntent)
        finish()
    }

    private fun handleError(error:VolleyError){

        val message = if (error.hashCode() == 401){
            error.message.toString()
        } else{
            error.toString()
        }
        Toast.makeText(this@MainActivity, "An error occurred: $message", Toast.LENGTH_LONG).show()
    }


    private fun loadPoi(token: String) {
        val url = "http://192.168.50.8:8000/api/poi"
        val poiList = apiHandler.getList()

        val getPoiListRequest = object : JsonArrayRequest(
            Method.GET, url, null,
            { request ->
//                val poiRequestList = request.getJSONArray(0)
                for (i in 0 until request.length()) {
                    val poi = request.getJSONObject(i)
                    poiList.add(
                        Poi(
                            poi.getInt("id"),
                            poi.getString("name"),
                            poi.getDouble("longitude"),
                            poi.getDouble("latitude"),
                            poi.getDouble("modifier")
                        )
                    )
                }
                startMainMenu()
            }, { error ->
                loadingDialog.hideLoadingDialog()
                handleError(error)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }
        apiHandler.addToRequestQueue(getPoiListRequest)
    }



}
