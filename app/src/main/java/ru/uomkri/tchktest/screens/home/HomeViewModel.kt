package ru.uomkri.tchktest.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import androidx.paging.rxjava3.observable
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.uomkri.tchktest.repo.Repository
import ru.uomkri.tchktest.repo.net.BaseUserData
import ru.uomkri.tchktest.repo.net.UserDataSource

class HomeViewModel : ViewModel() {

    val error = Repository.error

    @ExperimentalCoroutinesApi
    fun getSearchResults(query: String): Observable<PagingData<BaseUserData>> {
        return Pager(
                config = PagingConfig(
                        pageSize = 30,
                        enablePlaceholders = false
                ),
                pagingSourceFactory = { UserDataSource(query) }
        ).observable.cachedIn(viewModelScope)
    }
}