package com.tarciodiniz.orgs.extensions

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.math.BigDecimal

class BigDecimalJsonAdapter {
    @FromJson
    fun fromJson(json: String): BigDecimal {
        return BigDecimal(json)
    }

    @ToJson
    fun toJson(value: BigDecimal): String {
        return value.toString()
    }
}
