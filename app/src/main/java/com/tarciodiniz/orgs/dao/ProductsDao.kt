package com.tarciodiniz.orgs.dao

import com.tarciodiniz.orgs.model.Produto

class ProductsDao {

    private val products = mutableListOf<Produto>()

    fun setProduct(produto: Produto){
        products.add(produto)
    }

    fun getProduct(): List<Produto>{
        return products.toList()
    }

}