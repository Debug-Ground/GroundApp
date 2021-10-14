package com.uitlab.nogadacompany

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.kakao.sdk.user.UserApiClient
import com.uitlab.nogadacompany.adapter.ViewpagerMainActivityAdapater
import com.uitlab.nogadacompany.databinding.ActivityMainBinding
import com.uitlab.nogadacompany.kLogin.KakaoLogin
import com.uitlab.nogadacompany.mainfragment.EmployeeCardFg
import com.uitlab.nogadacompany.user.User

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root);


        binding.run {
            mainViewpager.adapter = ViewpagerMainActivityAdapater(this@MainActivity)
            mainViewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            mainViewpager.isUserInputEnabled = false //뷰페이저 스와이프 막기(구글권장)

            expandableBottomBar.onItemSelectedListener = { view, menuItem, _ ->
                when(menuItem.id) {
                    R.id.employCardMenu ->
                        mainViewpager.currentItem = 0
                    R.id.noticeMenu ->
                        mainViewpager.currentItem = 1
                    R.id.commuteCalendarMenu ->
                        mainViewpager.currentItem = 2
                }
            }
        }

    }
}