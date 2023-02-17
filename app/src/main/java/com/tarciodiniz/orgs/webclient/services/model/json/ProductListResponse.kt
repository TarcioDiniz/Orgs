package com.tarciodiniz.orgs.webclient.services.model.json

import com.tarciodiniz.orgs.model.Product

data class ProductListResponse(
    val content: List<Product>,
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
