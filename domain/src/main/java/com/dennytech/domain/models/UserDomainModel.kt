package com.dennytech.domain.models

open class PersonDomainModel(
    open val firstName: String,
    open val lastName: String,
    open val lastLogin: String,
    open  val email: String,
    open val phone: String,
    open val status: Int,
    open val fullName: String,
    open val role: Int,
)

data class UserDomainModel(
    val id: String,
    override val firstName: String,
    override val lastName: String,
    override val lastLogin: String,
    override val email: String,
    override val phone: String,
    override val status: Int,
    override val fullName: String,
    override val role: Int,
    val stores: List<StoreDomainModel>,
    val defaultStore: String
) : PersonDomainModel(firstName, lastName, lastLogin, email, phone, status, fullName, role) {

    companion object {
        fun  UserDomainModel.isAdmin() = this.role == 1 || this.role == 0
        fun UserDomainModel.isOwner() = this.role == 0
    }
}

data class StoreUserDomainModel(
    val id: String,
    val storeId: String,
    val stores: List<StoreDomainModel>,
    val defaultStore: String,
    override val firstName: String,
    override val lastName: String,
    override val lastLogin: String,
    override val email: String,
    override val phone: String,
    override val status: Int,
    override val fullName: String,
    override val role: Int
) : PersonDomainModel(firstName, lastName, lastLogin, email, phone, status, fullName, role) {

    companion object {
        fun  UserDomainModel.isAdmin() = this.role == 1 || this.role == 0
        fun UserDomainModel.isOwner() = this.role == 0
    }
}