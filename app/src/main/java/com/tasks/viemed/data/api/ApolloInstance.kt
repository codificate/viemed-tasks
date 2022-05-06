package com.tasks.viemed.data.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.apollographql.apollo3.network.okHttpClient
import com.tasks.viemed.BuildConfig
import com.tasks.viewmed.GenerateTokenMutation
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.HttpException
import java.io.IOException

class ApolloInstance {

    companion object {
        val BUILDER: ApolloClient.Builder by lazy {
            apolloClient
        }

        fun setTokenAuthorization(token: String) : OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val updatedRequest = original.newBuilder()
                        .addHeader(
                            "Authorization",
                            token
                        )
                        .build()

                    chain.proceed(updatedRequest)
                }.build()
        }

        private val apolloClient = ApolloClient
            .Builder()
            .serverUrl(BuildConfig.VIEMED_API_URL)
    }

}