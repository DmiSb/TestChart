package ru.dmisb.testchart.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
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
    private var gridLineWidth: Float = context.dpToPx(1f)
    private var shadowWidth: Float = context.dpToPx(8f)
    private var cellWidth: Float = 0f

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

    private var shadow : Drawable? = null

    private var downX: Float = 0f
    private var downY: Float = 0f
    private var selectedCell: SelectedCell? = null

    private var listener: ChartWidgetListener? = null

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.ChartWidget)
            rowHeight = a.getDimension(R.styleable.ChartWidget_rowHeight, context.dpToPx(DEFAULT_ROW_HEIGHT))
            footerHeight = a.getDimension(R.styleable.ChartWidget_footerHeight, context.dpToPx(DEFAULT_FOOTER_HEIGHT))
            maxColumns = a.getInt(R.styleable.ChartWidget_maxColumns, DEFAULT_MAX_COLUMNS)
            a.recycle()
        }

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

        footerPaint.color = context.color(R.color.chart_footer_text)
        footerPaint.isAntiAlias = true
        footerPaint.textSize = resources.getDimensionPixelSize(R.dimen.chart_footer_text_size).toFloat()
        footerPaint.style = Paint.Style.FILL

        shadow = ContextCompat.getDrawable(context, android.R.drawable.dialog_holo_light_frame)

        setOnTouchListener { _, event ->
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
        downX = 0f
        downY = 0f
        selectedCell = null

        TimeStep.values().firstOrNull { it.value == data?.timeStep.orEmpty() }?.let {
            dateFormat = SimpleDateFormat(it.formatPattern, Locale.getDefault())
        }

        history = data?.history.orEmpty()
        average = data?.data?.map {
            val time = dateFormat?.format(it.time).orEmpty()
            Pair(time, it.value)
        }.orEmpty()

        calcCellWidth()
        calcSize()
        invalidate()
    }

    fun setListener(listener: ChartWidgetListener?) {
        this.listener = listener
    }

    private fun calcCellWidth() {
        // Расчет ширины ячейки
        cellWidth = when {
            average.isEmpty() -> 0f
            average.size <= maxColumns -> (displayWidth - paddingLeft - paddingRight).toFloat() / average.size
            else -> (displayWidth - paddingLeft).toFloat() / maxColumns
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        calcSize()
    }

    private fun calcSize() {
        // Расчет размеров ChartView
        var newWidth = if (cellWidth == 0f) displayWidth
        else (cellWidth * average.size).toInt() + paddingLeft + paddingRight

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
        // Заполнение серым цветом всего пространства
        canvas.drawRect(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            (width - paddingRight).toFloat(),
            (height - paddingRight).toFloat(),
            backgroundPaint
        )
    }

    private fun drawAverage(canvas: Canvas) {
        // Отрисовка средних значений
        average.forEachIndexed { index, data ->
            val x1 = cellWidth * index + paddingLeft
            val x2 = x1 + (cellWidth * data.second).toFloat()
            val x3 = cellWidth * (index + 1) + paddingLeft

            val y1 = paddingTop.toFloat()
            val y2 = y1 + rowHeight

            // Если точка нажатия попадает в ячейку запоминаем как выделенную
            if (downX != 0f && downX >= x1 && downX <= x3 && downY != 0f && downY >= y1 && downY <= y2) {
                val cell = SelectedCell(
                    prodLine = "",
                    time = data.first,
                    value = data.second,
                    startX = x1,
                    valueX = x2,
                    endX = x3,
                    startY = y1,
                    endY = y2
                )

                if (selectedCell == null || cell != selectedCell) {
                    selectedCell = cell
                    listener?.onSelect(
                        ChartWidgetCell("", data.first, data.second)
                    )
                }
            }

            canvas.drawRect(x1, y1, x2, y2, averagePaint)
            canvas.drawRect(x2, y1, x3, y2, defaultPaint)
        }
    }

    private fun drawHistory(canvas: Canvas) {
        // Отрисовка данных
        prodLineNames.forEachIndexed { prodLineIndex, prodLineName ->
            history.firstOrNull { it.prodline_title == prodLineName }?.data?.let { prodLine ->
                average.forEachIndexed { timeIndex, data ->
                    prodLine.firstOrNull {
                        dateFormat?.format(it.time) == data.first
                    }?.let {
                        val x1 = cellWidth * timeIndex + paddingLeft              //
                        val x2 = x1 + (cellWidth * it.value).toFloat()
                        val x3 = cellWidth * (timeIndex + 1) + paddingLeft

                        val y1 = paddingTop.toFloat() + rowHeight * (prodLineIndex + 1)
                        val y2 = y1 + rowHeight

                        canvas.drawRect(x1, y1, x2, y2, valuePaint)
                        canvas.drawRect(x2, y1, x3, y2, defaultPaint)

                        // Если точка нажатия попадает в ячейку запоминаем как выделенную
                        if (downX != 0f && downX >= x1 && downX <= x3 && downY != 0f && downY >= y1 && downY <= y2) {
                            val cell = SelectedCell(
                                prodLine = prodLineName,
                                time = data.first,
                                value = it.value,
                                startX = x1,
                                valueX = x2,
                                endX = x3,
                                startY = y1,
                                endY = y2
                            )

                            if (selectedCell == null || cell != selectedCell) {
                                selectedCell = cell
                                listener?.onSelect(
                                    ChartWidgetCell(prodLineName, data.first, it.value)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun drawGrid(canvas: Canvas) {
        var y = 0f

        // Отрисовка горизонтальных линий
        (0 .. prodLineNames.size).forEach {
            // приращения по высоте для каждой производственной линии
            y = rowHeight * (it + 1)
            canvas.drawLine(
                paddingLeft.toFloat(),
                y,
                (width - paddingRight).toFloat(),
                y,
                gridPaint
            )
        }
        // отрисовка нижней линии с отступом на высоту footer
        canvas.drawLine(
            paddingLeft.toFloat(),
            y + footerHeight,
            (width - paddingRight).toFloat(),
            y + footerHeight,
            gridPaint
        )
        // отрисовка вертикальных линий
        if (history.isNotEmpty()) {
            val data = history[0].data
            var x: Float
            (1 .. data.size).forEach {
                // приращение по ширине на ширину ячейки
                x = cellWidth * it + paddingLeft
                canvas.drawLine(x, paddingTop.toFloat(), x, y, gridPaint)
            }
        }
    }

    private fun drawFooter(canvas: Canvas) {
        average.forEachIndexed { timeIndex, data ->
            val textBounds = Rect()
            footerPaint.getTextBounds(data.first, 0, data.first.length, textBounds)

            val x1 = cellWidth * timeIndex +                                  // начало ячейки по индексу
                    paddingLeft +                                               // отступ слева
                    (cellWidth - (textBounds.right - textBounds.left)) / 2    // половина оставшегося расстояния в ячейке по ширине

            val y1 = paddingTop.toFloat() +                                     // отступ сверху
                    rowHeight * (prodLineNames.size + 1) +                      // верх ячейки
                    (footerHeight + (textBounds.bottom - textBounds.top)) / 2   // половина оставшегося расстояния в ячейке по высоте

            canvas.drawText(
                data.first,
                x1,
                y1,
                footerPaint
            )
        }
    }

    private fun drawSelectedCell(canvas: Canvas) {
        // Отрисовка выделенной ячейки
        selectedCell?.apply {
            // Заливка ячейки фоном с тенью
            shadow?.setBounds(
                (this.startX - shadowWidth).toInt(),
                (this.startY - shadowWidth).toInt(),
                (this.endX + shadowWidth).toInt(),
                (this.endY + shadowWidth).toInt()
            )
            shadow?.draw(canvas)
            // Отрисовка значения
            canvas.drawRect(
                this.startX,
                this.startY,
                this.valueX,
                this.endY,
                if (this.prodLine.isEmpty()) averagePaint else valuePaint
            )
            // и оставшегося места
            canvas.drawRect(
                this.valueX,
                this.startY,
                this.endX,
                this.endY,
                defaultPaint
            )
        }
    }

    data class SelectedCell(
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