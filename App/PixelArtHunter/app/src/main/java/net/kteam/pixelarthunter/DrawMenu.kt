package net.kteam.pixelarthunter


import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.ColorPickerView
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener


class DrawMenu : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(net.kteam.pixelarthunter.R.layout.activity_draw_menu)

        val grid : PixelGridView = findViewById(R.id.pixelGridView)
        grid.setGridSize(16,16)

        findViewById<Button>(R.id.drawOnButton).setOnClickListener {
            grid.setDrawingOn(true)
        }
        findViewById<Button>(R.id.moveOnButton).setOnClickListener {
            grid.setDrawingOn(false)
        }

        findViewById<Button>(R.id.eraserButton).setOnClickListener {
            grid.resetView()
        }
        val paint1 = findViewById<Button>(R.id.paint1)
        val paint2 = findViewById<Button>(R.id.paint2)
        val paint3 = findViewById<Button>(R.id.paint3)
        val paint4 = findViewById<Button>(R.id.paint4)
        var currPaint :Button = paint1
//        val text = findViewById<TextView>(R.id.textView8)
        var builder = ColorPickerDialog.Builder(this)
            .setTitle("ColorPicker Dialog")
            .setPreferenceName("MyColorPickerDialog")
            .setPositiveButton("OK",
                ColorEnvelopeListener { envelope, _ ->
                    val hexColor = String.format("#%06X", 0xFFFFFF and envelope.color)
//                    currPaint.setBackgroundColor(Color.parseColor(hexColor))
                    val back = currPaint.background as GradientDrawable
                    back.setColor(Color.parseColor(hexColor))
                    grid.setColor(hexColor)
                })
            .setNegativeButton("CANCLE") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .attachAlphaSlideBar(false) // attach AlphaSlideBar
            .attachBrightnessSlideBar(true)

        val colorPickerDialog = builder.create()


        paint1.setOnLongClickListener {
//            val colorPickerDialog = builder.create()
            colorPickerDialog.show()
            currPaint = paint1
            true

        }
        paint2.setOnLongClickListener {
            colorPickerDialog.show()
            currPaint = paint2
            true
        }
        paint3.setOnLongClickListener {
            colorPickerDialog.show()
            currPaint = paint3
            true
        }
        paint4.setOnLongClickListener {
            colorPickerDialog.show()
            currPaint = paint4
            true
        }
    }
}