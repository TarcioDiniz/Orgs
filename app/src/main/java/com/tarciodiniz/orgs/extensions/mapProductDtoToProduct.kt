package com.tarciodiniz.orgs.extensions

import com.tarciodiniz.orgs.model.Product
import com.tarciodiniz.orgs.webclient.dto.ProductDto

fun mapProductDtoToProduct(dto: ProductDto): Product {
    return Product(
        id = dto.id.toString(),
        name = dto.name,
        description = dto.description,
        value = dto.valueProduct,
        image = dto.image,
        userID = dto.userID
    )
}
