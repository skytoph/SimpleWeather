package com.github.skytoph.simpleweather.core.util.formatter

import javax.inject.Inject

interface ProbabilityFormatter {
    fun format(number: Double): String

    class Base @Inject constructor() : ProbabilityFormatter {

        override fun format(number: Double): String = "${number.toInt()}%"
    }
}