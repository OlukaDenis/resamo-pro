package com.dennytech.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.dennytech.data.base.BaseDao
import com.dennytech.data.local.models.PaymentMethodEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PaymentMethodDao: BaseDao<PaymentMethodEntity> {
    @Query("SELECT * FROM payment_method")
    fun get(): Flow<List<PaymentMethodEntity>>

    @Query("SELECT * FROM payment_method WHERE id = :id")
    fun getById(id: Long): Flow<PaymentMethodEntity?>

    @Query("DELETE FROM payment_method")
    fun clear()
}
