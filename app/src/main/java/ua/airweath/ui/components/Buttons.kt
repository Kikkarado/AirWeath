package ua.airweath.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.airweath.ui.theme.customShapes
import ua.airweath.ui.theme.spacing

/**
 * Text button
 * @param onClick действие
 * @param modifier Modifier
 * @param enabled true or false
 * @param contentColor цвет для содержимого
 * @param text текст внутри кнопки
 * @param textStyle стиль для текста
 */
@Composable
fun AppTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentColor: Color = MaterialTheme.colors.onBackground,
    text: String,
    textStyle: TextStyle,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            contentColor = contentColor,
            disabledBackgroundColor = Color.Unspecified,
            disabledContentColor = contentColor.copy(ContentAlpha.disabled)
        ),
        shape = MaterialTheme.customShapes.small
    ) {
        Text(
            text = text,
            style = textStyle
        )
    }
}

/**
 * Button
 * @param onClick действие
 * @param modifier Modifier
 * @param enabled true or false
 * @param text текст внутри кнопки
 */
@Composable
fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = Color.White,
            disabledBackgroundColor = MaterialTheme.colors.primary.copy(.5f),
            disabledContentColor = Color.White
        ),
        shape = MaterialTheme.customShapes.small
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.button
        )
    }
}

@Preview
@Composable
fun ButtonPrev() {
    AppButton(
        onClick = {},
    modifier = Modifier,
    text = "gfgdsf",
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppCustomButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    background: Color = Color.Unspecified,
    contentColor: Color = MaterialTheme.colors.onBackground,
    elevation: Dp = 0.dp,
    padding: PaddingValues = PaddingValues(MaterialTheme.spacing.extraSmall),
    contentAlignment: Alignment = Alignment.Center,
    shape: Shape = MaterialTheme.customShapes.zero
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = background,
        contentColor = contentColor,
        elevation = elevation
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = contentAlignment
        ) {
            Text(
                modifier = Modifier
                    .padding(padding),
                text = text,
                style = MaterialTheme.typography.button,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppCustomButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    background: Color = Color.Unspecified,
    contentColor: Color = MaterialTheme.colors.onBackground,
    elevation: Dp = 0.dp,
    shape: Shape = MaterialTheme.customShapes.zero,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = background,
        contentColor = contentColor,
        elevation = elevation,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = contentAlignment
        ) {
            content()
        }
    }
}

@Composable
fun AppTextIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.button,
    textModifier: Modifier = Modifier,
    background: Color = Color.Unspecified,
    contentColor: Color = MaterialTheme.colors.onBackground,
    icon: Painter,
    iconTint: Color = Color.Unspecified,
    alignment: Alignment.Horizontal = Alignment.Start,
    padding: Dp = MaterialTheme.spacing.extraSmall,
    elevation: Dp = 0.dp,
    pressedElevation: Boolean = true,
    shape: Shape = MaterialTheme.customShapes.small,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = background.compositeOver(MaterialTheme.colors.background),
            contentColor = contentColor,
            disabledBackgroundColor = background.compositeOver(MaterialTheme.colors.background).copy(ContentAlpha.disabled),
            disabledContentColor = contentColor.copy(ContentAlpha.disabled)
        ),
        shape = shape,
        elevation = ButtonDefaults
            .elevation(
                defaultElevation = elevation,
                pressedElevation = if (pressedElevation) elevation else 0.dp,
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(padding, alignment),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconTint
            )
            Text(
                text = text,
                modifier = textModifier,
                style = textStyle,
                color = contentColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AppTextIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.button,
    textModifier: Modifier = Modifier,
    background: Color = Color.Unspecified,
    contentColor: Color = MaterialTheme.colors.onBackground,
    icon: ImageVector,
    iconTint: Color = Color.Unspecified,
    alignment: Alignment.Horizontal = Alignment.Start,
    padding: Dp = MaterialTheme.spacing.extraSmall,
    elevation: Dp = 0.dp,
    pressedElevation: Boolean = true,
    shape: Shape = MaterialTheme.customShapes.small,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = background.compositeOver(MaterialTheme.colors.background),
            contentColor = contentColor,
            disabledBackgroundColor = background.compositeOver(MaterialTheme.colors.background).copy(ContentAlpha.disabled),
            disabledContentColor = contentColor.copy(ContentAlpha.disabled)
        ),
        shape = shape,
        elevation = ButtonDefaults
            .elevation(
                defaultElevation = elevation,
                pressedElevation = if (pressedElevation) elevation else 0.dp,
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(padding, alignment),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint
            )
            Text(
                text = text,
                modifier = textModifier,
                style = textStyle,
                color = contentColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ServiceButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.button,
    textModifier: Modifier = Modifier,
    background: Color = Color.Unspecified,
    contentColor: Color = MaterialTheme.colors.onBackground,
    icon: Painter,
    iconTint: Color = Color.Unspecified,
    elevation: Dp = 0.dp,
    shape: Shape = MaterialTheme.customShapes.small,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = background.compositeOver(MaterialTheme.colors.background),
            contentColor = contentColor,
            disabledBackgroundColor = background.compositeOver(MaterialTheme.colors.background).copy(ContentAlpha.disabled),
            disabledContentColor = contentColor.copy(ContentAlpha.disabled)
        ),
        shape = shape,
        elevation = ButtonDefaults
            .elevation(
                defaultElevation = elevation
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconTint
            )
            Text(
                text = text,
                modifier = textModifier,
                style = textStyle,
                color = contentColor,
                textAlign = TextAlign.Center
            )
        }
    }
}