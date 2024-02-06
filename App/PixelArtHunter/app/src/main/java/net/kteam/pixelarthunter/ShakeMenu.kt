package net.kteam.pixelarthunter

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.ImageView
import android.widget.Toast
import kotlin.random.Random
import java.util.*
import kotlin.math.sqrt

import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class ShakeMenu : AppCompatActivity() {
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    private val shakeThreshold = 10
    private val shakeDelay: Long = 1000 // 2 seconds delay
    private var lastShakeTime: Long = 0
    private var progress = 0
    private lateinit var loadingDialog: LoadinDialog
    private var shaken: Boolean = false
    private val apiHandler = ApiRequestQueue.getInstance(this)
    private val shakeStatuses = arrayOf (R.drawable.shake0, R.drawable.shake20,R.drawable.shake40,
        R.drawable.shake60,R.drawable.shake80,R.drawable.shake100)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shake_menu)
        loadingDialog = LoadinDialog(this)


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        Objects.requireNonNull(sensorManager)!!
            .registerListener(sensorListener, sensorManager!!
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME)

        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH
    }

    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration

            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta

            val vib = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                getSystemService(VIBRATOR_SERVICE) as Vibrator
            }
            val shakeProgressDisplay = findViewById<ImageView>(R.id.shakeDisplay)


            if (acceleration > shakeThreshold && System.currentTimeMillis() - lastShakeTime > shakeDelay) {
                lastShakeTime = System.currentTimeMillis()
                if(progress < 5) {
                    progress += 1
                    shakeProgressDisplay.setImageResource(shakeStatuses[progress])
                }

                if(progress >= 5 && !shaken){
                    loadingDialog.showLoadingDialog()
                    val randPixels = Random.nextInt(10,41)
                    val url ="http://192.168.50.8:8000/api/user/pixels"
                    val att = JSONObject()
                    val pixels = apiHandler.user.pixels + randPixels
                    att.put("pixels",pixels)
                    val updatePixelsRequest = object: JsonObjectRequest(
                        Method.PATCH,url,att,{_ ->
                            apiHandler.user.pixels = pixels
                            showAlertDialog("Koniec", "Liczba wylosowanych przez ciebie pikseli: $randPixels")
                        },{ error ->
                            val errorMessage = if (error.hashCode() == 401) {
                                error.message.toString()
                            } else {
                                error.toString()
                            }
                            Toast.makeText(this@ShakeMenu, "An error occurred: $errorMessage", Toast.LENGTH_LONG).show()
                        }
                    ){override fun getHeaders(): MutableMap<String, String> {
                        val token = apiHandler.getToken(this@ShakeMenu)
                        val headers = HashMap<String, String>()
                        headers["Authorization"] = "Bearer $token"
                        return headers
                    }}
                    apiHandler.addToRequestQueue(updatePixelsRequest)
                    shaken = true

                }
                // Vibrate for 400 milliseconds
                @Suppress("DEPRECATION")
                vib.vibrate(400)
                // Adding a delay before the next shake event can be detected
                Handler(Looper.getMainLooper()).postDelayed({
                    // The delay has passed, and the app is ready to detect the next shake event
                }, shakeDelay)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }



    override fun onResume() {
        sensorManager?.registerListener(
            sensorListener,
            sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }

    override fun onPause() {
        sensorManager!!.unregisterListener(sensorListener)
        super.onPause()
    }

    private fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, which ->
                // Do something when the "OK" button is clicked
                dialog.dismiss()
                finish()
            }
            .show()
    }

}
