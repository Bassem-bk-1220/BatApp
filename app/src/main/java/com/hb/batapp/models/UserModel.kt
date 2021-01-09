package com.hb.batapp.models

data class UserModel(
    val fullName: String,
    val email: String,
    val password: String,
    val phoneNumber: String
) {
    constructor() : this("", "", "", "")
}