package kr.susemi99.fatcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlin.math.pow

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val viewModel = viewModel<BmiViewModel>()
      val navController = rememberNavController()
      val bmi = viewModel.bmi.value

      NavHost(navController = navController, startDestination = "home") {
        composable("home") {
          HomeScreen { height, weight ->
            viewModel.calculateBmi(height, weight)
            navController.navigate("result")
          }
        }
        composable("result") { ResultScreen(navController, bmi = bmi) }
      }
    }
  }
}

@Composable
fun HomeScreen(onResultClicked: (Double, Double) -> Unit) {
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
      Button(onClick = {
        if (height.isNotBlank() && weight.isNotBlank()) {
          onResultClicked.invoke(height.toDouble(), weight.toDouble())
        }
      }, Modifier.align(Alignment.End)) {
        Text("결과")
      }
    }
  }
}

@Composable
fun ResultScreen(navController: NavController, bmi: Double) {
  val text = when {
    bmi >= 35 -> "고도비만"
    bmi >= 30 -> "2단계 비만"
    bmi >= 25 -> "1단계 비만"
    bmi >= 23 -> "과체중"
    bmi >= 18.5 -> "정상"
    else -> "저체중"
  }

  val imageResId = when {
    bmi >= 23 -> R.drawable.ic_face1
    bmi >= 18.5 -> R.drawable.ic_face2
    else -> R.drawable.ic_face3
  }

  Scaffold(topBar = {
    TopAppBar(
      title = { Text("비만도 계산기") },
      navigationIcon = { Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "", modifier = Modifier.clickable { navController.popBackStack() }) })
  }) {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
      Text(text, fontSize = 30.sp)
      Spacer(modifier = Modifier.height(50.dp))
      Image(painter = painterResource(id = imageResId), contentDescription = null, modifier = Modifier.size(100.dp), colorFilter = ColorFilter.tint(Color.Black))
    }
  }
}

class BmiViewModel : ViewModel() {
  private val _bmi = mutableStateOf(0.0)
  val bmi: State<Double> = _bmi

  fun calculateBmi(height: Double, weight: Double) {
    _bmi.value = weight / (height / 100.0).pow(2)
  }
}