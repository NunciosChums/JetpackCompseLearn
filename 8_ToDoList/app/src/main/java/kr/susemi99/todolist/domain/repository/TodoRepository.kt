package kr.susemi99.todolist.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.susemi99.todolist.domain.model.Todo

interface TodoRepository {
  fun observeTodos(): Flow<List<Todo>>

  suspend fun addTodo(todo: Todo)

  suspend fun updateTodo(todo: Todo)

  suspend fun deleteTodo(todo: Todo)
}