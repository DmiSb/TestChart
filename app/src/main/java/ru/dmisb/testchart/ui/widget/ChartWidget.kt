package ru.dmisb.testchart.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
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
        private const val DEFAULT_ROW_HEIGHT = 32f
        private const val DEFAULT_FOOTER_HEIGHT = 24f
        private const val DEFAULT_MAX_COLUMNS = 6
    }

    private var rowHeight: Float = context.dpToPx(DEFAULT_ROW_HEIGHT)
    private var footerHeight : Float = context.dpToPx(DEFAULT_FOOTER_HEIGHT)
    private var maxColumns : Int = DEFAULT_MAX_COLUMNS

    private val displayWidth: Int = context.resources.displayMetrics.widthPixels
    private var columnWidth: Float = 0f
    private var gridLineWidth: Float = 0f

    private var prodLineNames: List<String> = listOf()
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
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.ChartWidget)
            rowHeight = a.getDimension(R.styleable.ChartWidget_rowHeight, context.dpToPx(DEFAULT_ROW_HEIGHT))
            footerHeight = a.getDimension(R.styleable.ChartWidget_footerHeight, context.dpToPx(DEFAULT_FOOTER_HEIGHT))
            maxColumns = a.getInt(R.styleable.ChartWidget_maxColumns, DEFAULT_MAX_COLUMNS)
            a.recycle()
        }

        gridLineWidth = context.dpToPx(1f)

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
            Log.d("TAG_APP", "ChartWidget OnTouchListener event=${event.actionMasked}")
            when (event.actionMasked)  {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_MOVE -> {
                    downX = 0f
                    downY = 0f
                }
                MotionEvent.ACTION_UP -> {
                    downX = event.x
                    downY = event.y
                    invalidate()
                }
            }
            true
        }
    }

    fun setProdLineNames(names: List<String>) {
        prodLineNames = names
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
            average.size <= maxColumns -> (displayWidth - paddingLeft - paddingRight).toFloat() / average.size
            else -> (displayWidth - paddingLeft).toFloat() / maxColumns
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var newWidth = if (columnWidth == 0f) displayWidth
        else (columnWidth * history.size).toInt() + paddingLeft + paddingRight

        if (newWidth < displayWidth) newWidth = displayWidth

        val newHeight = (rowHeight * (prodLineNames.size + 1)) +
                footerHeight +
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
            if (selectedCell != null)
                drawSelectedCell(it)
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
            val x3 = columnWidth * (index + 1) + paddingLeft

            val y1 = paddingTop.toFloat()
            val y2 = y1 + rowHeight

            if (downX != 0f && downX >= x1 && downX <= x3 && downY != 0f && downY >= y1 && downY <= y2) {
                selectedCell = SelectedCell(
                    prodLine = "",
                    time = data.first,
                    value = data.second,
                    startX = x1,
                    valueX = x2,
                    endX = x3,
                    startY = y1,
                    endY = y2
                )
            }

            canvas.drawRect(x1, y1, x2, y2, averagePaint)
            canvas.drawRect(x2, y1, x3, y2, defaultPaint)
        }
    }

    private fun drawHistory(canvas: Canvas) {
        prodLineNames.forEachIndexed { prodLineIndex, prodLineName ->
            history.firstOrNull { it.prodline_title == prodLineName }?.data?.let { prodLine ->
                average.forEachIndexed { timeIndex, data ->
                    prodLine.firstOrNull {
                        dateFormat?.format(it.time) == data.first
                    }?.let {
                        val x1 = columnWidth * timeIndex + paddingLeft
                        val x2 = x1 + (columnWidth * it.value).toFloat()
                        val x3 = columnWidth * (timeIndex + 1) + paddingLeft

                        val y1 = paddingTop.toFloat() + rowHeight * (prodLineIndex + 1)
                        val y2 = y1 + rowHeight

                        canvas.drawRect(x1, y1, x2, y2, valuePaint)
                        canvas.drawRect(x2, y1, x3, y2, defaultPaint)

                        if (downX != 0f && downX >= x1 && downX <= x3 && downY != 0f && downY >= y1 && downY <= y2) {
                            selectedCell = SelectedCell(
                                prodLine = prodLineName,
                                time = data.first,
                                value = it.value,
                                startX = x1,
                                valueX = x2,
                                endX = x3,
                                startY = y1,
                                endY = y2
                            )
                        }
                    }
                }
            }
        }
    }

    private fun drawGrid(canvas: Canvas) {
        var y = 0f
        (0 .. prodLineNames.size).forEach {
            y = rowHeight * (it + 1)
            canvas.drawLine(
                paddingLeft.toFloat(),
                y,
                (width - paddingRight).toFloat(),
                y,
                gridPaint
            )
        }

        canvas.drawLine(
            paddingLeft.toFloat(),
            y + footerHeight,
            (width - paddingRight).toFloat(),
            y + footerHeight,
            gridPaint
        )

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

    private fun drawSelectedCell(canvas: Canvas) {

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