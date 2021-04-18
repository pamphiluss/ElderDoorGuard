package com.syd.elderguard.di

import com.syd.elderguard.ui.viewmodel.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { RelationshipViewModel(get(), get()) }

    viewModel { EventListViewModel(get(), get()) }

    viewModel { RelationshipListViewModel(get()) }

    viewModel { AddAccountViewModel(get()) }

    viewModel { HomeViewModel(get(),get(), get()) }

    viewModel { SearchViewModel(get())}

    viewModel { DashboardViewModel(get()) }

    viewModel { TodoListViewModel(get()) }

    viewModel { TodoAddViewModel(get()) }

    viewModel { MineViewModel(get(), get(), get()) }
}