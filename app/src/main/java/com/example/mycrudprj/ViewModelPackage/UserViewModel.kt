package com.example.mycrudprj.ViewModelPackage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycrudprj.Data.User
import com.example.mycrudprj.Data.UserRepository
import com.example.mycrudprj.util.Routes
import com.example.mycrudprj.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel(){

    val users = repository.getAllNames()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun OnEvent(event: UserEvent){
        when(event){
            is UserEvent.OnUserClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_USER + "?userId=${event.user.id}"))
            }
            is UserEvent.OnSaveUserClick->{
                viewModelScope.launch {
                    repository.insertUser(
                        User(
                            name = event.username
                        )
                    )
                }
            }
            is UserEvent.OnDeleteClick ->{
                viewModelScope.launch {
                    repository.deleteUser(event.user)
                }
            }
            is UserEvent.OnDeleteAllClick ->{
                viewModelScope.launch {
                    repository.deleteAll()
                }
            }
            is UserEvent.onAddUserClick ->{
                sendUiEvent(UiEvent.Navigate(Routes.ADD_USER))
            }
        }
    }

    private fun sendUiEvent(uievent: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(uievent)
        }
    }
}