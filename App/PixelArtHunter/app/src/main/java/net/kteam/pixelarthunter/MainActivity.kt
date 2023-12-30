package net.kteam.pixelarthunter

import android.os.Bundle
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
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import net.kteam.pixelarthunter.ui.theme.PixelArtHunterTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_layout)
        val textView = findViewById<TextView>(R.id.textView)

        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.2.190:8000/api/poi"
        val poiList = POIListSingleton.getInstance().getPoiList()

// Request a string response from the provided URL.
        val stringRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
//                 Display the first 500 characters of the response string.
                for( i:Int in 0..1 ){
                    var currPoi = response.getJSONObject(i)
                    val id = currPoi.getInt("id")
                    val name = currPoi.getString("name")
                    val latitude = currPoi.getDouble("latitude")
                    val longitude = currPoi.getDouble("longitude")
                    val modifier = currPoi.getDouble("modifier")
                  val newPoi = POI(id,name,longitude,latitude,modifier)
                    poiList.add(newPoi)
                }
                textView.text=poiList[0].name
//                    textView.text="ELO"
//                textView.text=response.length().toString()
            },
            { error-> textView.text = error.toString()})

// Add the request to the RequestQueue.
        queue.add(stringRequest)

//        textView.text = poiList.size.toString()

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