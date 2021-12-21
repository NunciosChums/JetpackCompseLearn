package kr.susemi99.todolist.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kr.susemi99.todolist.ui.main.components.TodoItem

@Composable
fun MainScreen(vm: MainViewModel) {
  var text by rememberSaveable { mutableStateOf("") }
  val focusManager = LocalFocusManager.current
  val scaffoldState = rememberScaffoldState()
  val scope = rememberCoroutineScope()

  Scaffold(
    topBar = { TopAppBar(title = { Text("오늘 할 일") }) },
    scaffoldState = scaffoldState
  ) {
    Column(Modifier.fillMaxSize()) {
      OutlinedTextField(
        value = text,
        onValueChange = {
          text = it
        },
        modifier = Modifier
          .fillMaxWidth()
          .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        placeholder = { Text(text = "할 일") },
        trailingIcon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
          vm.addTodo(text)
          text = ""
          focusManager.clearFocus()
        })
      )

      Divider()

      LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        items(vm.items.value) { item ->
          Column {
            TodoItem(todo = item,
              onClick = { vm.toggle(it) },
              onDeleteClick = {
                vm.delete(it)

                scope.launch {
                  val result = scaffoldState.snackbarHostState.showSnackbar(message = "할 일 삭제됨", actionLabel = "취소")
                  if (result == SnackbarResult.ActionPerformed) {
                    vm.restoreTodo()
                  }
                }
              })
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.Black, thickness = 1.dp)
          }
        }
      }
    }
  }
}