package appcent.weather.data.city


import com.google.gson.annotations.SerializedName

data class ConsolidatedWeather(
    @SerializedName("air_pressure")
    val airPressure: Double,
    @SerializedName("applicable_date")
    val applicableDate: String,
    val created: String,
    val humidity: Int,
    val id: Long,
    @SerializedName("max_temp")
    val maxTemp: Double,
    @SerializedName("min_temp")
    val minTemp: Double,
    val predictability: Int,
    @SerializedName("the_temp")
    val theTemp: Double,
    val visibility: Double,
    @SerializedName("weather_state_abbr")
    val weatherStateAbbr: String,
    @SerializedName("weather_state_name")
    val weatherStateName: String,
    @SerializedName("wind_direction")
    val windDirection: Double,
    @SerializedName("wind_direction_compass")
    val windDirectionCompass: String,
    @SerializedName("wind_speed")
    val windSpeed: Double
)