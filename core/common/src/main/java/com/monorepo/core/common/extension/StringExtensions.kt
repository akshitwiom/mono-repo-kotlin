package com.monorepo.core.common.extension

/**
 * Returns true if the string is a valid phone number (10+ digits).
 */
fun String.isValidPhoneNumber(): Boolean =
    this.filter { it.isDigit() }.length >= 10

/**
 * Returns true if the string is a valid OTP (exactly 6 digits).
 */
fun String.isValidOtp(): Boolean =
    this.length == 6 && this.all { it.isDigit() }

/**
 * Returns true if the string is a plausible username (3–30 alphanumeric + underscore).
 */
fun String.isValidUsername(): Boolean =
    this.length in 3..30 && this.all { it.isLetterOrDigit() || it == '_' }

/**
 * Returns true if the string is a non-blank password of at least 6 characters.
 */
fun String.isValidPassword(): Boolean =
    this.length >= 6 && this.isNotBlank()
