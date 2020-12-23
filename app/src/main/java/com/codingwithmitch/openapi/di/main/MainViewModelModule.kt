package com.codingwithmitch.openapi.di.main

import androidx.lifecycle.ViewModel
import com.codingwithmitch.openapi.di.ViewModelKey
import com.codingwithmitch.openapi.ui.main.account.AccounViewModel
import com.codingwithmitch.openapi.ui.main.blog.viewmodel.BlogViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AccounViewModel::class)
    abstract fun bindAccountViewModel(authViewModel: AccounViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BlogViewModel::class)
    abstract fun bindBlogViewModel(blogViewModel: BlogViewModel): ViewModel

}