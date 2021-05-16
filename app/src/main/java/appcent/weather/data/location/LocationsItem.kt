package appcent.weather.data.location

data class LocationsItem(
    val distance: Int,
    val latt_long: String,
    val location_type: String,
    val title: String,
    val woeid: Int
)