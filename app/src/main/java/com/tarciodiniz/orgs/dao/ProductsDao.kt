package com.tarciodiniz.orgs.dao

import com.tarciodiniz.orgs.model.Produto
import java.math.BigDecimal

class ProductsDao {

    fun setProduct(produto: Produto) {
        products.add(produto)
    }

    fun getProduct(): List<Produto> {
        return products.toList()
    }

    companion object {
        private val products = mutableListOf<Produto>(
            Produto(
                name = "Cesta de frutas",
                description = "maçã, laranja, acerola",
                value = BigDecimal("11.99")
            )
        )
    }

}