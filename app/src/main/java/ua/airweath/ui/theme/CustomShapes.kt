package ua.airweath.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*

data class CustomShapes(
    /**0dp*/
    val zero: RoundedCornerShape = RoundedCornerShape(0.dp),
    /**10dp*/
    val small: RoundedCornerShape = RoundedCornerShape(10.dp),
    /**15dp*/
    val medium: RoundedCornerShape = RoundedCornerShape(15.dp),
    /**18dp*/
    val large: RoundedCornerShape = RoundedCornerShape(18.dp),
    /**50%*/
    val half: RoundedCornerShape = RoundedCornerShape(50)
)

val LocalShapes = compositionLocalOf { CustomShapes() }

val MaterialTheme.customShapes: CustomShapes
    @Composable
    @ReadOnlyComposable
    get() = LocalShapes.current

@Composable
fun Modifier.innerShadow(radius: Int = 10,
                         shadowColor : Float = .7f,
                         drawRoundRectX : Int = 15,
                         drawRoundRectY : Int = 15,
                         shadowRadius : Int = 15) = composed(
    inspectorInfo = {

    },
    factory = {

        val paint = remember() {
            Paint()
        }

        val foregroundPaint = remember() {
            Paint().apply {
                color = Color.Yellow
            }
        }

        val frameworkPaint = remember {
            paint.asFrameworkPaint()
        }

        Modifier.drawWithContent {
            this.drawIntoCanvas {
                val color = Color.LightGray

                val radiusPx = radius.dp.toPx()

                val shadowColorArgb = color
                    .copy(alpha = shadowColor)
                    .toArgb()
                val transparent = color
                    .copy(alpha = 0f)
                    .toArgb()

                frameworkPaint.color = transparent

                frameworkPaint.setShadowLayer(
                    radiusPx,
                    0f,
                    0f,
                    shadowColorArgb
                )
                val shadowRadiusPx = shadowRadius.dp.toPx()

                it.drawRoundRect(
                    left = 0f,
                    top = 0f,
                    right = this.size.width,
                    bottom = this.size.height,
                    radiusX = drawRoundRectX.dp.toPx(),
                    radiusY = drawRoundRectY.dp.toPx(),
                    paint = foregroundPaint
                )

                it.drawRoundRect(
                    left = 0f,
                    top = 0f,
                    right = this.size.width,
                    bottom = this.size.height,
                    radiusX = drawRoundRectX.dp.toPx(),
                    radiusY = drawRoundRectY.dp.toPx(),
                    paint = paint
                )

                it.drawRoundRect(
                    left = shadowRadiusPx,
                    top = shadowRadiusPx,
                    right = this.size.width - shadowRadiusPx,
                    bottom = this.size.height - shadowRadiusPx,
                    radiusX = drawRoundRectX.dp.toPx(),
                    radiusY = drawRoundRectY.dp.toPx(),
                    paint = foregroundPaint
                )
                drawContent()

            }
        }
    }
)



@Composable
fun Modifier.coloredShadow(
    color: Color,
    alpha: Float = 0.2f,
    borderRadius: Dp = 0.dp,
    shadowRadius: Dp = 20.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = composed {

    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparent = color.copy(alpha= 0f).toArgb()

    this.drawBehind {

        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            frameworkPaint.color = transparent

            frameworkPaint.setShadowLayer(
                shadowRadius.toPx(),
                offsetX.toPx(),
                offsetY.toPx(),
                shadowColor
            )
            it.drawRoundRect(
                0f,
                0f,
                this.size.width,
                this.size.height,
                borderRadius.toPx(),
                borderRadius.toPx(),
                paint
            )
        }
    }
}

@Composable
fun Modifier.customShadow(
    padding: Dp = 6.dp
) = composed {
    val density = LocalDensity.current
    Modifier
        .drawWithContent {
            val paddingPx = with(density) { padding.toPx()*2 }
            clipRect(
                left = -paddingPx,
                top = -paddingPx,
                right = size.width + paddingPx,
                bottom = size.height + paddingPx
            ) {
                this@drawWithContent.drawContent()
            }
        }
}

class MarkerShape(private val cornerRadius: Dp) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val cornerRadius = with(density){ cornerRadius.toPx() }
        val path = Path().apply {
            reset()
            arcTo(
                rect = Rect(
                    left = 0f,
                    top = 0f,
                    right = cornerRadius,
                    bottom = cornerRadius
                ),
                startAngleDegrees = 180.0f,
                sweepAngleDegrees = 90.0f,
                forceMoveTo = false
            )
            // Top right arc
            arcTo(
                rect = Rect(
                    left = size.width - cornerRadius,
                    top = 0f,
                    right = size.width,
                    bottom = cornerRadius
                ),
                startAngleDegrees = 270.0f,
                sweepAngleDegrees = 90.0f,
                forceMoveTo = false
            )
            // Bottom right arc
            arcTo(
                rect = Rect(
                    left = size.width - cornerRadius,
                    top = size.height - cornerRadius*1.5f,
                    right = size.width,
                    bottom = size.height - cornerRadius*.5f
                ),
                startAngleDegrees = 0.0f,
                sweepAngleDegrees = 90.0f,
                forceMoveTo = false
            )
            lineTo(size.width/2+cornerRadius/2, size.height - cornerRadius*.5f)
            lineTo(size.width/2, size.height)
            lineTo(size.width/2-cornerRadius/2, size.height - cornerRadius*.5f)
            // Bottom left arc
            arcTo(
                rect = Rect(
                    left = 0f,
                    top = size.height - cornerRadius*1.5f,
                    right = cornerRadius,
                    bottom = size.height - cornerRadius*.5f
                ),
                startAngleDegrees = 90.0f,
                sweepAngleDegrees = 90.0f,
                forceMoveTo = false
            )
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
fun drawerShape(width: Dp = 285.dp, rounded: Dp = 10.dp) =  object : Shape {
    val widthPx = with(LocalDensity.current) { width.toPx() }
    val roundedPx = with(LocalDensity.current) { rounded.toPx() }
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rounded(RoundRect(0f,0f, size.width /* width */, size.height /* height */, topRightCornerRadius = CornerRadius(roundedPx), bottomRightCornerRadius = CornerRadius(roundedPx)))
    }
}

@Preview
@Composable
private fun Prev(){

}