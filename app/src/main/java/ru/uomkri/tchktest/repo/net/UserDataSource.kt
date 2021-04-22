package ru.uomkri.tchktest.repo.net

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import io.reactivex.rxjava3.core.Single
import ru.uomkri.tchktest.repo.Repository

class UserDataSource(
        private var query: String
) : RxPagingSource<Int, BaseUserData>() {
    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, BaseUserData>> {
        val pos = params.key ?: 1

        return Repository.getSearchResults(pos, query)
    }

    override fun getRefreshKey(state: PagingState<Int, BaseUserData>): Int? {
        return null
    }
}