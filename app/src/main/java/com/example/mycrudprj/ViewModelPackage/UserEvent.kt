package com.example.mycrudprj.ViewModelPackage

import com.example.mycrudprj.Data.User

sealed class UserEvent{
    class OnSaveUserClick(val username: String): UserEvent()
    class OnDeleteClick(val user: User) : UserEvent()
    object OnDeleteAllClick: UserEvent()
    object onAddUserClick: UserEvent()
    data class OnUserClick(val user: User): UserEvent()
}
