package ru.uomkri.tchktest.repo.net

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.github.com"

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val logging = HttpLoggingInterceptor()

private val client = OkHttpClient.Builder().addInterceptor(logging.apply {
    level = HttpLoggingInterceptor.Level.BODY
}).build()


private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .baseUrl(BASE_URL)
        .client(client)
        .build()


interface GithubApiService {
    @GET("users/{userLogin}")
    fun getUserData(
            @Path("userLogin") userLogin: String?
    ): Observable<User>

    @GET("search/users")
    fun getSearchResult(
            @Query("q") query: String,
            @Query("page") page: Int?,
            @Query("per_page") perPage: Int = 30
    ): Single<SearchResult>


}

object GithubApi {
    val retrofitService: GithubApiService by lazy {
        retrofit.create(GithubApiService::class.java)
    }
}
