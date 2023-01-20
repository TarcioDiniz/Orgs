package com.tarciodiniz.orgs.dao

import com.tarciodiniz.orgs.model.Produto

class ProductsDao {

    fun setProduct(produto: Produto){
        products.add(produto)
    }

    fun getProduct(): List<Produto>{
        return products.toList()
    }

    companion object {
        private val products = mutableListOf<Produto>()
    }

}