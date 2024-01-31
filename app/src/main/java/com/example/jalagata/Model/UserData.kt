package com.example.jalagata.Model

data class UserData(
    val name: String,
    val email: String,
    val profileImage: String = ""
) {
    constructor() : this(name = "", email = "", profileImage = "")
}
