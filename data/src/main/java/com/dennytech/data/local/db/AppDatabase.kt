package com.dennytech.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dennytech.data.local.dao.AddressDao
import com.dennytech.data.local.dao.CountryDao
import com.dennytech.data.local.dao.PaymentMethodDao
import com.dennytech.data.local.dao.ProfileDao
import com.dennytech.data.local.dao.ProvinceDao
import com.dennytech.data.local.dao.StateDao
import com.dennytech.data.local.dao.UserDao
import com.dennytech.data.local.models.AddressEntity
import com.dennytech.data.local.models.CountryEntity
import com.dennytech.data.local.models.PaymentMethodEntity
import com.dennytech.data.local.models.ProfileEntity
import com.dennytech.data.local.models.ProvinceEntity
import com.dennytech.data.local.models.StateEntity
import com.dennytech.data.local.models.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ProvinceEntity::class,
        StateEntity::class,
        CountryEntity::class,
        PaymentMethodEntity::class,
        AddressEntity::class,
        ProfileEntity::class
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
    abstract fun countryDao(): CountryDao
    abstract fun stateDao(): StateDao
    abstract fun provinceDao(): ProvinceDao
    abstract fun paymentMethodDao(): PaymentMethodDao
    abstract fun addressDao(): AddressDao
    abstract fun profileDao(): ProfileDao
}