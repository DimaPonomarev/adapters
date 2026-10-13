package org.example.project.adapters

import org.example.project.interfaces.IEntity
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

abstract class IdentifiableEntity : IEntity {
    @OptIn(ExperimentalUuidApi::class)
    val uuid: Uuid = Uuid.random()
}