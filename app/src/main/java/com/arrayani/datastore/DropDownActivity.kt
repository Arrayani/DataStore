package com.arrayani.datastore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.arrayani.datastore.databinding.ActivityDropDownBinding
import com.arrayani.datastore.utils.Constants
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class DropDownActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDropDownBinding
    var xglobal : String? = null
    private lateinit var dataStore: DataStore<Preferences>// ambil Preferences yg versi datastore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDropDownBinding.inflate(layoutInflater)
        val scope = CoroutineScope(Dispatchers.Default)
        dataStore = createDataStore(name = Constants.PROFILE)

        setContentView(binding.root)  //setContentView(R.layout.activity_drop_down)

        val feelings = resources.getStringArray(R.array.feelings)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, feelings)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        binding.syncBtn.setOnClickListener{

           scope.launch {
               xglobal = async { helloMessage() }.await()
               //println(xglobal)
           }
            println(xglobal)
        }
        //println(xglobal)

        binding.admBtn.setOnClickListener{

            lifecycleScope.launch {
                val idTag = "Admin"
                val intent = Intent(this@DropDownActivity,AdminHome::class.java)
                save2(Constants.IDTAG, idTag)
             //   val intent = Intent(this@DropDownActivity,AdminHome::class.java)
                startActivity(intent)
            }
        }
        binding.rosemedBtn.setOnClickListener{

            lifecycleScope.launch {
                val idTag = "Rose Medical"
                val intent = Intent(this@DropDownActivity,RoseMedHome::class.java)
                save2(Constants.IDTAG, idTag)
                //   val intent = Intent(this@DropDownActivity,AdminHome::class.java)
                startActivity(intent)
            }
        }
    }

    private suspend fun helloMessage(): String = withContext(Dispatchers.Default) {
        //logMessage("in helloMessage")
        val a = "1"
        delay(1000)
        //"Hello "
        a
    }
    private suspend fun save2(key: String, value: String) {
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }
    private suspend fun read2(key: String): String? { //ketika read, kita hanya butuh key, karena kita ga tau value nya apa
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }
}