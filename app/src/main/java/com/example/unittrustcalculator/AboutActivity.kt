package com.example.unittrustcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unittrustcalculator.ui.theme.UnitTrustCalculatorTheme
import java.util.Calendar

class AboutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnitTrustCalculatorTheme {
                AboutScreen(
                    // Add a lambda to handle the "back" press
                    onNavigateUp = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onNavigateUp: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About") },
                // This adds a back arrow to the toolbar
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back"
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
            // Center everything horizontally
            horizontalAlignment = Alignment.CenterHorizontally,
            // Add space between elements
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Application Icon
            Image(
                painter = painterResource(id = R.drawable.icon_png), // Make sure icon_png.png is in res/drawable
                contentDescription = "Application Icon",
                modifier = Modifier.size(128.dp) // Make the icon a nice size
            )

            // 2. Author Information
            AuthorInfo()

            Spacer(modifier = Modifier.weight(1f)) // Pushes the next items to the bottom

            // 3. Clickable GitHub Link
            GithubLink()

            // 4. Copyright Notice
            CopyrightNotice()
        }
    }
}

@Composable
fun AuthorInfo() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text("Personal Information", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Name: Nur Damiaa Afrina Zubaidi", style = MaterialTheme.typography.bodyLarge)
        Text("Matric No: 2023261672", style = MaterialTheme.typography.bodyLarge)
        Text("Course: Computer Science (CS2405A)", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun CopyrightNotice() {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    Text(
        text = "ICT602 Copyright © $currentYear Damiaa. All rights reserved.",
        style = MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 16.dp)
    )
}

@Composable
fun GithubLink() {
    val uriHandler = LocalUriHandler.current
    val githubRepoUrl = "https://github.com/damiaaafrina/Unit-Trust-Calculator-Android"

    val annotatedString = buildAnnotatedString {
        append("Visit my project on GitHub: ")

        pushStringAnnotation(tag = "URL", annotation = githubRepoUrl)

        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary, // Use theme color for links
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(githubRepoUrl)
        }

        pop()
    }

    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    uriHandler.openUri(annotation.item)
                }
        },
        style = MaterialTheme.typography.bodyLarge
    )
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    UnitTrustCalculatorTheme {
        AboutScreen(onNavigateUp = {})
    }
}
