package io.marcofracassi

import org.assertj.core.api.Assertions.assertThat
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest
import software.amazon.awssdk.services.sqs.model.Message
import software.amazon.awssdk.services.sqs.model.PurgeQueueRequest
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest
import java.net.URI

class SqsMessage(private val endpoint: String, private val queue: String) {

    fun empty() {
        sqsClient().purgeQueue(PurgeQueueRequest.builder().queueUrl(queueUrl()).build())
    }

    fun assertExists(message: String) {
        assertThat(read().single { it.body() == message }).isNotNull
    }

    private fun read(): MutableList<Message> {
        val receiveMessageRequest = ReceiveMessageRequest.builder().queueUrl(queueUrl()).build()
        return sqsClient().receiveMessage(receiveMessageRequest).messages()
    }

    private fun queueUrl() = sqsClient().getQueueUrl(GetQueueUrlRequest.builder().queueName(queue).build()).queueUrl()

    private fun sqsClient(): SqsClient {
        val credentialsProvider = StaticCredentialsProvider.create(AwsBasicCredentials.create("ignore", "ignore"))
        return SqsClient.builder().credentialsProvider(credentialsProvider)
            .endpointOverride(URI(endpoint))
            .region(Region.AP_EAST_1)
            .build()
    }
}