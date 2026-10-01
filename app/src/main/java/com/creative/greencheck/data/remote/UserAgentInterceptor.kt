package com.creative.greencheck.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * An interceptor that adds a standard custom User-Agent header to identify the app cleanly.
 */
class UserAgentInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val userAgent = "GreenCheck/2.0 (Android; Mobile)"

        val newRequest = originalRequest.newBuilder()
            .header("User-Agent", userAgent)
            .build()

        return chain.proceed(newRequest)
    }
}
