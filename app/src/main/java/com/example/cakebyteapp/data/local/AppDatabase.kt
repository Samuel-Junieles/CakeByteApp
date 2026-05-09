package com.example.cakebyteapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cakebyteapp.data.local.dao.AuthDao
import com.example.cakebyteapp.data.local.dao.ProductDao
import com.example.cakebyteapp.data.local.entity.ProductEntity
import com.example.cakebyteapp.data.local.entity.UserEntity

@Database(entities = [UserEntity::class, ProductEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authDao(): AuthDao
    abstract fun productDao(): ProductDao
}
