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
import com.dennytech.resamopro.ui.theme.TruliOrange
import com.dennytech.resamopro.utils.formatters.CustomXAxisFormatter
import com.dennytech.resamopro.utils.formatters.CustomYAxisFormatter
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry


@Composable
fun CombinedBarGraph(
    modifier: Modifier = Modifier,
    firstValues: List<Float>,
    secondValues: List<Float>,
    xData: List<Float>,
    xLabels: Array<String>,
    barOneColor: Color = DeepSeaBlue,
    barTwoColor: Color = TruliOrange,
    barWidth: Double = 0.1,
    axisTextColor: Color = DeepSeaBlue,
    backgroundColor: Color = Color.White,
    highLightedColor: Color = DeepSeaBlue,
    showGrid: Boolean = false,
    drawValues: Boolean = false,
    descriptionEnabled: Boolean = false,
    legendEnabled: Boolean = false,
    graphOneLabel: String = "",
    graphTwoLabel: String = "",
    yAxisRightEnabled: Boolean = false,
    xAxisPosition: XAxis.XAxisPosition = XAxis.XAxisPosition.BOTTOM
){
    AndroidView(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .height(200.dp),
        factory = { context ->
            val chart = BarChart(context)

            val entriesOne: List<BarEntry> = xData.zip(firstValues) { x, y -> BarEntry(x, y) }  // Convert the x and y data into entries
            val entriesTwo: List<BarEntry> = xData.zip(secondValues) { x, y -> BarEntry(x, y) }  // Convert the x and y data into entries

            val dataSetOne = BarDataSet(entriesOne, graphOneLabel).apply {
                color = barOneColor.toArgb()
                setDrawValues(drawValues)
                highLightColor = highLightedColor.toArgb()
            }

            val dataSetTwo = BarDataSet(entriesTwo, graphTwoLabel).apply {
                color = barTwoColor.toArgb()
                setDrawValues(drawValues)
                highLightColor = highLightedColor.toArgb()
            }

            // Pass the dataset to the chart
            val data = BarData(dataSetOne, dataSetTwo)
            data.setValueTextSize(10f)
            data.barWidth = barWidth.toFloat()
            chart.data = data
            chart.groupBars(0f, 0.4f, 0.1f)

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

            // Set legend position to top
            val legend: Legend = chart.legend
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT

            // Customize x axis
            val xAxis = chart.xAxis
            xAxis.valueFormatter = CustomXAxisFormatter(xLabels)
            xAxis.granularity = 1f // avoid label duplicating
            xAxis.isGranularityEnabled = true

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