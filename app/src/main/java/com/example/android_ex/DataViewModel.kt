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
    //Model連接ViewModel
    private var _trafficData = MutableLiveData<MutableList<TrafficData>>()
    val trafficData:MutableLiveData<MutableList<TrafficData>> get() = _trafficData

    //連線okhttp
    private val TrafficClient = OkHttpClient()
    //定義函數
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
                        val JSONRecord = JSONAll.getJSONArray("news")

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
                    Log.d("Traffictag","下載失敗")
                }
            }
        })
    }


}