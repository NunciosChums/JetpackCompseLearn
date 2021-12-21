package kr.susemi99.todolist.domain.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.susemi99.todolist.data.repository.TodoRepositoryImpl
import kr.susemi99.todolist.domain.repository.TodoRepository
import kr.susemi99.todolist.ui.main.MainViewModel

class TodoAndroidViewModelFactory(
  private val application: Application,
  private val repository: TodoRepository = TodoRepositoryImpl(application)
) :
  ViewModelProvider.AndroidViewModelFactory(application) {
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
      MainViewModel(application, repository) as T
    } else {
      super.create(modelClass)
    }
  }
}