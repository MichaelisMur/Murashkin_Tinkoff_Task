package ru.michaelismur.androiddevtest.web

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.michaelismur.androiddevtest.model.ResponseWrapper
import java.io.IOException


class PostsApi(private val apiService: ApiService) {
    suspend fun getRandomPost() = withContext(IO) {
        try {
            ResponseWrapper.Success(apiService.getRandomPost())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ResponseWrapper.Failure(null, null, true)
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    ResponseWrapper.Failure(code, errorResponse)
                }
                else -> {
                    ResponseWrapper.Failure(null, null, false)
                }
            }
        }
    }

    private fun convertErrorBody(throwable: HttpException): String? {
        return try {
            throwable.response()?.errorBody()?.string()!!
        } catch (exception: Exception) {
            null
        }
    }
}
