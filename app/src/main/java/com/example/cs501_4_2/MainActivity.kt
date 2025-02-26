package com.example.cs501_4_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cs501_4_2.ui.theme.CS501_4_2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CS501_4_2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HangMan(
                        modifier = Modifier.padding(innerPadding).windowInsetsPadding(WindowInsets.systemBars)
                    )
                }
            }
        }
    }
}

@Composable
fun HangMan(modifier: Modifier = Modifier) {
    val hangManImage = setOf(
        "start" to R.drawable.start,
        "head" to R.drawable.head,
        "left_leg" to R.drawable.left_leg,
        "right_leg" to R.drawable.right_leg,
        "left_arm" to R.drawable.left_arm,
        "right_arm" to R.drawable.right_arm,
        "dead" to R.drawable.dead
    )
}

