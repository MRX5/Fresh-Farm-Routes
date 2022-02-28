package com.example.freshfarmroutes.di

import com.example.freshfarmroutes.domain.repository.HyperRepository
import com.example.freshfarmroutes.domain.use_case.GetBranches
import com.example.freshfarmroutes.domain.use_case.GetHyper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCasesModule {

    @Provides
    @ViewModelScoped
    fun provideGetBranchesUseCase(repository: HyperRepository):GetBranches=GetBranches(repository)

    @Provides
    @ViewModelScoped
    fun provideGetHyperUseCase(repository: HyperRepository):GetHyper= GetHyper(repository)
}