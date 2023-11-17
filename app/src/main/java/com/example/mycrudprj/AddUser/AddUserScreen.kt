package com.example.mycrudprj.AddUser

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AddUserScreen(
    onPopBackStack: () -> Unit,
    addViewModel: AddViewModel = hiltViewModel()
){
    var addnew by remember { mutableStateOf("") }

    // THis block might not be needed
    /*LaunchedEffect(key1 = true){
        viewModel.uiEvent.collect { event ->
            when(event){
                is UiEvent.popBackStack -> onPopBackStack()
            }
        }
    }*/
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Add New Item",
                color = Color.Green,
                fontSize = 25.sp,
                modifier = Modifier.padding(10.dp)
            )
            Spacer(modifier = Modifier.size(60.dp))

            OutlinedTextField(
                value = addViewModel.name,//addnew,
                onValueChange = {
                    addnew = it
                    addViewModel.onEvent(AddEvent.onUserChange(addnew))
                },
                label = {
                    Text(text = "Add new")
                },
                leadingIcon = {
                    IconButton(onClick = { addnew = "" }) {
                        Icon(imageVector = Icons.Filled.Delete,
                            contentDescription = "Clear text")
                    }
                },
                trailingIcon = {
                    IconButton(onClick = {
                        addViewModel.onEvent(AddEvent.OnSaveClick)
                        addnew = ""
                    }) {
                        Icon(imageVector = Icons.Filled.Done,
                            contentDescription = "Add entry")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    //keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done  // this is to change the style of text ok view
                    //.Go/Send/Previous/Next/Done
                ),
                keyboardActions = KeyboardActions(onSearch = {
                }),
                enabled = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.size(50.dp))
            Button(
                onClick = { onPopBackStack() },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
            ) {
                Text(
                    text = "Done",
                    color = Color.White
                )
            }
        }
    }
}