package com.example.freshfarmroutes.di

import androidx.lifecycle.ViewModel
import com.example.freshfarmroutes.data.repository.HyperRepositoryImpl
import com.example.freshfarmroutes.domain.repository.HyperRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideHyperRepository():HyperRepository=HyperRepositoryImpl()
}