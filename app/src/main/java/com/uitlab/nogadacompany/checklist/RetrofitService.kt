package com.uitlab.nogadacompany.checklist

import com.google.gson.GsonBuilder
import com.uitlab.nogadacompany.noticedata.NoticeData
import com.uitlab.nogadacompany.user.UserDTO
import com.uitlab.nogadacompany.user.UserRegularDate
import com.uitlab.nogadacompany.user.UserResDTO
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

interface RetrofitService {

    @GET("dash/checkList")
    fun getCheckList() : Call<CheckData>

    @FormUrlEncoded
    @POST("dash/updateUserCheck")
    fun updateUserCheck(@Field("uid") userId: String, @Field("wischeck") check: Int) : Call<UserResDTO>

    @GET("/dash/AppnoticeDetail")
    fun getNoticeList() : Call<NoticeData>

    @POST("auth/insertBodyUserInfo")
    fun setUserInfo(@Body userDTO: UserDTO) : Call<UserResDTO>

    @FormUrlEncoded
    @POST("auth/findBodyUserInfo")
    fun findUserInfo(@Field("uid") userId: String) : Call<UserResDTO>

    @FormUrlEncoded
    @POST("auth/updateBodyUserInfo")
    fun updateUserInfo(@Field("unickname") wName: String, @Field("uemail") wEmail: String, @Field("uthumbnailImageUrl") wImage: String) : Call<UserResDTO>

    @FormUrlEncoded
    @POST("dash/reqAt")
    fun userAttendance(@Field("wid") userId: String, @Field("atDate") date: String) : Call<UserResDTO>

    @FormUrlEncoded
    @POST("auth/reqRD")
    fun selectUserReqularDate(@Field("wid") userId: String) : Call<UserRegularDate>



    companion object {
        var gson = GsonBuilder().setLenient().create()

        fun create(): RetrofitService {
            return Retrofit.Builder()
                .baseUrl("https://grounda.hopto.org")
                .client(getUnsafeOkHttpClient().build()) // ssl 우회
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RetrofitService::class.java)
        }

        fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

                }

                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {

                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            val sslSocketFactory = sslContext.socketFactory

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { hostname, session -> true }

            return builder
        }

    }
}