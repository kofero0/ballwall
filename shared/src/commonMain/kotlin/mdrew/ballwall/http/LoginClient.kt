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

fun interface LoginClient {
    suspend fun login(request: Request): Result<Response, Error>

    data class Request(val user: String, val pass: String) {
        fun interface Serializer {
            fun serialize(request: Request): String
        }
    }

    data class Response(val apiToken: String, val time: Long) {
        fun interface Deserializer {
            fun deserialize(response: String): Response
        }
    }

    sealed interface Error : HttpError {
        data object ConnectionRefused : Error
        data class HttpError(val statusCode: Int, val body: String) : Error
        data object Other : Error
    }
}

private fun hashedPasswordLoginSerializer(hasher: (String) -> String) =
    LoginClient.Request.Serializer { request ->
        "{\"user\":${request.user}, \"passHash\":${hasher(request.pass)}}"
    }

private fun defaultLoginDeserializer() = LoginClient.Response.Deserializer { response ->
    val json = Json.parseToJsonElement(response).jsonObject
    LoginClient.Response(
        apiToken = json["token"]?.jsonPrimitive?.content ?: "",
        time = json["time"]?.jsonPrimitive?.longOrNull ?: 0L
    )
}

internal fun defaultLoginClient(
    httpClient: HttpClient,
    url: String,
    serializer: LoginClient.Request.Serializer = hashedPasswordLoginSerializer {
        it.hashCode().toString()
    },
    deserializer: LoginClient.Response.Deserializer = defaultLoginDeserializer()
) = LoginClient { request ->
    val response = httpClient.post(url) {
        setBody(serializer.serialize(request))
    }
    when (response.status) {
        HttpStatusCode.OK -> Result.Success(deserializer.deserialize(response.bodyAsText()))
        else -> Result.Failure(
            LoginClient.Error.HttpError(
                statusCode = response.status.value, response.bodyAsText()
            )
        )
    }
}