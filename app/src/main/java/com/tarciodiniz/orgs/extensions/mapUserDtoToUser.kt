package com.tarciodiniz.orgs.extensions

import com.tarciodiniz.orgs.model.User
import com.tarciodiniz.orgs.webclient.dto.UserDTO

fun mapUserDtoToUser(dto: UserDTO): User {
    return User(
        id = dto.username,
        name = dto.name,
        password = dto.password
    )
}