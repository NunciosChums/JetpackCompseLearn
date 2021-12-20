package kr.susemi99.webbrowser

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
  val url = mutableStateOf("https://google.com")

  private val _undoSharedFlow = MutableSharedFlow<Boolean>()
  val undoSharedFlow = _undoSharedFlow.asSharedFlow()

  private val _redoSharedFlow = MutableSharedFlow<Boolean>()
  val redoSharedFlow = _redoSharedFlow.asSharedFlow()

  private val _canGoBack = MutableLiveData(false)
  val canGoBack: LiveData<Boolean> = this._canGoBack

  private val _canGoForward = MutableLiveData(false)
  val canGoForward: LiveData<Boolean> = _canGoForward

  fun undo() {
    viewModelScope.launch {
      _undoSharedFlow.emit(true)
    }
  }

  fun redo() {
    viewModelScope.launch {
      _redoSharedFlow.emit(true)
    }
  }

  fun updateCanGo(back: Boolean? = false, forward: Boolean? = false) {
    this._canGoBack.value = back
    _canGoForward.value = forward
  }
}