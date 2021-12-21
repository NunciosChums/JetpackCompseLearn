package kr.susemi99.todolist.ui.main

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kr.susemi99.todolist.domain.model.Todo
import kr.susemi99.todolist.domain.repository.TodoRepository

class MainViewModel(application: Application, private val todoRepository: TodoRepository) : AndroidViewModel(application) {
  private val _items = mutableStateOf(emptyList<Todo>())
  val items: State<List<Todo>> = _items

  private var recentlyDeleteTodo: Todo? = null

  init {
    viewModelScope.launch {
      todoRepository.observeTodos().collect { todos ->
        _items.value = todos
      }
    }
  }

  fun addTodo(text: String) {
    viewModelScope.launch {
      todoRepository.addTodo(Todo(title = text))
    }
  }

  fun toggle(uid: Int) {
    findItem(uid)?.let {
      viewModelScope.launch {
        todoRepository.updateTodo(it.copy(isDone = !it.isDone).apply { this.uid = it.uid })
      }
    }
  }

  fun delete(uid: Int) {
    findItem(uid)?.let {
      viewModelScope.launch {
        todoRepository.deleteTodo(it)
        recentlyDeleteTodo = it
      }
    }
  }

  fun restoreTodo() {
    viewModelScope.launch {
      todoRepository.addTodo(recentlyDeleteTodo ?: return@launch)
      recentlyDeleteTodo = null
    }
  }

  private fun findItem(uid: Int) = items.value.find { it.uid == uid }
}