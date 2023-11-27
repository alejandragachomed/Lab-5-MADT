package com.example.lab_5_madt
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var parsedDataList: List<String> // Declaring parsedDataList as a property
    private lateinit var originalDataList: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        val filter = findViewById<EditText>(R.id.filter)

        // Create an instance of DataLoader and pass a lambda to handle loaded data
        val dataLoader = DataLoader { result ->
            onDataLoaded(result)
        }
        dataLoader.loadData("https://www.floatrates.com/daily/eur.xml")

        filter.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterList(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                filterList(s.toString())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterList(s.toString())
            }
        })
    }

    private fun onDataLoaded(result: String?) {
        Log.d("MainActivity", "Data received: $result")

        runOnUiThread {
            // Parse the data and obtain the list to display
            val parser = Parser()
            parser.parseXML(result ?: "")
            parsedDataList = parser.getParsedDataList() // Assigning to the class property
            originalDataList = ArrayList(parsedDataList)

            // Initialize adapter with parsed data list
            adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, parsedDataList)

            // Set adapter to ListView
            listView.adapter = adapter
        }
    }

    private fun filterList(text: String) {
        if (::parsedDataList.isInitialized) {
            val filteredList = originalDataList.filter { it.contains(text, true) }
            adapter.clear()
            adapter.addAll(filteredList)
            adapter.notifyDataSetChanged()
        }
    }

}