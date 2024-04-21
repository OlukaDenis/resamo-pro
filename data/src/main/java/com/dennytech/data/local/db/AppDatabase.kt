package com.dennytech.data.local.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dennytech.data.local.converters.StringListConverter
import com.dennytech.data.local.dao.StoreDao
import com.dennytech.data.local.dao.StoreUserDao
import com.dennytech.data.local.dao.UserDao
import com.dennytech.data.local.models.StoreEntity
import com.dennytech.data.local.models.StoreUserEntity
import com.dennytech.data.local.models.UserEntity

@Database(
    entities = [
        UserEntity::class,
        StoreUserEntity::class,
        StoreEntity::class
    ],
    version = 1,
    exportSchema = true,
    autoMigrations = [
//        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun storeDao(): StoreDao
    abstract fun storeUserDao(): StoreUserDao
}