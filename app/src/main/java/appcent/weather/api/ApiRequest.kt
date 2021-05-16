package appcent.weather.api

import appcent.weather.data.city.DailyForecast
import appcent.weather.data.location.Locations
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiRequest {

    @GET("location/search/?")
    fun getNearLocation(@Query("lattlong") lattlong : String): Call<Locations>


    @GET("location/{woeid}")
    fun getSpecificCity(@Path("woeid") id : Int) : Call<DailyForecast>
}