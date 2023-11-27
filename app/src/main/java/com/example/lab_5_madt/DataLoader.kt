package com.example.lab_5_madt
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import java.io.BufferedReader
import java.io.InputStreamReader

class DataLoader(private val onDataLoaded: (String?) -> Unit) {

    @OptIn(DelicateCoroutinesApi::class)
    fun loadData(urlString: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()

                val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
                val result = StringBuilder()
                var line: String?

                while (inputStream.readLine().also { line = it } != null) {
                    result.append(line).append("\n")
                }

                inputStream.close()
                connection.disconnect()

                onDataLoaded(result.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                onDataLoaded(null)
            }
        }
    }
}