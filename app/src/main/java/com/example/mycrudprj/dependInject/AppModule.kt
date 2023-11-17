package com.example.mycrudprj.dependInject

import android.app.Application
import androidx.room.Room
import com.example.mycrudprj.Data.UserDatabase
import com.example.mycrudprj.Data.UserRepository
import com.example.mycrudprj.Data.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// tell dagger, we have module

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
// define functions how dependencies should be created

    // Start by defining how dagger can create our room database
    @Provides
    @Singleton
    fun providesTodoDatabase(app: Application): UserDatabase {
        return Room.databaseBuilder(
            app,
            UserDatabase::class.java,
            "todo_db"
        ).build()
    }

    //when we create above function, dagger hilt knows how to construct this object to be able to inject it into single class.

    // we will inject these into central place which is viewmodels

    // Dagger hilt knows how to create database instance, but it doesn't know how to create repository instance, we need to define that.
    // for that we need database instance
    @Provides
    @Singleton
    fun provideUserRepository(db: UserDatabase) : UserRepository{
        return UserRepositoryImpl(db.dao)
    }

    // After writing this function, Dagger now knows how to create repository.
    // Next step is to implement viewModels
    // Normally we have 1 viewModel / screen
}