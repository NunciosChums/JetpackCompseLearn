package kr.susemi99.todolist.ui.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.susemi99.todolist.R
import kr.susemi99.todolist.domain.model.Todo
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TodoItem(
  todo: Todo,
  onClick: (uid: Int) -> Unit = {},
  onDeleteClick: (uid: Int) -> Unit = {}
) {
  val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

  Row(verticalAlignment = Alignment.CenterVertically) {
    Icon(
      painter = painterResource(id = R.drawable.ic_delete),
      contentDescription = "delete",
      tint = Color(0xffa51212),
      modifier = Modifier
        .padding(8.dp)
        .clickable { onDeleteClick(todo.uid) }
    )
    Column(
      Modifier
        .weight(1f)
        .clickable { onClick(todo.uid) }) {
      Text(
        text = format.format(Date(todo.date)),
        color = if (todo.isDone) Color.Gray else Color.Black,
        style = TextStyle(textDecoration = if (todo.isDone) TextDecoration.LineThrough else TextDecoration.None)
      )

      Text(
        text = todo.title,
        color = if (todo.isDone) Color.Gray else Color.Black,
        style = TextStyle(textDecoration = if (todo.isDone) TextDecoration.LineThrough else TextDecoration.None)
      )
    }

    if (todo.isDone) {
      Icon(
        painter = painterResource(id = R.drawable.ic_done),
        contentDescription = null,
        tint = Color(0xff00bcb4),
        modifier = Modifier.padding(8.dp)
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun TodoItemDonePreview() {
  TodoItem(todo = Todo("제목", date = Date().time, isDone = true))
}

@Preview(showBackground = true)
@Composable
fun TodoItemPreview2() {
  TodoItem(todo = Todo("제목2"))
}