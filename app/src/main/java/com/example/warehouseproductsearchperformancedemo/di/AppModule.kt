package com.example.warehouseproductsearchperformancedemo.di

import android.content.Context
import com.example.warehouseproductsearchperformancedemo.data.AppDatabase
import com.example.warehouseproductsearchperformancedemo.data.ProductDao
import com.example.warehouseproductsearchperformancedemo.util.DatabaseSeeder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }
    
    @Provides
    @Singleton
    fun provideProductDao(database: AppDatabase): ProductDao {
        return database.productDao()
    }
    
    @Provides
    @Singleton
    fun provideDatabaseSeeder(database: AppDatabase): DatabaseSeeder {
        return DatabaseSeeder(database)
    }
} 