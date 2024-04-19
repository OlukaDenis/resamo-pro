package com.dennytech.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dennytech.data.local.dao.StoreDao
import com.dennytech.data.local.dao.UserDao
import com.dennytech.data.local.models.StoreEntity
import com.dennytech.data.local.models.UserEntity

@Database(
    entities = [
        UserEntity::class,
        StoreEntity::class
    ],
//    views = [
//        DataEntryMeasurementViewEntity::class
//    ],
    version = 1,
    exportSchema = true,
//    autoMigrations = []
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun storeDao(): StoreDao
}