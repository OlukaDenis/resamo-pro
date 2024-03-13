package com.dennytech.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.dennytech.data.base.BaseDao
import com.dennytech.data.local.models.ProvinceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProvinceDao: BaseDao<ProvinceEntity> {
    @Query("SELECT * FROM province")
    fun get(): Flow<List<ProvinceEntity>>

    @Query("SELECT * FROM province WHERE id = :id")
    fun getById(id: Long): Flow<ProvinceEntity?>

    @Query("DELETE FROM province")
    fun clear()
}