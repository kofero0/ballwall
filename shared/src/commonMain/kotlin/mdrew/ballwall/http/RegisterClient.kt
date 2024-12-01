package mdrew.ballwall.http

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import mdrew.ballwall.Result


fun interface RegisterClient {
    suspend fun register(request: Request): Result<Response, Error>

    data class Response(val apiToken: String?) {
        fun interface Deserializer {
            fun deserialize(response: String): Response
        }
    }

    data class Request(val user: String, val pass: String) {
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

private fun hashedPasswordRegisterSerializer(passwordHasher: (String) -> String) =
    RegisterClient.Request.Serializer { request ->
        "{\"user\":${request.user}, \"passHash\":${passwordHasher(request.pass)}}"
    }

private fun defaultRegisterDeserializer() = RegisterClient.Response.Deserializer { response ->
    RegisterClient.Response(Json.parseToJsonElement(response).jsonObject["token"]?.jsonPrimitive?.content)
}

internal fun defaultRegisterClient(
    httpClient: HttpClient,
    url: String,
    serializer: RegisterClient.Request.Serializer = hashedPasswordRegisterSerializer {
        it.hashCode().toString()
    },
    deserializer: RegisterClient.Response.Deserializer = defaultRegisterDeserializer()
) = RegisterClient { request ->
    val response = httpClient.post(url) {
        setBody(serializer.serialize(request))
    }
    when (response.status) {
        HttpStatusCode.OK -> Result.Success(deserializer.deserialize(response.bodyAsText()))
        else -> Result.Failure(
            RegisterClient.Error.HttpError(
                statusCode = response.status.value, response.bodyAsText()
            )
        )
    }
}



