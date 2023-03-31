package com.gunder.compose.converter

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gunder.compose.R
import com.gunder.compose.ui.theme.ComposeTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        StatefulTemperatureInput()
                        ConverterApp()
                        TwoWayConverterApp()
                    }
                }
            }
        }
    }

    //    sample stateful compose
    @Composable
    fun StatefulTemperatureInput(modifier: Modifier = Modifier) {
        var input by remember {
            mutableStateOf("")
        }
        var output by remember {
            mutableStateOf("")
        }
        Column(modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.stateful_converter),
                style = MaterialTheme.typography.h5
            )
            OutlinedTextField(
                value = input,
                label = { Text(stringResource(R.string.enter_celsius)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { newInput ->
                    input = newInput
                    output = convertToFahrenheit(newInput)
                })
            Text(stringResource(R.string.temperature_fahrenheit, output))
        }
    }

    // stateless compose func
    @Composable
    fun StatelessTemperatureInput(
        input: String,
        output: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.stateless_converter),
                style = MaterialTheme.typography.h5
            )
            OutlinedTextField(
                value = input,
                label = { Text(stringResource(R.string.enter_celsius)) },
                onValueChange = onValueChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Text(stringResource(R.string.temperature_fahrenheit, output))
        }
    }

    @Composable
    private fun ConverterApp(modifier: Modifier = Modifier) {
        var input by remember {
            mutableStateOf("")
        }
        var output by remember {
            mutableStateOf("")
        }
        StatelessTemperatureInput(input = input, output = output, onValueChange = {
            input = it
            output = convertToFahrenheit(it)
        })
    }

    private fun convertToFahrenheit(celsius: String) =
        celsius.toDoubleOrNull()?.let { (it * 9 / 5) + 32 }.toString()

    private fun convertToCelsius(fahrenheit: String) =
        fahrenheit.toDoubleOrNull()?.let { (it - 32) * 5 / 9 }.toString()

    enum class Scale(val scaleName: String) {
        CELSIUS("Celsius"),
        FAHRENHEIT("Fahrenheit")
    }

    @Composable
    fun GeneralTemperatureInput(
        scale: Scale,
        input: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier) {
            OutlinedTextField(value = input, onValueChange = onValueChange, label = {
                Text(
                    stringResource(R.string.enter_temperature, scale.scaleName)
                )
            }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        }
    }

    @Composable
    fun TwoWayConverterApp(modifier: Modifier = Modifier) {
        var celsius by remember {
            mutableStateOf("")
        }
        var fahrenheit by remember {
            mutableStateOf("")
        }
        Column(modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.two_way_converter),
                style = MaterialTheme.typography.h5
            )
            GeneralTemperatureInput(
                scale = Scale.CELSIUS,
                input = celsius,
                onValueChange = {
                    celsius = it
                    fahrenheit = convertToFahrenheit(it)
                })

            GeneralTemperatureInput(
                scale = Scale.FAHRENHEIT,
                input = fahrenheit,
                onValueChange = {
                    fahrenheit = it
                    celsius = convertToCelsius(it)
                })
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        Column {
            StatefulTemperatureInput()
            ConverterApp()
            TwoWayConverterApp()
        }
    }
}