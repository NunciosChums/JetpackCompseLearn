package kr.susemi99.basic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kr.susemi99.basic.ui.theme.BasicTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      BasicTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
          Box(Modifier
            .background(Color.Green)
            .fillMaxWidth()
            .height(200.dp)) {
            Text("Hello")
            Box(Modifier
              .fillMaxSize()
              .padding(16.dp),
              contentAlignment = Alignment.BottomEnd) {
              Text("12345~~~~~~~~")
            }
          }
        }
      }
    }
  }
}