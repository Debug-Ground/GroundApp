package com.uitlab.nogadacompany

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility

class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        var keyHash = Utility.getKeyHash(this)
        Log.e("해쉬값", keyHash)
        KakaoSdk.init(this,"df76105f104673f0c8c4052534d35167"); //카카오 SDK 초기화(네이티브 앱 키)
    }
}