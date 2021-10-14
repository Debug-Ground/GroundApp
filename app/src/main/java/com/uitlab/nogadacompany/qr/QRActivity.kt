package com.uitlab.nogadacompany.qr

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.kakao.sdk.user.UserApiClient
import com.uitlab.nogadacompany.checklist.RetrofitService
import com.uitlab.nogadacompany.databinding.PopupviewQrCreateBinding
import com.uitlab.nogadacompany.user.UserResDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class QRActivity : AppCompatActivity() {
    private lateinit var binding: PopupviewQrCreateBinding
    var api = RetrofitService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PopupviewQrCreateBinding.inflate(layoutInflater)
        setContentView(binding.root);
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("사용자정보 요청", "사용자 정보 요청 실패", error)
            } else {
                if (user != null) {
                    QRCreate(user?.id.toString())
                }
            }
        }

        binding.attendanceButton.setOnClickListener {
            UserApiClient.instance.me { user, error ->
                if (error != null) {
                    Log.e("사용자정보 요청", "사용자 정보 요청 실패", error)
                } else {
                    if (user != null) {

                        var now = System.currentTimeMillis();
                        var date: Date = Date(now);
                        var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        var time = dateFormat.format(date)
                        api.userAttendance(user.id.toString(), time)
                            .enqueue(object : Callback<UserResDTO> {
                                override fun onResponse(
                                    call: Call<UserResDTO>,
                                    response: Response<UserResDTO>
                                ) {
                                    var responseMessage = response.body()?.message.toString()
                                    Log.e("[QRActivity]", responseMessage)
                                    Log.e("[QRActivity]", "성공")
                                }

                                override fun onFailure(call: Call<UserResDTO>, t: Throwable) {
                                    Log.e("[QRActivity]", "실패")
                                }
                            })
                    }
                }
            }
        }
    }

    fun QRCreate(userid: String) {
        val qrCode = QRCodeWriter()
        val bitMtx = qrCode.encode(userid, BarcodeFormat.QR_CODE, 600, 600)
        val bitmap: Bitmap = Bitmap.createBitmap(bitMtx.width, bitMtx.height, Bitmap.Config.RGB_565)
        for (i in 0..bitMtx.width - 1) {
            for (j in 0..bitMtx.height - 1) {
                var color = 0
                if (bitMtx.get(i, j)) {
                    color = Color.BLACK
                } else {
                    color = Color.WHITE
                }
                bitmap.setPixel(i, j, color)
            }
        }
        binding.qrImg.setImageBitmap(bitmap)
    }
}