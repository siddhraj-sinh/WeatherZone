package com.example.wheatherzone

import android.annotation.SuppressLint
import android.location.Location
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.system.Os.close
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.HandlerCompat.postDelayed
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import org.json.JSONObject
import org.w3c.dom.Text
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var navigationView: NavigationView
    private lateinit var progressBar: ProgressBar
    private lateinit var mainContainer: ConstraintLayout
    private lateinit var locationCity: TextView
    private lateinit var statusWeather: TextView
    private lateinit var tempreture: TextView
    private lateinit var minTemp: TextView
    private lateinit var maxTemp: TextView
    private lateinit var sunRise: TextView
    private lateinit var sunSet: TextView
    private lateinit var windCity: TextView
    private lateinit var pressureCity: TextView
    private lateinit var humadityCity: TextView
    private lateinit var cloudCity: TextView
    val CITY: String = "vadodara,in"
    val API: String = "b492f83dff3460890467fb9ef070ed64"
    //checking

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Status bar color
        window.statusBarColor = ContextCompat.getColor(this,R.color.firstYellow)
        // Initialzing
        drawerLayout = findViewById(R.id.drawerLayout)
        topAppBar = findViewById(R.id.topAppBar)
        navigationView = findViewById(R.id.navigationView)
        progressBar = findViewById(R.id.loader)
        mainContainer =  findViewById(R.id.constraintLayout)
        locationCity = findViewById(R.id.tv_location)
        statusWeather = findViewById(R.id.tv_atmosphere)
        minTemp = findViewById(R.id.tv_min_temp)
        maxTemp = findViewById(R.id.tv_max_temp)
        tempreture = findViewById(R.id.tv_temperature)
        sunRise = findViewById(R.id.tv_sunrise)
        sunSet = findViewById(R.id.tv_sunset)
        windCity = findViewById(R.id.tv_wind)
        pressureCity = findViewById(R.id.tv_pressure)
        humadityCity = findViewById(R.id.tv_humadity)
        cloudCity = findViewById(R.id.tv_cloud)
        // Drawer
        topAppBar.setNavigationOnClickListener{
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item1 -> {
                    Toast.makeText(this,"one",Toast.LENGTH_SHORT).show()
                }
                R.id.item2 -> {
                    Toast.makeText(this,"two",Toast.LENGTH_SHORT).show()
                }
                R.id.item3 -> {
                    Toast.makeText(this,"three",Toast.LENGTH_SHORT).show()
                }
                else -> {
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        weatherTask().execute()
    }

    inner class weatherTask(): AsyncTask<String, Void, String>(){

        override fun onPreExecute() {
            super.onPreExecute()
            progressBar.visibility = View.VISIBLE
            mainContainer.visibility = View.INVISIBLE

        }
        override fun doInBackground(vararg params: String?): String {
          var response: String
            try {
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
                    Charsets.UTF_8
                )
            }
            catch (e: Exception){
                response = null.toString()
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try{
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main") // if object is contains single value then jsonObj.OptString("Object Name") for string jsonObj.OptInt(int) for int
                val sys = jsonObj.getJSONObject("sys")// I figured they did nearly the same thing with one difference: If the key or value isn't found, then getJSONObject() throws a JSONException whereas optJSONObject() simply returns null
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val clouds = jsonObj.getJSONObject("clouds")

                val temp = main.getString("temp")+"°C"
                val tempMin = "Min Temp: " + main.getString("temp_min")+"°C"
                val tempMax = "Max Temp: " + main.getString("temp_max")+"°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")

                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")

                val address = jsonObj.getString("name")+", "+sys.getString("country")

                val cloud = clouds.getString("all")

                /* Populating extracted data into our views */
                locationCity.text = address
                //findViewById<TextView>(R.id.updated_at).text =  updatedAtText
                statusWeather.text = weatherDescription.capitalize()
                tempreture.text = temp
                minTemp.text = tempMin
                maxTemp.text = tempMax
                sunRise.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                sunSet.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                windCity.text = windSpeed
                pressureCity.text = pressure
                humadityCity.text = humidity
                cloudCity.text = cloud
                /* Views populated, Hiding the loader, Showing the main design */
                progressBar.visibility = View.INVISIBLE
                    mainContainer.visibility = View.VISIBLE
            }
            catch (e: Exception){
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(this@MainActivity,"Some error occured",Toast.LENGTH_SHORT).show()
            }
        }
    }

}
