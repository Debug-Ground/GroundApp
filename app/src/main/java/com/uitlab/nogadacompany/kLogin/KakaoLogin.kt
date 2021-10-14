package com.uitlab.nogadacompany.kLogin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.uitlab.nogadacompany.MainActivity
import com.uitlab.nogadacompany.checklist.RetrofitService
import com.uitlab.nogadacompany.databinding.ActivityKakologinBinding
import com.uitlab.nogadacompany.user.UserDTO
import com.uitlab.nogadacompany.user.UserResDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KakaoLogin:AppCompatActivity() {
    var api = RetrofitService.create()
    private lateinit var binding: ActivityKakologinBinding
    var tokenOX: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKakologinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        * 토큰 정보가 있으면 바로 로그인
        */
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e("KakaoLogin.kt", "토큰 정보 보기 실패")
            } else if (tokenInfo != null) {
                Log.d("KakaoLogin.kt", "토큰 정보 보기 성공 ${tokenInfo}")
                updateUser()
            }
        }

        /*
        * 로그인하려는 사용자 정보를 넘겨 검증된 회원이면(카카오사용자) 토큰을 받는다.
        */
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            tokenVerification(token, error)
        }

        /*
        * 카카오톡이 설치가 되어 있으면 카카오톡 로그인으로 연결된다.
        * 카카오톡이 설치가 안되어 있으면 홈페이지 로그인으로 연결된다.
        */
        binding.kakoLoginButton.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    /*
        * 사용자가 로그인 하면 토큰번호가 지정이 되는데 그 값이 회원번호와 같다.
        * 로그아웃을 하고 다시 로그인을 해도 회원번호 즉 토큰번호는 변경되지 않는다.
    */
    fun updateUser(){
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("[KakaoLogin.kt]사용자정보 요청", "사용자 정보 요청 실패", error)
            } else {
                api.updateUserInfo(user?.properties?.get("nickname").toString(), user?.kakaoAccount?.email!!, user?.kakaoAccount?.profile?.thumbnailImageUrl!!).enqueue(object : Callback<UserResDTO> {
                    override fun onResponse(call: Call<UserResDTO>, response: Response<UserResDTO>) {
                        Log.e("log", response.body()?.message.toString())
                        var responseMessage = response.body()?.message.toString()

                        if(responseMessage.equals("1")){
                            val intent = Intent(this@KakaoLogin, MainActivity::class.java)
                            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                            finish()
                        }

                    }
                    override fun onFailure(call: Call<UserResDTO>, t: Throwable) {
                        Log.d("[KakaoLogin.kt]Log",t.message.toString())
                        Log.d("[KakaoLogin.kt]Log","fail")
                    }
                })
            }
        }
    }

    /*
    * 콜백을 통해 받은 토큰으로 검증을 한다.
    */
    fun tokenVerification(token:OAuthToken?, error:Throwable?) {
        if (error != null) {
            when {
                error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                    Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                    Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                    Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT)
                        .show()
                }
                error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                    Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                    Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                    Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT)
                        .show()
                }
                error.toString() == AuthErrorCause.ServerError.toString() -> {
                    Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                    Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                }
                else -> { // Unknown
                    Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else if (token != null) {
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e("[KakaoLogin.kt]사용자정보 요청", "사용자 정보 요청 실패", error)
                } else {
                    if (user != null) {
                        api.findUserInfo(user?.id.toString()).enqueue(object : Callback<UserResDTO> {
                            override fun onResponse(call: Call<UserResDTO>, response: Response<UserResDTO>) {
                                var responseMessage = response.body()?.message.toString()
                                if(responseMessage.equals("0")){ //회원 정보가 없을 때
                                    Log.e("[KakaoLogin.kt]사용자정보 요청", "회원정보가 없습니다.")
                                    var userDTO = UserDTO(user.id.toInt(), user.kakaoAccount?.email!!,user.properties?.get("nickname").toString(), user.kakaoAccount?.profile?.thumbnailImageUrl!!)
                                    Log.e("이름", user.properties?.get("nickname").toString())
                                    api.setUserInfo(userDTO).enqueue(object : Callback<UserResDTO> {
                                        override fun onResponse(call: Call<UserResDTO>, response: Response<UserResDTO>) {
                                            var responseMessage = response.body()?.message.toString()
                                            if(responseMessage.equals("1")){
                                                val intent = Intent(this@KakaoLogin, MainActivity::class.java)
                                                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                                finish()
                                            }
                                        }
                                        override fun onFailure(call: Call<UserResDTO>, t: Throwable) {
                                            Log.d("[Login.kt]log",t.message.toString())
                                            Log.d("[Login.kt]log","fail")
                                        }
                                    })
                                }else if(responseMessage.equals("1")){ //회원정보가 있을 때
                                    Log.e("[KakaoLogin.kt]사용자정보 요청", "회원정보가 있습니다.")
                                    updateUser()
                                }
                            }
                            override fun onFailure(call: Call<UserResDTO>, t: Throwable) {
                                Log.d("[KakaoLogin.kt]log",t.message.toString())
                                Log.d("[KakaoLogin.kt]log","fail")
                            }
                        })
                    }
                }
            }
        }
    }
}
