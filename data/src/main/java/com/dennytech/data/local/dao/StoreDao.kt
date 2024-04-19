package com.dennytech.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.dennytech.data.base.BaseDao
import com.dennytech.data.local.models.StoreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao: BaseDao<StoreEntity> {

    @Query("SELECT * FROM store")
    fun get(): Flow<List<StoreEntity>>

    @Query("SELECT * FROM store WHERE id = :id")
    suspend fun getById(id: String): List<StoreEntity>

    @Query("DELETE FROM store")
    fun clear()
}