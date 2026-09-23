package com.creative.greencheck.data.remote

import com.creative.greencheck.data.local.UsageManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * An interceptor that adds a mandatory custom User-Agent header to every request.
 * It identifies the application and uses a unique device UUID to manage API limits fairly.
 */
class UserAgentInterceptor @Inject constructor(
    private val usageManager: UsageManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val installationId = runBlocking { usageManager.getInstallationId() }
        
        val originalRequest = chain.request()
        val userAgent = "GreenCheck - Android - 2.0 - $installationId - https://github.com/rajbirsehmi/GreenCheck"
        
        val newRequest = originalRequest.newBuilder()
            .header("User-Agent", userAgent)
            .build()
            
        return chain.proceed(newRequest)
    }
}
