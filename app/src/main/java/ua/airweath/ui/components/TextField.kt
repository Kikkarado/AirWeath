package ua.airweath.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.airweath.ui.theme.customShapes
import ua.airweath.ui.theme.spacing

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    error: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.subtitle1,
    textPaddingValues: PaddingValues = PaddingValues(MaterialTheme.spacing.medium, 0.dp),
    alignmentVertical: Alignment.Vertical = Alignment.CenterVertically,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black),
    shape: Shape = MaterialTheme.customShapes.small,
    borderStroke: BorderStroke = BorderStroke(0.dp, Color.Transparent),
    background: Color = MaterialTheme.colors.background,
    contentColor: Color = MaterialTheme.colors.onBackground,
    elevation: Dp = 6.dp,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush
    ) { innerTextField ->
        Card(
            modifier = Modifier,
            shape = shape,
            backgroundColor = if (enabled) background else MaterialTheme.colors.onBackground.copy(
                .05f
            ).compositeOver(
                Color.White
            ),
            contentColor = if (enabled) contentColor else MaterialTheme.colors.onBackground.copy(.2f)
                .compositeOver(
                    Color.White
                ),
            border = if (!error) borderStroke else BorderStroke(1.dp, MaterialTheme.colors.error),
            elevation = elevation
        ) {
            Row(
                modifier = Modifier
                    .padding(textPaddingValues)
                    //.fillMaxSize()
                    .width(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = alignmentVertical
            ) {
                leadingIcon?.invoke()
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = MaterialTheme.spacing.medium),
                    propagateMinConstraints = true
                ) {
                    innerTextField()
                    if (value == "" && value.isEmpty()) placeholder?.invoke()
                }
                trailingIcon?.invoke()
            }
        }
    }
}

@Composable
fun AppTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    error: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.subtitle1,
    textPaddingValues: PaddingValues = PaddingValues(MaterialTheme.spacing.medium, 0.dp),
    alignmentVertical: Alignment.Vertical = Alignment.CenterVertically,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black),
    shape: Shape = MaterialTheme.customShapes.small,
    borderStroke: BorderStroke = BorderStroke(0.dp, Color.Transparent),
    background: Color = MaterialTheme.colors.background,
    contentColor: Color = MaterialTheme.colors.onBackground,
    elevation: Dp = 6.dp,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush
    ) { innerTextField ->
        val errorColor = MaterialTheme.colors.error
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = shape,
            backgroundColor = if (enabled) background else MaterialTheme.colors.onBackground.copy(
                .05f
            ).compositeOver(
                Color.White
            ),
            contentColor = if (enabled) contentColor else MaterialTheme.colors.onBackground.copy(.2f)
                .compositeOver(
                    Color.White
                ),
            border = if (!error) borderStroke else BorderStroke(1.dp, errorColor),
            elevation = elevation
        ) {
            Row(
                modifier = Modifier
                    .padding(textPaddingValues)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = alignmentVertical
            ) {
                leadingIcon?.invoke()
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = MaterialTheme.spacing.medium),
                    propagateMinConstraints = true
                ) {
                    innerTextField()
                    if (value.text == "" && value.text.isEmpty()) placeholder?.invoke()
                }
                trailingIcon?.invoke()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppClickTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    enabledClick: Boolean = false,
    readOnly: Boolean = false,
    error: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.subtitle1,
    textPaddingValues: PaddingValues = PaddingValues(MaterialTheme.spacing.medium, 0.dp),
    alignmentVertical: Alignment.Vertical = Alignment.CenterVertically,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black),
    shape: Shape = MaterialTheme.customShapes.small,
    borderStroke: BorderStroke = BorderStroke(0.dp, Color.Transparent),
    background: Color = MaterialTheme.colors.background,
    contentColor: Color = MaterialTheme.colors.onBackground,
    elevation: Dp = 6.dp,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush
    ) { innerTextField ->
        Card(
            onClick = onClick,
            modifier = Modifier,
            shape = shape,
            enabled = enabledClick,
            backgroundColor = if (enabled) background else MaterialTheme.colors.onBackground.copy(
                .05f
            ).compositeOver(
                Color.White
            ),
            contentColor = if (enabled) contentColor else MaterialTheme.colors.onBackground.copy(.2f)
                .compositeOver(
                    Color.White
                ),
            border = if (!error) borderStroke else BorderStroke(1.dp, MaterialTheme.colors.error),
            elevation = elevation
        ) {
            Row(
                modifier = Modifier
                    .padding(textPaddingValues)
                    //.fillMaxSize()
                    .width(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = alignmentVertical
            ) {
                leadingIcon?.invoke()
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = MaterialTheme.spacing.medium),
                    propagateMinConstraints = true
                ) {
                    innerTextField()
                    if (value == "" && value.isEmpty()) placeholder?.invoke()
                }
                trailingIcon?.invoke()
            }
        }
    }
}

@Composable
fun TextFieldWithTitle(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.body1
        )
        AppTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            singleLine = true
        )
    }
}

@Composable
fun TextFieldWithTitle(
    title: @Composable RowScope.() -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Row { title() }
        AppTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            singleLine = true
        )
    }
}

@Composable
fun TextFieldWithTitle(
    title: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.body1
        )
        AppTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            singleLine = true
        )
    }
}