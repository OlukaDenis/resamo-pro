package com.dennytech.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.dennytech.data.base.BaseDao
import com.dennytech.data.local.models.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao: BaseDao<UserEntity> {

    @Query("SELECT * FROM user LIMIT 1")
    fun getTopUser(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user")
    fun get(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user WHERE id = :id")
    fun getById(id: Long): Flow<UserEntity?>

    @Query("DELETE FROM user")
    fun clear()
}