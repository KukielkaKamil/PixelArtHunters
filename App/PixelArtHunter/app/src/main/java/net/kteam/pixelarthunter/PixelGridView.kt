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

class PixelGridView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private companion object {
        private const val DEFAULT_SIZE = 10
    }

    private var size = DEFAULT_SIZE

    private var cellSize: Float = 0f
    private var totalWidth: Float = 0f
    private var totalHeight: Float = 0f

    private var zoomFactor: Float = 1f
    private val transformMatrix = Matrix()
    private val scaleGestureDetector: ScaleGestureDetector

    private val canvasPaint: Paint = Paint()
    private val paint: Paint = Paint()

    private var lastTouchX: Float = 0f
    private var lastTouchY: Float = 0f

    private var isMoving: Boolean = false
    private var isDrawing: Boolean = true
    var cost = 0
    private var cellTracker: Array<Array<Boolean>> = Array(size) { Array(size) { false } }
    var cellColors: Array<Array<Int>> = Array(size) { Array(size) { Color.WHITE } }

    init {
        canvasPaint.color = Color.BLACK
        canvasPaint.style = Paint.Style.FILL

        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
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

    fun setGridSize(size: Int) {
        this.size = size
        cellColors = Array(size) { Array(size) { Color.WHITE } }
        cellTracker= Array(size) { Array(size) { false } }
        calculateCellSize()
//        this.layoutParams.height = this.columns
        invalidate()
    }

    private fun calculateCellSize() {
        cellSize = kotlin.math.min(width / size.toFloat(), height / size.toFloat())
        totalWidth = size * cellSize
        totalHeight = size * cellSize
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateCellSize()
    }

    fun setDrawingOn(value: Boolean){
        isDrawing = value
        isMoving = !value
    }

    fun setColor(color: String){
        paint.color= Color.parseColor(color)
    }

    fun resetView(){
        zoomFactor = 1.0f
        transformMatrix.reset()
        invalidate()
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()
        canvas.concat(transformMatrix)

        // Draw background
        canvas.drawColor(Color.TRANSPARENT)


        // Draw colored cells based on the 2D array
        for (i in 0 until size) {
            for (j in 0 until size) {
                val left = i * cellSize
                val top = j * cellSize
                val right = left + cellSize
                val bottom = top + cellSize

                canvasPaint.color = cellColors[i][j]

                canvas.drawRect(left, top, right, bottom, canvasPaint)
            }
        }

        // Draw grid lines
        canvasPaint.color = Color.GRAY
        canvasPaint.strokeWidth = 2f / zoomFactor

        // Draw vertical lines
        for (i in 0..size) {
            val x = i * cellSize
            canvas.drawLine(x, 0f, x, totalHeight, canvasPaint)
        }

        // Draw horizontal lines
        for (j in 0..size) {
            val y = j * cellSize
            canvas.drawLine(0f, y, totalWidth, y, canvasPaint)
        }

        // Restore original paint settings
        canvasPaint.color = Color.BLACK
        canvasPaint.strokeWidth = 0f

        canvas.restore()
    }


    @SuppressLint("ClickableViewAccessibility")
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
                if (zoomFactor > 1.0f && isMoving) {
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

        val clickedColumn = (touchPoint[0] / cellSize).toInt().coerceIn(0, size - 1)
        val clickedRow = (touchPoint[1] / cellSize).toInt().coerceIn(0, size - 1)

        // Update the color in the 2D array
        cellColors[clickedColumn][clickedRow] = paint.color

        if (!cellTracker[clickedColumn][clickedRow]){
            cellTracker[clickedColumn][clickedRow] = true
            cost+=1
        }

        // Redraw the view to reflect the changes
        invalidate()
    }



    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        private var focusX = 0f
        private var focusY = 0f

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            focusX = detector.focusX
            focusY = detector.focusY
            return super.onScaleBegin(detector)
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            if(isMoving) {
                val scaleFactor = detector.scaleFactor
                zoomFactor *= scaleFactor
                zoomFactor = zoomFactor.coerceIn(1.0f, 3.0f) // Limit zoom level

                transformMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY)

                invalidate()
                return true
            }
            return false
        }
    }
}
