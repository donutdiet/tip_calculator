package com.example.tiptracker.screens.addentry

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiptracker.R
import com.example.tiptracker.ui.theme.TipTrackerTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiptracker.ui.LogViewModel

@Composable
fun DiningDescriptionScreen(
    modifier: Modifier = Modifier,
    onCancelButtonClicked: () -> Unit = {},
    onSaveButtonClicked: () -> Unit = {},
    viewModel: LogViewModel,
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
                .safeDrawingPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Describe your experience",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            TextInputField(
                label = R.string.restaurant_name,
                leadingIcon = R.drawable.storefront,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                value = viewModel.restaurantName.value,
                onValueChange = { viewModel.onRestaurantNameChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            MultiLineTextInputField(
                label = R.string.restaurant_description,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                value = viewModel.restaurantDescription.value,
                onValueChange = { viewModel.onRestaurantDescriptionChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
            )
            FinalBill(
                tip = viewModel.getCalculatedTip(),
                total = viewModel.getCalculatedTotal(),
                personCount = viewModel.personCount.value.toIntOrNull() ?: 1,
                totalPerPerson = viewModel.getCalculatedTotalPerPerson(),
                modifier = Modifier.padding(vertical = 20.dp)
            )
            TwoButtonRow(
                buttonLabelL = R.string.cancel ,
                buttonLabelR = R.string.save,
                onButtonClickL = onCancelButtonClicked,
                onButtonClickR = onSaveButtonClicked
            )
        }
    }
}

@Composable
fun MultiLineTextInputField(
    @StringRes label: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        label = {
            Text(
                stringResource(label),
                style = MaterialTheme.typography.labelSmall
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        modifier = modifier
            .heightIn(min = 200.dp)
            .focusRequester(focusRequester),
        maxLines = 12
    )
}

@Preview(showBackground = true)
@Composable
fun DiningDescriptionScreenPreview() {
    TipTrackerTheme {
        DiningDescriptionScreen(viewModel = viewModel())
    }
}

@Preview(showBackground = true)
@Composable
fun DiningDescriptionScreenPreviewDarkMode() {
    TipTrackerTheme(darkTheme = true) {
        DiningDescriptionScreen(viewModel = viewModel())
    }
}