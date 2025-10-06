package com.firman.gita.batombe.di

import android.content.Context
import com.firman.gita.batombe.data.local.datastore.AuthPreferences
import com.firman.gita.batombe.data.local.datastore.OnboardingPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Provides
    @Singleton
    fun provideAuthPreferences(@ApplicationContext context: Context): AuthPreferences {
        return AuthPreferences(context)
    }

    @Provides
    @Singleton
    fun provideOnBoardingPreferences(@ApplicationContext context: Context): OnboardingPreferences{
        return OnboardingPreferences(context)
    }
}