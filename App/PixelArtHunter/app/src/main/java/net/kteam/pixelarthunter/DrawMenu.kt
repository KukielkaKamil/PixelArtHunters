package net.kteam.pixelarthunter


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.android.volley.toolbox.JsonObjectRequest
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import org.json.JSONObject


class DrawMenu : ComponentActivity() {
    var size = 0
    var poiId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_menu)
        val apiHandler = ApiRequestQueue.getInstance(this)
        size = intent.getIntExtra("canvasSize",8)
        poiId = intent.getIntExtra("poiId",0)

        val grid : PixelGridView = findViewById(R.id.pixelGridView)
        grid.setGridSize(size)

        findViewById<ImageButton>(R.id.drawOnButton).setOnClickListener {
            grid.setDrawingOn(true)
        }
        findViewById<ImageButton>(R.id.moveOnButton).setOnClickListener {
            grid.setDrawingOn(false)
        }

        findViewById<ImageButton>(R.id.resetButton).setOnClickListener {
            grid.resetView()
        }

        findViewById<ImageButton>(R.id.saveButton).setOnClickListener{
            val url = "http://192.168.50.8:8000/api/art/create"
            val att = JSONObject()
            att.put("size",size)
            att.put("image", Art.decArrayToBinString(grid.cellColors))
            att.put("poi_id",poiId)
            att.put("user_id", apiHandler.user.getId())
            att.put("cost",grid.cost)

            val saveImageRequest = object: JsonObjectRequest(
              Method.POST,url,att,{ _ ->
                    apiHandler.user.pixels -= grid.cost
                    Toast.makeText(this@DrawMenu, "Saved image", Toast.LENGTH_LONG).show()
                },{error->
                    val message = if (error.hashCode() == 401){
                        error.message.toString()
                    } else{
                        error.toString()
                    }
                    Toast.makeText(this@DrawMenu, "An error occurred: $message", Toast.LENGTH_LONG).show()
                }
            ){}

            if(apiHandler.user.pixels >= grid.cost){
                apiHandler.addToRequestQueue(saveImageRequest)
            }
            else{
                Toast.makeText(this@DrawMenu, "Nie masz wystarczająco pikseli", Toast.LENGTH_LONG).show()
            }

        }

        val paint1 = findViewById<Button>(R.id.paint1)
        val paint2 = findViewById<Button>(R.id.paint2)
        val paint3 = findViewById<Button>(R.id.paint3)
        val paint4 = findViewById<Button>(R.id.paint4)
        val paint5 = findViewById<Button>(R.id.paint5)
        val paint6 = findViewById<Button>(R.id.paint6)
        val paint7 = findViewById<Button>(R.id.paint7)
        val paint8 = findViewById<Button>(R.id.paint8)
        var currPaint :Button = paint1
//        val text = findViewById<TextView>(R.id.textView8)
        val builder = ColorPickerDialog.Builder(this)
            .setTitle("ColorPicker Dialog")
            .setPreferenceName("MyColorPickerDialog")
            .setPositiveButton("OK",
                ColorEnvelopeListener { envelope, _ ->
                    val hexColor = String.format("#%06X", 0xFFFFFF and envelope.color)
//                    currPaint.setBackgroundColor(Color.parseColor(hexColor))
                    currPaint.setBackgroundColor(Color.parseColor(hexColor))

                    grid.setColor(hexColor)
                })
            .setNegativeButton("CANCEL") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .attachAlphaSlideBar(false) // attach AlphaSlideBar
            .attachBrightnessSlideBar(true)

        val colorPickerDialog = builder.create()


        paint1.setOnLongClickListener {
            colorPickerDialog.show()
            currPaint = paint1
            true
        }
        paint1.setOnClickListener {
            val backgroundColor = (paint1.background as ColorDrawable).color
            val color = String.format("#%06X", 0xFFFFFF and backgroundColor)
            grid.setColor(color)
        }
        paint2.setOnLongClickListener {
            colorPickerDialog.show()
            currPaint = paint2
            true
        }
        paint2.setOnClickListener {
            val backgroundColor = (paint2.background as ColorDrawable).color
            val color = String.format("#%06X", 0xFFFFFF and backgroundColor)
            grid.setColor(color)
        }
        paint3.setOnLongClickListener {
            colorPickerDialog.show()
            currPaint = paint3
            true
        }
        paint3.setOnClickListener {
            val backgroundColor = (paint3.background as ColorDrawable).color
            val color = String.format("#%06X", 0xFFFFFF and backgroundColor)
            grid.setColor(color)
        }
        paint4.setOnLongClickListener {
            colorPickerDialog.show()
            currPaint = paint4
            true
        }
        paint4.setOnClickListener {
            val backgroundColor = (paint4.background as ColorDrawable).color
            val color = String.format("#%06X", 0xFFFFFF and backgroundColor)
            grid.setColor(color)
        }
        paint5.setOnLongClickListener {
            colorPickerDialog.show()
            currPaint = paint5
            true
        }
        paint5.setOnClickListener {
            val backgroundColor = (paint5.background as ColorDrawable).color
            val color = String.format("#%06X", 0xFFFFFF and backgroundColor)
            grid.setColor(color)
        }
        paint6.setOnLongClickListener {
            colorPickerDialog.show()
            currPaint = paint6
            true
        }
        paint6.setOnClickListener {
            val backgroundColor = (paint6.background as ColorDrawable).color
            val color = String.format("#%06X", 0xFFFFFF and backgroundColor)
            grid.setColor(color)
        }
        paint7.setOnLongClickListener {
            colorPickerDialog.show()
            currPaint = paint7
            true
        }
        paint7.setOnClickListener {
            val backgroundColor = (paint7.background as ColorDrawable).color
            val color = String.format("#%06X", 0xFFFFFF and backgroundColor)
            grid.setColor(color)
        }
        paint8.setOnLongClickListener {
            colorPickerDialog.show()
            currPaint = paint8
            true
        }
        paint8.setOnClickListener {
            val backgroundColor = (paint8.background as ColorDrawable).color
            val color = String.format("#%06X", 0xFFFFFF and backgroundColor)
            grid.setColor(color)
        }
    }
}