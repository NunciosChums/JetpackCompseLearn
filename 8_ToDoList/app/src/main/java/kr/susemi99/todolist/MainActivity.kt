package kr.susemi99.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import kr.susemi99.todolist.domain.util.TodoAndroidViewModelFactory
import kr.susemi99.todolist.ui.main.MainScreen
import kr.susemi99.todolist.ui.main.MainViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val vm: MainViewModel = viewModel(factory = TodoAndroidViewModelFactory(application))
      MainScreen(vm)
    }
  }
}