package kr.susemi99.basic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val (text, setValue) = remember { mutableStateOf("") }
      val scaffoldState = rememberScaffoldState()
      val scope = rememberCoroutineScope()
      val keyboardController = LocalSoftwareKeyboardController.current

      Scaffold(
        scaffoldState = scaffoldState
      ) {
        Column(
          Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          TextField(value = text, onValueChange = setValue)
          Button(onClick = {
            keyboardController?.hide()
            scope.launch { scaffoldState.snackbarHostState.showSnackbar("hello $text") }
          }) {
            Text(text = "클릭!")
          }
        }
      }
    }
  }
}
