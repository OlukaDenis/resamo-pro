package com.dennytech.data.di

import android.content.Context
import com.dennytech.data.utils.EncryptedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.IOException
import java.security.GeneralSecurityException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    internal fun provideEncryptedPref(
        @ApplicationContext context: Context
    ): EncryptedPreferences? {
        return try {
            EncryptedPreferences(context)
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

}