package com.dennytech.data.remote.datasource

import android.annotation.SuppressLint
import android.net.http.HttpException
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dennytech.data.remote.models.RemoteSaleModel.Companion.toDomain
import com.dennytech.data.remote.services.ApiService
import com.dennytech.data.utils.MAX_PAGE_SIZE
import com.dennytech.domain.models.SaleDomainModel
import java.io.IOException

class SalePagingSource(
    private val apiService: ApiService,
    private val filters: HashMap<String, Any>
) : PagingSource<Int, SaleDomainModel>() {
    override fun getRefreshKey(state: PagingState<Int, SaleDomainModel>): Int? {
        return state.anchorPosition
    }

    @SuppressLint("NewApi")
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SaleDomainModel> {
        return try {
            val currentPage = params.key ?: 1

            filters["page"] = currentPage
            filters["pageSize"] = MAX_PAGE_SIZE

            val response = apiService.fetchSales(filters)
            val list = response.data.map { it.toDomain() }

            LoadResult.Page(
                data = list,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (list.isEmpty()) null else currentPage + 1
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}
