package kr.susemi99.fatcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
//      HomeScreen()
      ResultScreen(bmi = 10.0)
    }
  }
}

@Composable
fun HomeScreen() {
  val (height, setHeight) = rememberSaveable { mutableStateOf("") }
  val (weight, setWeight) = rememberSaveable { mutableStateOf("") }

  Scaffold(topBar = { TopAppBar(title = { Text(text = "비만도 계산기") }) }) {
    Column(Modifier.padding(16.dp)) {
      OutlinedTextField(
        value = height,
        onValueChange = setHeight,
        label = { Text(text = "키") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
      )
      OutlinedTextField(
        value = weight,
        onValueChange = setWeight,
        label = { Text(text = "몸무게") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
      )
      Spacer(modifier = Modifier.height(8.dp))
      Button(onClick = { }, Modifier.align(Alignment.End)) {
        Text("결과")
      }
    }
  }
}

@Composable
fun ResultScreen(bmi: Double) {
  Scaffold(topBar = { TopAppBar(title = { Text("비만도 계산기") }) }) {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
      Text("과체중", fontSize = 30.sp)
      Spacer(modifier = Modifier.height(50.dp))
      Image(painter = painterResource(id = R.drawable.ic_face1), contentDescription = null, modifier = Modifier.size(100.dp), colorFilter = ColorFilter.tint(Color.Black))
    }
  }
}