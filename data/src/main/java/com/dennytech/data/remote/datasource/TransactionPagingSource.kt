package com.dennytech.data.remote.datasource

import android.annotation.SuppressLint
import android.net.http.HttpException
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dennytech.data.remote.models.RemoteTransactionModel.Companion.toDomain
import com.dennytech.data.remote.services.ApiService
import com.dennytech.data.utils.MAX_PAGE_SIZE
import com.dennytech.domain.models.TransactionDomainModel
import java.io.IOException

class TransactionPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, TransactionDomainModel>() {
    override fun getRefreshKey(state: PagingState<Int, TransactionDomainModel>): Int? {
        return state.anchorPosition
    }

    @SuppressLint("NewApi")
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TransactionDomainModel> {
        return try {
            val currentPage = params.key ?: 1
            val request = HashMap<String, Any>().apply {
                this["page"] = currentPage
                this["pageSize"] = MAX_PAGE_SIZE
            }
            val transactions = apiService.getTransactions(request)
            val list = transactions.data.map { it.toDomain() }

            LoadResult.Page(
                data = list,
                prevKey = if(currentPage == 1) null else currentPage - 1,
                nextKey = if(list.isEmpty()) null else currentPage + 1
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}