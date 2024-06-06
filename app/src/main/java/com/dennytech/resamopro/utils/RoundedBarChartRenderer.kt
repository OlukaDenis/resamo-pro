package com.dennytech.resamopro.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class RoundedBarChartRenderer(
    chart: BarChart,
    animator: com.github.mikephil.charting.animation.ChartAnimator,
    viewPortHandler: ViewPortHandler
) : BarChartRenderer(chart, animator, viewPortHandler) {

    private val mBarShadowRectBuffer = RectF()

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val trans = mChart.getTransformer(dataSet.axisDependency)
        mBarBorderPaint.color = dataSet.barBorderColor
        mBarBorderPaint.strokeWidth = dataSet.barBorderWidth

        val drawBorder = dataSet.barBorderWidth > 0f

        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY

        if (mChart.isDrawBarShadowEnabled) {
            mShadowPaint.color = dataSet.barShadowColor

            val barData = mChart.barData
            val barWidth = barData.barWidth
            val barWidthHalf = barWidth / 2.0f

            var i = 0
            val count = Math.min(
                Math.ceil((dataSet.entryCount.toFloat() * phaseX).toDouble()).toInt(),
                dataSet.entryCount
            )
            while (i < count) {

                val e = dataSet.getEntryForIndex(i)

                mBarShadowRectBuffer.left = e.x - barWidthHalf
                mBarShadowRectBuffer.right = e.x + barWidthHalf

                trans.rectValueToPixel(mBarShadowRectBuffer)

                if (!mViewPortHandler.isInBoundsLeft(mBarShadowRectBuffer.right)) {
                    i++
                    continue
                }

                if (!mViewPortHandler.isInBoundsRight(mBarShadowRectBuffer.left)) break

                mBarShadowRectBuffer.top = mViewPortHandler.contentTop()
                mBarShadowRectBuffer.bottom = mViewPortHandler.contentBottom()

                c.drawRoundRect(mBarShadowRectBuffer, 20f, 20f, mShadowPaint)
                i++
            }
        }

        // initialize the buffer
        val buffer = mBarBuffers[index]
        buffer.setPhases(phaseX, phaseY)
        buffer.setDataSet(index)
        buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
        buffer.setBarWidth(mChart.barData.barWidth)

        buffer.feed(dataSet)

        trans.pointValuesToPixel(buffer.buffer)

        val isSingleColor = dataSet.colors.size == 1

        if (isSingleColor) {
            mRenderPaint.color = dataSet.color
        }

        var j = 0
        while (j < buffer.size()) {

            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) {
                j += 4
                continue
            }

            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j])) break

            if (!isSingleColor) {
                mRenderPaint.color = dataSet.getColor(j / 4)
            }

            val left = buffer.buffer[j]
            val top = buffer.buffer[j + 1]
            val right = buffer.buffer[j + 2]
            val bottom = buffer.buffer[j + 3]

            c.drawRoundRect(left, top, right, bottom, 20f, 20f, mRenderPaint)

            if (drawBorder) {
                c.drawRoundRect(left, top, right, bottom, 20f, 20f, mBarBorderPaint)
            }
            j += 4
        }
    }
}
