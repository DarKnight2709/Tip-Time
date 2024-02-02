
package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    TipTimeLayout()
                }
            }
        }
    }
}

@Composable
fun TipTimeLayout(modifier: Modifier = Modifier) {
    var amountInput by remember { mutableStateOf("") }
    var tipInput by remember { mutableStateOf("") }
    var checkValue by remember { mutableStateOf(false) }

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
    val tip = calculateTip(amount, tipPercent, checkValue)

    Column(
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(
            leadingIcon = R.drawable.money,
            label = R.string.bill_amount,
            value = amountInput,
            onValueChange = {
                amountInput = it
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        EditNumberField(
            leadingIcon = R.drawable.percent,
            label = R.string.how_was_the_service,
            value = tipInput,
            onValueChange = {
                 tipInput = it
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        RoundTheTipRow(
            modifier = Modifier.padding(bottom = 32.dp),
            checkValue = checkValue,
            onCheckedChange = {
                checkValue = it
            }
        )


        Text(
            text = stringResource(R.string.tip_amount, tip),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun EditNumberField(
    @DrawableRes leadingIcon: Int,
    @StringRes label: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
){

    TextField(
        value = value,
        leadingIcon = {
                      Icon(painter = painterResource(id = leadingIcon), null)
        },
        onValueChange = onValueChange,
        singleLine = true,
        label = {
            Text(text = stringResource(id = label))
        },
        keyboardOptions = keyboardOptions,
        modifier = modifier
    )
}

@Composable
fun RoundTheTipRow(
    modifier: Modifier = Modifier,
    checkValue: Boolean,
    onCheckedChange : (Boolean) -> Unit
    ){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.round_up_tip)
        )
        Switch(
            modifier = modifier
                .padding(top = 22.dp)
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End),
            checked = checkValue,
            onCheckedChange = onCheckedChange
        )

    }
}

/**
 * Calculates the tip based on the user input and format the tip amount
 * according to the local currency.
 * Example would be "$10.00".
 */

@VisibleForTesting
internal fun calculateTip(amount: Double, tipPercent: Double = 15.0, checkValue: Boolean): String {
    val tip =  tipPercent / 100 * amount
    return NumberFormat.getCurrencyInstance().format(if(checkValue) kotlin.math.ceil(tip) else tip)
}

@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview() {
    TipTimeTheme {
        TipTimeLayout(modifier = Modifier
            .fillMaxSize()

        )
    }
}

