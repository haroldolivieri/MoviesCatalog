package haroldolivieri.moviescatalog.remote

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.regex.Pattern


class CacheRevalidationInterceptor(private val context: Context) : Interceptor {
    companion object {
        private val urlRegExp = Pattern.compile(".*/(stores|products).*")
        private val WARNING_RESPONSE_IS_STALE = "110"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalResponse = chain.proceed(request)

        if (shouldSkipInterceptor(request, originalResponse)) {
            return originalResponse
        }

        val modifiedRequest = removeCacheHeaders(request)

        return try {
            val retriedResponse = chain.proceed(modifiedRequest)

            if (retriedResponse == null || !retriedResponse.isSuccessful()) {
                originalResponse
            } else retriedResponse

        } catch (e: IOException) {
            originalResponse
        }

    }

    private fun shouldSkipInterceptor(request: Request, response: Response?): Boolean {
        if (response == null) {
            return true
        }

        Log.i("Cache", "URL ${request.url()}... Checking for cache...")
        val matcher = urlRegExp.matcher(request.url().toString())
        if (matcher.matches()) {
            Log.i("Cache", "URL ${request.url()}... Tyring to use cache")
            return true
        }

        val warningHeaders = response.headers("Warning")

        warningHeaders.filter { it.startsWith(WARNING_RESPONSE_IS_STALE) }
                .forEach {
                    if (!context.isNetworkAvailable()) {
                        return true
                    }
                }

        return false
    }

    private fun removeCacheHeaders(request: Request): Request {
        val modifiedHeaders = request.headers()
                .newBuilder()
                .removeAll("Cache-Control")
                .build()

        return request.newBuilder()
                .headers(modifiedHeaders)
                .build()
    }
}

fun Context.isNetworkAvailable(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.activeNetworkInfo
    return netInfo != null && netInfo.isConnectedOrConnecting
}