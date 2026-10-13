@file:OptIn(ExperimentalObjCName::class)
package ru.europlan.mws.api.interfaces

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@ObjCName(
    "IEupGroup",
    swiftName = "IEupGroup",
    exact = false
)
interface IEupGroup<T: IEupCheckboxCell> {
    val idGroup: Int

    val titleGroup: String

    var isSelectedGroup: Boolean

    var isExpandedGroup: Boolean

    val children: List<T>
}