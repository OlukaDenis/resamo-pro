package com.dennytech.data.remote.models

import com.dennytech.domain.models.AddressDomainModel
import com.dennytech.domain.utils.DomainHelpers.getRandomNumber

data class RemoteAddressModel(
    val address: String?,
    val address2: String?,
    val city: String?,
    val countryCode: String?,
    val postalCode: String?,
    val province: String?,
) {

   companion object {
       fun RemoteAddressModel.toDomain(profileId: String): AddressDomainModel {
           return AddressDomainModel(
               id = getRandomNumber(),
               profileId = profileId,
               userId = profileId,
               address = this.address.orEmpty(),
               address2 =  this.address2.orEmpty(),
               city =  this.city.orEmpty(),
               postalCode =  this.postalCode.orEmpty(),
               countryCode =  this.countryCode.orEmpty(),
               province =  this.province.orEmpty()
           )
       }
   }
}