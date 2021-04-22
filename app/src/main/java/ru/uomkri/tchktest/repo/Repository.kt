package ru.uomkri.tchktest.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.uomkri.tchktest.repo.net.BaseUserData
import ru.uomkri.tchktest.repo.net.GithubApi
import ru.uomkri.tchktest.repo.net.SearchResult
import ru.uomkri.tchktest.repo.net.User

object Repository {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private val _authSuccess = MutableLiveData<Boolean>(false)
    val authSuccess: LiveData<Boolean>
        get() = _authSuccess

    fun getUser(login: String) {
        GithubApi.retrofitService.getUserData(login)
                .subscribeOn(Schedulers.io())
                .doOnError {
                    _error.value = it.message
                    _error.value = null
                }
                .subscribe {
                    if (it.message == null) {
                        _user.postValue(it)
                    } else {
                        _error.value = it.message
                        _error.value = null
                    }
                }
    }

    fun clearSelectedUser() {
        _user.postValue(null)
    }

    fun setSuccess() {
        _authSuccess.value = true
        _authSuccess.value = false
    }

    fun getSearchResults(pos: Int, query: String): Single<PagingSource.LoadResult<Int, BaseUserData>> {
        return GithubApi.retrofitService.getSearchResult(query, pos)
                .subscribeOn(Schedulers.io())
                .map { toLoadResult(it, pos) }
                .onErrorReturn { PagingSource.LoadResult.Error(it) }
                .doOnError {
                    _error.value = it.message
                    _error.value = null
                }
    }

    private fun toLoadResult(data: SearchResult, pos: Int): PagingSource.LoadResult<Int, BaseUserData> {
        return PagingSource.LoadResult.Page(
                data = data.items,
                prevKey = if (pos == 1) null else pos - 1,
                nextKey = if (pos == data.totalCount) null else pos + 1
        )
    }
}