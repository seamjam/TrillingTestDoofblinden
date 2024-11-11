package com.example.trillingtestdoofblinden

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trillingtestdoofblinden.ui.theme.TrillingTestDoofblindenTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrillingTestDoofblindenTheme {
                MainScreen(
                    onVibrate = { pattern -> vibratePattern(pattern) }
                )
            }
        }
    }

    private fun vibratePattern(pattern: LongArray) {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val amplitudes = IntArray(pattern.size) { index ->
                    if (index % 2 == 0) 0 else 255
                }
                val vibrationEffect = VibrationEffect.createWaveform(pattern, amplitudes, -1)
                vibrator.vibrate(vibrationEffect)
            } else {
                vibrator.vibrate(pattern[1])
            }
        }
    }
}

@Composable
fun MainScreen(onVibrate: (LongArray) -> Unit) {
    val vibrationPatterns = listOf(
        "Trilling 1" to longArrayOf(0, 100, 400, 100, 200),         // Korte trilling, lange pauze, korte trilling
        "Trilling 2" to longArrayOf(0, 800, 200, 800),              // Twee lange trillingen met korte pauze
        "Trilling 3" to longArrayOf(0, 300, 100, 300, 100, 300),    // Snel herhaalde korte trillingen
        "Trilling 4" to longArrayOf(0, 1500),                       // Eén hele lange trilling
        "Trilling 5" to longArrayOf(0, 100, 100, 100, 100, 500),    // Vier korte trillingen gevolgd door een langere
        "Trilling 6" to longArrayOf(0, 100, 50, 100, 50, 100, 50, 100, 400), // Meerdere korte, dan een langere pauze
        "Trilling 7" to longArrayOf(0, 200, 200, 200, 200, 1000),   // Herhalende trilling, dan lange trilling
        "Trilling 8" to longArrayOf(0, 400, 400, 400, 400),         // Regelmatige middelmatige trillingen
        "Trilling 9" to longArrayOf(0, 100, 200, 100, 200, 100, 500), // Verspringende korte en lange trillingen
        "Trilling 10" to longArrayOf(0, 1000, 100, 1000)            // Lange trilling, korte pauze, weer lange trilling
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFFFF0E0)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Wie is er?",
            fontSize = 38.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Cursive,
            modifier = Modifier.padding(bottom = 16.dp)

        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(vibrationPatterns) { (name, pattern) ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    VibrationPatternRow(name = name, pattern = pattern, onVibrate = onVibrate)
                }
            }
        }
    }
}

@Composable
fun VibrationPatternRow(name: String, pattern: LongArray, onVibrate: (LongArray) -> Unit) {
    var isPlaying = remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = name,
            fontFamily = FontFamily.Cursive,
            fontWeight = FontWeight.Bold,
            fontSize = 23.sp,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = {
            isPlaying.value = true
            onVibrate(pattern)
        }) {
            Image(
                painter = painterResource(id = if (isPlaying.value) R.drawable.ic_pause else R.drawable.ic_play),
                contentDescription = if (isPlaying.value) "Pauzeer trilling" else "Speel trilling af",
                modifier = Modifier.size(24.dp)
            )
        }
    }

    LaunchedEffect(isPlaying.value) {
        if (isPlaying.value) {
            val totalDuration = pattern.sum()
            delay(totalDuration)
            isPlaying.value = false
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TrillingTestDoofblindenTheme {
        MainScreen(onVibrate = {})
    }
}