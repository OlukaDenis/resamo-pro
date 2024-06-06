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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun FilledLineGraph(
    modifier: Modifier = Modifier,
    xData: List<Float>,
    yData: List<Float>,
    xLabels: Array<String>? =null,
    yLabels: Array<String>? =null,
    dataLabel: String = "",
    lineColor: Color = DeepSeaBlue,
    colorFill: Color = ElectricBlue,
    alphaFill: Int = 150,
    lineWidth: Double = 1.8,
    axisTextColor: Color = DeepSeaBlue,
    backgroundColor: Color = Color.White,
    highLightedColor: Color = DeepSeaBlue,
    showGrid: Boolean = false,
    drawValues: Boolean = false,
    drawMarkers: Boolean = false,
    drawFilled: Boolean = true,
    descriptionEnabled: Boolean = false,
    legendEnabled: Boolean = false,
    yAxisRightEnabled: Boolean = false,
    xAxisPosition: XAxis.XAxisPosition = XAxis.XAxisPosition.BOTTOM
){
    AndroidView(
        modifier = modifier.fillMaxSize().background(backgroundColor).height(200.dp),
        factory = { context ->
            val chart = LineChart(context)  // Initialise the chart
            val entries: List<Entry> = xData.zip(yData) { x, y -> Entry(x, y) }  // Convert the x and y data into entries

            // Create a dataset of entries
            val dataSet = LineDataSet(entries, dataLabel).apply {
                // Here we apply styling to the dataset
                color = lineColor.toArgb()
                setDrawValues(drawValues)
                setDrawCircles(drawMarkers)
                setDrawFilled(drawFilled)
                setLineWidth(lineWidth.toFloat())
                setDrawHorizontalHighlightIndicator(true)
                highLightColor = highLightedColor.toArgb()
                circleRadius = 4f
                fillColor = colorFill.toArgb()
                fillAlpha = alphaFill
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }

            // Pass the dataset to the chart
            chart.data = LineData(dataSet)

            //  Extra chart styling
            chart.description.isEnabled = descriptionEnabled
            chart.legend.isEnabled = legendEnabled
            chart.axisLeft.textColor = axisTextColor.toArgb()
            chart.axisRight.isEnabled = yAxisRightEnabled
            chart.xAxis.textColor = axisTextColor.toArgb()
            chart.xAxis.position = xAxisPosition
            chart.setDrawGridBackground(showGrid)

            // Enable touch gestures
            chart.setTouchEnabled(false)
            chart.isDragEnabled = true
            chart.isScaleXEnabled = true
            chart.isScaleYEnabled = true

            // toggle grid
            chart.axisRight.setDrawAxisLine(showGrid)
            chart.axisLeft.setDrawAxisLine(showGrid)
            chart.xAxis.setDrawAxisLine(showGrid)
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

            // Refresh and return the chart
            chart.invalidate()
            chart
        }
    )
}