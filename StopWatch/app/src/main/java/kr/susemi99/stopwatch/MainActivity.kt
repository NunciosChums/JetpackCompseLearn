package kr.susemi99.stopwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.*
import kotlin.concurrent.timer

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val vm = viewModel<MainViewModel>()
      val sec = vm.sec.value
      val milli = vm.milli.value
      val isRunning = vm.isRunning.value
      val lapTimes = vm.lapTimes.value

      MainScreen(sec = sec, milli = milli, isRunning = isRunning, lapTime = lapTimes,
        onReset = { vm.reset() },
        onToggle = { if (it) vm.pause() else vm.start() },
        onLapTime = { vm.recordLapTime() })
    }
  }
}

@Composable
fun MainScreen(
  sec: Int,
  milli: Int,
  isRunning: Boolean,
  lapTime: List<String>,
  onReset: () -> Unit,
  onToggle: (Boolean) -> Unit,
  onLapTime: () -> Unit
) {
  Scaffold(topBar = {
    TopAppBar(title = { Text("스톱워치") })
  }) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
      Spacer(modifier = Modifier.height(40.dp))

      Row(verticalAlignment = Alignment.Bottom) {
        Text("$sec", fontSize = 100.sp)
        Text("$milli")
      }

      Spacer(modifier = Modifier.height(16.dp))

      Column(Modifier
        .weight(1f)
        .verticalScroll(rememberScrollState())) {
        lapTime.forEach { Text("$it") }
      }

      Row(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {

        FloatingActionButton(onClick = { onReset() }, backgroundColor = Color.Red) {
          Icon(painter = painterResource(id = R.drawable.ic_reset), contentDescription = "reset")
        }

        FloatingActionButton(onClick = { onToggle(isRunning) }, backgroundColor = Color.Green) {
          Icon(painter = painterResource(id = if (isRunning) R.drawable.ic_pause else R.drawable.ic_play), contentDescription = "start/pause")
        }

        Button(onClick = { onLapTime() }) {
          Text("랩 타임")
        }
      }
    }
  }
}

class MainViewModel : ViewModel() {
  private var time = 0
  private var timerTask: Timer? = null

  private val _isRunning = mutableStateOf(false)
  val isRunning: State<Boolean> = _isRunning

  private val _sec = mutableStateOf(0)
  val sec: State<Int> = _sec

  private val _milli = mutableStateOf(0)
  val milli: State<Int> = _milli

  private val _lapTimes = mutableStateOf(mutableListOf<String>())
  val lapTimes: State<List<String>> = _lapTimes

  private var lap = 1


  fun start() {
    _isRunning.value = true
    timerTask = timer(period = 10) {
      time++
      _sec.value = time / 100
      _milli.value = time % 100
    }
  }

  fun pause() {
    _isRunning.value = false
    timerTask?.cancel()
  }

  fun reset() {
    _isRunning.value = false
    timerTask?.cancel()
    time = 0
    _sec.value = 0
    _milli.value = 0
    lap = 1
    _lapTimes.value.clear()
  }

  fun recordLapTime() {
    _lapTimes.value.add(0, "$lap LAP: ${sec.value}:${milli.value}")
    lap++
  }
}