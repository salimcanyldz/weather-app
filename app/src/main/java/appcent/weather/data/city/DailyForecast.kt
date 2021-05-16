package appcent.weather.data.city


import com.google.gson.annotations.SerializedName

data class DailyForecast(
    @SerializedName("consolidated_weather")
    val consolidatedWeather: List<ConsolidatedWeather>,
    @SerializedName("latt_long")
    val lattLong: String,
    @SerializedName("location_type")
    val locationType: String,
    val parent: Parent,
    val sources: List<Source>,
    @SerializedName("sun_rise")
    val sunRise: String,
    @SerializedName("sun_set")
    val sunSet: String,
    val time: String,
    val timezone: String,
    @SerializedName("timezone_name")
    val timezoneName: String,
    val title: String,
    val woeid: Int
)