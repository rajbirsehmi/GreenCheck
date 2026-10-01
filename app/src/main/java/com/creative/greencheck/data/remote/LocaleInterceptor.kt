package com.creative.greencheck.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale
import javax.inject.Inject

/**
 * An interceptor that adds standard Accept-Language headers for localized responses
 * without mutating request query parameters or URL structure.
 */
class LocaleInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val locale = Locale.getDefault()
        val languageTag = locale.toLanguageTag()
        val languageCode = locale.language.lowercase()

        val newRequest = originalRequest.newBuilder()
            .header("Accept-Language", "$languageTag,$languageCode;q=0.9,en;q=0.8")
            .build()

        return chain.proceed(newRequest)
    }
}
