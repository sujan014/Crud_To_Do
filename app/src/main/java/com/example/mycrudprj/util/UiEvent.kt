package com.example.mycrudprj.util

sealed class UiEvent{
    object popBackStack: UiEvent()
    data class Navigate(val route: String): UiEvent()
    data class ShowSnackbar(
        val message: String,
        val action: String?= null
    ): UiEvent()
}
