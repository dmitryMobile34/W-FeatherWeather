package com.welcomeleviqx.featherweathercomrat

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.welcomeleviqx.featherweathercomrat.databinding.ActivityMainBinding
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    var weather_url1 = ""

    // ключ API под погоду
    var api_id1 = "fd648c1cdb014e759086abea81e377dc"

    // переменная под ViewBinding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // интеграция ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // подгрузка анимации облака через функцию
        setLottie("https://assets4.lottiefiles.com/packages/lf20_dxkh5nn5.json")

        // подтягивание API + координаты города
        weather_url1 = "https://api.weatherbit.io/v2.0/current?" + "lat=" + 46.31 + "&lon=" + 28.66 + "&key=" + api_id1

        binding.mainButton.setOnClickListener {

            // скрытие текста и анимации через функцию
            hideView(binding.weatherText)
            hideView(binding.lottieAnimation)

            // показ температуры через функцию и смена текста
            // postDelayed для начала работы после отработки функции скрытия
            Handler().postDelayed ({
                getTemp()
                binding.weatherText
                    .animate()
                    .withEndAction {
                        binding.mainButton.isEnabled = false
                        binding.refreshButton.isEnabled = true
                    }
            },1500)

        }

        binding.refreshButton.setOnClickListener {

            hideView(binding.weatherText)
            hideView(binding.lottieAnimation)

            Handler().postDelayed ({
                binding.weatherText.textSize = 17f
                binding.weatherText.setTextColor(Color.parseColor("#401E1D22"))
                binding.weatherText.setTypeface(null, Typeface.ITALIC)
                binding.weatherText.text = "для начала нажмите на кнопку"

                setLottie("https://assets4.lottiefiles.com/packages/lf20_dxkh5nn5.json")
                revealView(binding.weatherText)
                revealView(binding.lottieAnimation)

                binding.weatherText
                    .animate()
                    .withEndAction {
                        binding.mainButton.isEnabled = true
                        binding.refreshButton.isEnabled = false
                    }

            },1500)

        }

    }

    fun getTemp() {
        val queue = Volley.newRequestQueue(this)
        val url: String = weather_url1
        Log.e("lat", url)

        val stringReq = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                Log.e("lat", response.toString())

                val obj = JSONObject(response)

                val arr = obj.getJSONArray("data")
                Log.e("lat obj1", arr.toString())

                val obj2 = arr.getJSONObject(0)
                Log.e("lat obj2", obj2.toString())

                if (obj2.getString("temp").toFloat() < 0) {

                    revealView(binding.lottieAnimation)
                    setLottie("https://assets4.lottiefiles.com/private_files/lf30_w5u9xr3a.json")
                    revealView(binding.weatherText)

                } else {

                    revealView(binding.lottieAnimation)
                    setLottie("https://assets3.lottiefiles.com/temp/lf20_dgjK9i.json")
                    revealView(binding.weatherText)

                }

                binding.weatherText.textSize = 20f
                binding.weatherText.setTextColor(Color.parseColor("#1E1D22"))
                binding.weatherText.setTypeface(Typeface.DEFAULT)
                binding.weatherText.text = obj2.getString("temp") + " °C в Комрате"

            },
            Response.ErrorListener {
                Toast.makeText(this, "Не получилось получить данные.", Toast.LENGTH_SHORT)
            })
        queue.add(stringReq)
    }

    private fun hideView(view: View) {
        view.animate()
            .alpha(0f)
            .setDuration(1500)
            .setListener(null)
    }

    private fun revealView(view: View) {
        view.animate()
            .alpha(1f)
            .setDuration(1500)
            .setListener(null)
    }

    private fun setLottie (url: String) {
        binding.lottieAnimation.setAnimationFromUrl(url)
        binding.lottieAnimation.loop(true)
        binding.lottieAnimation.playAnimation()
    }

}