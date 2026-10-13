package org.example.project.extensions

import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTimeTz
import com.soywiz.klock.DayOfWeek
import com.soywiz.klock.Month
import com.soywiz.klock.Time

fun DateTime.toDisplayString(format: String = "dd.MM.yyyy"): String {
    return this.format(format)
}

fun Time.toDisplaySting(): String {
    return this.format("HH:mm")
}

fun DateTime.toISO8601String(): String {
    return this.format("yyyy-MM-dd'T'HH:mm:ssZ")
}

fun DateTimeTz.toDisplayStringTz(format: String = "dd.MM.yyyy"): String {
    return this.format(format)
}

fun DateTimeTz.toISO8601String(): String {
    return this.format("yyyy-MM-dd'T'HH:mm:ssZ")
}

val Month.russian
    get() = when (this) {
        Month.January -> "Январь"
        Month.February -> "Февраль"
        Month.March -> "Март"
        Month.April -> "Апрель"
        Month.May -> "Май"
        Month.June -> "Июнь"
        Month.July -> "Июль"
        Month.August -> "Август"
        Month.September -> "Сентябрь"
        Month.October -> "Октябрь"
        Month.November -> "Ноябрь"
        Month.December -> "Декабрь"
    }

val Month.russianV2
    get() = when (this) {
        Month.January -> "Января"
        Month.February -> "Февраля"
        Month.March -> "Марта"
        Month.April -> "Апреля"
        Month.May -> "Мая"
        Month.June -> "Июня"
        Month.July -> "Июля"
        Month.August -> "Августа"
        Month.September -> "Сентября"
        Month.October -> "Октября"
        Month.November -> "Ноября"
        Month.December -> "Декабря"
    }

val DayOfWeek.russianShortName
    get() = when (this) {
        DayOfWeek.Sunday -> "вс"
        DayOfWeek.Monday -> "пн"
        DayOfWeek.Tuesday -> "вт"
        DayOfWeek.Wednesday -> "ср"
        DayOfWeek.Thursday -> "чт"
        DayOfWeek.Friday -> "пт"
        DayOfWeek.Saturday -> "сб"
    }