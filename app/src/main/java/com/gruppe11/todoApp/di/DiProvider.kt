package com.gruppe11.todoApp.di

import com.gruppe11.todoApp.repository.ISubtaskRepository
import com.gruppe11.todoApp.repository.ITaskRepository
import com.gruppe11.todoApp.repository.ITimeSlotRepository
import com.gruppe11.todoApp.repository.SubtaskRepositoryImpl
import com.gruppe11.todoApp.repository.TaskRepositoryImpl
import com.gruppe11.todoApp.repository.TimeSlotRepositoryImpl
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

    @Provides
    @Singleton
    fun providesSubtaskRepository() : ISubtaskRepository {
        return SubtaskRepositoryImpl()
    }

    @Provides
    @Singleton
    fun providesTimeSlotRepository(): ITimeSlotRepository {
        return TimeSlotRepositoryImpl()
    }
}