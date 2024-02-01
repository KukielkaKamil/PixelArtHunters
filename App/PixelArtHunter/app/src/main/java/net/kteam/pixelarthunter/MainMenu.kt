package net.kteam.pixelarthunter

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.ViewAnnotationAnchor
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.AnnotationManager
import com.mapbox.maps.plugin.annotation.AnnotationPlugin
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.viewannotation.ViewAnnotationManager


class MainMenu : AppCompatActivity() {

//    private lateinit var permissionsManager: PermissionsManager

    lateinit var mapView: MapView
    var annotationObjectMap: HashMap<PointAnnotation, Poi> =
        HashMap<PointAnnotation, Poi>()
//    val annotationApi: AnnotationPlugin? = null
//    val pointAnnotationManager: PointAnnotationManager? = null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        mapView = findViewById(R.id.mapView)


        addAnnotationToMap()



    }

    private fun addAnnotationToMap() {

        val annotationApi = mapView.annotations
        val pointAnnotationManager = annotationApi.createPointAnnotationManager(AnnotationConfig())
        pointAnnotationManager.addClickListener{ pointAnnotation ->
            // Handle click event here
            // For example, you can show a Toast with the annotation's ID
            val test = annotationObjectMap[pointAnnotation]
            Toast.makeText(this@MainMenu, "Clicked annotation with id: ${test?.name}", Toast.LENGTH_LONG).show()
            true
        }
        val point = Point.fromLngLat( 22.013638,50.0275186)
        val pointAnnotationOptions = PointAnnotationOptions()
            .withPoint(point) // replace with your coordinates
            .withIconImage(BitmapFactory.decodeResource(resources,R.drawable.red_marker)) // replace with your icon id

        val test = Poi("Millenum Hall",22.013638,50.0275186,2.0)

// Add the annotation to the map
        val tak = pointAnnotationManager.create(pointAnnotationOptions)
        annotationObjectMap[tak] = test
    }

    private fun createMarker(){
        var bitmap = BitmapFactory.decodeResource(resources,R.drawable.red_marker)
        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
            .withPoint(Point.fromLngLat(50.0264101,22.0116127)).withIconImage(bitmap)
    }


}