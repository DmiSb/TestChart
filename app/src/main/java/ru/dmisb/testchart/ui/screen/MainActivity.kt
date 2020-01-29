package ru.dmisb.testchart.ui.screen

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import ru.dmisb.testchart.R
import ru.dmisb.testchart.ui.widget.ChartWidgetCell
import ru.dmisb.testchart.ui.widget.ChartWidgetListener

class MainActivity : AppCompatActivity() {

    companion object {
        private val PROD_LINE_NAME = listOf("2.1", "2.2", "2.3", "2.4", "4.0", "4.1", "4.2", "5.1", "5.2", "5.3")
    }

    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        observeViewModel()

        chartView.setProdLineNames(PROD_LINE_NAME)
        chartView.setListener(chartCellSelectListener)

        fillChartTitles()

        periodView.adapter = ArrayAdapter<String>(
            periodView.context,
            R.layout.item_spinner,
            R.id.spinnerTextView,
            resources.getStringArray(R.array.chert_period)
        )
        periodView.setSelection(viewModel.period)
        periodView.onItemSelectedListener = periodListener
    }

    private val chartCellSelectListener = object : ChartWidgetListener {
        override fun onSelect(cell: ChartWidgetCell) {
            viewModel.setSelectedCell(cell)
        }
    }

    private fun fillChartTitles() {
        PROD_LINE_NAME.forEach {
            val view = layoutInflater.inflate(R.layout.item_prod_line, null) as TextView
            view.text = it
            chartTitles.addView(view)
            val lp = view.layoutParams
            lp.height = resources.getDimensionPixelOffset(R.dimen.chart_row_height)
            view.layoutParams = lp
        }
    }

    private val periodListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            viewModel.period = position
        }
    }

    private fun observeViewModel() {
        viewModel.data.observe(this, Observer {
            chartView.setData(it)

            val lp = chartView.layoutParams as FrameLayout.LayoutParams
            lp.width = FrameLayout.LayoutParams.WRAP_CONTENT
            chartView.layoutParams = lp
        })

        viewModel.selectedCell.observe(this, Observer {
            prodLineView.text = if (it?.prodLine?.isEmpty() == true) getString(R.string.chart_widget_average)
            else getString(R.string.chart_prod_line, it?.prodLine.orEmpty())
            timeView.text = getString(R.string.chart_time, it?.time.orEmpty())
            valueView.text = getString(R.string.chart_value, it?.value?.toString() ?: "")
        })
    }
}
