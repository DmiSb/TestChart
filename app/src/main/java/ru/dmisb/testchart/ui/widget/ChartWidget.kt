package ru.dmisb.testchart.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import ru.dmisb.testchart.R
import ru.dmisb.testchart.data.model.ChartData
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
    private var average: List<Pair<String, Double>> = listOf()
    private var dateFormat : SimpleDateFormat? = null

    private val backgroundPaint = Paint()
    private val averagePaint = Paint()
    private val defaultPaint = Paint()
    private val valuePaint = Paint()
    private val gridPaint = Paint()
    private val footerPaint = Paint()

    private var downX: Float = 0f
    private var downY: Float = 0f
    private var selectedCell: SelectedCell? = null

    init {
        gridLineWidth = context.dpToPx(1f)
        dataRowHeight = context.dpToPx(ROW_HEIGHT)

        backgroundPaint.color = context.color(R.color.chart_background)
        backgroundPaint.style = Paint.Style.FILL

        averagePaint.color = context.color(R.color.chart_average)
        averagePaint.style = Paint.Style.FILL

        defaultPaint.color = context.color(R.color.chart_default)
        defaultPaint.style = Paint.Style.FILL

        valuePaint.color = context.color(R.color.chart_value)
        valuePaint.style = Paint.Style.FILL

        gridPaint.color = context.color(R.color.chart_grid)
        gridPaint.style = Paint.Style.STROKE
        gridPaint.strokeWidth = gridLineWidth

        setOnTouchListener { _, event ->
            when (event.action and MotionEvent.ACTION_MASK)  {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.x
                    downY = event.y
                }
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                    invalidate()
                }
            }
            false
        }
    }

    fun setWorkLineNames(names: List<String>) {
        workLineNames = names
        measure(0, 0)
        invalidate()
    }

    fun setData(data: ChartData?) {
        TimeStep.values().firstOrNull { it.value == data?.timeStep.orEmpty() }?.let {
            dateFormat = SimpleDateFormat(it.formatPattern, Locale.getDefault())
        }

        history = data?.history.orEmpty()
        average = data?.data?.map {
            val time = dateFormat?.format(it.time).orEmpty()
            Pair(time, it.value)
        }.orEmpty()

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
            val x2 = x1 + (columnWidth * data.second).toFloat()
            val x3 = columnWidth * (index + 1) + paddingLeft - gridLineWidth

            val y1 = paddingTop.toFloat()
            val y2 = y1 + dataRowHeight - gridLineWidth

            canvas.drawRect(x1, y1, x2, y2, averagePaint)
            canvas.drawRect(x2, y1, x3, y2, defaultPaint)
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

    class SelectedCell(
        val prodLine: String,
        val time: String,
        val value: Double,
        val startX: Float,
        val endX: Float,
        val valueX: Float,
        val startY: Float,
        val endY: Float
    )
}