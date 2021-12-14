package kr.susemi99.basic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      HomeScreen()
    }
  }
}

@Composable
fun HomeScreen(viewModel: MainViewModel = viewModel()) {
  val text3: State<String> = viewModel.data.observeAsState("hello world")
  Column() {
    Text(text3.value)
    Button(onClick = { viewModel.changeValue(System.currentTimeMillis().toString()) }) {
      Text("클릭")
    }
  }
}

class MainViewModel : ViewModel() {
  private val _data = MutableLiveData<String>()
  val data: LiveData<String> = _data

  fun changeValue(value: String) {
    _data.value = value
  }
}