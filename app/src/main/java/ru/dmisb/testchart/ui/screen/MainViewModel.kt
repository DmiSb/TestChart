package ru.dmisb.testchart.ui.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dmisb.testchart.data.MockData
import ru.dmisb.testchart.data.model.ChartData
import ru.dmisb.testchart.ui.widget.ChartWidgetCell

class MainViewModel : ViewModel() {

    private val _data = MutableLiveData<ChartData>()
    val data : LiveData<ChartData> = _data

    private val _selectedCell = MutableLiveData<ChartWidgetCell>()
    val selectedCell : LiveData<ChartWidgetCell> = _selectedCell

    var period: Int = 0
        set(value) {
            field = value
            selectPeriod()
        }

    fun setSelectedCell(cell: ChartWidgetCell) {
        _selectedCell.value = cell
    }

    private fun selectPeriod() {
        _data.value = when (period) {
            1 -> MockData.getDailyData10Hour()
            2 -> MockData.getMonthly()
            else -> MockData.getDailyData5Hour()
        }
    }
}