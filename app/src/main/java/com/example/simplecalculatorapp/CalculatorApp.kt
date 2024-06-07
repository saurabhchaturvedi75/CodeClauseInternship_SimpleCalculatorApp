package com.example.simplecalculatorapp

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun CalculatorApp() {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var isResultDisplayed by remember { mutableStateOf(false) }

    fun appendNumber(number: String) {
        if (isResultDisplayed) {
            input = number
            isResultDisplayed = false
        } else {
            input += number
        }
    }

    fun appendOperation(op: String) {
        if (isResultDisplayed) {
            input = result
            isResultDisplayed = false
        }
        if (input.isNotEmpty() && !input.last().isDigit()) {
            // Replace the last operation if already there
            input = input.dropLast(1)
        }
        if (input.isNotEmpty()) {
            input += op
        }
    }

    fun calculateResult() {
        try {
            val tokens = input.split("([+\\-*/])".toRegex()).map { it.toDouble() }
            val operations = input.filter { it in "+-*/" }
            var resultValue = tokens.firstOrNull() ?: return

            for (i in operations.indices) {
                val nextValue = tokens.getOrNull(i + 1) ?: return
                when (operations[i]) {
                    '+' -> resultValue += nextValue
                    '-' -> resultValue -= nextValue
                    '*' -> resultValue *= nextValue
                    '/' -> if (nextValue != 0.0) resultValue /= nextValue else {
                        result = "Error: Division by zero"
                        return
                    }
                }
            }
            result = resultValue.toString()
            isResultDisplayed = true
        } catch (e: Exception) {
            result = "Error: Invalid input"
        }
    }

    fun backClear() {
        if (input.isNotEmpty()) {
            input = input.dropLast(1)
        }
    }

    fun clear() {
        input = ""
        result = ""
        isResultDisplayed = false
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Calculator") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnimatedContent(
                targetState = if (isResultDisplayed) result else input,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) with fadeOut(animationSpec = tween(300))
                }
            ) { displayText ->
                Text(
                    text = displayText,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                        .padding(16.dp)
                )
            }

            val buttonModifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .padding(4.dp)

            val buttonColors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            )
            val buttonColors2 = ButtonDefaults.buttonColors(
                containerColor = Color.Cyan,
                contentColor = Color.Black
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { appendNumber("7") }, modifier = buttonModifier, colors = buttonColors) { Text("7") }
                    Button(onClick = { appendNumber("8") }, modifier = buttonModifier, colors = buttonColors) { Text("8") }
                    Button(onClick = { appendNumber("9") }, modifier = buttonModifier, colors = buttonColors) { Text("9") }
                    Button(onClick = { appendOperation("/") }, modifier = buttonModifier, colors = buttonColors2) { Text("/") }
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { appendNumber("4") }, modifier = buttonModifier, colors = buttonColors) { Text("4") }
                    Button(onClick = { appendNumber("5") }, modifier = buttonModifier, colors = buttonColors) { Text("5") }
                    Button(onClick = { appendNumber("6") }, modifier = buttonModifier, colors = buttonColors) { Text("6") }
                    Button(onClick = { appendOperation("*") }, modifier = buttonModifier, colors = buttonColors2) { Text("*") }
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { appendNumber("1") }, modifier = buttonModifier, colors = buttonColors) { Text("1") }
                    Button(onClick = { appendNumber("2") }, modifier = buttonModifier, colors = buttonColors) { Text("2") }
                    Button(onClick = { appendNumber("3") }, modifier = buttonModifier, colors = buttonColors) { Text("3") }
                    Button(onClick = { appendOperation("-") }, modifier = buttonModifier, colors = buttonColors2) { Text("-") }
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { appendNumber(".") }, modifier = buttonModifier, colors = buttonColors2) { Text(".") }
                    Button(onClick = { appendNumber("0") }, modifier = buttonModifier, colors = buttonColors) { Text("0") }
                    IconButton(
                        onClick = { backClear() },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Red),
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(corner = CornerSize(1.dp)))
                    ) { Icon(imageVector = Icons.Rounded.Clear, contentDescription = "Clear", tint = Color.White) }
                    Button(onClick = { appendOperation("+") }, modifier = buttonModifier, colors = buttonColors2) { Text("+") }
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { clear() },
                        modifier = buttonModifier,
                        colors = buttonColors2
                    ) { Text("C") }
                    Spacer(modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp))
                    Spacer(modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp))
                    Button(
                        onClick = { calculateResult() },
                        modifier = buttonModifier,
                        colors = buttonColors2
                    ) { Text("=") }
                }
            }
        }
    }
}
