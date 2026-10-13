package org.example.project.enums

enum class SearchTypeEnum {

    NONE, // Поисковое поле отсутствует
    SEARCH_IN_LIST, // Введенное значение (в поисковое поле) фильтрует существующий список
    SEARCH_FOR_QUERY // Введенное значение (в поисковое поле) отправляется в апи для получения нового списка

}