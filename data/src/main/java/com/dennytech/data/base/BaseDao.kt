package com.dennytech.data.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T): Long

//    @Insert
//    suspend fun insert( entity: T): LongArray

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: T): Int

    @Delete
    suspend fun delete(entity: T): Int
}

//@Transaction
suspend inline fun <reified T> BaseDao<T>.insertOrUpdate(item: T) {
    if (insert(item) != -1L) return
    update(item)
}