package com.codingwithmitch.openapi.di.main

import com.codingwithmitch.openapi.api.main.OpenApiMainService
import com.codingwithmitch.openapi.persistence.AccountPropertiesDao
import com.codingwithmitch.openapi.persistence.AppDatabase
import com.codingwithmitch.openapi.persistence.BlogPostDao
import com.codingwithmitch.openapi.repository.main.AccountRepository
import com.codingwithmitch.openapi.repository.main.BlogRepository
import com.codingwithmitch.openapi.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {

    @Provides
    @MainScope
    fun provideOpenApiMainService(retrofitBuilder: Retrofit.Builder): OpenApiMainService {
        return retrofitBuilder.build().create(OpenApiMainService::class.java)
    }


    @Provides
    @MainScope
    fun provideAccountRepository(
        openApiMainService: OpenApiMainService,
        accountPropertiesDao: AccountPropertiesDao,
        sessionManager: SessionManager
    ): AccountRepository {
        return AccountRepository(
            openApiMainService,
            accountPropertiesDao,
            sessionManager
        )
    }


    @Provides
    @MainScope
    fun providesBlogPostDao(db: AppDatabase): BlogPostDao {
        return db.getBlogPostDao()
    }


    @Provides
    @MainScope
    fun providesBlogRepository(
        openApiMainService: OpenApiMainService,
        blogPostDao: BlogPostDao,
        sessionManager: SessionManager
    ): BlogRepository {
        return BlogRepository(
            openApiMainService,
            blogPostDao,
            sessionManager
        )
    }

}