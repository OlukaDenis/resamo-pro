package com.dennytech.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.dennytech.data.base.BaseDao
import com.dennytech.data.local.models.CountryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao: BaseDao<CountryEntity> {
    @Query("SELECT * FROM country")
    fun get(): Flow<List<CountryEntity>>

    @Query("SELECT * FROM country WHERE id = :id")
    fun getById(id: Long): Flow<CountryEntity?>

    @Query("DELETE FROM country")
    fun clear()
}
