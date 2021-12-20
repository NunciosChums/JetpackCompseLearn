package kr.susemi99.webbrowser

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val vm = viewModel<MainViewModel>()
      HomeScreen(vm)
    }
  }
}

@Composable
fun HomeScreen(vm: MainViewModel) {
  val (url, setUrl) = rememberSaveable {
    mutableStateOf("https://google.com")
  }
  val focusManager = LocalFocusManager.current
  val scaffoldState = rememberScaffoldState()

  Scaffold(topBar = {
    TopAppBar(title = { Text("나만의 웹 브라우저") }, actions = {
      IconButton(onClick = { vm.undo() }) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "이전 페이지", tint = Color.White)
      }
      IconButton(onClick = { vm.redo() }) {
        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "다음 페이지", tint = Color.White)
      }
    })
  }, scaffoldState = scaffoldState) {
    Column(Modifier.fillMaxWidth()) {
      OutlinedTextField(
        value = url,
        onValueChange = setUrl,
        label = { Text("https://") },
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
          vm.url.value = url
          focusManager.clearFocus()
        }),
      )
      Spacer(modifier = Modifier.height(8.dp))
      MyWebView(vm, scaffoldState)
    }
  }
}

@Composable
fun MyWebView(vm: MainViewModel, scaffoldState: ScaffoldState) {
  val webView = rememberWebView()

  LaunchedEffect(Unit) {
    vm.undoSharedFlow.collectLatest {
      if (webView.canGoBack())
        webView.goBack()
      else
        scaffoldState.snackbarHostState.showSnackbar("뒤로 갈 수 없습니다.")
    }
  }

  LaunchedEffect(Unit) {
    vm.redoSharedFlow.collectLatest {
      if (webView.canGoForward())
        webView.goForward()
      else
        scaffoldState.snackbarHostState.showSnackbar("앞으로 갈 수 없습니다.")
    }
  }

  AndroidView(modifier = Modifier.fillMaxSize(),
    factory = { webView },
    update = { it.loadUrl(vm.url.value) })
}

@Composable
fun rememberWebView(): WebView {
  val context = LocalContext.current
  return remember {
    WebView(context).apply {
      settings.javaScriptEnabled = true
      webViewClient = WebViewClient()
      loadUrl("https://google.com")
    }
  }
}
