package com.example.mycrudprj.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

//@ntity acts as Table for database
@Entity
data class User(
    @PrimaryKey
    val id: Int? = null,
    val name: String
)