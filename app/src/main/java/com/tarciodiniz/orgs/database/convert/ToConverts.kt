package com.tarciodiniz.orgs.database.convert

import androidx.room.TypeConverter
import java.math.BigDecimal

class ToConverts {

    @TypeConverter
    fun fromDouble(value: Double?): BigDecimal {
        return value?.let { BigDecimal(value.toString()) } ?: BigDecimal.ZERO
    }

    @TypeConverter
    fun bigDecimalToDouble(value: BigDecimal?): Double?{
        return value?.let { value.toDouble() }
    }

}