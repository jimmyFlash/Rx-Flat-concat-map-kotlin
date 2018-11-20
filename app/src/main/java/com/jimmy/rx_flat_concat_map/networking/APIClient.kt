package com.jimmy.rx_flat_concat_map.networking

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * Created by jamal.safwat on 1/7/2018.
 */

class APIClient {

    // defining static function / variables using companion objects
    companion object {

        fun  resetclient(){

        }

        // https://api.androidhive.info/json/airline-tickets.php

        // To fetch individual ticket price
        // https://api.androidhive.info/json/airline-tickets-price.php

        private var retrofit: Retrofit? = null

        fun getClient() : Retrofit{

            /*
                HttpLoggingInterceptor are added to print the Request / Response in LogCat for debugging purpose
             */
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

       /*     val client = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build()*/

            val client = getUnsafeOkHttpClient()
                    ?.addInterceptor(interceptor)
                    ?.addInterceptor { chain :Interceptor.Chain ->
                        val original = chain.request()
                        val requestBuilder = original.newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("Request-Type", "Android")
                            .addHeader("Content-Type", "application/json")
                        val request = requestBuilder.build()
                        chain.proceed(request)
                    }
                    ?.readTimeout(60, TimeUnit.SECONDS)
                    ?.connectTimeout(60, TimeUnit.SECONDS)
                    ?.writeTimeout(60, TimeUnit.SECONDS)
                    ?.build()

            retrofit = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            return retrofit!!

        }

        /**
         * enables okhttp to accept untrusted certificates (when behind firewall or proxy)
         */
        fun getUnsafeOkHttpClient() : OkHttpClient.Builder?{

             lateinit var builder : OkHttpClient.Builder

            try {

                val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager{
                    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                       //
                    }

                    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                      //
                    }
                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }

                })
                // Create a trust manager that does not validate certificate chains
                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, java.security.SecureRandom())

                // Create an ssl socket factory with our all-trusting manager
                val  sslSocketFactory : SSLSocketFactory = sslContext.socketFactory

                builder = OkHttpClient.Builder()
                builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)

                builder.hostnameVerifier { hostname, session -> true }
                return builder
            } catch ( e : Exception) {
//                throw  RuntimeException(e)
                println("OKHTTP error >>>>>>>>>>>> ${e.message}")
                return OkHttpClient.Builder()
            }

        }
    }

}
