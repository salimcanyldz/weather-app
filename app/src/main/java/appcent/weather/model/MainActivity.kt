package appcent.weather.model

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import appcent.weather.R
import appcent.weather.api.RequestService
import appcent.weather.data.location.Locations
import com.google.android.gms.location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), LocationListener{

    private val PERMISSION_ID = 1010
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLoc : Location? = null
    private var cities : Locations? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermission()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)



        findViewById<ListView>(R.id.locationsHolderLayout).onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val intent = Intent(this, CityDetailActivity::class.java)
                intent.putExtra("woeid", cities?.get(id.toInt())?.woeid)
                startActivity(intent)
            }
    }


    private fun getLocation(){
        if (checkPermission()){
            if (isLocationActive()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { t ->
                    val location = t.result
                    if (location == null){
                        requestNewLocation()
                    }
                    else{
                        currentLoc = location
                        getNearbyCities()
                    }
                }
            }
            else{
                Toast.makeText(this, "Please activate your GPS", Toast.LENGTH_SHORT).show()
                intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
        else{
            requestPermission()
        }
    }

    private fun getNearbyCities(){
        val coor = "${currentLoc?.latitude.toString()},${currentLoc?.longitude.toString()}"
        val response = RequestService().getLocationService(coor)
        response.enqueue(object: Callback<Locations> {
            override fun onResponse(call: Call<Locations>, response: Response<Locations>) {
                if (response.isSuccessful){
                    cities = response.body()
                    val cityList = arrayListOf<String>()
                    for (i in cities!!){
                        cityList.add(i.title)
                    }
                    val adapter = ArrayAdapter<String>(this@MainActivity,
                        android.R.layout.simple_list_item_1,
                        cityList)
                    findViewById<ListView>(R.id.locationsHolderLayout).adapter = adapter
                }
            }

            override fun onFailure(call: Call<Locations>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Something went wrong...", Toast.LENGTH_LONG).show()
            }

        })


    }

    private fun requestNewLocation(){
        val locationReq = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5
            fastestInterval = 0
            numUpdates = 1
        }
        checkPermission()
        fusedLocationProviderClient.requestLocationUpdates(locationReq, locationCallback, Looper.myLooper())
    }

    private val locationCallback = object : LocationCallback(){

        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            currentLoc = p0.lastLocation
            getNearbyCities()
        }
    }

    // Permission control func.
    private fun checkPermission() : Boolean{
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    // Get permission requests
    private fun requestPermission(){
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION),
            PERMISSION_ID)
    }

    // Check location service and network is active
    private fun isLocationActive() : Boolean{
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLocation()
            }
        }
    }

    override fun onLocationChanged(p0: Location) {
        requestNewLocation()
    }
}