package ru.dmisb.testchart.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import ru.dmisb.testchart.R
import ru.dmisb.testchart.data.model.ChartData
import ru.dmisb.testchart.data.model.History
import ru.dmisb.testchart.utils.color
import ru.dmisb.testchart.utils.dpToPx

class ChartWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val ROW_HEIGHT = 32
        private const val ROW_COUNT = 11
        private const val FOOTER_HEIGHT = 24
    }

    private val displayWidth: Int = context.resources.displayMetrics.widthPixels - paddingLeft - paddingRight

    private var history: List<History> = listOf()
    private var columnWidth: Float = 0f

    private val backgroundPaint = Paint()
    private val valuePaint = Paint()
    private val defaultPaint = Paint()
    private val gridPaint = Paint()
    private val footerPaint = Paint()

    private var lineStrokeWidth: Float = 0f

    init {
        lineStrokeWidth = context.dpToPx(1).toFloat()

        backgroundPaint.color = context.color(R.color.chart_background)
        backgroundPaint.style = Paint.Style.FILL
    }

    fun setData(data: ChartData?) {
        history = data?.history.orEmpty()
        calcColumnWidth()
        invalidate()
    }

    private fun calcColumnWidth() {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var newWidth = if (history.isEmpty()) displayWidth
        else (columnWidth * history.size).toInt()

        if (newWidth < displayWidth) newWidth = displayWidth

        val newHeight = context.dpToPx(ROW_HEIGHT) * ROW_COUNT + context.dpToPx(FOOTER_HEIGHT)
        setMeasuredDimension(newWidth, newHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            drawBackground(it)
            drawHistory(it)
            drawGrid(it)
            drawFooter(it)
        }
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawRect(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            (width - paddingRight).toFloat(),
            (height - paddingRight).toFloat(),
            backgroundPaint
        )
    }

    private fun drawHistory(canvas: Canvas) {

    }

    private fun drawGrid(canvas: Canvas) {

    }

    private fun drawFooter(canvas: Canvas) {

    }
}