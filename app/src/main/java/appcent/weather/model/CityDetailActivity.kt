package appcent.weather.model

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import appcent.weather.R
import appcent.weather.api.RequestService
import appcent.weather.data.city.ConsolidatedWeather
import appcent.weather.data.city.DailyForecast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CityDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.city_detail)

        findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
        findViewById<RelativeLayout>(R.id.todayContainer).visibility = View.GONE

        val serviceResponse = RequestService().getCityService(intent.extras?.get("woeid") as Int)
        serviceResponse.enqueue(object : Callback<DailyForecast> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<DailyForecast>, response: Response<DailyForecast>) {
                if (response.isSuccessful){
                    val resBody = response.body()
                    val res = preparePage(resBody)
                    if (res){
                        findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                        findViewById<TextView>(R.id.errorText).visibility = View.GONE
                        findViewById<RelativeLayout>(R.id.todayContainer).visibility = View.VISIBLE
                    }
                    else{
                        findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                        findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
                    }

                }
            }
            override fun onFailure(call: Call<DailyForecast>, t: Throwable) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
            }

        })

    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun preparePage(resBody: DailyForecast?) : Boolean {
        findViewById<TextView>(R.id.addressTextView).text = resBody?.title

        val parsedDate = LocalDateTime.parse(resBody?.time, DateTimeFormatter.ISO_DATE_TIME)
        val formatted = parsedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        findViewById<TextView>(R.id.dateTextView).text = formatted

        val res = setImage(resBody?.consolidatedWeather?.get(0))

        if (res){
            findViewById<TextView>(R.id.currentStatusTextView).text = resBody?.consolidatedWeather?.get(0)?.weatherStateName
            findViewById<TextView>(R.id.temperatureTextView).text = "${resBody?.consolidatedWeather?.get(0)?.theTemp?.toInt()}°C"
            findViewById<TextView>(R.id.minTemp).text = "${resBody?.consolidatedWeather?.get(0)?.minTemp?.toInt()}°C"
            findViewById<TextView>(R.id.maxTemp).text = "${resBody?.consolidatedWeather?.get(0)?.maxTemp?.toInt()}°C"
            prepareOtherDays(resBody)
        }
        else{
            return false
        }

        return true
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun prepareOtherDays(resBody: DailyForecast?) {

        val dateTextArr = arrayOf(R.id.tomorrowTextView, R.id.twoDaysLaterTextView, R.id.threeDaysLaterTextView,
        R.id.fourDaysLaterTextView, R.id.fiveDaysLaterTextView, R.id.sixDaysLaterTextView)
        val imageArr = arrayOf(R.id.otherDay1Image, R.id.otherDay2Image, R.id.otherDay3Image,
            R.id.otherDay4Image, R.id.otherDay5Image, R.id.otherDay6Image)
        val tempTextArr = arrayOf(R.id.otherDay1Text, R.id.otherDay2Text, R.id.otherDay3Text,
            R.id.otherDay4Text, R.id.otherDay5Text, R.id.otherDay6Text)

        for (i in resBody?.consolidatedWeather?.indices!!){

            val format = SimpleDateFormat("yyyy-MM-dd")
            val formatted = format.parse(resBody.consolidatedWeather[i].applicableDate)
            val res = SimpleDateFormat("dd.MM.yyyy")
            val end = res.format(formatted)

            findViewById<TextView>(dateTextArr[i]).text = end

            when(resBody.consolidatedWeather[i].weatherStateAbbr) {
                "c" -> findViewById<ImageView>(imageArr[i]).setImageResource(R.drawable.ic_c)
                "h" -> findViewById<ImageView>(imageArr[i]).setImageResource(R.drawable.ic_h)
                "hc" -> findViewById<ImageView>(imageArr[i]).setImageResource(R.drawable.ic_hc)
                "hr" -> findViewById<ImageView>(imageArr[i]).setImageResource(R.drawable.ic_hr)
                "lc" -> findViewById<ImageView>(imageArr[i]).setImageResource(R.drawable.ic_lc)
                "lr" -> findViewById<ImageView>(imageArr[i]).setImageResource(R.drawable.ic_lr)
                "s" -> findViewById<ImageView>(imageArr[i]).setImageResource(R.drawable.ic_s)
                "sl" -> findViewById<ImageView>(imageArr[i]).setImageResource(R.drawable.ic_sl)
                "sn" -> findViewById<ImageView>(imageArr[i]).setImageResource(R.drawable.ic_sn)
                "t" -> findViewById<ImageView>(imageArr[i]).setImageResource(R.drawable.ic_t)
                else -> {
                    findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                    findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
                }
            }
            findViewById<TextView>(tempTextArr[i]).text = "${resBody.consolidatedWeather[i].theTemp.toInt()}°C"
        }
    }


    private fun setImage(weather: ConsolidatedWeather?) : Boolean {

        when(weather?.weatherStateAbbr){
            "c" -> findViewById<ImageView>(R.id.todayImage).setImageResource(R.drawable.ic_c)
            "h" -> findViewById<ImageView>(R.id.todayImage).setImageResource(R.drawable.ic_h)
            "hc" -> findViewById<ImageView>(R.id.todayImage).setImageResource(R.drawable.ic_hc)
            "hr" -> findViewById<ImageView>(R.id.todayImage).setImageResource(R.drawable.ic_hr)
            "lc" -> findViewById<ImageView>(R.id.todayImage).setImageResource(R.drawable.ic_lc)
            "lr" -> findViewById<ImageView>(R.id.todayImage).setImageResource(R.drawable.ic_lr)
            "s" -> findViewById<ImageView>(R.id.todayImage).setImageResource(R.drawable.ic_s)
            "sl" -> findViewById<ImageView>(R.id.todayImage).setImageResource(R.drawable.ic_sl)
            "sn" -> findViewById<ImageView>(R.id.todayImage).setImageResource(R.drawable.ic_sn)
            "t" -> findViewById<ImageView>(R.id.todayImage).setImageResource(R.drawable.ic_t)
            else ->{
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
                return false
            }
        }
        return true
    }
}
