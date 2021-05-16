package appcent.weather.api

import appcent.weather.data.city.DailyForecast
import appcent.weather.data.location.Locations
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RequestService {

    private val BASE_URL = "https://www.metaweather.com/api/"

    private val serviceProvider = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service = serviceProvider.create(ApiRequest::class.java)

    fun getLocationService(lattlong : String) : Call<Locations> {
        return service.getNearLocation(lattlong)
    }

    fun getCityService(woeid : Int) : Call<DailyForecast> {
        return service.getSpecificCity(woeid)
    }
}