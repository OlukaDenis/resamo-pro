package com.dennytech.resamopro.ui.components.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.dennytech.resamopro.ui.theme.DeepSeaBlue
import com.dennytech.resamopro.ui.theme.ElectricBlue
import com.dennytech.resamopro.utils.formatters.CustomXAxisFormatter
import com.dennytech.resamopro.utils.formatters.CustomYAxisFormatter
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate

@Composable
fun BarGraph(
    modifier: Modifier = Modifier,
    xData: List<Float>,
    yData: List<Float>,
    xLabels: Array<String>? =null,
    dataLabel: String = "",
    barColor: Color = DeepSeaBlue,
    barWidth: Double = 0.3,
    axisTextColor: Color = DeepSeaBlue,
    backgroundColor: Color = Color.White,
    highLightedColor: Color = DeepSeaBlue,
    showGrid: Boolean = false,
    drawValues: Boolean = false,
    descriptionEnabled: Boolean = false,
    legendEnabled: Boolean = false,
    yAxisRightEnabled: Boolean = false,
    xAxisPosition: XAxis.XAxisPosition = XAxis.XAxisPosition.BOTTOM
){
    AndroidView(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .height(200.dp),
        factory = { context ->
            val chart = BarChart(context)  // Initialise the chart
            val entries: List<BarEntry> = xData.zip(yData) { x, y -> BarEntry(x, y) }  // Convert the x and y data into entries

            // Create a dataset of entries
            val dataSet = BarDataSet(entries, dataLabel).apply {
                // Here we apply styling to the dataset
                color = barColor.toArgb()
                setDrawValues(drawValues)
                highLightColor = highLightedColor.toArgb()

                val colors = ArrayList<Int>()
                for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
                colors.add(ColorTemplate.getHoloBlue())
                setColors(colors)

            }

            val sets = ArrayList<IBarDataSet>()
            sets.add(dataSet)

            // Pass the dataset to the chart
            val data = BarData(sets)
            data.setValueTextSize(10f)
            data.barWidth = barWidth.toFloat()
            chart.data = data
//            chart.renderer = RoundedBarChartRenderer(chart, chart.animator, chart.viewPortHandler)

            //  Extra chart styling
            chart.description.isEnabled = descriptionEnabled
            chart.legend.isEnabled = legendEnabled
            chart.axisLeft.textColor = axisTextColor.toArgb()
            chart.axisRight.isEnabled = yAxisRightEnabled
            chart.xAxis.textColor = axisTextColor.toArgb()
            chart.xAxis.position = xAxisPosition
            chart.setDrawGridBackground(showGrid)

            // Enable touch gestures
            chart.setTouchEnabled(true)
            chart.isDragEnabled = true
            chart.isScaleXEnabled = true
            chart.isScaleYEnabled = true

            // toggle grid
            chart.axisRight.setDrawAxisLine(showGrid)
            chart.axisLeft.setDrawAxisLine(true)
            chart.xAxis.setDrawAxisLine(true)

            chart.axisRight.setDrawGridLines(showGrid)
            chart.axisLeft.setDrawGridLines(showGrid)
            chart.xAxis.setDrawGridLines(showGrid)

            // Customize x axis
            if (xLabels != null) {
                val xAxis = chart.xAxis
                xAxis.valueFormatter = CustomXAxisFormatter(xLabels)
                xAxis.granularity = 1f // avoid label duplicating
                xAxis.isGranularityEnabled = true
            }

            // Customize y axis
            val leftAxis = chart.axisLeft
            val yAxisFormatter = CustomYAxisFormatter()
            leftAxis.valueFormatter = yAxisFormatter
            leftAxis.spaceTop = 15f
            leftAxis.setAxisMinimum(0f)

            // Refresh and return the chart
            chart.invalidate()
            chart
        }
    )
}