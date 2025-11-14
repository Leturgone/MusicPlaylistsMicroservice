package rabbit

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.serialization.json.Json

class RabbitConsumer(private val queueName: String, private val onMessageReceived: (GenreMessage) -> Unit) {
    val connectionFactory = ConnectionFactory().apply {
        host = "localhost"
        port = 5672
        username = "guest"
        password = "guest"
    }

    private val connection = connectionFactory.newConnection()
    private val channel = connection.createChannel()

    fun subscribe() {
        channel.queueDeclare(queueName, true, false, false, null)
        val deliverCallback = DeliverCallback { consumerTag, delivery ->
            val messageBody = String(delivery.body)
            println("Raw message body: $messageBody")
            val messageMap: GenreMessage = Json.decodeFromString<GenreMessage>(messageBody)
            onMessageReceived(messageMap)
        }
        channel.basicConsume(queueName, true, deliverCallback, { _ -> })
    }
}