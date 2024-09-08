package com.github.skytoph.simpleweather.di.weather

import com.github.skytoph.simpleweather.data.location.mapper.LocalNameDataMapper
import com.github.skytoph.simpleweather.data.location.mapper.LocationDataMapper
import com.github.skytoph.simpleweather.data.location.mapper.PlaceToCloudMapper
import com.github.skytoph.simpleweather.data.weather.cache.mapper.WeatherDBToDataMapper
import com.github.skytoph.simpleweather.data.weather.cloud.mapper.IndicatorsCloudToDataMapper
import com.github.skytoph.simpleweather.data.weather.cloud.mapper.WeatherCloudToDataMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.ContentDBToDataMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.current.CurrentWeatherDataMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.current.ForecastToCurrentDataMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.DailyForecastDataListMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.DailyForecastDataMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.FindForecastMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.ForecastDBToDataMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.HourlyForecastDataListMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.HourlyForecastDataMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.WarningDataMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.WarningListDataMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.forecast.WeatherCodeMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.horizon.HorizonDataMapper
import com.github.skytoph.simpleweather.data.weather.mapper.content.indicators.ForecastToIndicatorsMapper
import com.github.skytoph.simpleweather.data.weather.update.UpdateWeatherMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class WeatherDataMapperModule {

    @Binds
    abstract fun cloudToDataMapper(mapper: WeatherCloudToDataMapper.Base): WeatherCloudToDataMapper

    @Binds
    abstract fun placeToCloudMapper(mapper: PlaceToCloudMapper.Base): PlaceToCloudMapper

    @Binds
    abstract fun updateMapper(mapper: UpdateWeatherMapper.Base): UpdateWeatherMapper

    @Binds
    abstract fun contentMapper(mapper: ContentDBToDataMapper.Base): ContentDBToDataMapper

    @Binds
    abstract fun findForecastMapper(mapper: FindForecastMapper.Base): FindForecastMapper

    @Binds
    abstract fun forecastToCurrentMapper(mapper: ForecastToCurrentDataMapper.Base): ForecastToCurrentDataMapper

    @Binds
    abstract fun currentWeatherMapper(mapper: CurrentWeatherDataMapper.Base): CurrentWeatherDataMapper

    @Binds
    abstract fun indicatorsMapper(mapper: IndicatorsCloudToDataMapper.Base): IndicatorsCloudToDataMapper

    @Binds
    abstract fun indicatorsDataMapper(mapper: ForecastToIndicatorsMapper.Base): ForecastToIndicatorsMapper

    @Binds
    abstract fun horizonMapper(mapper: HorizonDataMapper.Base): HorizonDataMapper

    @Binds
    abstract fun alertMapper(mapper: WarningDataMapper.Base): WarningDataMapper

    @Binds
    abstract fun alertsMapper(mapper: WarningListDataMapper.Base): WarningListDataMapper

    @Binds
    abstract fun forecastMapper(mapper: ForecastDBToDataMapper.Base): ForecastDBToDataMapper

    @Binds
    abstract fun dailyMapper(mapper: DailyForecastDataMapper.Base): DailyForecastDataMapper

    @Binds
    abstract fun dailyListMapper(mapper: DailyForecastDataListMapper.Base): DailyForecastDataListMapper

    @Binds
    abstract fun hourlyMapper(mapper: HourlyForecastDataMapper.Base): HourlyForecastDataMapper

    @Binds
    abstract fun hourlyListMapper(mapper: HourlyForecastDataListMapper.Base): HourlyForecastDataListMapper

    @Binds
    abstract fun weatherCodeMapper(mapper: WeatherCodeMapper.Base): WeatherCodeMapper

    @Binds
    abstract fun dbToDataMapper(mapper: WeatherDBToDataMapper.Base): WeatherDBToDataMapper

    @Binds
    abstract fun locationMapper(mapper: LocationDataMapper.Base): LocationDataMapper

    @Binds
    abstract fun localNameMapper(mapper: LocalNameDataMapper.Base): LocalNameDataMapper
}