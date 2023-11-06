package com.gruppe11.todoApp.di

import com.gruppe11.todoApp.repository.ITaskRepository
import com.gruppe11.todoApp.repository.TaskRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiProvider {

    @Provides
    @Singleton
    fun providesTaskRepository() : ITaskRepository {
        return TaskRepositoryImpl()
    }
}