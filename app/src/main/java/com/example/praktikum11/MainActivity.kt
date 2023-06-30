package com.example.praktikum11

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Jalankan AsyncTask untuk mengambil data pengguna
        FetchUsersTask().execute()
            }



    private inner class FetchUsersTask : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void): String? {
            var result: String? = null

            try {
                val url = URL("https://jsonplaceholder.typicode.com/users")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val stringBuilder = StringBuilder()
                    var line: String? = reader.readLine()
                    while (line != null) {
                        stringBuilder.append(line)
                        line = reader.readLine()
                    }
                    reader.close()
                    result = stringBuilder.toString()
                } else {
                    Log.e("FetchUsersTask", "HTTP Error Code: $responseCode")
                }
            } catch (e: Exception) {
                Log.e("FetchUsersTask", "Error fetching users", e)
            }

            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            if (result != null) {
                // Proses hasil respons JSON
                try {
                    val linearLayoutUsers = findViewById<LinearLayout>(R.id.linearLayoutUsers)

                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val userObject = jsonArray.getJSONObject(i)
                        val id = userObject.getInt("id")
                        val name = userObject.getString("name")
                        val username = userObject.getString("username")
                        val email = userObject.getString("email")

                        // Membuat TextView baru untuk setiap pengguna
                        val textViewUser = TextView(this@MainActivity)
                        textViewUser.text = "ID: $id, Name: $name, Username: $username, Email: $email"

                        // Menambahkan TextView pengguna ke LinearLayout
                        linearLayoutUsers.addView(textViewUser)
                    }
                } catch (e: Exception) {
                    Log.e("FetchUsersTask", "Error parsing JSON", e)
                }
            }
        }

    }

}