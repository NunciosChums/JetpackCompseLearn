package kr.susemi99.todolist.data.repository

import android.app.Application
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import kr.susemi99.todolist.data.data_source.TodoDatabase
import kr.susemi99.todolist.domain.model.Todo
import kr.susemi99.todolist.domain.repository.TodoRepository

class TodoRepositoryImpl(application: Application) : TodoRepository {
  private val db = Room.databaseBuilder(application, TodoDatabase::class.java, "todo-db").build()

  override fun observeTodos(): Flow<List<Todo>> {
    return db.todoDao().todos()
  }

  override suspend fun addTodo(todo: Todo) {
    return db.todoDao().insert(todo)
  }

  override suspend fun updateTodo(todo: Todo) {
    return db.todoDao().update(todo)
  }

  override suspend fun deleteTodo(todo: Todo) {
    return db.todoDao().delete(todo)
  }
}