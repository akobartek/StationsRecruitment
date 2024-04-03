package pl.sokolowskibartlomiej.stations.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.TripOrigin
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DistanceCalculationDrawing(modifier: Modifier = Modifier) {
    val lineColor = MaterialTheme.colorScheme.onSurface

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.height(200.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.TripOrigin,
            contentDescription = null,
            modifier = Modifier
                .padding(end = 4.dp)
                .size(20.dp)
        )
        Canvas(modifier = modifier.size(width = 200.dp, height = 200.dp)) {
            val width = size.width
            val height = size.height
            val path = Path().apply {
                moveTo(width.times(.0f), height.times(.05f))
                cubicTo(
                    width.times(.1f),
                    height.times(.55f),
                    width.times(.15f),
                    height.times(.5f),
                    width.times(.3f),
                    height.times(.3f)
                )
                moveTo(width.times(.3f), height.times(.3f))
                cubicTo(
                    width.times(.5f),
                    height.times(.1f),
                    width.times(.7f),
                    height.times(.1f),
                    width.times(.75f),
                    height.times(.2f)
                )
                moveTo(width.times(.75f), height.times(.2f))
                cubicTo(
                    width.times(.85f),
                    height.times(.4f),
                    width.times(.6f),
                    height.times(.48f),
                    width.times(.6f),
                    height.times(.5f)
                )
                moveTo(width.times(.6f), height.times(.5f))
                cubicTo(
                    width.times(.3f),
                    height.times(.7f),
                    width.times(.4f),
                    height.times(.85f),
                    width,
                    height.times(.91f)
                )
                moveTo(width, height.times(.91f))
                close()
            }
            drawPath(
                color = lineColor,
                path = path,
                style = Stroke(
                    width = 3.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                )
            )
        }
        Column {
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 2.dp, bottom = 8.dp)
                    .size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DistanceResultPreview() {
    DistanceCalculationDrawing()
}