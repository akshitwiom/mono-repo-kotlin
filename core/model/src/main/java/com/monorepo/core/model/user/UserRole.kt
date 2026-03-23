package com.monorepo.core.model.user

import kotlinx.serialization.Serializable

@Serializable
enum class UserRole {
    PARTNER,
    INSTALLER,
}
