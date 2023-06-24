@file:OptIn(ExperimentalMaterialApi::class)

package ua.airweath.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.airweath.ui.theme.spacing


@Composable
fun AppDropDown(
    modifier: Modifier = Modifier,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    onClick: (id: Int, option: String) -> Unit,
    readOnly: Boolean = true,
    value: String,
    onValueChange: (String) -> Unit,
    error: Boolean = options.isEmpty(),
    elevation: Dp = 3.dp
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        AppTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            error = error,
            textStyle = MaterialTheme.typography.body1,
            singleLine = true,
            elevation = elevation
        ) {
            ExposedDropdownMenuDefaults.TrailingIcon(
                expanded = expanded
            )
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            options.forEachIndexed { id, option ->
                DropdownMenuItem(onClick = {
                    onClick(id, option)
                }) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}

@Composable
fun AppDropDown(
    modifier: Modifier = Modifier,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    onClick: (id: Int, option: String) -> Unit,
    readOnly: Boolean = true,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    error: Boolean = options.isEmpty(),
    elevation: Dp = 0.dp
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        AppTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            error = error,
            textStyle = MaterialTheme.typography.body1,
            singleLine = true,
            elevation = elevation
        ) {
            ExposedDropdownMenuDefaults.TrailingIcon(
                expanded = expanded
            )
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            options.forEachIndexed { id, option ->
                DropdownMenuItem(onClick = {
                    onClick(id, option)
                }) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}

@Composable
fun AppDropDownPlaces(
    modifier: Modifier = Modifier,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    onClick: (id: Int, option: String) -> Unit,
    readOnly: Boolean = true,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    error: Boolean = false,
    elevation: Dp = 0.dp,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIconClick: () -> Unit = {}
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        AppTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            error = error,
            textStyle = MaterialTheme.typography.body1,
            singleLine = true,
            elevation = elevation,
            trailingIcon = {
                IconButton(
                    onClick = {
                        onValueChange(TextFieldValue(text = "", selection = TextRange.Zero))
                        trailingIconClick()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.small)
                            .fillMaxHeight()
                            .aspectRatio(1f)
                    )
                }
            },
            placeholder = placeholder,
        )
        if (options.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismissRequest
            ) {
                options.forEachIndexed { id, option ->
                    DropdownMenuItem(onClick = {
                        onClick(id, option)
                    }) {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }
        }
    }
}