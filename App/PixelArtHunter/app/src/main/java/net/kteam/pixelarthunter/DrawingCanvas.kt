package net.kteam.pixelarthunter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View

class DrawingCanvas @JvmOverloads constructor(
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

    private var zoomFactor: Float = 1f
    private val transformMatrix = Matrix()
    private val scaleGestureDetector: ScaleGestureDetector

    private val paint: Paint

    private var lastTouchX: Float = 0f
    private var lastTouchY: Float = 0f

    private var isMoving: Boolean = false
    private var isDrawing: Boolean = true
    private var cellColors: Array<Array<Int>> = Array(columns) { Array(rows) { Color.WHITE } }

    init {
        paint = Paint()
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL

        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    }

    fun setGridSize(columns: Int, rows: Int) {
        this.columns = columns
        this.rows = rows
        calculateCellSize()
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

    fun setDrawingOn(value: Boolean){
        isDrawing = value
        isMoving = !value
    }

    fun restetView(){
        zoomFactor = 1.0f
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()
        canvas.concat(transformMatrix)

        // Draw background
        canvas.drawColor(Color.WHITE)


        // Draw colored cells based on the 2D array
        for (i in 0 until columns) {
            for (j in 0 until rows) {
                val left = i * cellSize
                val top = j * cellSize
                val right = left + cellSize
                val bottom = top + cellSize

                paint.color = cellColors[i][j]
                canvas.drawRect(left, top, right, bottom, paint)
            }
        }

        // Draw grid lines
        paint.color = Color.GRAY
        paint.strokeWidth = 2f / zoomFactor

        // Draw vertical lines
        for (i in 0..columns) {
            val x = i * cellSize
            canvas.drawLine(x, 0f, x, totalHeight, paint)
        }

        // Draw horizontal lines
        for (j in 0..rows) {
            val y = j * cellSize
            canvas.drawLine(0f, y, totalWidth, y, paint)
        }

        // Restore original paint settings
        paint.color = Color.BLACK
        paint.strokeWidth = 0f

        canvas.restore()
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // When fully zoomed out, handle click event
                if (isDrawing) {
                    handleClickEvent(event.x, event.y)
                }
                lastTouchX = event.x
                lastTouchY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                if (zoomFactor > 1.0f) {
                    val deltaX: Float = event.x - lastTouchX
                    val deltaY: Float = event.y - lastTouchY

                    // Adjust the translation based on the movement
                    transformMatrix.postTranslate(deltaX, deltaY)

                    // Update the last touch coordinates
                    lastTouchX = event.x
                    lastTouchY = event.y

                    invalidate()
                }
            }
        }

        return true
    }

    private fun handleClickEvent(x: Float, y: Float) {
        val inverseMatrix = Matrix()
        transformMatrix.invert(inverseMatrix)

        val touchPoint = floatArrayOf(x, y)
        inverseMatrix.mapPoints(touchPoint)

        val clickedColumn = (touchPoint[0] / cellSize).toInt().coerceIn(0, columns - 1)
        val clickedRow = (touchPoint[1] / cellSize).toInt().coerceIn(0, rows - 1)

        // Update the color in the 2D array
        cellColors[clickedColumn][clickedRow] = Color.BLUE

        // Redraw the view to reflect the changes
        invalidate()
    }



    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        private var focusX = 0f
        private var focusY = 0f

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            focusX = detector?.focusX ?: 0f
            focusY = detector?.focusY ?: 0f
            return super.onScaleBegin(detector)
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            if(isMoving) {
                val scaleFactor = detector.scaleFactor
                zoomFactor *= scaleFactor
                zoomFactor = zoomFactor.coerceIn(1.0f, 3.0f) // Limit zoom level

                transformMatrix.setScale(zoomFactor, zoomFactor, focusX, focusY)

                invalidate()
                return true
            }
            return false
        }
    }
}
