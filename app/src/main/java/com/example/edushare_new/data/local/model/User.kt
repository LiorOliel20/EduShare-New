package com.example.edushare_new.data.local.model

data class User(
    val uid: String,
    var displayName: String? = null,
    var email: String? = null,
    var photoUrl: String? = null
)