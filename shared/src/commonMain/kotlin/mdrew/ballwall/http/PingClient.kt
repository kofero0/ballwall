package mdrew.ballwall.http

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import mdrew.ballwall.Result


fun interface PingClient {
    suspend fun ping(request: Request): Result<Response, Error>

    data class Response(val version: String, val time: Long) {
        fun interface Deserializer {
            fun deserialize(response: String): Response
        }
    }

    data class Request(val deviceId: String) {
        fun interface Serializer {
            fun serialize(request: Request): String
        }
    }

    sealed interface Error : HttpError {
        data object ConnectionRefused : Error
        data class HttpError(val statusCode: Int, val body: String) : Error
        data object Other : Error
    }
}

private fun pingSerializer() =
    PingClient.Request.Serializer { request ->
        "{\"deviceId\":${request.deviceId}}"
    }

private fun pingDeserializer() = PingClient.Response.Deserializer { response ->
    val json = Json.parseToJsonElement(response).jsonObject
    PingClient.Response(
        version = json["version"]?.jsonPrimitive?.content ?: "",
        time = json["time"]?.jsonPrimitive?.longOrNull ?: 0L
    )
}

internal fun defaultPingClient(
    httpClient: HttpClient,
    url: String,
    serializer: PingClient.Request.Serializer = pingSerializer(),
    deserializer: PingClient.Response.Deserializer = pingDeserializer()
) = PingClient { request ->
    val response = httpClient.post(url) {
        setBody(serializer.serialize(request))
    }
    when (response.status) {
        HttpStatusCode.OK -> Result.Success(deserializer.deserialize(response.bodyAsText()))
        else -> Result.Failure(
            PingClient.Error.HttpError(
                statusCode = response.status.value, response.bodyAsText()
            )
        )
    }
}



