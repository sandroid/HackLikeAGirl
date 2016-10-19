package com.nike.hacklikeagirl.network;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Get retrofit network interfaces for making network requests.
 */
public final class RestClient {

    public static final int HTTP_TIMEOUT_40_SECONDS = 40;
    private static final String TAG = RestClient.class.getSimpleName();
    private static final String RFC_339_TIME_FORMAT = "yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'";
    private static final String JSON_CACHE_LOCATION = "json-cached-content";
    private static final long CACHE_SIZE = 20 * 1024 * 1024; // 20mb
    private static final char[] STORE_PASS = {'N', 'i', 'k', 'e', 'N', 'i', 'k', 'e'};
    /** cache needs a separate lock to avoid deadlock */
    private static final Object CACHE_LOCK = new Object();
    private static final String HTTPS = "https";
    private static OkHttpClient sXmlClient;
    private static OkHttpClient sJsonClient;
    private static OkHttpClient sJsonNoOAuthClient;
    private static Cache sCache;
    private static Gson sGson;

    private static WeatherNetworkAPI sWeatherClient;


    private RestClient() {
    }

    /**
     * clear all http client caches
     */
    public static void clearOkHttpCache() {
        try {
            if (sCache != null) {
                sCache.evictAll();
            }
        } catch (IOException e) {
//            Log.toExternalCrashReporting(TAG, "clear cache failed", e);
        }
    }


    static WeatherNetworkAPI getWeather(Context context)
            {
        if (sWeatherClient == null) {
            synchronized (RestClient.class) {
                if (sWeatherClient == null) {
                    sWeatherClient = getJsonNoAuthRetrofit(context,
                            "http://api.openweathermap.org")
                            .create(WeatherNetworkAPI.class);
                }
            }
        }
        return sWeatherClient;
    }

    /**
     * Not using the nike oauth2 access tokens
     */
    private static Retrofit getJsonNoAuthRetrofit(Context context, String baseUrl) {
        return new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .client(getJsonNoOAuthClient(context))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
    }


    /**
     * Create a Retrofit REST adapter for handling Json responses.
     *
     * @param requiresCookies endpoint requires cookies passed in header for authentication for
     *                        commerce endpoints
     *                        (i.e. jCart addItems to cart,order history, cart summary)
     */
    private static Retrofit getJsonRetrofit(Context context, String baseURL,
            boolean requiresCookies)
             {
        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(getJsonClient(context))
                .addConverterFactory(GsonConverterFactory.create(getGson()))
//                .callbackExecutor(NetworkExecutor.NETWORK)
                .build();
    }



    private static OkHttpClient getJsonClient(Context context)
             {
        if (sJsonClient == null) {
            synchronized (RestClient.class) {
                if (sJsonClient == null) {
//                    OkHttpClient.Builder builder = getJsonClientBuilder(context, getCache(context),
//                            false);
//
//                    sJsonClient = builder.build();
                }
            }
        }
        return sJsonClient;
    }



    /**
     * Gets a basic (with logging) OkhttpClient with no auth interceptors.
     */
    private static OkHttpClient getJsonNoOAuthClient(Context context) {
        if (sJsonNoOAuthClient == null) {
            synchronized (RestClient.class) {
                if (sJsonNoOAuthClient == null) {
                    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                            .cache(getCache(context));
//                    addHttpAuthInterceptor(clientBuilder, context);
//                    clientBuilder.addInterceptor(sLoggingInterceptor);
                    sJsonNoOAuthClient = clientBuilder.build();
                }
            }
        }
        return sJsonNoOAuthClient;
    }




    /**
     * singleton cache that is used for all http clients
     */
    private static Cache getCache(Context context) {
        if (sCache == null || sCache.isClosed()) {
            synchronized (CACHE_LOCK) {
                if (sCache == null || sCache.isClosed()) {
                    sCache = new Cache(new File(context.getCacheDir(),
                            JSON_CACHE_LOCATION),
                            CACHE_SIZE);
                }
            }
        }
        return sCache;
    }

    @NonNull
    private static Gson getGson() {
        if (sGson == null) {
            synchronized (Gson.class) {
                if (sGson == null) {
                    sGson = new GsonBuilder()
                            .setDateFormat(RFC_339_TIME_FORMAT)
                            .create();
                }
            }
        }
        return sGson;
    }



}
