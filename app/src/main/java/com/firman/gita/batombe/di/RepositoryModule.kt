package com.firman.gita.batombe.di

import com.firman.gita.batombe.data.repository.analyze.AnalyzeRepository
import com.firman.gita.batombe.data.repository.analyze.AnalyzeRepositoryImpl
import com.firman.gita.batombe.data.repository.article.ArticleRepository
import com.firman.gita.batombe.data.repository.article.ArticleRepositoryImpl
import com.firman.gita.batombe.data.repository.example_video.ExampleVideoRepository
import com.firman.gita.batombe.data.repository.example_video.ExampleVideoRepositoryImpl
import com.firman.gita.batombe.data.repository.feed.FeedRepository
import com.firman.gita.batombe.data.repository.feed.FeedRepositoryImpl
import com.firman.gita.batombe.data.repository.generatePantun.GeneratePantunRepository
import com.firman.gita.batombe.data.repository.generatePantun.GeneratePantunRepositoryImpl
import com.firman.gita.batombe.data.repository.history.HistoryRepository
import com.firman.gita.batombe.data.repository.history.HistoryRepositoryImpl
import com.firman.gita.batombe.data.repository.login.LoginRepository
import com.firman.gita.batombe.data.repository.login.LoginRepositoryImpl
import com.firman.gita.batombe.data.repository.register.RegisterRepository
import com.firman.gita.batombe.data.repository.register.RegisterRepositoryImpl
import com.firman.gita.batombe.data.repository.speech.SpeechRepository
import com.firman.gita.batombe.data.repository.speech.SpeechRepositoryImpl
import com.firman.gita.batombe.data.repository.user.UserRepository
import com.firman.gita.batombe.data.repository.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindLoginRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository

    @Binds
    @Singleton
    abstract fun bindRegisterRepository(registerRepositoryImpl: RegisterRepositoryImpl): RegisterRepository

    @Binds
    @Singleton
    abstract fun bindArticleRepository(articleRepositoryImpl: ArticleRepositoryImpl): ArticleRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindSpeechRepository(speechRepositoryImpl: SpeechRepositoryImpl): SpeechRepository

    @Binds
    @Singleton
    abstract fun bindAnalyzeRepository(analyzeRepositoryImpl: AnalyzeRepositoryImpl): AnalyzeRepository

    @Binds
    @Singleton
    abstract fun bindHistoryRepository(historyRepositoryImpl: HistoryRepositoryImpl): HistoryRepository

    @Binds
    @Singleton
    abstract fun bindGeneratePantunRepository(generatePantunRepositoryImpl: GeneratePantunRepositoryImpl): GeneratePantunRepository

    @Binds
    @Singleton
    abstract fun bindExampleVideoRepository(exampleVideoRepositoryImpl: ExampleVideoRepositoryImpl): ExampleVideoRepository

    @Binds
    @Singleton
    abstract fun bindFeedRepository(feedRepositoryImpl: FeedRepositoryImpl): FeedRepository
}