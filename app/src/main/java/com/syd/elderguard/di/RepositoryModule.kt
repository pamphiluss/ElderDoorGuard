package com.syd.elderguard.di

import com.syd.elderguard.repository.RelationshipRepository
import org.koin.dsl.module

val repositoryModule = module {

    single { RelationshipRepository(get()) }

}