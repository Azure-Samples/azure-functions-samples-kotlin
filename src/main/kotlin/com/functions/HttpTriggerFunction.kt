package com.functions

import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.annotation.AuthorizationLevel
import com.microsoft.azure.functions.annotation.FixedDelayRetry
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger
import java.util.*

/**
 * Azure Functions with HTTP Trigger.
 */
class HttpTriggerFunction {

    /**
     * This function listens at endpoint "/api/HttpTriggerKotlin". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpTriggerKotlin&code={your function key}
     * 2. curl "{your host}/api/HttpTriggerKotlin?name=HTTP%20Query&code={your function key}"
     * Function Key is not needed when running locally, it is used to invoke function deployed to Azure.
     * More details: https://aka.ms/functions_authorization_keys
     */
    @FunctionName("HttpTriggerKotlin")
    fun run(
        @HttpTrigger(
            name = "req",
            methods = [HttpMethod.GET, HttpMethod.POST],
            authLevel = AuthorizationLevel.FUNCTION
        ) request: HttpRequestMessage<Optional<String>>,
        context: ExecutionContext
    ): HttpResponseMessage {

        context.logger.info("HTTP trigger processed a ${request.httpMethod.name} request.")

        val query = request.queryParameters["name"]
        val name = request.body.orElse(query)

        name?.let {
            return request
                .createResponseBuilder(HttpStatus.OK)
                .body("Hello, $name!")
                .build()
        }

        return request
            .createResponseBuilder(HttpStatus.BAD_REQUEST)
            .body("Please pass a name on the query string or in the request body")
            .build()
    }

    companion object {
        @JvmField
        var count = 1
    }

    /**
     * This function listens at endpoint "/api/HttpExampleRetry". The function is re-executed in case of errors until the maximum number of retries occur.
     * Retry policies: https://docs.microsoft.com/en-us/azure/azure-functions/functions-bindings-error-pages?tabs=java
     */
    @FunctionName("HttpExampleRetry")
    @FixedDelayRetry(maxRetryCount = 3, delayInterval = "00:00:05")
    @Throws(Exception::class)
    fun HttpExampleRetry(
        @HttpTrigger(
            name = "req",
            methods = [HttpMethod.GET, HttpMethod.POST],
            authLevel = AuthorizationLevel.FUNCTION
        ) request: HttpRequestMessage<Optional<String>>,
        context: ExecutionContext
    ): HttpResponseMessage {
        context.logger.info("Kotlin HTTP trigger processed a request.$count")
        if (count < 3) {
            count++
            throw Exception("error")
        }

        // Parse query parameter
        val query = request.queryParameters["name"]
        val name = request.body.orElse(query)
        name?.let {
            return request
                .createResponseBuilder(HttpStatus.OK)
                .body("Hello, $name!")
                .build()
        }

        return request
            .createResponseBuilder(HttpStatus.BAD_REQUEST)
            .body("Please pass a name on the query string or in the request body")
            .build()
    }


    /**
     * This function listens at endpoint "/api/HttpTriggerJavaVersion".
     * It can be used to verify the Java home and java version currently in use in your Azure function
     */
    @FunctionName("HttpTriggerJavaVersion")
    fun HttpTriggerJavaVersion(
        @HttpTrigger(
            name = "req",
            methods = [HttpMethod.GET, HttpMethod.POST],
            authLevel = AuthorizationLevel.FUNCTION
        ) request: HttpRequestMessage<Optional<String>>,
        context: ExecutionContext
    ): HttpResponseMessage {
        context.logger.info("Java HTTP trigger processed a request.")
        val javaVersion = getJavaVersion()
        context.logger.info("Function - HttpTriggerJavaVersion $javaVersion")

        return request
            .createResponseBuilder(HttpStatus.OK)
            .body("HttpTriggerJavaVersion $javaVersion")
            .build()
    }

    fun getJavaVersion(): String {
        return System.getProperty("java.home") + " - " + System.getProperty("java.version")
    }

}
