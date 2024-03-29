package com.github.skytoph.simpleweather.domain.weather.mapper

import com.github.skytoph.simpleweather.R
import com.github.skytoph.simpleweather.core.Mapper
import com.github.skytoph.simpleweather.core.util.formatter.TimeFormatter
import com.github.skytoph.simpleweather.presentation.weather.model.WarningUi
import javax.inject.Inject

interface WarningDomainToUiMapper : Mapper<WarningUi.Basic> {
    fun map(
        event: String,
        time: Long,
        started: Boolean,
        description: String,
    ): WarningUi.Basic

    class Base @Inject constructor(
        private val timeFormatter: TimeFormatter,
    ) : WarningDomainToUiMapper {
        override fun map(
            event: String,
            time: Long,
            started: Boolean,
            description: String,
        ): WarningUi.Basic = WarningUi.Basic(
            event,
            description,
            timeFormatter.timeFull(time),
            if (started) R.string.expected_warning_end_time else R.string.expected_warning_start_time
        )
    }
}