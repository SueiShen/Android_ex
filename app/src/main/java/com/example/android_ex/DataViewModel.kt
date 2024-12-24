package com.example.android_ex

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONObject
import java.util.Objects

class DataViewModel:ViewModel() {
    //trafficModel連接ViewModel
    private var _trafficData = MutableLiveData<MutableList<TrafficData>>()
    val trafficData:MutableLiveData<MutableList<TrafficData>> get() = _trafficData
    //weatherModel連接ViewModel
    private var _weatherData = MutableLiveData< MutableList<WeatherData>>()
    val weather:MutableLiveData< MutableList<WeatherData>> get() = _weatherData

    //連線okhttp_traffic
    private val TrafficClient = OkHttpClient()
    //連線okhttp_weather
    private val WeatherClient = OkHttpClient()

    //定義函數_traffic
    fun TrafficPost(){
        val TrafficRequest = Request.Builder()
                .url("https://tcgbusfs.blob.core.windows.net/dotapp/news.json")
                .build()
        var mData:MutableList<TrafficData> = ArrayList()

        TrafficClient.newCall(TrafficRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    response.body.string().let { getTrafficString->
                        val trafficAllData = TrafficAllData(pp=getTrafficString)
                        //處理JSON
                        val JSONAll = JSONObject(trafficAllData.pp)
                        val JSONRecord = JSONAll.getJSONArray("News")

                        for (i in 0..<JSONRecord.length()){
                            var s:TrafficData = TrafficData()
                            var oneObject = JSONRecord.getJSONObject(i)

                            s.apply {
                                chtmessage = oneObject.getString("chtmessage")
                                updatetime = oneObject.getString("updatetime")
                                content = oneObject.getString("content")
                            }
                            mData.add(s)
                        }
                        _trafficData.postValue(mData)
                    }
                }else{
                    Log.d("TrafficTag","下載失敗")
                }
            }
        })
    }

    //定義函數_weather
    fun WeatherPost(){
        var mData : MutableList<WeatherData> = ArrayList()
        val request = Request.Builder()
            .url("https://opendata.cwa.gov.tw/fileapi/v1/opendataapi/F-C0032-009?Authorization=rdec-key-123-45678-011121314&format=JSON")
            .build()
        WeatherClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {

                if (response.isSuccessful) {
                    response.body.string().let { getBackString ->
                        val weather = WeatherAllData(p = getBackString)
                        val mAll = JSONObject(weather.p)

                        val Cwaopendata = mAll.getJSONObject("cwaopendata")
                        val Dataset = Cwaopendata.getJSONObject("dataset")
                        val ParameterSet = Dataset.getJSONObject("parameterSet")
                        val Parameter = ParameterSet.getJSONArray("parameter")
                        val Location = Dataset.getJSONObject("location")
                        val DatasetInfo = Dataset.getJSONObject("datasetInfo")
                        val ParameterValue = Parameter.getJSONObject(1)


                        var s:WeatherData = WeatherData()
                        s.apply {
                            locationName =Location.getString("locationName")
                            parameterValue = ParameterValue.getString("parameterValue")
                            issueTime = DatasetInfo.getString("issueTime")
                        }
                        mData.add(s)
                        _weatherData.postValue(mData)
                    }
                } else {
                    Log.d("myTag","下載失敗")
                }
            }
        })
    }
}