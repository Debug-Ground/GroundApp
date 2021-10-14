package com.uitlab.nogadacompany.mainfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uitlab.nogadacompany.adapter.RecyclerViewNoticeAdapater
import com.uitlab.nogadacompany.checklist.RetrofitService
import com.uitlab.nogadacompany.databinding.FragmentNoticeBinding
import com.uitlab.nogadacompany.noticedata.Notice
import com.uitlab.nogadacompany.noticedata.NoticeData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeFg : Fragment() {
    lateinit var binding: FragmentNoticeBinding
    lateinit var recyclerViewNoticeAdapater: RecyclerViewNoticeAdapater
    val datas = mutableListOf<Notice>()
    var api = RetrofitService.create()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val activity = context as Activity //액티비티 받기
        binding = FragmentNoticeBinding.inflate(inflater, container, false)
        binding.run {
          recyclerViewNoticeAdapater = RecyclerViewNoticeAdapater(inflater.context)
            noticeRecyclerview.adapter = recyclerViewNoticeAdapater
        }

        api.getNoticeList().enqueue(object : Callback<NoticeData> {
            override fun onResponse(call: Call<NoticeData>, response: Response<NoticeData>) {
                Log.e("log", response.body()?.message.toString())

                for(index in response.body()?.result?.indices!! step(1)) {
                    datas.add(index, response.body()?.result?.get(index)!!)
                }

                recyclerViewNoticeAdapater.listData = datas;
                recyclerViewNoticeAdapater.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<NoticeData>, t: Throwable) {
                Log.d("log",t.message.toString())
                Log.d("log","fail")
            }
        })
        return binding!!.root
    }
}