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
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val SUPABASE_URL = "https://memoeqamvxzlxmxxxnkt.supabase.co"
    private const val SUPABASE_ANON_KEY = "sb_publishable_3ThJXD3rkfblpR6KYTnVXg_HhjpJucj"

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_ANON_KEY
        ) {
            install(Auth)
            install(Postgrest)
        }
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cakebyte_db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideAuthDao(db: AppDatabase): AuthDao {
        return db.authDao()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authDao: AuthDao, supabaseClient: SupabaseClient): AuthRepository {
        return AuthRepositoryImpl(authDao, supabaseClient)
    }
}
