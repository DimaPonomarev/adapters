@file:OptIn(ExperimentalObjCName::class)

package ru.europlan.mws.api.interfaces

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@ObjCName(
    "IEupCheckboxCell",
    swiftName = "IEupCheckboxCell",
    exact = false
)
interface IEupCheckboxCell {
    val idCell: Int
    val titleCell: String
    val subTitleCell: String?
    var isSelectedCell: Boolean
}