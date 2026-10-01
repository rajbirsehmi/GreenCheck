package com.creative.greencheck.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Singleton

/**
 * An interceptor that enforces a minimum interval between outgoing HTTP requests
 * to avoid hammering the remote Open Food Facts API servers and prevent rate-limiting.
 */
@Singleton
class RateLimitInterceptor @Inject constructor() : Interceptor {
    private val lastRequestTime = AtomicLong(0L)
    private val minIntervalMs = 300L

    override fun intercept(chain: Interceptor.Chain): Response {
        val now = System.currentTimeMillis()
        val previous = lastRequestTime.getAndSet(now)
        val elapsed = now - previous

        if (previous > 0L && elapsed < minIntervalMs) {
            try {
                Thread.sleep(minIntervalMs - elapsed)
            } catch (_: InterruptedException) {
                // Thread interrupted; continue with request
            }
        }

        return chain.proceed(chain.request())
    }
}
