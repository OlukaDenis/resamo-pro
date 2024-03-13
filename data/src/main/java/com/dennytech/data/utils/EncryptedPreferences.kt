package com.dennytech.data.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences

import androidx.security.crypto.MasterKey




class EncryptedPreferences(context: Context) {
    private var sharedPreferences: SharedPreferences? = null

    companion object {
        const val ENCRYPTED_PREF = "EncryptedPref"
    }

    init {
        initPreferences(context)
    }

    private fun initPreferences(context: Context) {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            ENCRYPTED_PREF,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun putString(key: String, value: String) {
        sharedPreferences?.edit()?.putString(key, value)?.apply()
    }

    fun getString(key: String): String {
        return sharedPreferences?.getString(key, "") ?: ""
    }

}