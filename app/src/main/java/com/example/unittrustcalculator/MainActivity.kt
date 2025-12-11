package com.example.unittrustcalculator

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unittrustcalculator.ui.theme.UnitTrustCalculatorTheme
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnitTrustCalculatorTheme {
                UnitTrustCalculatorApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitTrustCalculatorApp() {
    // State variables for inputs
    var investAmount by remember { mutableStateOf("") }
    var dividendRate by remember { mutableStateOf("") }
    var monthCount by remember { mutableStateOf("") }

    // --- NEW: State variables to hold each line of the calculation breakdown ---
    var calculationTitle by remember { mutableStateOf("") }
    var monthlyDividendStep1 by remember { mutableStateOf("") }
    var monthlyDividendStep2 by remember { mutableStateOf("") }
    var totalDividendTitle by remember { mutableStateOf("") }
    var totalDividendStep1 by remember { mutableStateOf("") }
    var totalDividendStep2 by remember { mutableStateOf("") }


    // State for the navigation menu
    var showMenu by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Unit Trust Calculator") },
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.icon_png), // Make sure this name is correct
                        contentDescription = "App Icon",
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp).size(32.dp)
                    )
                },
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Home") },
                            onClick = {
                                showMenu = false
                                Toast.makeText(context, "Already on Home screen", Toast.LENGTH_SHORT).show()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("About") },
                            onClick = {
                                showMenu = false
                                context.startActivity(Intent(context, AboutActivity::class.java))
                            }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // --- UI Elements ---
            OutlinedTextField(
                value = investAmount,
                onValueChange = { investAmount = it },
                label = { Text("Total Invested (RM)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = dividendRate,
                onValueChange = { dividendRate = it },
                label = { Text("Dividend (%)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = monthCount,
                onValueChange = { monthCount = it },
                label = { Text("Months Invested") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val amount = investAmount.toDoubleOrNull()
                    val rate = dividendRate.toDoubleOrNull()
                    val months = monthCount.toIntOrNull()

                    // Clear previous results
                    calculationTitle = ""
                    monthlyDividendStep1 = ""
                    monthlyDividendStep2 = ""
                    totalDividendTitle = ""
                    totalDividendStep1 = ""
                    totalDividendStep2 = ""

                    if (amount == null || rate == null || months == null) {
                        Toast.makeText(context, "Please fill in all values", Toast.LENGTH_SHORT).show()
                    } else {
                        // --- UPDATED LOGIC TO BUILD STEP-BY-STEP STRINGS ---
                        val rateAsDecimal = rate / 100
                        val monthlyDividend = amount * rateAsDecimal / 12
                        val totalDividend = monthlyDividend * months

                        // For formatting numbers without scientific notation
                        val decimalFormat = DecimalFormat("#,##0.00")
                        val amountFormatted = decimalFormat.format(amount)

                        // Build the strings for each step
                        calculationTitle = "Calculation"
                        monthlyDividendStep1 = "Monthly Dividend = RM$amountFormatted x $rateAsDecimal / 12"
                        monthlyDividendStep2 = "= ${monthlyDividend} "

                        totalDividendTitle = "Year-end total dividend:"
                        totalDividendStep1 = "${monthlyDividend} x $months months invested,"
                        totalDividendStep2 = "= RM ${decimalFormat.format(totalDividend)} "
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("CALCULATE")
            }

            if (calculationTitle.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalAlignment = Alignment.Start, // Align text to the left
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = calculationTitle, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text(text = monthlyDividendStep1, style = MaterialTheme.typography.bodyLarge)
                    Text(text = monthlyDividendStep2, style = MaterialTheme.typography.bodyLarge)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = totalDividendTitle, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text(text = totalDividendStep1, style = MaterialTheme.typography.bodyLarge)
                    Text(text = totalDividendStep2, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun UnitTrustCalculatorAppPreview() {
    UnitTrustCalculatorTheme {
        UnitTrustCalculatorApp()
    }
}
