package com.dennytech.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.dennytech.data.base.BaseDao
import com.dennytech.data.local.models.ProductCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductCategoryDao : BaseDao<ProductCategoryEntity> {
    @Query("SELECT * FROM product_category LIMIT 1")
    fun getTopUser(): Flow<List<ProductCategoryEntity>>

    @Query("SELECT * FROM product_category")
    fun get(): Flow<List<ProductCategoryEntity>>

    @Query("SELECT * FROM product_category WHERE id = :id")
    fun getById(id: String): Flow<ProductCategoryEntity?>

    @Query("SELECT * FROM product_category WHERE storeId = :storeId")
    fun getStoreCategories(storeId: String): Flow<List<ProductCategoryEntity>>

    @Query("DELETE FROM product_category")
    fun clear()
}