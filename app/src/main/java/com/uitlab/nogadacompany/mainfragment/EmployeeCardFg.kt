package com.uitlab.nogadacompany.mainfragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.kakao.sdk.user.UserApiClient
import com.uitlab.nogadacompany.GlobalApplication
import com.uitlab.nogadacompany.MainActivity
import com.uitlab.nogadacompany.checklist.CheckList
import com.uitlab.nogadacompany.checklist.RetrofitService
import com.uitlab.nogadacompany.databinding.FragmentEmployeecardBinding
import com.uitlab.nogadacompany.kLogin.KakaoLogin
import com.uitlab.nogadacompany.qr.QRActivity
import com.uitlab.nogadacompany.user.User
import com.uitlab.nogadacompany.user.UserDTO
import com.uitlab.nogadacompany.user.UserRegularDate
import com.uitlab.nogadacompany.user.UserResDTO
import kotlinx.android.synthetic.main.popupview_qr_create.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmployeeCardFg() : Fragment() {
    private lateinit var binding: FragmentEmployeecardBinding
    var api = RetrofitService.create()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val activity = context as Activity //액티비티 받기
        binding = FragmentEmployeecardBinding.inflate(inflater, container, false)

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("사용자정보 요청", "사용자 정보 요청 실패", error)
            } else {
                if (user != null) {
                    api.selectUserReqularDate(user?.id.toString()).enqueue(object : Callback<UserRegularDate> {
                        override fun onResponse(call: Call<UserRegularDate>, response: Response<UserRegularDate>) {
                            binding.run {
                                emploeyCardTitle.text = response.body()?.result?.get(0)?.wDate
                                emploeyCardTitleS.text = "GROUND"
                                emploeyCardUserNr.text = response.body()?.result?.get(0)?.wRegular + " / " + user?.properties?.get("nickname").toString()

                                Glide.with(this@EmployeeCardFg)
                                    .load(user.kakaoAccount?.profile?.thumbnailImageUrl!!)
                                    .into(userImg)
                            }

                        }
                        override fun onFailure(call: Call<UserRegularDate>, t: Throwable) {
                            Log.d("[EmployeeCardFG.kt]Log",t.message.toString())
                            Log.d("[EmployeeCardFG.kt]Log","fail")
                        }
                    })
                }
            }
        }

        binding.run {
            qrCreateButton.setOnClickListener {
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e("사용자정보 요청", "사용자 정보 요청 실패", error)
                    } else {
                        if (user != null) {
                            api.updateUserCheck(user.id.toString(), 0).enqueue(object :
                                Callback<UserResDTO> {
                                override fun onResponse(call: Call<UserResDTO>, response: Response<UserResDTO>) {
                                    var responseMessage = response.body()?.message.toString()
                                    Log.d("[EmployeeCardFG.kt]log",responseMessage)
                                    val intent = Intent(context, CheckList::class.java)
                                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                }
                                override fun onFailure(call: Call<UserResDTO>, t: Throwable) {
                                    Log.d("[EmployeeCardFG.kt]log",t.message.toString())
                                    Log.d("[EmployeeCardFG.kt]log","fail")
                                }
                            })
                        }
                    }
                }
            }
            employLogoutImg.setOnClickListener {
                UserApiClient.instance.logout { error ->
                    if (error != null) {
                        Log.e("로그아웃", "로그아웃 실패. SDK에서 토큰 삭제됨", error);
                    } else {
                        Log.d("로그아웃", "로그아웃 성공. SDK에서 토큰 삭제됨")
                        val intent = Intent(context, KakaoLogin::class.java)
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    }
                }
            }
        }
        return binding!!.root
    }
}