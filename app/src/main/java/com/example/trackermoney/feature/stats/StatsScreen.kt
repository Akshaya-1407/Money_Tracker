package com.example.trackermoney.feature.stats

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.trackermoney.R
import com.example.trackermoney.Utils
import com.example.trackermoney.feature.home.TransactionList
import com.example.trackermoney.ui.theme.Zinc
import com.example.trackermoney.viewmodel.StatsViewModel
import com.example.trackermoney.viewmodel.StatsViewModelFactory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter

@Composable
fun StatsScreen(navController: NavController){
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
                    )
            {
                Image(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    colorFilter = ColorFilter.tint(Color.Black),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                Text(
                    text = "Statistics",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Zinc,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
                Image(
                    imageVector = Icons.Default.MoreVert,
                    colorFilter = ColorFilter.tint(Color.Black),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
    ) {
        val viewModel =
            StatsViewModelFactory(navController.context).create(StatsViewModel::class.java)
        val dataState = viewModel.entries.collectAsState(emptyList())
        val topExpenses = viewModel.topExpenses.collectAsState(emptyList())
        Column(modifier = Modifier.padding(it)) {
            val entries = viewModel.getEntriesForChart(dataState.value)
            Log.e("LineChart", "Entries: $entries")
            LineChart(entries = entries)
            Spacer(modifier = Modifier.height(16.dp))
            TransactionList(Modifier, list = topExpenses.value,"Top Spending")
        }
    }
}

@SuppressLint("InflateParams")
@Composable
fun LineChart(entries: List<Entry>){
    val context = LocalContext.current
    AndroidView(factory = {
        val view = LayoutInflater.from(context).inflate(R.layout.stats_line_chart,null)
        view
    }, modifier = Modifier.fillMaxWidth().height(250.dp) ){view->
        val lineChart = view.findViewById<LineChart>(R.id.LineChart)
        val dataSet = LineDataSet(entries,"Expenses").apply {
//            color = android.graphics.Color.parseColor("#FF2F7E79")
//            valueTextColor = android.graphics.Color.BLACK
//            lineWidth = 2f
//            axisDependency = YAxis.AxisDependency.RIGHT
//            setDrawFilled(true)
//            mode = LineDataSet.Mode.CUBIC_BEZIER
//            valueTextSize = 10f
//            valueTextColor = android.graphics.Color.parseColor("#FF2F7E79")
            color = android.graphics.Color.parseColor("#FF2F7E79")
            lineWidth = 2f
            valueTextSize = 10f
            valueTextColor = android.graphics.Color.BLACK
            setDrawValues(true)
            setDrawCircles(true)
            circleRadius = 4f
            setDrawCircleHole(false)
            setDrawFilled(true)
            fillColor = android.graphics.Color.parseColor("#FF2F7E79")
            mode = LineDataSet.Mode.CUBIC_BEZIER
            val drawable = context.getDrawable(R.drawable.chart_gradient)
            drawable?.let {
                fillDrawable = it
            }
        }

        lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            setDrawAxisLine(false)
            granularity = 1f
            valueFormatter = object : ValueFormatter(){
                override fun getFormattedValue(value: Float): String {
                    return Utils.formatDateForChart(value.toLong())
                }
            }
        }

        lineChart.axisLeft.apply {
            setDrawGridLines(false)
            setDrawAxisLine(false)
            isEnabled = false
        }

        lineChart.axisRight.apply {
            setDrawGridLines(false)
            setDrawAxisLine(false)
            isEnabled = false
        }

        lineChart.data = LineData(dataSet)
        lineChart.setTouchEnabled(true)      // Enable touch gestures
        lineChart.setPinchZoom(true)         // Allow pinch zoom
        lineChart.description.isEnabled = false // Remove default description
        lineChart.invalidate()
    }
}



