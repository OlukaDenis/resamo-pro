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
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF


@Composable
fun PieGraph(
    modifier: Modifier = Modifier,
    entries: List<PieEntry>,
    dataLabel: String = "",
    pieColor: Color = DeepSeaBlue,
    backgroundColor: Color = Color.White,
    descriptionEnabled: Boolean = false,
    legendEnabled: Boolean = false
){
    AndroidView(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .height(250.dp),
        factory = { context ->
            val chart = PieChart(context)
            chart.setUsePercentValues(true)
            chart.description.isEnabled = false
            chart.setExtraOffsets(5f, 10f, 5f, 5f)
            chart.setDragDecelerationFrictionCoef(0.95f)
//            chart.setCenterText(generateCenterSpannableText())
            chart.isDrawHoleEnabled = true
            chart.setHoleColor(Color.White.toArgb())
            chart.setTransparentCircleColor(Color.White.toArgb())
            chart.setTransparentCircleAlpha(110)
//            chart.holeRadius = 50f
//            chart.transparentCircleRadius = 55f
            chart.setDrawCenterText(true)
            chart.setRotationAngle(1f)
            chart.isRotationEnabled = true
            chart.isHighlightPerTapEnabled = true

            // Create a dataset of entries
            val dataSet = PieDataSet(entries, dataLabel).apply {
                // Here we apply styling to the dataset
                color = pieColor.toArgb()
//                setDrawValues(drawValues)
                setDrawIcons(false)
                setSliceSpace(0f)
                setIconsOffset(MPPointF(0f, 40f))
                selectionShift = 5f

                // Set labels to be outside the slices
                xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
                yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

                val colors = ArrayList<Int>()
                for (c in ColorTemplate.MATERIAL_COLORS) colors.add(c)
                colors.add(ColorTemplate.getHoloBlue())
                setColors(colors)
            }

            // Pass the dataset to the chart
            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(10f)
            data.setValueTextColor(Color.White.toArgb())
            chart.data = data

            chart.description.isEnabled = descriptionEnabled
            chart.legend.isEnabled = legendEnabled
            chart.setEntryLabelColor(DeepSeaBlue.toArgb())
            chart.setEntryLabelTextSize(12f)

            // Enable touch gestures
            chart.setTouchEnabled(true)

            // undo all highlights
            chart.highlightValues(null)

            // Refresh and return the chart
            chart.invalidate()
            chart
        }
    )
}