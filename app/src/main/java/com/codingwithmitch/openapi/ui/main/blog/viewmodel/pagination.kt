package com.codingwithmitch.openapi.ui.main.blog.viewmodel

import android.util.Log
import com.codingwithmitch.openapi.ui.main.blog.state.BlogStateEvent.*
import com.codingwithmitch.openapi.ui.main.blog.state.BlogViewState

fun BlogViewModel.resetPage() {
    val update = getCurrentViewStateOrNew()
    update.blogFields.page = 1
    setViewState(update)
}

fun BlogViewModel.loadFirstPage() {
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    setStateEvent(BlogSearchEvent())
}

fun BlogViewModel.incrementPageNumber() {
    val update = getCurrentViewStateOrNew()
    val page = update.copy().blogFields.page
    update.blogFields.page = page + 1
    setViewState(update)
}

fun BlogViewModel.nextPage() {
    if (!getIsQueryExhausted()
        && !getIsQueryInProgress()
    ) {
        Log.d(TAG, "BlogViewModel:  Attempting to load nextPage.. ")
        incrementPageNumber()
        setQueryInProgress(true)
        setStateEvent(BlogSearchEvent())
    }
}


fun BlogViewModel.handleIncommingBlogListData(blogViewState: BlogViewState) {
    setQueryExhausted(blogViewState.blogFields.isQueryExhausted)
    setQueryInProgress(blogViewState.blogFields.isQueryInProgress)
    setBlogListData(blogViewState.blogFields.blogList)
}