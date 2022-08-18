package com.functions

import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.OutputBinding
import com.microsoft.azure.functions.annotation.*

/**
 * Azure Functions with Azure Storage Blob.
 * https://docs.microsoft.com/en-us/azure/azure-functions/functions-bindings-storage-blob-trigger?tabs=java
 */
class BlobTriggerFunction {
    /**
     * This function will be invoked when a new or updated blob is detected at the specified path. The blob contents are provided as input to this function.
     * The location of the blob is provided in the path parameter.
     */
    @FunctionName("BlobTriggerToBlobTest")
    @StorageAccount("AzureWebJobsStorage")
    fun BlobTriggerToBlobTest(
        @BlobTrigger(
            name = "triggerBlob",
            path = "input/{name}",
            dataType = "binary"
        ) triggerBlob: ByteArray,
        @BindingName("name") fileName: String,
        @BlobInput(name = "inputBlob", path = "input/{name}", dataType = "binary") inputBlob: ByteArray,
        @BlobOutput(name = "outputBlob", path = "output/{name}", dataType = "binary"
        ) outputBlob: OutputBinding<ByteArray>,
        context: ExecutionContext
    ) {
        context.logger.info(
            """Kotlin Blob trigger function BlobTriggerToBlobTest processed a blob. Name: $fileName Size: ${triggerBlob.size} Bytes"""
        )
        outputBlob.value = inputBlob
    }
}