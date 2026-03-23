package com.monorepo.core.common.result

/**
 * A sealed interface representing the outcome of an operation.
 * Used across domain and data layers to communicate success/failure
 * without relying on exceptions for expected error paths.
 */
sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable, val message: String? = null) : Result<Nothing>
    data object Loading : Result<Nothing>
}

/**
 * Maps the [Success] value to a new type using the given [transform].
 */
inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.Success(transform(data))
    is Result.Error -> this
    is Result.Loading -> this
}

/**
 * Returns the [Success] value or the [defaultValue] if the result is not [Success].
 */
fun <T> Result<T>.getOrDefault(defaultValue: T): T = when (this) {
    is Result.Success -> data
    else -> defaultValue
}

/**
 * Returns the [Success] value or null if the result is not [Success].
 */
fun <T> Result<T>.getOrNull(): T? = when (this) {
    is Result.Success -> data
    else -> null
}
