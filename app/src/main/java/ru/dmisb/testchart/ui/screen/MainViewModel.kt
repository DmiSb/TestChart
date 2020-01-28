package ru.dmisb.testchart.ui.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dmisb.testchart.data.MockData
import ru.dmisb.testchart.data.model.ChartData

class MainViewModel : ViewModel() {

    private val _data = MutableLiveData<ChartData>()
    val data : LiveData<ChartData> = _data

    init {
        _data.value = MockData.getDailyData10Hour()
    }
}