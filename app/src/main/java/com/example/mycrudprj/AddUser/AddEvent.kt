package com.example.mycrudprj.AddUser

sealed class AddEvent{
    data class onUserChange(val name: String): AddEvent()
    object OnSaveClick: AddEvent()
}