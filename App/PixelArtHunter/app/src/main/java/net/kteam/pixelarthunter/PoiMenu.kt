package net.kteam.pixelarthunter

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PoiMenu : AppCompatActivity() {


    var canvasSize = 8
//    val extras = intent.extras
    var id = 0
    var name =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poi_menu)
        id = intent.getIntExtra("id",0)
        name = intent.getStringExtra("name") ?: "POI"
        val poiName = findViewById<TextView>(R.id.poiName)
        poiName.text = name


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        if(id != 0) {
            val adapter = PoiAdapter(this, id)
            adapter.fetchDataAndInitializeAdapter()
            recyclerView.adapter = adapter
        }

        val createButton = findViewById<ImageButton>(R.id.createImageButton)
        createButton.setOnClickListener{
            showSelectionDialog()
        }
    }

    private fun showSelectionDialog() {
        // Get the layout inflater
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.dialog_select, null)

        // Initialize the spinner
        val spinner: Spinner = view.findViewById(R.id.spinner)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.canvas_sizes,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Handle the selected value here
                val selectedIndex = position
                val options = intArrayOf(8,16,32,64)
                canvasSize = options[selectedIndex]

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected (if needed)
                canvasSize =8
            }
        }

        // Build the AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select a Value")
            .setView(view)
            .setPositiveButton("OK") { dialog, which ->
                // Handle the selected value here
                val selectedValue = spinner.selectedItem.toString()
                // Do something with the selected value
                val drawingIntent = Intent(this,DrawMenu::class.java)
                drawingIntent.putExtra("canvasSize", canvasSize)
                drawingIntent.putExtra("poiId",id)
                startActivity(drawingIntent)
            }
            .setNegativeButton("Cancel") { dialog, which ->
                // Handle cancel button click
                dialog.dismiss()
            }

        // Show the dialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}