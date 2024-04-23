package com.dennytech.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("product_category")
data class ProductCategoryEntity(
    @PrimaryKey
    val id: String,
    val storeId: String,
    val name: String
)
