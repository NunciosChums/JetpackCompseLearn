package kr.susemi99.basic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = "first") {
        composable("first") { FirstScreen(navController) }
        composable("second") { SecondScreen(navController) }
        composable("third/{value}") { backStackEntry ->
          ThirdScreen(
            navController = navController,
            value = backStackEntry.arguments?.getString("value") ?: ""
          )
        }
      }
    }
  }
}

@Composable
fun FirstScreen(navController: NavController) {
  val (text, setValue) = remember { mutableStateOf("") }

  Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
    Text("첫 화면")
    Spacer(Modifier.height(16.dp))
    Button(onClick = { navController.navigate("second") }) {
      Text("두 번째")
    }
    Spacer(Modifier.height(16.dp))
    TextField(value = text, onValueChange = setValue)
    Button(onClick = {
      if (text.isNotBlank()) {
        navController.navigate("third/$text")
      }
    }) {
      Text("세 번째")
    }
  }
}

@Composable
fun SecondScreen(navController: NavController) {
  Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
    Text("두 번째 화면")
    Spacer(Modifier.height(16.dp))
    Button(onClick = { navController.navigateUp() }) {
      Text("뒤로가기")
    }
  }
}

@Composable
fun ThirdScreen(navController: NavController, value: String) {
  Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
    Text("세 번째 화면")
    Spacer(Modifier.height(16.dp))
    Text(value)
    Button(onClick = { navController.navigateUp() }) {
      Text("뒤로가기")
    }
  }
}
