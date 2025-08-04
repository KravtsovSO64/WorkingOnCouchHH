package ru.practicum.android.diploma.util

sealed class Resource<T>(val code: Int? = null, val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(code: Int? = null, message: String, data: T? = null) : Resource<T>(code, data, message)
}
