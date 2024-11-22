package mdrew.wallball.strings

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import java.io.PrintWriter


data class StringResource(val key: String, val values: List<StringValue>) {
    data class StringValue(val locale: String, val value: String)
}

fun main() {
    if(!File("../iosApp").exists() || !File("../composeApp").exists()){
        error("do not run!")
    }
    object {}.javaClass.classLoader?.getResourceAsStream("strings.json")?.let { inStream ->
        val resourceList = mutableListOf<StringResource>()
        Json.parseToJsonElement(inStream.bufferedReader().readText()).jsonArray.forEach { element ->
            val key = element.jsonObject["key"]?.jsonPrimitive?.contentOrNull ?: ""
            val values = mutableListOf<StringResource.StringValue>().apply {
                element.jsonObject["values"]?.jsonArray?.forEach { value ->
                    add(
                        StringResource.StringValue(
                            locale = value.jsonObject["locale"]?.jsonPrimitive?.content ?: "",
                            value = value.jsonObject["value"]?.jsonPrimitive?.content ?: ""
                        )
                    )
                }
            }
            resourceList.add(StringResource(key, values))
        }
        val writerMap = mutableMapOf<String, PrintWriter>()
        resourceList.forEach { resource ->
            resource.values.forEach { value ->
                val dir = File("../composeApp/src/commonMain/composeResources/values-${value.locale}")
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                val file = File(dir, "strings.xml")
                if (!file.exists()) {
                    file.createNewFile()
                }
                writerMap[value.locale] = file.printWriter()
            }
        }
        writerMap.entries.forEach { entry ->
            entry.value.use { out ->
                out.print("<resources>")
                resourceList.forEach { resource ->
                    resource.values.find { it.locale == entry.key }?.let { localeValue ->
                        out.print("<string name=\"${resource.key}\">${localeValue.value}</string>")
                    }
                }
                out.print("</resources>")
            }
        }
        val iosStrings = File("../iosApp/Localizable.xcstrings")
        if(!iosStrings.exists()) { iosStrings.createNewFile() }
        iosStrings.printWriter().use { out ->
            out.print("{\"sourceLanguage\":\"en\",\"strings\":{")
            resourceList.forEach { resource ->
                out.print("\"${resource.key}\":{\"extractionState\":\"manual\",\"localizations\":{")
                resource.values.forEach { value ->
                    out.print("\"${value.locale}\":{\"stringUnit\":{\"state\":\"translated\",\"value\":\"${value.value}\"}}")
                    if (resource.values.indexOf(value) + 1 != resource.values.size) {
                        out.print(",")
                    }
                }
                out.print("}}")
                if (resourceList.indexOf(resource) + 1 != resourceList.size) {
                    out.print(",")
                }
            }
            out.print("},\"version\":\"1.0\"}")
        }
    }
}
