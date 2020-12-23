package com.codingwithmitch.openapi.ui.main.blog.state

import com.codingwithmitch.openapi.models.BlogPost

data class BlogViewState(

    //BlogFragment Var

    var blogFields: BlogFields = BlogFields(),

//ViewBlogFragments vars

    var viewBlogFields: ViewBlogFields = ViewBlogFields()

//UpdateBlogFragments vars

) {

    data class BlogFields(
        var blogList: List<BlogPost> = ArrayList<BlogPost>(),
        var serchQuery: String = "",
        var page: Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false
    )

    data class ViewBlogFields(
        var blogPost: BlogPost? = null,
        var isAutherOfBlogPost: Boolean = false
    )

}