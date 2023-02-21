package com.tarciodiniz.orgs.webclient

import com.squareup.moshi.Moshi
import com.tarciodiniz.orgs.extensions.BigDecimalJsonAdapter
import com.tarciodiniz.orgs.webclient.services.ProductServices
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class InitializerRetrofit {

//    for didactic purposes, HTTP was allowed in
//    the AndroidManifest.xml file:
//    <application
//    android:usesCleartextTraffic="true">

    private val moshi: Moshi = Moshi.Builder()
        .add(BigDecimalJsonAdapter())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.6:8080")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val productServices: ProductServices = retrofit.create(ProductServices::class.java)

}