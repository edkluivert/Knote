package com.kluivert.knote.di

import com.kluivert.knote.data.repo.NoteRepository
import com.kluivert.knote.data.viewModel.NoteViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { NoteRepository(get()) }

}

@InternalCoroutinesApi
val viewModelModule = module {

    viewModel { NoteViewModel(get()) }

}