import android.graphics.PointF
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun CustomComponent(
    canvasSize: Dp = 300.dp,
    indicatorValue: Int = 0,
    maxIndicatorValue: Int = 160,
    backgroundIndicatorColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    backgroundIndicatorStrokeWidth: Float = 100f,
    foregroundIndicatorColor: Color = Color(0x993BDE8D),
    foregroundIndicatorStrokeWidth: Float = 100f,
//    indicatorStrokeCap: StrokeCap = StrokeCap.Round,
    bigTextFontSize: TextUnit = MaterialTheme.typography.headlineLarge.fontSize,
    bigTextColor: Color = MaterialTheme.colorScheme.onSurface,
    bigTextSuffix: String = "GB",
    smallText: String = "Remaining",
    smallTextFontSize: TextUnit = MaterialTheme.typography.headlineSmall.fontSize,
    smallTextColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
) {
    var allowedIndicatorValue by remember {
        mutableStateOf(maxIndicatorValue)
    }
    allowedIndicatorValue = if (indicatorValue <= maxIndicatorValue) {
        indicatorValue
    } else {
        maxIndicatorValue
    }

    var animatedIndicatorValue by remember { mutableStateOf(0f) }
    LaunchedEffect(key1 = allowedIndicatorValue) {
        animatedIndicatorValue = allowedIndicatorValue.toFloat()
    }

    val percentage =
        (animatedIndicatorValue / maxIndicatorValue) * 100

    val sweepAngle by animateFloatAsState(
        targetValue = (2.4 * percentage).toFloat(),
        animationSpec = tween(1000)
    )

    val receivedValue by animateIntAsState(
        targetValue = allowedIndicatorValue,
        animationSpec = tween(1000)
    )

    val animatedBigTextColor by animateColorAsState(
        targetValue = if (allowedIndicatorValue == 0)
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        else
            bigTextColor,
        animationSpec = tween(1000)
    )

    Column(
        modifier = Modifier
            .size(canvasSize)
            .background(color = Color(0xff0a0f23))
            .padding(4.dp)
            .drawBehind {
                val componentSize = size / 1.25f

                foregroundIndicator(
                    sweepAngle = sweepAngle,
                    componentSize = componentSize,
                    indicatorColor = foregroundIndicatorColor,
                    indicatorStrokeWidth = foregroundIndicatorStrokeWidth,
                )

                backgroundIndicator(
                    componentSize = componentSize,
                    indicatorColor = backgroundIndicatorColor,
                    indicatorStrokeWidth = backgroundIndicatorStrokeWidth,
                    receivedValue = receivedValue
                )

                lineIndicator(
                    sweepAngle = sweepAngle,
                    componentSize = componentSize,
                    indicatorColor = foregroundIndicatorColor,
                    indicatorStrokeWidth = foregroundIndicatorStrokeWidth,
                )
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmbeddedElements(
            bigText = receivedValue,
            bigTextFontSize = bigTextFontSize,
            bigTextColor = animatedBigTextColor,
            bigTextSuffix = bigTextSuffix,
            smallText = smallText,
            smallTextColor = smallTextColor,
            smallTextFontSize = smallTextFontSize
        )
    }
}

fun DrawScope.backgroundIndicator(
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
    receivedValue: Int,
) {
    val size2 = size * 0.92f

    val size3 = size * 0.7f
    drawArc(
        size = size3,
        color = Color(0xff5ce3a0),
        startAngle = 141f,
        sweepAngle = 259f,
        useCenter = false,
        style = Stroke(
            width = 1.03.dp.toPx(),
            cap = StrokeCap.Square
        ),
        topLeft = Offset(
            x = (size3.width * 0.22f),
            y = (size3.height * 0.22f)
        )
    )

    val size4 = size * 0.665f
    drawArc(
        size = size4,
        color = Color(0xff0c2c1c),
        startAngle = 142f,
        sweepAngle = 256f,
        useCenter = false,
        style = Stroke(
            width = 8.dp.toPx(),
            cap = StrokeCap.Square
        ),
        topLeft = Offset(
            x = (size4.width * 0.258f),
            y = (size4.height * 0.258f)
        )
    )

    val centerCirclePoint = PointF(size.width / 2f, size.height / 2f)

    val startAngle = 150f
    val totalSweepAngle = 240f
    val segments = 16
    val sweepAnglePerSegment = totalSweepAngle / segments

    val radius = size.height / 2
    val radius2 = (size2.height + 13) / 2
    val radius3 = (size3.height) / 2

    for (i in 0 until segments) {
        val segmentStartAngle = startAngle + i * sweepAnglePerSegment
        val segmentEndAngle = segmentStartAngle + sweepAnglePerSegment

        val startRad = Math.toRadians(segmentStartAngle.toDouble())
        val endRad = Math.toRadians(segmentEndAngle.toDouble())

        // Tính điểm bắt đầu của bán kính đường tròn thứ 1 từ ngoài vào
        val startX = center.x + radius * cos(startRad).toFloat()
        val startY = center.y + radius * sin(startRad).toFloat()

        val endX = center.x + radius * cos(endRad).toFloat()
        val endY = center.y + radius * sin(endRad).toFloat()

        // Tính điểm bắt đầu của bán kính đường tròn thứ 2 từ ngoài vào
        val startX2 = center.x + radius2 * cos(startRad).toFloat()
        val startY2 = center.y + radius2 * sin(startRad).toFloat()

        val endX2 = center.x + radius2 * cos(endRad).toFloat()
        val endY2 = center.y + radius2 * sin(endRad).toFloat()

        // Vẽ đường Line tại điểm bắt đầu của mỗi khoảng
        drawLine(
            color = if (i % 2 == 0) {
                Color(0xff63d6d6)
            } else {
                Color.Red
            },
            start = Offset(x = startX2, y = startY2),
            end = Offset(x = startX, y = startY),
            strokeWidth = 6.5f
        )

        // Vẽ đường Line tại điểm kết thúc của mỗi khoảng
        drawLine(
            color = Color(0xff63d6d6),
            start = Offset(x = endX2, y = endY2),
            end = Offset(x = endX, y = endY),
            strokeWidth = 6.5f
        )

    }

    val textValues = listOf("0", "20", "40", "60", "80", "100", "120", "140", "160", "180", "200")
    val textPaint = android.graphics.Paint().apply {
        color = Color(0xFFA2A5B1).toArgb()
        textSize = 14.dp.toPx() // Chuyển đổi từ dp sang px
        textAlign = android.graphics.Paint.Align.CENTER
        isFakeBoldText = true
    }
    val textSegment = 8
    val sweepAnglePerSegment2 = totalSweepAngle / textSegment
    for (i in 0 until 9) {
        val segmentStartAngle = startAngle + i * sweepAnglePerSegment2
        val startRad = Math.toRadians(segmentStartAngle.toDouble())

        // Tính toán vị trí của text
        val textRadius = radius3 * 1.15f // Khoảng cách từ tâm ra vị trí đặt text
        val textX = center.x + textRadius * cos(startRad).toFloat()
        val textY = center.y + textRadius * sin(startRad).toFloat() + 5

        // Vẽ text
        drawContext.canvas.nativeCanvas.drawText(
            textValues[i],
            textX,
            textY,
            textPaint.apply {
                if (receivedValue > textValues[i].toInt()) {
                    color = Color.White.toArgb()
                }
            }
        )
    }

    // vẽ đường tròn bán kính size1 này sau để đè lên các vạch bị dài
    drawArc(
        size = size,
        color = Color(0xff63d6d6),
        startAngle = 140f,
        sweepAngle = 260f,
        useCenter = false,
        style = Stroke(
            width = 1.6.dp.toPx(),
            cap = StrokeCap.Round
        ),
    )

    // vẽ đường tròn bán kính size2 này sau để đè lên các vạch bị dài
    drawArc(
        size = size2,
        color = Color(0xff5ce3a0),
        startAngle = 141f,
        sweepAngle = 258f,
        useCenter = false,
        style = Stroke(
            width = 6.25.dp.toPx(),
            cap = StrokeCap.Square
        ),
        topLeft = Offset(
            x = (size2.width * 0.045f),
            y = (size2.height * 0.04f)
        )
    )


//    drawArc(
//        size = componentSize,
//        color = indicatorColor,
//        startAngle = 150f,
//        sweepAngle = 240f,
//        useCenter = false,
//        style = Stroke(
//            width = indicatorStrokeWidth,
//            cap = StrokeCap.Round,
//        ),
//        topLeft = Offset(
//            x = (size.width - componentSize.width) / 2f,
//            y = (size.height - componentSize.height) / 2f
//        )
//    )
}

fun DrawScope.foregroundIndicator(
    sweepAngle: Float,
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
) {
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 140f,
        sweepAngle = if (sweepAngle > 0) {
            sweepAngle + 10f
        } else {
            sweepAngle
        },
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Butt
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        )
    )


}

fun DrawScope.lineIndicator(
    sweepAngle: Float,
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
) {
    val radius = size.height / 2
    val startAngle = 140f
    val segmentStartAngle = startAngle + sweepAngle
    val segmentEndAngle = segmentStartAngle + sweepAngle

    val startRad = Math.toRadians(segmentStartAngle.toDouble())
    val endRad = Math.toRadians(segmentEndAngle.toDouble())

    val lineStartX = center.x + radius * cos(startRad).toFloat()
    val lineStartY = center.y + radius * sin(startRad).toFloat()

    val lineEndX = center.x + radius * cos(endRad).toFloat()
    val lineEndY = center.y + radius * sin(endRad).toFloat()

    drawLine(
        color = Color(0xff63d6d6),
        start = Offset(x = center.x, y = center.y),
        end = Offset(x = 0f, y = size.height / 2f + 350),
        strokeWidth = 8f
    )
}

@Composable
fun EmbeddedElements(
    bigText: Int,
    bigTextFontSize: TextUnit,
    bigTextColor: Color,
    bigTextSuffix: String,
    smallText: String,
    smallTextColor: Color,
    smallTextFontSize: TextUnit
) {
    Text(
        text = smallText,
        color = Color.White,
        fontSize = smallTextFontSize,
        textAlign = TextAlign.Center
    )
    Text(
        text = "$bigText",
        color = Color.White,
        fontSize = bigTextFontSize,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
    )
}

@Composable
@Preview(showBackground = true)
fun CustomComponentPreview() {
    CustomComponent(
        indicatorValue = 50,
        bigTextColor = Color.White,
        smallTextColor = Color.White
    )
}
