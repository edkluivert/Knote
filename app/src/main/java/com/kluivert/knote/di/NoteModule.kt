package com.kluivert.knote.di

import com.kluivert.knote.data.repo.NoteRepository
import com.kluivert.knote.data.viewModel.NoteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { NoteRepository(get()) }

}

val viewModelModule = module {

    viewModel { NoteViewModel() }

}