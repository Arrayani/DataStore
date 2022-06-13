package com.arrayani.datastore

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.arrayani.datastore.databinding.ActivityMainBinding
import com.arrayani.datastore.utils.Constants
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    private lateinit var dataStore: DataStore<Preferences>// ambil Preferences yg versi datastore
    private lateinit var dataStore2: DataStore<Preferences>// ambil Preferences yg versi datastore
    val scope = CoroutineScope(Dispatchers.Default)
    private var idTagGlobe :String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        dataStore = createDataStore(name = "settings")
        dataStore2 = createDataStore(name = Constants.PROFILE)

        binding!!.btnSave.setOnClickListener {
            lifecycleScope.launch {
                save(    //ini cara penggunaan function save
                    binding!!.etSaveKey.text.toString(),  //ini ada koma
                    binding!!.etSaveValue.text.toString()
                )
            }
        }
        binding!!.btnRead.setOnClickListener {
            lifecycleScope.launch {
                val value = read(binding!!.etReadkey.text.toString())
                binding!!.tvReadValue.text = value ?: "No value found"
            }
        }

        val textInputLayout = binding!!.menuDrop
        val autoCompleteTextView =  binding!!.dropItems
        // var items = {"item1","item2"}
        val items = arrayOf("Pcs","Box","Karton","Lembar","Botol")
        val itemAdapter : ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.simple_selectable_list_item, items)
            autoCompleteTextView.setAdapter(itemAdapter)
        val selectedItemd = binding!!.itemSelected
        autoCompleteTextView.setOnClickListener{

        }
      // val satuan = resources.getStringArray(R.array.)
        val dropDownActivity = binding!!.dropDownTV
        dropDownActivity.setOnClickListener{
            val intent = Intent(this,DropDownActivity::class.java)
            val yourPhone = "08111901081"
            val uid ="secrettext"
            val dummy = "Not Yet"
            lifecycleScope.launch {
                save2(Constants.NOHP, yourPhone.toString())
                save2(Constants.NOUUID, uid.toString())
                save2(Constants.IDTAG, dummy)
            }

           startActivity(intent)
        }

    }


    private suspend fun save(key: String, value: String) {
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun read(key: String): String? { //ketika read, kita hanya butuh key, karena kita ga tau value nya apa
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    private suspend fun save2(key: String, value: String) {
        val dataStoreKey = preferencesKey<String>(key)
        dataStore2.edit { settings ->
            settings[dataStoreKey] = value
        }
    }
    private suspend fun read2(key: String): String? { //ketika read, kita hanya butuh key, karena kita ga tau value nya apa
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    private suspend fun retrieveIdTag(key:String): String = withContext(Dispatchers.Default) {

        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore2.data.first()
        preferences[dataStoreKey].toString()
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null //ini buat menghindari memory leak
    }

    override fun onStart() {
        super.onStart()
  //      println("onstart mas bro "+idTagGlobe)//1
//        scope.launch {
//            idTagGlobe =  async { retrieveIdTag(Constants.IDTAG) }.await()
//            println("onstart mas bro inside scope1st "+idTagGlobe)
//        }
//        println("onstart mas bro "+idTagGlobe) //2

     //   if (idTagGlobe == null){  // karena in masih di main thread. malah ini duluan yg dikerjain
            runBlocking {

                val nameTag =  async { retrieveIdTag(Constants.IDTAG) }.await()
                println("onstart mas bro last inside scope2 "+nameTag)
                if (nameTag=="Not Yet"){
                val intent = Intent(this@MainActivity,DropDownActivity::class.java)
                startActivity(intent)
                }
                if(nameTag=="Rose Medical"){
                    val intent = Intent(this@MainActivity,RoseMedHome::class.java)
                    startActivity(intent)
                }
                if(nameTag=="Admin"){
                    val intent = Intent(this@MainActivity,AdminHome::class.java)
                    startActivity(intent)
                }

        }
        //println("onstart mas bro last "+idTagGlobe)  //3

        //if (idTagGlobe=="Not Yet"){
//            val intent = Intent(this,DropDownActivity::class.java)
//            startActivity(intent)
        //    println("onstart mas bro Masuk ke user option"+idTagGlobe)
       // }
    }
}