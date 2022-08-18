package com.functions

import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.annotation.AuthorizationLevel
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger
import com.microsoft.azure.functions.annotation.TimerTrigger
import java.time.LocalDateTime
import java.util.*

/**
 * Azure Functions with Timer Trigger.
 */
class TimerTriggerFunction {
    /**
     * This function will be invoked periodically according to the specified schedule.
     * The below function is executed each time the minutes have a value divisible by five
     */
    @FunctionName("TimerTrigger")
    fun timerHandler(
        @TimerTrigger(
            name = "timerInfo",
            schedule = "0 */5 * * * *"
        ) timerInfo: String?,
        context: ExecutionContext
    ) {
        context.logger.info("Java Timer trigger function executed at: " + LocalDateTime.now())
    }

}
