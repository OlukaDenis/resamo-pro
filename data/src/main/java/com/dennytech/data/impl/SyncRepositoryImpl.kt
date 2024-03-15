package com.dennytech.data.impl

import com.dennytech.data.remote.services.ApiService
import com.dennytech.domain.repository.SyncRepository
import javax.inject.Inject

class SyncRepositoryImpl @Inject constructor(
    private val service: ApiService,
): SyncRepository {

}