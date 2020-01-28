package ru.dmisb.testchart.ui.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import ru.dmisb.testchart.R

class MainActivity : AppCompatActivity() {

    companion object {
        private val WORK_LINE_NAME = listOf("2.1", "2.2", "2.3", "2.4", "4.0", "4.1", "4.2", "5.1", "5.2", "5.3")
    }

    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chartView.setWorkLineNames(WORK_LINE_NAME)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.data.observe(this, Observer {
            chartView.setData(it)
        })
    }
}
