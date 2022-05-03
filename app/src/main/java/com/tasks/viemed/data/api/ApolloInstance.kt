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
        val BUILDER: ApolloClient by lazy {
            apolloClient
        }

        @OptIn(DelicateCoroutinesApi::class)
        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->

                val original = chain.request()
                var updatedRequest = chain.request()

                GlobalScope.launch {

                    try {
                        val tokenResponse = BUILDER.mutation(
                            GenerateTokenMutation(
                                BuildConfig.VIEMED_API_KEY,
                                BuildConfig.VIEMED_USER_NAME
                            )
                        ).execute()

                        tokenResponse.data?.let {
                            updatedRequest = original.newBuilder()
                                .addHeader(
                                    "Authorization",
                                    it.generateAccessToken.toString()
                                )
                                .build()
                        }

                    } catch (e: ApolloException) {
                        updatedRequest = chain.request()
                    } catch(e: HttpException) {
                        updatedRequest = chain.request()
                    } catch(e: IOException) {
                        updatedRequest = chain.request()
                    }
                }

                chain.proceed(updatedRequest)
            }.build()

        private val apolloClient = ApolloClient
            .Builder()
            .serverUrl(BuildConfig.VIEMED_API_URL)
            .okHttpClient(okHttpClient)
            .build()
    }

}