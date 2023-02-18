package com.tarciodiniz.orgs.webclient.model.json

import com.tarciodiniz.orgs.webclient.dto.ProductDto

data class ProductListResponse(
    val content: List<ProductDto>,
    val pageable: Pageable,
    val totalElements: Int,
    val totalPages: Int,
    val last: Boolean,
    val size: Int,
    val number: Int,
    val sort: Sort,
    val numberOfElements: Int,
    val first: Boolean,
    val empty: Boolean
)
