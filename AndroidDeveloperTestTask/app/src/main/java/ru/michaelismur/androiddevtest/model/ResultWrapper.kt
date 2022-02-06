package ru.michaelismur.androiddevtest.model

sealed class ResponseWrapper<out T> {
    data class Success<out T>(val value: T) : ResponseWrapper<T>()
    data class Failure(
        val code: Int? = null,
        val error: String? = null,
        val networkError: Boolean = false
    ) : ResponseWrapper<Nothing>()
}