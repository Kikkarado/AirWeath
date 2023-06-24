package ua.airweath.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import ua.airweath.ui.theme.StarDust
import ua.airweath.ui.theme.spacing

@Composable
fun Caption(
    modifier: Modifier = Modifier,
    label: String,
) {
    Text(
        text = label,
        style = MaterialTheme.typography.caption,
        modifier = modifier,
        color = StarDust
    )
}

@Composable
fun Caption(
    modifier: Modifier = Modifier,
    label: String,
    fontWeight: FontWeight = FontWeight.W400,
    color: Color = StarDust,
) {
    Text(
        text = label,
        style = MaterialTheme.typography.caption,
        modifier = modifier,
        color = color,
        fontWeight = fontWeight
    )
}

@Composable
fun Placeholder(
    value: String,
) {
    Text(
        text = value,
        style = MaterialTheme.typography.body1,
        color = StarDust
    )
}

@Composable
fun Link(
    value: String,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ClickableText(
        text = AnnotatedString(
            text = value,
            spanStyle = SpanStyle(
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colors.primary,
            )
        ),
        style = MaterialTheme.typography.body1,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun CaptionWithDivider(
    label: String,
) {
    Box {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            thickness = 1.dp,
            color = MaterialTheme.colors.primaryVariant
        )
        Caption(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .padding(horizontal = MaterialTheme.spacing.extraSmall)
                .align(Alignment.Center),
            label = label
        )
    }
}