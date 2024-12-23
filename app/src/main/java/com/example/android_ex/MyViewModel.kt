package com.example.android_ex

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.awareness.state.Weather
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONObject

class MyViewModel:ViewModel() {
    private var _weather = MutableLiveData< MutableList<SiteData>>()
    val weather:MutableLiveData< MutableList<SiteData>> get() = _weather
    private val client = OkHttpClient()
    fun feachWeather() {
        var mData : MutableList<SiteData> = ArrayList()
        val request = Request.Builder()
            .url("https://opendata.cwa.gov.tw/fileapi/v1/opendataapi/F-C0032-009?Authorization=rdec-key-123-45678-011121314&format=JSON")
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {

                if (response.isSuccessful) {
                    response.body.string().let { getBackString ->
                        val weather = Weather(pp = getBackString)


                        val mAll = JSONObject(weather.pp)
                        val Cwaopendata = mAll.getJSONObject("cwaopendata")
                        val location = Cwaopendata.getJSONObject("dataset")
                        val parameterSet = location.getJSONObject("parameterSet")
                        val parameter = parameterSet.getJSONArray("parameter")
                        val LocationName = location.getJSONObject("location")
                        val IssueTime = location.getJSONObject("datasetInfo")
                        val ParameterValue = parameter.getJSONObject(1)


                        var s:SiteData = SiteData()
                        s.apply {
                            locationName =LocationName.getString("locationName")
                            parameterValue = ParameterValue.getString("parameterValue")
                            issueTime = IssueTime.getString("issueTime")
                        }
                        mData.add(s)
                        _weather.postValue(mData)
                    }
                } else {
                    Log.d("myTag","下載失敗")
                }
            }
        })
    }
}
