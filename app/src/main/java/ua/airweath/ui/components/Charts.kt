package ua.airweath.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.rotate
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.min

@OptIn(ExperimentalTextApi::class)
@Composable
internal fun GaugeChart(
    modifier: Modifier = Modifier,
    percent: Int,
    value: String,
    subValue: String = "",
    elementColor: Color,
    animated: Boolean = true,
    brush: Brush
) {

    val progress = remember { Animatable(initialValue = 0f) }
    LaunchedEffect(percent) {
        progress.animateTo(
            targetValue = if(percent <= 100) percent.toFloat() else 100f,
            animationSpec = tween(if (animated) 1000 else 0)
        )
    }

    val density = LocalDensity.current
    val needleBaseSize = with(density) { 8.dp.toPx() }
    val strokeWidth = with(density) { 12.dp.toPx() }

    val textMeasurer = rememberTextMeasurer()
    val valueText = textMeasurer.measure(
        value,
        style = MaterialTheme.typography.h5.copy(textAlign = TextAlign.Center)
    )
    val subValueText = textMeasurer.measure(
        subValue,
        style = MaterialTheme.typography.subtitle1.copy(textAlign = TextAlign.Center)
    )

    val gaugeDegrees = 270
    val startAngle = 135f

    val needlePaint = remember(elementColor) { Paint().apply { color = elementColor } }

    Layout(
        content = { BoxWithConstraints(content = {
            BoxWithConstraints(contentAlignment = Alignment.Center) {

                val canvasSize = min(constraints.maxWidth, constraints.maxHeight)
                val size = Size(canvasSize.toFloat(), canvasSize.toFloat())
                val canvasSizeDp = with(density) { canvasSize.toDp() }
                val width = size.width
                val height = size.height
                val center = Offset(width / 2, height / 2)

                Canvas(
                    modifier = Modifier.size(canvasSizeDp).progressSemantics(),
                    onDraw = {
                        rotate(90f, center) {

                            drawArc(
                                brush = brush,
                                startAngle = startAngle-90f,
                                sweepAngle = gaugeDegrees.toFloat(),
                                useCenter = false,
                                size = size,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        }

                        drawIntoCanvas { canvas ->

                            canvas.save()

                            canvas.rotate(
                                degrees = progress.value.percentToAngle(max = gaugeDegrees)-45f,
                                pivotX = center.x,
                                pivotY = center.y
                            )

                            canvas.drawPath(
                                Path().apply {
                                    moveTo(center.x/3, center.x)
                                    lineTo(center.x/2, center.y + needleBaseSize)
                                    lineTo(20f, center.y)
                                    lineTo(center.x/2, center.y - needleBaseSize)
                                    close()
                                },
                                needlePaint
                            )

                            canvas.restore()

                        }
                        drawText(
                            valueText,
                            topLeft = Offset(center.x-valueText.size.width/2, center.y-valueText.size.height/2)
                        )

                        drawText(
                            subValueText,
                            topLeft = Offset(center.x-subValueText.size.width/2, height-subValueText.size.height)
                        )
                    }
                )
            }
        }) },
        modifier = modifier
    ) { measurables, constraints ->
        val placeable = measurables.firstOrNull()?.measure(constraints)
        layout(constraints.maxWidth, placeable?.height ?: 0) {
            placeable?.place(0, 0)
        }
    }

}

private fun Float.percentToAngle(max: Int): Float {
    return (this * max / 100)
}