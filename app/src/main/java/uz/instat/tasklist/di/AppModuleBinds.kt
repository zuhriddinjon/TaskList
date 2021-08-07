package uz.instat.tasklist.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.instat.tasklist.framework.datasource.MainDataSource
import uz.instat.tasklist.framework.datasource.MainDataSourceImpl
import uz.instat.tasklist.framework.repo.MainRepository
import uz.instat.tasklist.framework.repo.MainRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBinds {

    @Singleton
    @Binds
    abstract fun bindMainDataSource(mainDataSourceImpl: MainDataSourceImpl): MainDataSource

    @Singleton
    @Binds
    abstract fun bindMainRepository(mainRepositoryImpl: MainRepositoryImpl): MainRepository

}