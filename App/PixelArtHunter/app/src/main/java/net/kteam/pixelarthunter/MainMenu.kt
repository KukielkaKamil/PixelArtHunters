package net.kteam.pixelarthunter

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonObjectRequest
import com.mapbox.android.gestures.StandardScaleGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.OnScaleListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.scalebar.scalebar


class MainMenu : AppCompatActivity() {
    
    lateinit var mapView: MapView
    private var annotationObjectMap: HashMap<PointAnnotation, Poi> =
        HashMap<PointAnnotation,Poi>()
    private var apiHandler = ApiRequestQueue.getInstance(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        updateGui()
        mapView = findViewById(R.id.mapView)
        var torchOn = false
        val flashBT = findViewById<ImageButton>(R.id.flashButton)
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]
        flashBT.setOnClickListener {
            torchOn = if(!torchOn){
                cameraManager.setTorchMode(cameraId,true)
                true
            } else{
                cameraManager.setTorchMode(cameraId,false)
                false
            }
        }

        val profileBT = findViewById<ImageButton>(R.id.profileButton)
        profileBT.setOnClickListener{
            val profileIntent = Intent(this,ProfileMenu::class.java)
            startActivity(profileIntent)

        }

        val shakeBT = findViewById<ImageButton>(R.id.shakeButton)
        shakeBT.setOnClickListener{
            val shakeCheckUrl = "http://192.168.50.8:8000/api/user/shake"
            val shakeCheckRequest = object: JsonObjectRequest(
              Method.GET,shakeCheckUrl,null,{_ ->
                    val shakeIntent = Intent(this,ShakeMenu::class.java)
                    startActivity(shakeIntent)
                },{ error ->
                    val message = if (error.networkResponse.statusCode == 429){
                        "Ta akcja została już dzisiaj wykonana"

                    } else{
                        error.toString()
                    }
                    Toast.makeText(this@MainMenu, "An error occurred: $message", Toast.LENGTH_LONG).show()
                }
            ){
                override fun getHeaders(): MutableMap<String, String> {
                    val token = apiHandler.getToken(this@MainMenu)
                    val headers = java.util.HashMap<String, String>()
                    headers["Authorization"] = "Bearer $token"
                    return headers
                }
            }
            apiHandler.addToRequestQueue(shakeCheckRequest)
        }
        if (apiHandler.user.isAdmin){
            val reportedBT = findViewById<ImageButton>(R.id.reportedButton)
            reportedBT.setOnClickListener {
                val reportedIntent = Intent(this, ReportedImages::class.java)
                startActivity(reportedIntent)
            }
        }

        val logoutBT = findViewById<ImageButton>(R.id.logoutButton)
        logoutBT.setOnClickListener{
            val logoutUrl = "http://192.168.50.8:8000/api/logout"
            val logoutRequest = object: JsonObjectRequest(
                Method.GET,logoutUrl,null,{
                    apiHandler.removeToken(this)
                    val logoutIntent = Intent(this,MainActivity::class.java)
                    startActivity(logoutIntent)
                },{ error ->
                    val message = if (error.networkResponse.statusCode == 401){
                        error.message.toString()
                    } else{
                        error.toString()
                    }
                    Toast.makeText(this@MainMenu, "An error occurred: $message", Toast.LENGTH_LONG).show()
                }
            ){
                override fun getHeaders(): MutableMap<String, String> {
                    val token = apiHandler.getToken(this@MainMenu)
                    val headers = java.util.HashMap<String, String>()
                    headers["Authorization"] = "Bearer $token"
                    return headers
                }
            }
            apiHandler.addToRequestQueue(logoutRequest)
        }
        val reportedBT = findViewById<ImageButton>(R.id.reportedButton)
        if(apiHandler.user.isAdmin){
            reportedBT.visibility= View.VISIBLE
            reportedBT.setOnClickListener {
                val reportedIntent = Intent(this,ReportedImages::class.java)
                startActivity(reportedIntent)
            }
        }

        mapView.gestures.scrollEnabled=false
        mapView.gestures.doubleTapToZoomInEnabled=false
        mapView.gestures.doubleTouchToZoomOutEnabled=false
        mapView.gestures.quickZoomEnabled=false
        mapView.scalebar.enabled = false
        var lastPoint: Point? = null
        val locationPlugin = mapView.location
        locationPlugin.addOnIndicatorPositionChangedListener { point ->
            lastPoint = point
            mapView.mapboxMap.setCamera(CameraOptions.Builder().center(point).build())
        }

        val minZoom = 11.0
        val maxZoom = 17.0

        mapView.gestures.addOnScaleListener(object : OnScaleListener {
            override fun onScale(detector: StandardScaleGestureDetector) {
                val currentZoom = mapView.mapboxMap.cameraState.zoom
                if (currentZoom < minZoom) {
                    mapView.mapboxMap.setCamera(
                        CameraOptions.Builder().zoom(minZoom).build()
                    )
                } else if (currentZoom > maxZoom) {
                    mapView.mapboxMap.setCamera(
                        CameraOptions.Builder().zoom(maxZoom).build()
                    )
                }
                mapView.mapboxMap.setCamera(CameraOptions.Builder().center(lastPoint).build())

            }

            override fun onScaleBegin(detector: StandardScaleGestureDetector) {
                mapView.mapboxMap.setCamera(CameraOptions.Builder().center(lastPoint).build())
            }

            override fun onScaleEnd(detector: StandardScaleGestureDetector) {
                mapView.mapboxMap.setCamera(CameraOptions.Builder().center(lastPoint).build())
            }

        })
        addAnnotationToMap()


    }

    private fun addAnnotationToMap() {

        val annotationApi = mapView.annotations
        val pointAnnotationManager = annotationApi.createPointAnnotationManager(AnnotationConfig())
        pointAnnotationManager.addClickListener{ pointAnnotation ->
            // Handle click event here
            // For example, you can show a Toast with the annotation's ID
            val poi = annotationObjectMap[pointAnnotation]
            val poiMenuIntent = Intent(this,PoiMenu::class.java)
            poiMenuIntent.putExtra("id",poi?.id)
            poiMenuIntent.putExtra("name",poi?.name)
            startActivity(poiMenuIntent)
            true

        }
        for (poi in apiHandler.getList()){
            val point = Point.fromLngLat( poi.longitude,poi.latitude)
            val pointAnnotationOptions = PointAnnotationOptions()
                .withPoint(point) // replace with your coordinates
                .withIconImage(BitmapFactory.decodeResource(resources,R.drawable.red_marker)) // replace with your icon id

// Add the annotation to the map
            val poiElement = pointAnnotationManager.create(pointAnnotationOptions)
            annotationObjectMap[poiElement] = poi
        }


    }

    private fun updateGui(){
        val pixelDP = findViewById<TextView>(R.id.yourPixels)
        val pix = "Twoje piksele: ${apiHandler.user.pixels}"
        pixelDP.text = pix

        val usernameDP = findViewById<TextView>(R.id.userNameDisplay)
        usernameDP.text= apiHandler.user.name
    }

    override fun onResume() {
        super.onResume()
        updateGui()
    }




}