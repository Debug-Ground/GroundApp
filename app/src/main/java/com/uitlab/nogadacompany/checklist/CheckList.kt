package com.uitlab.nogadacompany.checklist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.user.UserApiClient
import com.uitlab.nogadacompany.GlobalApplication
import com.uitlab.nogadacompany.MainActivity
import com.uitlab.nogadacompany.adapter.RecyclerViewCheckListAdapter
import com.uitlab.nogadacompany.databinding.ActivityChecklistBinding
import com.uitlab.nogadacompany.qr.QRActivity
import com.uitlab.nogadacompany.user.User
import com.uitlab.nogadacompany.user.UserDTO
import com.uitlab.nogadacompany.user.UserResDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckList: AppCompatActivity() {

    lateinit var recyclerViewCheckListAdapter:RecyclerViewCheckListAdapter
    lateinit var binding : ActivityChecklistBinding
    var api = RetrofitService.create()
    var datas : MutableList<CheckDataApi> = mutableListOf()
    var checkList: MutableMap<Int, Boolean> = mutableMapOf()
    var size = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChecklistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val callbackListener:ListOnclickInterface = object :ListOnclickInterface{
            override fun onCheckBox(checkListIsCheckedMap: MutableMap<Int, Boolean>) {
                checkList = checkListIsCheckedMap
                Log.e("[CheckList]체크유무", checkListIsCheckedMap.toString())
            }
        }

        recyclerViewCheckListAdapter = RecyclerViewCheckListAdapter(applicationContext)
        recyclerViewCheckListAdapter.setCallbackListener(callbackListener)
        binding.checklistRecyclerview.adapter = recyclerViewCheckListAdapter

        api.getCheckList().enqueue(object : Callback<CheckData> {
            override fun onResponse(call: Call<CheckData>, response: Response<CheckData>) {
                Log.e("log", response.body()?.message.toString())
                size = response.body()?.result?.size!!
                for(index in response.body()?.result?.indices!! step(1)) {
                    datas.add(index, response.body()?.result?.get(index)!!)
                }

                recyclerViewCheckListAdapter.listData = datas;
                recyclerViewCheckListAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<CheckData>, t: Throwable) {
                Log.d("log",t.message.toString())
                Log.d("log","fail")
            }
        })

        binding.checklistButton.setOnClickListener {
            var count = 0
            for (index in 0..(size-1)){
                if(checkList.get(index) == true){
                    count++
                }
            }
            Log.e("사용자정보 요청", count.toString() + '/' + size)
            if(count == size){
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e("사용자정보 요청", "사용자 정보 요청 실패", error)
                    } else {
                        if (user != null) {
                            api.updateUserCheck(user.id.toString(), 1).enqueue(object : Callback<UserResDTO> {
                                override fun onResponse(call: Call<UserResDTO>, response: Response<UserResDTO>) {
                                    var responseMessage = response.body()?.message.toString()
                                    if(responseMessage.equals("1")){
                                        val intent = Intent(this@CheckList, QRActivity::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                                override fun onFailure(call: Call<UserResDTO>, t: Throwable) {
                                    Log.d("[Login.kt]log",t.message.toString())
                                    Log.d("[Login.kt]log","fail")
                                }
                            })
                        }
                    }
                }
            }else{
                Log.e("[CheckList]체크유무", "모두 체크를 해주세요.")
            }
        }
    }

}