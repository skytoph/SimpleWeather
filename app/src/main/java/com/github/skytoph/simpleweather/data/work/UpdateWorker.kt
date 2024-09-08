package com.github.skytoph.simpleweather.data.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.github.skytoph.simpleweather.core.Logger
import com.github.skytoph.simpleweather.domain.weather.mapper.UpdatedLately
import com.github.skytoph.simpleweather.domain.work.UpdateForecastInteractor
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class UpdateWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val interactor: UpdateForecastInteractor,
    private val notification: LoadingNotification,
    private val logger: Logger,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        if (notification.isAllowed()) setForeground(getForegroundInfo())
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val criteria = map(inputData.getInt(ARG_CRITERIA, CRITERIA_ANY))
                val updated = interactor.updateForecasts(criteria)
                Result.success(Data.Builder().putBoolean(ARG_RESULT, updated).build())
            } catch (exception: Exception) {
                logger.log(this::class.java.simpleName, exception)
                val retry = inputData.getBoolean(ARG_RETRY, false)
                if (retry) Result.retry() else Result.failure()
            }
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo = notification.createForegroundInfo()

    private fun map(input: Int): UpdatedLately = when (input) {
        CRITERIA_DAY -> UpdatedLately.LastDay
        CRITERIA_HOUR -> UpdatedLately.LastHour
        else -> UpdatedLately.Anytime
    }

    companion object {
        const val CRITERIA_ANY = 1000
        const val CRITERIA_DAY = 1001
        const val CRITERIA_HOUR = 1002
        const val ARG_CRITERIA = "arg_update_criteria"
        const val ARG_RETRY = "arg_retry"
        const val ARG_RESULT = "arg_result"
    }
}