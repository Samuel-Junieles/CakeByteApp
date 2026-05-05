package com.example.cakebyteapp.di

import android.content.Context
import androidx.room.Room
import com.example.cakebyteapp.data.local.AppDatabase
import com.example.cakebyteapp.data.local.dao.AuthDao
import com.example.cakebyteapp.data.repository.AuthRepositoryImpl
import com.example.cakebyteapp.domain.repository.AuthRepository
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
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cakebyte_db"
        )
        .fallbackToDestructiveMigration() // Evita crashes por cambios de versión
        .build()
    }

    @Provides
    fun provideAuthDao(db: AppDatabase): AuthDao {
        return db.authDao()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authDao: AuthDao): AuthRepository {
        return AuthRepositoryImpl(authDao)
    }
}