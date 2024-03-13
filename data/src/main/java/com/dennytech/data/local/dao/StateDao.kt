package com.dennytech.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.dennytech.data.base.BaseDao
import com.dennytech.data.local.models.StateEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface StateDao: BaseDao<StateEntity> {
    @Query("SELECT * FROM state")
    fun get(): Flow<List<StateEntity>>

    @Query("SELECT * FROM state WHERE id = :id")
    fun getById(id: Long): Flow<StateEntity?>

    @Query("DELETE FROM state")
    suspend fun clear()
}