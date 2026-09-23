package com.creative.greencheck.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale
import javax.inject.Inject

/**
 * An interceptor that dynamically updates the Open Food Facts API host based on the user's current locale.
 * For example, if the locale is set to France (FR), it changes the host from 'world' to 'fr'.
 * This leads to more relevant local results while maintaining 'world' as a fallback.
 */
class LocaleInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        
        // We only want to target our specific API domain
        if (originalUrl.host == "world.openfoodfacts.org") {
            val countryCode = Locale.getDefault().country.lowercase()
            
            if (countryCode.isNotEmpty()) {
                val newHost = "$countryCode.openfoodfacts.org"
                val newUrl = originalUrl.newBuilder()
                    .host(newHost)
                    .build()
                
                return chain.proceed(originalRequest.newBuilder().url(newUrl).build())
            }
        }
        
        return chain.proceed(originalRequest)
    }
}
