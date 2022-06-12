package com.shakib.etheriumchecker.di

import android.app.Application
import com.shakib.etheriumchecker.model.ModelResult
import com.shakib.etheriumchecker.network.ApiCheckBalance
import com.shakib.etheriumchecker.network.NetworkRequestHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    fun provideDashboardApi(retrofit: Retrofit): ApiCheckBalance {
        return retrofit.create(ApiCheckBalance::class.java)
    }


    @Provides
    fun provideCheckApiBalanceHelper(application: Application) =
        NetworkRequestHelper<ModelResult>(application)


}