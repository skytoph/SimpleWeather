package com.github.skytoph.simpleweather.domain.weather.mapper

import com.github.skytoph.simpleweather.R

abstract class IndicatorsTooltipMapper : AirQualityTooltipMapper, UltravioletTooltipMapper {
    override fun aq(aq: Int): Int = when {
        aq in 0..20 -> R.string.tooltip_air_quality_1
        aq in 21..40 -> R.string.tooltip_air_quality_2
        aq in 41..60 -> R.string.tooltip_air_quality_3
        aq in 61..80 -> R.string.tooltip_air_quality_4
        aq in 81..100 -> R.string.tooltip_air_quality_5
        aq > 100 -> R.string.tooltip_air_quality_5
        else -> R.string.tooltip_value_unknown
    }

    override fun uv(uv: Int): Int = when (uv) {
        in 0..2 -> R.string.tooltip_ultraviolet_index_low
        in 3..5 -> R.string.tooltip_ultraviolet_index_moderate
        in 6..7 -> R.string.tooltip_ultraviolet_index_high
        in 8..10 -> R.string.tooltip_ultraviolet_index_very_high
        in 11..Int.MAX_VALUE -> R.string.tooltip_ultraviolet_index_extreme
        else -> R.string.tooltip_value_unknown
    }
}