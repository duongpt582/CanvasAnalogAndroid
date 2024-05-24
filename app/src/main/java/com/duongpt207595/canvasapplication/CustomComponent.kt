import android.graphics.PointF
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp


@Composable
fun CustomComponent(
    canvasSize: Dp = 300.dp,
    indicatorValue: Int = 0,
    maxIndicatorValue: Int = 100,
    backgroundIndicatorColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    backgroundIndicatorStrokeWidth: Float = 100f,
    foregroundIndicatorColor: Color = MaterialTheme.colorScheme.primary,
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
            .drawBehind {
                val componentSize = size / 1.25f
                backgroundIndicator(
                    componentSize = componentSize,
                    indicatorColor = backgroundIndicatorColor,
                    indicatorStrokeWidth = backgroundIndicatorStrokeWidth,
//                    indicatorStokeCap = indicatorStrokeCap
                )
                foregroundIndicator(
                    sweepAngle = sweepAngle,
                    componentSize = componentSize,
                    indicatorColor = foregroundIndicatorColor,
                    indicatorStrokeWidth = foregroundIndicatorStrokeWidth,
//                    indicatorStokeCap = indicatorStrokeCap
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
//    indicatorStokeCap: StrokeCap
) {
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

    val size2 = size * 0.92f
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

    val centerCirclePoint = PointF( size.width / 2f, size.height / 2f)
//    drawLine(
//        color = Color(0xff63d6d6),
//        start = Offset(x = centerCirclePoint.x, y = centerCirclePoint.y),
//        end = Offset(x = 0f, y = size.height / 2f + 350),
//        strokeWidth = 8f
//    )

    drawLine(
        color = Color(0xff63d6d6),
        start = Offset(x = centerCirclePoint.x, y = centerCirclePoint.y),
        end = Offset(x = 0f, y = size.height / 2f + 280),
        strokeWidth = 8f
    )

    drawLine(
        color = Color(0xff63d6d6),
        start = Offset(x = centerCirclePoint.x, y = centerCirclePoint.y),
        end = Offset(x = size.width, y = size.height / 2f + 280),
        strokeWidth = 8f
    )

    drawLine(
        color = Color(0xff63d6d6),
        start = Offset(x = centerCirclePoint.x, y = centerCirclePoint.y),
        end = Offset(x = 0f, y = size.height / 2f + 180),
        strokeWidth = 8f
    )

    drawLine(
        color = Color(0xff63d6d6),
        start = Offset(x = centerCirclePoint.x, y = centerCirclePoint.y),
        end = Offset(x = size.width, y = size.height / 2f + 180),
        strokeWidth = 8f
    )



    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 150f,
        sweepAngle = 240f,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        )
    )
}

fun DrawScope.foregroundIndicator(
    sweepAngle: Float,
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float,
//    indicatorStokeCap: StrokeCap
) {
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 150f,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        )
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
        color = smallTextColor,
        fontSize = smallTextFontSize,
        textAlign = TextAlign.Center
    )
    Text(
        text = "$bigText ${bigTextSuffix.take(2)}",
        color = bigTextColor,
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
