package com.aravind.todolist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aravind.todolist.ui.theme.TODOListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TODOListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFDED6D6) // Light gray background
                ) {
                    MainPage()
                }


            }
        }
    }
}


@Composable
fun MainPage() {


    val myContext = LocalContext.current
    val todoName = remember { mutableStateOf("") }
    val itemsList = readData(myContext)
    val deleteDialogStatus = remember { mutableStateOf(false) }
    val clickedItemIndex = remember { mutableStateOf(0) }
    val updateDialogStatus = remember { mutableStateOf(false) }
    val clickedItem = remember { mutableStateOf("") }
    val textDialogStatus = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.padding(15.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextField(
                value = todoName.value,
                onValueChange = {
                    todoName.value = it
                },

                label = { Text(text = "Enter Task", fontSize = 15.sp) },

                // colour // TextFieldColors has been deprecated
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedContainerColor = Color(0xFF25B4D9),
                    unfocusedContainerColor = Color(0xFF5ABBD3)
                ),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
                    .weight(7f)
                    .height(60.dp),
                singleLine = true,
                textStyle = TextStyle(textAlign = TextAlign.Center)

            )
            Spacer(modifier = Modifier.padding(5.dp))

            Button(
                onClick = {
                    if (todoName.value.isNotEmpty()) {
                        itemsList.add(todoName.value)
                        writeDate(itemsList, myContext) // Save the updated list
                        todoName.value = "" // Clear the input field after adding
                    } else {
                        Toast.makeText(myContext, "Please enter a task", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .weight(3f)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.green),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(1.dp, Color.Black)

            ) {

                Text(
                    text = "Add Task",
                    color = Color.White,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )
            }

        }

        LazyColumn {
            items(
                count = itemsList.size, // Replace with your dynamic list size
                itemContent = { index ->
                    val item = itemsList[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        shape = RoundedCornerShape(0.dp),
                        border = BorderStroke(1.dp, Color.Black),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item,
                                fontSize = 18.sp,
                                color = Color.White,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .weight(1f) // Allow text to take available space
                                    .clickable{
                                        clickedItem.value = item // Set the current item to the input field for editing
                                        textDialogStatus.value = true
                                    }
                            )
                            // Add any additional UI elements here, like a delete button

                            Row(

                            ) {
                                IconButton(onClick = {
                                    updateDialogStatus.value = true
                                    clickedItemIndex.value = index
                                    clickedItem.value =item // Set the current item to the input field for editing
                                }) {
                                    Icon(
                                        Icons.Filled.Edit,
                                        contentDescription = "Edit",
                                        tint = Color.White
                                    )
                                }
                                IconButton(onClick = {
                                    deleteDialogStatus.value = true
                                    clickedItemIndex.value = index
                                }) {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.White
                                    )
                                }
                            }


                        }
                    }

                }
            )
        }

        if(deleteDialogStatus.value){
            AlertDialog(onDismissRequest = {
                deleteDialogStatus.value = false
            },
                title = { Text(text = "Delete Task") },
                text = { Text(text = "Are you sure you want to delete this task?") },
                confirmButton = {
                    Button(onClick = {
                        itemsList.removeAt(clickedItemIndex.value)
                        writeDate(itemsList, myContext) // Save the updated list after deletion
                        deleteDialogStatus.value = false
                    }) {
                        Text(text = "Yes")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        deleteDialogStatus.value = false
                    }) {
                        Text(text = "No")
                    }
                }
            )
            }

        if(updateDialogStatus.value){
            AlertDialog(onDismissRequest = {
                updateDialogStatus.value = false
            },
                title = { Text(text = "Update Task") },
                text = { TextField(
                    value = clickedItem.value,
                    onValueChange = {
                    clickedItem.value = it
                }) },
                confirmButton = {
                    Button(onClick = {
                        itemsList[clickedItemIndex.value] = clickedItem.value
                        writeDate(itemsList, myContext) // Save the updated list after deletion
                        updateDialogStatus.value = false
                        Toast.makeText(myContext, "Task Updated", Toast.LENGTH_SHORT).show()
                    }) {
                        Text(text = "Yes")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        updateDialogStatus.value = false
                    }) {
                        Text(text = "No")
                    }
                }
            )
        }


        if(textDialogStatus.value){
            AlertDialog(onDismissRequest = {
                textDialogStatus.value = false
            },
                title = { Text(text = "Todo Items") },
                text = {
                    Text(text = clickedItem.value)
                },

                confirmButton = {
                    Button(onClick = {

                        textDialogStatus.value = false

                    }) {
                        Text(text = "OK")
                    }
                }
            )
        }
        }

    }



