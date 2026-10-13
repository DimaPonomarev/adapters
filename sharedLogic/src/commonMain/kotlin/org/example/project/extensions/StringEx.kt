package org.example.project.extensions

import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.parse
import com.soywiz.klock.parseUtc

fun String.toDateTime(): DateTime? {
    val dateFormats = listOf(
        DateFormat("dd.MM.yyyy"),
        DateFormat("yyyy-MM-dd'T'HH:mm:ss"),
        DateFormat("yyyy-MM-dd'T'HH:mm:ssZ"),
        DateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'"),
        DateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"),
        DateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
        DateFormat("yyyy-MM-dd")
    )

    dateFormats.forEach {
        try {
            val dateTime = it.parse(this)
            return it.parseUtc(this)
        } catch (ex: Throwable) {

        }
    }

    return null
}

fun String.toISO8601String() = this.toDateTime()?.toISO8601String()

fun String.trimToSimpleFormatAndCastToDateTime(): DateTime? {
    return this.replace(Regex("T.*"), "").toDateTime()
}

fun String.toDoubleIgnoreWhiteSpaces() = replace(" ".toRegex(), "").toDouble()

fun String.toMaskPhone(): String {
    var result = ""
    if (this.isNotEmpty() && this.filter { it.isDigit() }.length > 10) {
        val num = "+${this.filter { it.isDigit() }}"
        result += num.substring(0, if (num.length >= 2) 2 else num.length)
        result += " (${num.substring(2, if (num.length >= 5) 5 else num.length)})"
        result += " ${num.substring(5, if (num.length >= 8) 8 else num.length)}"
        result += " ${num.substring(8, if (num.length >= 10) 10 else num.length)}"
        result += " ${num.substring(10, if (num.length >= 12) 12 else num.length)}"
    }
    return result.ifEmpty { this }
}

fun String?.isNotNullAndNotEmpty(): Boolean {
    return this.isNullOrEmpty().not()
}

fun String.toDateString() = toDateTime()?.toDisplayString()

fun String.isDigit() = all { it.isDigit() }

fun String.toDateTimeTz(): DateTimeTz? {
    val dateFormats = listOf(
        DateFormat("dd.MM.yyyy"),
        DateFormat("yyyy-MM-dd'T'HH:mm:ss"),
        DateFormat("yyyy-MM-dd'T'HH:mm:ssZ"),
        DateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'"),
        DateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"),
        DateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSS'Z'"),
        DateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
        DateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'"),
        DateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'"),
        DateFormat("yyyy-MM-dd")
    )

    dateFormats.forEach {
        try {
            return it.parseUtc(this).local
        } catch (ex: Throwable) {
            //Napier.d { "toDateTime ex:${ex}" }
        }
    }

    return null
}


inline fun String?.letIfNotEmpty(block: (String) -> Unit) {
    if (!this.isNullOrBlank()) block(this)
}
