package com.arrayani.datastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.arrayani.datastore.utils.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RoseMedHome : AppCompatActivity() {

    private lateinit var dataStore: DataStore<Preferences>// ambil Preferences yg versi datastore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rose_med_home)

        dataStore = createDataStore(name = Constants.PROFILE)

        lifecycleScope.launch{
            val idTag= read2(Constants.IDTAG)
            println(idTag)
        }
    }

    private suspend fun read2(key: String): String? { //ketika read, kita hanya butuh key, karena kita ga tau value nya apa
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }
}