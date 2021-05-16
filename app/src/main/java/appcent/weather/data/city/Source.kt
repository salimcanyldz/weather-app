package appcent.weather.data.city


import com.google.gson.annotations.SerializedName

data class Source(
    @SerializedName("crawl_rate")
    val crawlRate: Int,
    val slug: String,
    val title: String,
    val url: String
)