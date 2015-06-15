package com.droibit.boltssamplekotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import bolts.Continuation
import bolts.Task
import com.droibit.boltssamplekotlin.model.Weather
import com.droibit.boltssamplekotlin.model.WeatherService
import retrofit.Callback
import retrofit.RestAdapter
import retrofit.RetrofitError
import retrofit.client.Response
import java.util.*
import java.util.concurrent.Executor

public class MainActivity : AppCompatActivity() {

    private val mWeatherService: WeatherService

    init {
        val adapter = RestAdapter.Builder()
                .setEndpoint("http://weather.livedoor.com")
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build()
        mWeatherService = adapter.create(javaClass<WeatherService>())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item!!.getItemId()

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun onContinueWithTask(v: View) {
        val weathers = ArrayList<String>()
        getWeatherAsync(400040).continueWithTask {
            weathers.add(it.getResult())
            getWeatherAsync(130010)
        } continueWith {
            weathers.add(it.getResult())
            showResult(weathers)
        }
    }

    fun onSuccessTask(v: View) {
        val weathers = ArrayList<String>()
        getWeatherAsync(400040).onSuccessTask {
            weathers.add(it.getResult())
            getWeatherAsync(130010)
        } continueWith {
            weathers.add(it.getResult())
            showResult(weathers)
        }
    }

    fun onSuccessTaskWithError(v: View) {
        var weathers = ArrayList<String>()

        getWeatherAsync(400040) continueWithTask {
            weathers.add(it.getResult())

            throw IllegalStateException()
            getWeatherAsync(400040)
        } onSuccessTask {
            weathers.add(it.getResult())
            getWeatherAsync(400040)
        } onSuccessTask {
            weathers.add(it.getResult())
            getWeatherAsync(400040)
        } continueWith {
            if (it.getError() != null) {
                Log.d(BuildConfig.BUILD_TYPE, "Task Error: ", it.getError())
            } else {
                weathers.add(it.getResult())
            }
            showResult(weathers)
        }
    }

    fun continueTaskWithError(v: View) {
        var weathers = ArrayList<String>()

        getWeatherAsync(400040).continueWithTask {
            throw IllegalStateException("Error!!!")
            getWeatherAsync(400040)
        } continueWithTask {
            if (it.getError() != null) {
                weathers.add(it.getError().getMessage())
            }
            getWeatherAsync(400040)
        } continueWith {
            weathers.add(it.getResult())
            showResult(weathers)
        }
    }

    fun onWhenAllResult(v: View) {
        getAllWeatherAsync().continueWith {
            showResult(it.getResult())
        }
    }

    fun onWhenAnyResult(v: View) {
        getAnyWeatherAsync().continueWith {
            showResult(arrayListOf(it.getResult().getResult()))
        }
    }

    private fun getAllWeatherAsync() = Task.whenAllResult(
            arrayListOf(
                getWeatherAsync(400040),
                getWeatherAsync(130010),
                getWeatherAsync(280010)
        )
    )

    private fun getAnyWeatherAsync() = Task.whenAnyResult(
            arrayListOf(
                getWeatherAsync(400040),
                getWeatherAsync(130010),
                getWeatherAsync(280010)
            )
    )

    private fun getWeatherAsync(city: Int) = Task.callInBackground {
        mWeatherService.weather(city).toString()
    }


    private fun showResult(weathers: List<String>) {
        runOnUiThread {
            Toast.makeText(this, weathers.join("\n"), Toast.LENGTH_LONG).show()
        }
    }
}