package com.example.android_ex

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import androidx.fragment.app.activityViewModels

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TrafficFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TrafficFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    //連接View和ViewModel
    private val TrafficViewModel:DataViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_traffic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val resultListView:ListView = view.findViewById<ListView>(R.id.TrafficListView)
        var ListAlldata = mutableListOf<String>()

        TrafficViewModel.trafficData.observe(viewLifecycleOwner){mytrafficdata->
            var message:String = ""
            for (i in 0..<mytrafficdata.size){
                message = "標題：${mytrafficdata[i].chtmessage}\n" +
                        "上傳時間：${mytrafficdata[i].updatetime}\n" +
                        "內容：\n${mytrafficdata[i].content}"
                ListAlldata.add(message)
            }
            //adapter設定
            val TrafficAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,ListAlldata)
            resultListView.adapter = TrafficAdapter
        }
        TrafficViewModel.TrafficPost()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TrafficFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                TrafficFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}