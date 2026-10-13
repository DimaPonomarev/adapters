@file:OptIn(ExperimentalObjCName::class)

package ru.europlan.mws.api.interfaces

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@ObjCName(
    "IEupCalendar",
    swiftName = "IEupCalendar",
    exact = false
)
interface IEupCalendar {
    val calendarDateFrom: String?
    val calendarDateTo: String?
}