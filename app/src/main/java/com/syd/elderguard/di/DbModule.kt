package com.syd.elderguard.di

import androidx.room.Room
import com.syd.elderguard.db.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dbModule = module {

    single {
        Room
            .databaseBuilder(androidApplication(), AppDatabase::class.java, "Favor")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().relationshipDao() }

    single { get<AppDatabase>().eventDao() }

    single { get<AppDatabase>().accountDao() }

    single { get<AppDatabase>().todoDao() }
}