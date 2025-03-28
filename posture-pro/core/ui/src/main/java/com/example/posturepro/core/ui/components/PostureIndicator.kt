package com.example.posturepro.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.posturepro.core.ui.theme.PostureProTheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PostureIndicator(
    angle: Float,
    sensitivity: Int,
    modifier: Modifier = Modifier
) {
    val goodColor = Color(0xFF4CAF50)
    val fairColor = Color(0xFFFFC107)
    val poorColor = Color(0xFFF44336)
    
    Canvas(modifier = modifier) {
        val radius = size.minDimension / 2
        val center = Offset(size.width / 2, size.height / 2)
        
        // Draw background arcs
        drawArc(
            color = poorColor.copy(alpha = 0.2f),
            startAngle = -90f - sensitivity,
            sweepAngle = sensitivity * 2f,
            useCenter = false,
            size = size,
            style = Stroke(width = radius * 0.1f)
        )
        
        drawArc(
            color = fairColor.copy(alpha = 0.2f),
            startAngle = -90f - sensitivity/2f,
            sweepAngle = sensitivity.toFloat(),
            useCenter = false,
            size = size,
            style = Stroke(width = radius * 0.1f)
        )
        
        drawArc(
            color = goodColor.copy(alpha = 0.2f),
            startAngle = -90f - sensitivity/4f,
            sweepAngle = sensitivity/2f,
            useCenter = false,
            size = size,
            style = Stroke(width = radius * 0.1f)
        )
        
        // Draw indicator line
        val radians = Math.toRadians(angle.toDouble())
        val endX = center.x + radius * 0.8f * cos(radians).toFloat()
        val endY = center.y + radius * 0.8f * sin(radians).toFloat()
        
        val lineColor = when {
            angle < -sensitivity -> poorColor
            angle > sensitivity -> poorColor
            angle < -sensitivity/2 -> fairColor
            angle > sensitivity/2 -> fairColor
            else -> goodColor
        }
        
        drawLine(
            color = lineColor,
            start = center,
            end = Offset(endX, endY),
            strokeWidth = radius * 0.05f
        )
        
        // Draw center dot
        drawCircle(
            color = lineColor,
            radius = radius * 0.05f,
            center = center
        )
    }
}

@Preview
@Composable
fun PostureIndicatorPreview() {
    PostureProTheme {
        PostureIndicator(
            angle = 10f,
            sensitivity = 15,
            modifier = Modifier.size(200.dp)
        )
    }
}