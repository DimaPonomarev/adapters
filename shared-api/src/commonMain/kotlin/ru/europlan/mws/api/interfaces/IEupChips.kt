@file:OptIn(ExperimentalObjCName::class)

package ru.europlan.mws.api.interfaces

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@ObjCName(
    "IEupChips",
    swiftName = "IEupChips",
    exact = false
)
interface IEupChips {
    val idChips: Int
    val titleChips: String
    var isSelectedChips: Boolean
}