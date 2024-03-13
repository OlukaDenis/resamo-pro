package com.dennytech.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.dennytech.data.base.BaseDao
import com.dennytech.data.local.models.ProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao: BaseDao<ProfileEntity> {
    @Query("SELECT * FROM profile")
    fun get(): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM profile WHERE id = :id")
    fun getById(id: Long): Flow<ProfileEntity?>

    @Query("DELETE FROM profile")
    fun clear()
}