package com.example.mycrudprj.AddUser

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycrudprj.Data.User
import com.example.mycrudprj.Data.UserRepository
import com.example.mycrudprj.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val repository: UserRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){
    var user by mutableStateOf<User?>(null)
    private set     // can only be changed from within viewModel

    var name by mutableStateOf("")
    private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init{
        // if the add is from existing todo edit, load from database
        val userId = savedStateHandle.get<Int>("userId")!!
        if (userId != -1){
            viewModelScope.launch {
                repository.getUserById(userId)?.let { User ->
                    name = User.name
                    this@AddViewModel.user = User
                }
            }
            Log.d("AddModel", "$userId : $name")
        }
    }

    fun onEvent(event: AddEvent){
        when(event){
            is AddEvent.onUserChange ->{
                name = event.name
                Log.d("ADDEventTag","$name")
            }
            is AddEvent.OnSaveClick ->{
                if (name.isNotEmpty()){
                    viewModelScope.launch {
                        repository.insertUser(
                            User(
                                name = name,
                                id = user?.id
                            )
                        )
                    }
                }
            }
        }
    }

    private fun sendUiEvent(uiEvent: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
}