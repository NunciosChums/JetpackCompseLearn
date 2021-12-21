package kr.susemi99.todolist.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
  val title: String,
  val date: Long = System.currentTimeMillis(),
  val isDone: Boolean = false,
) {
  @PrimaryKey(autoGenerate = true)
  var uid: Int = 0
}
