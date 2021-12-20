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
import androidx.compose.runtime.livedata.observeAsState
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
    mutableStateOf("https://www.google.com")
  }
  val focusManager = LocalFocusManager.current

  val canGoBack = vm.canGoBack.observeAsState(initial = false)
  val canGoForward = vm.canGoForward.observeAsState(initial = false)

  Scaffold(topBar = {
    TopAppBar(title = { Text("나만의 웹 브라우저") }, actions = {
      IconButton(onClick = { vm.undo() }, enabled = canGoBack.value) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "이전 페이지", tint = if (canGoBack.value) Color.White else Color.Gray)
      }
      IconButton(onClick = { vm.redo() }, enabled = canGoForward.value) {
        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "다음 페이지", tint = if (canGoForward.value) Color.White else Color.Gray)
      }
    })
  }) {
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
        })
      )
      Spacer(modifier = Modifier.height(8.dp))
      MyWebView(vm)
    }
  }
}

@Composable
fun MyWebView(vm: MainViewModel) {
  val webView = rememberWebView(vm)

  LaunchedEffect(Unit) {
    vm.undoSharedFlow.collectLatest { webView.goBack() }
  }

  LaunchedEffect(Unit) {
    vm.redoSharedFlow.collectLatest { webView.goForward() }
  }

  AndroidView(modifier = Modifier.fillMaxSize(),
    factory = { webView },
    update = {
      it.loadUrl(vm.url.value)
    })
}

@Composable
fun rememberWebView(vm: MainViewModel): WebView {
  val context = LocalContext.current

  return remember {
    WebView(context).apply {
      settings.javaScriptEnabled = true
      webViewClient = CustomWebViewClient(vm)
    }
  }
}

class CustomWebViewClient(private val vm: MainViewModel) : WebViewClient() {
  override fun onPageFinished(view: WebView?, url: String?) {
    super.onPageFinished(view, url)
    vm.updateCanGo(view?.canGoBack(), view?.canGoForward())
  }
}

