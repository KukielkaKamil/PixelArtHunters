package net.kteam.pixelarthunter


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewTreeObserver
import kotlin.math.roundToInt

class DisplayGridView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private companion object {
        private const val DEFAULT_COLUMNS = 10
        private const val DEFAULT_ROWS = 10
    }

    private var columns = DEFAULT_COLUMNS
    private var rows = DEFAULT_ROWS


    private var cellSize: Float = 0f
    private var totalWidth: Float = 0f
    private var totalHeight: Float = 0f


    private val canvasPaint: Paint = Paint()
    private val paint: Paint = Paint()

    var cellColors: Array<Array<Int>> = Array(columns) { Array(rows) { Color.WHITE } }

    init {
        canvasPaint.color = Color.BLACK
        canvasPaint.style = Paint.Style.FILL
        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                viewTreeObserver.removeOnPreDrawListener(this)
                setHeightEqualToWidth()
                return true
            }
        })
    }
    private fun setHeightEqualToWidth() {
        val screenWidth = resources.displayMetrics.widthPixels
        layoutParams.height = screenWidth
        requestLayout()
    }

    fun setGridSize(columns: Int, rows: Int) {
        this.columns = columns
        this.rows = rows
        cellColors = Array(columns) { Array(rows) { Color.WHITE } }
        calculateCellSize()
//        this.layoutParams.height = this.columns
        invalidate()
    }

    fun loadImage(image: Array<Array<Int>>){
        cellColors=image
        invalidate()
    }
    private fun calculateCellSize() {
        cellSize = kotlin.math.min(width / columns.toFloat(), height / rows.toFloat())
        totalWidth = columns * cellSize
        totalHeight = rows * cellSize
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateCellSize()
    }

    fun setColor(color: String){
        paint.color= Color.parseColor(color)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()

        // Draw background
        canvas.drawColor(Color.TRANSPARENT)


        // Draw colored cells based on the 2D array
        for (i in 0 until columns) {
            for (j in 0 until rows) {
                val left = i * cellSize
                val top = j * cellSize
                val right = left + cellSize
                val bottom = top + cellSize

                canvasPaint.color = cellColors[i][j]
                canvas.drawRect(left, top, right, bottom, canvasPaint)
            }
        }

        // Restore original paint settings
        canvasPaint.color = Color.BLACK
        canvasPaint.strokeWidth = 0f

        canvas.restore()
    }


}
