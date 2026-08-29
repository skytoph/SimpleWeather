package com.github.skytoph.simpleweather.domain.work

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.github.skytoph.simpleweather.data.work.UpdateWorker
import java.util.UUID
import java.util.concurrent.TimeUnit

interface UpdateForecastWork {
    fun scheduleWork(updateCriteria: Int = UpdateWorker.CRITERIA_DAY)
    fun observeWork(owner: LifecycleOwner, observer: Observer<WorkInfo?>)

    abstract class Abstract(protected val workManager: WorkManager) : UpdateForecastWork {
        protected var workId: UUID? = null

        override fun observeWork(owner: LifecycleOwner, observer: Observer<WorkInfo?>) {
            workId?.let {
                workManager.getWorkInfoByIdLiveData(it).observe(owner, observer)
            }
        }

    }

    class Periodically(workManager: WorkManager) : Abstract(workManager) {

        override fun scheduleWork(updateCriteria: Int) {
            val data =
                Data.Builder().putInt(UpdateWorker.ARG_CRITERIA, updateCriteria)
                    .putBoolean(UpdateWorker.ARG_RETRY, true).build()
            val request =
                PeriodicWorkRequestBuilder<UpdateWorker>(12, TimeUnit.HOURS, 30, TimeUnit.MINUTES)
                    .setConstraints(
                        Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                    )
                    .setBackoffCriteria(
                        BackoffPolicy.LINEAR,
                        PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                        TimeUnit.MILLISECONDS
                    )
                    .setInputData(data)
                    .build()
            workId = request.id
            workManager.enqueueUniquePeriodicWork(
                WORK_NAME, ExistingPeriodicWorkPolicy.REPLACE, request
            )
        }

        private companion object {
            const val WORK_NAME = "update_weather_forecasts_periodically_work"
        }
    }

    class Once(workManager: WorkManager) : Abstract(workManager) {

        override fun scheduleWork(updateCriteria: Int) {
            val data =
                Data.Builder().putInt(UpdateWorker.ARG_CRITERIA, updateCriteria)
                    .putBoolean(UpdateWorker.ARG_RETRY, false).build()
            val request = OneTimeWorkRequestBuilder<UpdateWorker>()
                .setConstraints(
                    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                )
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    0,
                    TimeUnit.MILLISECONDS
                )
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setInputData(data)
                .build()
            workId = request.id
            workManager.enqueueUniqueWork(WORK_NAME, ExistingWorkPolicy.REPLACE, request)
        }

        private companion object {
            const val WORK_NAME = "update_weather_forecasts_once_work"
        }
    }
}
