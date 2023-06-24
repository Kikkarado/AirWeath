package ua.airweath.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ua.airweath.ui.theme.customShapes
import ua.airweath.ui.theme.spacing

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IconPanel(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Int,
    tint: Color = Color.Unspecified,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.customShapes.small,
        color = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface,
        elevation = 6.dp,
    ) {
        Box(
            modifier = Modifier
                .padding(MaterialTheme.spacing.small),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = tint
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Panel(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    background: Color = MaterialTheme.colors.surface,
    content: @Composable () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        color = background,
        elevation = 6.dp,
        enabled = enabled,
        shape = MaterialTheme.customShapes.small
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.medium)
        ) {
            content()
        }
    }
}
