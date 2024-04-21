package com.dennytech.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.dennytech.data.base.BaseDao
import com.dennytech.data.local.models.StoreUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreUserDao: BaseDao<StoreUserEntity> {

    @Query("SELECT * FROM store_user LIMIT 1")
    fun getTopUser(): Flow<List<StoreUserEntity>>

    @Query("SELECT * FROM store_user")
    fun get(): Flow<List<StoreUserEntity>>

    @Query("SELECT * FROM store_user WHERE id = :id")
    fun getById(id: String): Flow<StoreUserEntity?>

    @Query("SELECT * FROM store_user WHERE storeId = :storeId")
    fun getStoreUsers(storeId: String): Flow<List<StoreUserEntity>>

    @Query("DELETE FROM store_user")
    fun clear()
}