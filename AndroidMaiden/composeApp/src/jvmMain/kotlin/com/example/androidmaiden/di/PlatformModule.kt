package com.example.androidmaiden.di

import com.example.androidmaiden.core.experimental.job.BackgroundJobManager
import com.example.androidmaiden.platform.job.DesktopBackgroundJobManager
import org.koin.dsl.module

/**
 * Desktop-specific dependency injection module.
 */
val platformModule = module {
    // Desktop Services
    single<BackgroundJobManager> { DesktopBackgroundJobManager() }
    
    // Add other desktop-specific implementations here
}
