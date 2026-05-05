package com.example.cakebyteapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cakebyteapp.data.local.dao.AuthDao
import com.example.cakebyteapp.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authDao(): AuthDao
}