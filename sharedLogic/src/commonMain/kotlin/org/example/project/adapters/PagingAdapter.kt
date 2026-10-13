package org.example.project.adapters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.example.project.enums.SearchTypeEnum
import org.example.project.interfaces.IEntity

open class PagingAdapter<T : IEntity>(
    val searchType: SearchTypeEnum = SearchTypeEnum.NONE,
    val title: String = "",
    val searchTitle: String? = null,
    val pageSize: Int = 15,
    val loadData: (success: ((List<T>) -> Unit)?) -> Unit
) : ViewModel() {

    private val _pagingData = MutableStateFlow<List<T>>(emptyList())
    private val _isPagination = MutableStateFlow(true)
    private val _isRefreshing = MutableStateFlow(false)
    private val _isLoadingMore = MutableStateFlow(false) // Идёт догрузка следующей страницы (не refresh)
    private val _search = MutableStateFlow("") // Строка в поле для поиска

    var page: Int = 0
    val offset: Int get() = (page * pageSize)

    val pagingData: MutableStateFlow<List<T>> = _pagingData
    val isPagination: MutableStateFlow<Boolean> = _isPagination
    val isRefreshing: MutableStateFlow<Boolean> = _isRefreshing
    val isLoadingMore: MutableStateFlow<Boolean> = _isLoadingMore // Идёт догрузка следующей страницы (не refresh)
    val search: MutableStateFlow<String> = _search // Строка в поле для поиска

    var requestVersion = 0
    init {
        viewModelScope.launch {
            _search.collect {
                doRefresh()
            }
        }
    }

    fun setSearch(text: String) {
        _search.value = text
    }

    fun doRefresh() {
        requestVersion += 1
        _isRefreshing.value = true
        loadMore { _isRefreshing.value = false }
    }

    fun loadMore(success: (() -> Unit)? = null) {
        val requestRefreshId = requestVersion
        _isPagination.value = false
        if (_isRefreshing.value) clearData()
        _isLoadingMore.value = !_isRefreshing.value
        loadData {
            if (requestRefreshId == requestVersion) {
                addData(it)
                if (it.size == pageSize) _isPagination.value = true
                _isLoadingMore.value = false
                success?.invoke()
            }
        }
    }

    fun addData(newList: List<T>) {
        _pagingData.value = _pagingData.value.plus(newList)
        page += 1
    }

    fun clearData() {
        page = 0
        _pagingData.value = emptyList()
        _isLoadingMore.value = false
    }

}