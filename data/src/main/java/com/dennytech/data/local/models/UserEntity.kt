package com.dennytech.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey


open class Person(
    open val firstName: String,
    open val lastName: String,
    open val lastLogin: String,
    open  val email: String,
    open val phone: String,
    open val status: Int,
    open val fullName: String,
    open val role: Int,
)

@Entity(
    tableName = "user",
)
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    override val firstName: String,
    override val lastName: String,
    override val lastLogin: String,
    override val email: String,
    override val phone: String,
    override val status: Int,
    override val fullName: String,
    override val role: Int,
) : Person(firstName, lastName, lastLogin, email, phone, status, fullName, role)

@Entity(
    tableName = "store_user",
)
data class StoreUserEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val storeId: String,
    override val firstName: String,
    override val lastName: String,
    override val lastLogin: String,
    override val email: String,
    override val phone: String,
    override val status: Int,
    override val fullName: String,
    override val role: Int,
) : Person(firstName, lastName, lastLogin, email, phone, status, fullName, role)