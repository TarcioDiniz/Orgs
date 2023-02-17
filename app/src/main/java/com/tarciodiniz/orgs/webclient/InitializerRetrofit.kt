package com.tarciodiniz.orgs.webclient

import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.tarciodiniz.orgs.webclient.services.ProductServices
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.math.BigDecimal

class InitializerRetrofit {

    object BigDecimalAdapter {
        @FromJson
        fun fromJson(value: String): BigDecimal {
            return BigDecimal(value)
        }

        @ToJson
        fun toJson(value: BigDecimal): String {
            return value.toString()
        }
    }

//    for didactic purposes, HTTP was allowed in
//    the AndroidManifest.xml file:
//    <application
//    android:usesCleartextTraffic="true">

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.7:8080")
        .addConverterFactory(MoshiConverterFactory.create(
            Moshi.Builder()
                .add(BigDecimalAdapter)
                .build()
        ))
        .build()

    val productServices: ProductServices = retrofit.create(ProductServices::class.java)

}