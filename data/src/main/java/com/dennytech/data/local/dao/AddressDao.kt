package com.dennytech.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.dennytech.data.base.BaseDao
import com.dennytech.data.local.models.AddressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao: BaseDao<AddressEntity> {
    @Query("SELECT * FROM address")
    fun get(): Flow<List<AddressEntity>>

    @Query("SELECT * FROM address WHERE id = :id")
    fun getById(id: Long): Flow<AddressEntity?>

    @Query("DELETE FROM address")
    fun clear()
}