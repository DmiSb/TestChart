package ru.dmisb.testchart.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import ru.dmisb.testchart.R
import ru.dmisb.testchart.data.model.ChartData
import ru.dmisb.testchart.data.model.Data
import ru.dmisb.testchart.data.model.History
import ru.dmisb.testchart.data.model.TimeStep
import ru.dmisb.testchart.utils.color
import ru.dmisb.testchart.utils.dpToPx
import java.text.SimpleDateFormat
import java.util.*

class ChartWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val ROW_HEIGHT = 32f
        private const val FOOTER_HEIGHT = 24f
        private const val MAX_COLUMNS = 6
    }

    private val displayWidth: Int = context.resources.displayMetrics.widthPixels
    private var columnWidth: Float = 0f
    private var gridLineWidth: Float = 0f
    private var dataRowHeight: Float = 0f

    private var workLineNames: List<String> = listOf()
    private var history: List<History> = listOf()
    private var average: List<Data> = listOf()
    private var timeFormat : SimpleDateFormat? = null

    private val backgroundPaint = Paint()
    private val averagePaint = Paint()
    private val defaultPaint = Paint()
    private val valuePaint = Paint()
    private val gridPaint = Paint()
    private val footerPaint = Paint()

    init {
        gridLineWidth = context.dpToPx(1f)
        dataRowHeight = context.dpToPx(ROW_HEIGHT)

        backgroundPaint.color = context.color(R.color.chart_background)
        backgroundPaint.style = Paint.Style.FILL

        averagePaint.color = context.color(R.color.chart_average)
        averagePaint.style = Paint.Style.FILL

        defaultPaint.color = context.color(R.color.chart_default)
        defaultPaint.style = Paint.Style.FILL

        gridPaint.color = context.color(R.color.chart_grid)
        gridPaint.style = Paint.Style.STROKE
        gridPaint.strokeWidth = gridLineWidth
    }

    fun setWorkLineNames(names: List<String>) {
        workLineNames = names
        measure(0, 0)
        invalidate()
    }

    fun setData(data: ChartData?) {
        history = data?.history.orEmpty()
        average = data?.data.orEmpty()

        TimeStep.values().firstOrNull { it.value == data?.timeStep.orEmpty() }?.let {
            timeFormat = SimpleDateFormat(it.formatPattern, Locale.getDefault())
        }

        calcColumnWidth()

        measure(0, 0)
        invalidate()
    }

    private fun calcColumnWidth() {
        columnWidth = when {
            average.isEmpty() -> 0f
            average.size <= MAX_COLUMNS -> (displayWidth - paddingLeft - paddingRight).toFloat() / average.size
            else -> (displayWidth - paddingLeft).toFloat() / MAX_COLUMNS
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var newWidth = if (columnWidth == 0f) displayWidth
        else (columnWidth * history.size).toInt() + paddingLeft + paddingRight

        if (newWidth < displayWidth) newWidth = displayWidth

        val newHeight = (dataRowHeight * (workLineNames.size + 1)) +
                context.dpToPx(FOOTER_HEIGHT) +
                paddingTop + paddingBottom
        setMeasuredDimension(newWidth, newHeight.toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            drawBackground(it)
            drawAverage(it)
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

    private fun drawAverage(canvas: Canvas) {
        average.forEachIndexed { index, data ->
            val x1 = columnWidth * index + paddingLeft
            val y = paddingTop.toFloat()
            val x2 = x1 + (columnWidth * data.value).toFloat()
            val x3 = columnWidth * (index + 1) + paddingLeft - gridLineWidth
            canvas.drawRect(x1, y, x2, y + dataRowHeight, averagePaint)
            canvas.drawRect(x2, y, x3, y + dataRowHeight, defaultPaint)
        }
    }

    private fun drawHistory(canvas: Canvas) {

    }

    private fun drawGrid(canvas: Canvas) {
        var y = 0f
        (1 .. workLineNames.size + 1).forEach {
            y = dataRowHeight * it
            canvas.drawLine(
                paddingLeft.toFloat(),
                y,
                (width - paddingRight).toFloat(),
                y,
                gridPaint
            )
        }

        if (history.isNotEmpty()) {
            val data = history[0].data
            var x: Float
            (1 .. data.size).forEach {
                x = columnWidth * it + paddingLeft
                canvas.drawLine(x, paddingTop.toFloat(), x, y, gridPaint)
            }
        }
    }

    private fun drawFooter(canvas: Canvas) {

    }
}