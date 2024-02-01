package net.kteam.pixelarthunter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View

class ZoomableCanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    companion object {
        private const val MIN_SCALE = 1.0f
        private const val MAX_SCALE = 3.0f
    }

    private var canvas: Canvas? = null
    private val paint: Paint = Paint()
    private val path: Path = Path()

    private var scale = 1.0f
    private var translateX = 0.0f
    private var translateY = 0.0f

    private val scaleGestureDetector: ScaleGestureDetector =
        ScaleGestureDetector(context, ScaleListener())
    private val gestureDetector: GestureDetector =
        GestureDetector(context, GestureListener())

    init {
        paint.color = Color.BLACK
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Initialize or update canvas dimensions based on the view's width and height
        canvas = Canvas(Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888))
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw your canvas content here
        canvas.save()
        canvas.scale(scale, scale)
        canvas.translate(translateX, translateY)
        this.canvas = canvas
        canvas.drawPath(path, paint)
        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)
        invalidate()
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scale *= detector.scaleFactor
            scale = scale.coerceIn(MIN_SCALE, MAX_SCALE)
            return true
        }
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            translateX -= distanceX / scale
            translateY -= distanceY / scale
            return true
        }
    }
}
