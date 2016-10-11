package com.nike.hacklikeagirl.network;

import com.nike.mynike.BuildConfig;
import com.nike.mynike.R;
import com.nike.mynike.environment.EnvironmentManager;
import com.nike.mynike.logging.Log;
import com.nike.mynike.model.generated.commerce.offers.Object;
import com.nike.mynike.utils.Constants;
import com.nike.mynike.utils.StethoUtils;
import com.nike.retroasterisk.auth.BasicAuthInterceptor;
import com.nike.retroasterisk.auth.OAuthRefreshInterceptor;
import com.nike.retroasterisk.pincert.CertificatePinning;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


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
    private static CommerceV1NetworkApi sCommerceClient;
    private static Gson sGson;
    private static DoubleClickFloodLightTagsApi sDoubleClickClient;
    private static SearchApi sTypeAheadSearchClient;
    private static StoreLocationsNetworkAPI sStoreLocationsClient;
    private static KochavaApi sKochavaApi;
    private static NikeIdNetworkAPI sNikeIdNetworkClient;
    private static NikeEventsNetworkApi sNikeEventClient;
    private static OffersNetworkApi sOffersClient;
    private static NikeClientConfigApi sNikeClientConfigClient;
    private static NikeAuthenticationApi sNikeAuthenticationClient;
    private static NikePlusMigrationNetworkApi sNikePlusMigrationClient;
    private static SwooshLoginNetworkAPI sSwooshClient;
    private static HttpLoggingInterceptor sLoggingInterceptor = new HttpLoggingInterceptor(
            new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.d(TAG, message);
                }
            });
    private static NikeIdNetworkAPI sNikeIdXMLClient;

    static {
        sLoggingInterceptor.setLevel(BuildConfig.SHOW_LOGS ? HttpLoggingInterceptor.Level.BODY :
                HttpLoggingInterceptor.Level.NONE);
    }

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
            Log.toExternalCrashReporting(TAG, "clear cache failed", e);
        }
    }

    static CommerceV1NetworkApi getCommerceV1NetworkApi(Context context)
            throws PinCertsException {
        if (sCommerceClient == null) {
            synchronized (RestClient.class) {
                if (sCommerceClient == null) {
                    sCommerceClient = getJsonRetrofit(context,
                            EnvironmentManager.getCurrentEnvironment(context)
                                    .getCommerceApiUrl(), false).create(CommerceV1NetworkApi.class);
                }
            }
        }
        return sCommerceClient;
    }

    static NikeEventsNetworkApi getNikeEventsNetworkApi(Context context)
            throws PinCertsException {
        if (sNikeEventClient == null) {
            synchronized (RestClient.class) {
                if (sNikeEventClient == null) {
                    sNikeEventClient = getJsonRetrofit(context,
                            new Uri.Builder()
                                    .authority(BuildConfig.EVENTS_DEFAULT_ENVIRONMENT_AUTHORITY)
                                    .scheme(HTTPS)
                                    .build().toString(), false).create(NikeEventsNetworkApi.class);
                }
            }
        }
        return sNikeEventClient;
    }

    static OffersNetworkApi getOffersNetworkApi(Context context)
            throws PinCertsException {
        if (sOffersClient == null) {
            synchronized (RestClient.class) {
                if (sOffersClient == null) {
                    sOffersClient = getJsonRetrofit(context,
                            EnvironmentManager.getCurrentEnvironment(context).getOffersApiUrl(),
                            false).create(OffersNetworkApi.class);
                }
            }
        }
        return sOffersClient;
    }

    static NikeClientConfigApi getNikeClientConfigApi(Context context)
            throws PinCertsException {
        if (sNikeClientConfigClient == null) {
            synchronized (NikeClientConfigApi.class) {
                if (sNikeClientConfigClient == null) {
                    sNikeClientConfigClient = getJsonRetrofit(context,
                            EnvironmentManager.getCurrentEnvironment(context)
                                    .getNikeClientConfigApiUrl(), false)
                            .create(NikeClientConfigApi.class);
                }
            }
        }
        return sNikeClientConfigClient;
    }

    static CartNetworkAPI getCartNetworkApi(Context context)
            throws PinCertsException {
        // TODO - WHEN fix cookie interceptor is complete, uncomment.
//        if (sCartClient == null) {
//            synchronized (RestClient.class) {
//                if (sCartClient == null) {
//                    sCartClient = getJsonRetrofit(context,
//                            EnvironmentManager.getCurrentEnvironment(context).getBuyUrl(),
//                            true).create(CartNetworkAPI.class);
//                }
//            }
//        }
//        return sCartClient;
        return getJsonRetrofitWithTimeout(context,
                EnvironmentManager.getCurrentEnvironment(context).getBuyUrl(),
                true, HTTP_TIMEOUT_40_SECONDS).create(CartNetworkAPI.class);
    }

    static OrderHistoryNetworkAPI getOrderHistoryNetworkApi(Context context)
            throws PinCertsException {
        // TODO - WHEN fix cookie interceptor is complete, uncomment.
//        if (sOrderHistoryClient == null) {
//            synchronized (RestClient.class) {
//                if (sOrderHistoryClient == null) {
//                    sOrderHistoryClient = getJsonRetrofit(context,
//                            EnvironmentManager.getCurrentEnvironment(context)
//                                    .getSecureNikeTownStoreDomain(), true)
//                            .create(OrderHistoryNetworkAPI.class);
//                }
//            }
//        }
//        return sOrderHistoryClient;
        return getJsonRetrofit(context,
                EnvironmentManager.getCurrentEnvironment(context)
                        .getSecureNikeTownStoreDomain(), true)
                .create(OrderHistoryNetworkAPI.class);
    }

    static StoreLocationsNetworkAPI getNikeStores(Context context) {
        if (sStoreLocationsClient == null) {
            synchronized (RestClient.class) {
                if (sStoreLocationsClient == null) {
                    sStoreLocationsClient = getJsonNoAuthRetrofit(context,
                            BuildConfig.NIKE_BRICKWORKS_DOMAIN)
                            .create(StoreLocationsNetworkAPI.class);
                }
            }
        }
        return sStoreLocationsClient;
    }

    static KochavaApi getKochavaApi(Context context) {
        if (sKochavaApi == null) {
            synchronized (RestClient.class) {
                if (sKochavaApi == null) {
                    sKochavaApi = getJsonNoAuthRetrofit(context,
                            BuildConfig.KOCHAVA_HOST)
                            .create(KochavaApi.class);
                }
            }
        }
        return sKochavaApi;
    }

    static NikeIdNetworkAPI getNikeIdNetworkApi(Context context)
            throws PinCertsException {
        if (sNikeIdNetworkClient == null) {
            synchronized (RestClient.class) {
                if (sNikeIdNetworkClient == null) {
                    sNikeIdNetworkClient = getJsonRetrofit(context,
                            EnvironmentManager.getCurrentEnvironment(context).getNikeIdApiUrl(),
                            false).create(NikeIdNetworkAPI.class);
                }
            }
        }
        return sNikeIdNetworkClient;
    }

    static CommerceAuthNetworkAPI getCommerceAuthNetworkAPI(Context context)
            throws PinCertsException {
        // TODO - WHEN fix cookie interceptor is complete, uncomment.
//        if (sCommerceAuthClient == null) {
//            synchronized (RestClient.class) {
//                if (sCommerceAuthClient == null) {
//                    sCommerceAuthClient = getJsonRetrofit(context,
//                            EnvironmentManager.getCurrentEnvironment
//                                    (context).getApigeeUrl(), true)
//                            .create(CommerceAuthNetworkAPI.class);
//                }
//            }
//        }
//        return sCommerceAuthClient;
        return getJsonRetrofit(context,
                EnvironmentManager.getCurrentEnvironment
                        (context).getApigeeUrl(), true)
                .create(CommerceAuthNetworkAPI.class);
    }

    static NikeAuthenticationApi getNikeAuthenticationApi(Context context)
            throws PinCertsException {
        if (sNikeAuthenticationClient == null) {
            synchronized (NikeAuthenticationApi.class) {
                if (sNikeAuthenticationClient == null) {
                    sNikeAuthenticationClient = getJsonRetrofit(context,
                            EnvironmentManager.getCurrentEnvironment
                                    (context).getApigeeUrl(), false)
                            .create(NikeAuthenticationApi.class);
                }
            }
        }
        return sNikeAuthenticationClient;
    }

    static NikeIdNetworkAPI getNikeIdXMLNetworkApi(Context context)
            throws PinCertsException {
        if (sNikeIdXMLClient == null) {
            synchronized (RestClient.class) {
                if (sNikeIdXMLClient == null) {
                    sNikeIdXMLClient = getXmlRetrofit(context,
                            EnvironmentManager.getCurrentEnvironment(context)
                                    .getSecureStoreDomain()).create(NikeIdNetworkAPI.class);
                }
            }
        }
        return sNikeIdXMLClient;
    }

    static SearchApi getTypeAheadSearchApi(Context context) {
        if (sTypeAheadSearchClient == null) {
            synchronized (RestClient.class) {
                if (sTypeAheadSearchClient == null) {
                    sTypeAheadSearchClient = getJsonNoAuthRxRetrofit(context,
                            EnvironmentManager.getCurrentEnvironment(context).getNikeSearchUrl())
                            .create(SearchApi.class);
                }
            }
        }
        return sTypeAheadSearchClient;
    }

    static TetheredProductPasscodeApi getPasscodeApi(Context context)
            throws PinCertsException {
        // TODO - WHEN fix cookie interceptor is complete, uncomment.
//        if (sTetheredProductPasscodeClient == null) {
//            synchronized (RestClient.class) {
//                if (sTetheredProductPasscodeClient == null) {
//                    sTetheredProductPasscodeClient = getJsonRetrofit(context,
//                            EnvironmentManager.getCurrentEnvironment(context)
//                                    .getSecureStoreDomain(), true)
//                            .create(TetheredProductPasscodeApi.class);
//                }
//            }
//        }
//        return sTetheredProductPasscodeClient;
        return getJsonRetrofit(context,
                EnvironmentManager.getCurrentEnvironment(context)
                        .getSecureStoreDomain(), true)
                .create(TetheredProductPasscodeApi.class);
    }

    static DoubleClickFloodLightTagsApi getDoubleClickFloodLightTagsApi(Context context) {
        if (sDoubleClickClient == null) {
            synchronized (RestClient.class) {
                if (sDoubleClickClient == null) {
                    sDoubleClickClient = getJsonNoAuthRetrofit(context,
                            BuildConfig.DOUBLECLICK_HOSTNAME)
                            .create(DoubleClickFloodLightTagsApi.class);
                }
            }
        }
        return sDoubleClickClient;
    }

    static NikePlusMigrationNetworkApi getNikePlusMigrationApi(Context context)
            throws PinCertsException {
        if (sNikePlusMigrationClient == null) {
            synchronized (RestClient.class) {
                if (sNikePlusMigrationClient == null) {
                    sNikePlusMigrationClient = getJsonRetrofit(context,
                            EnvironmentManager.getCurrentEnvironment(context).getApigeeUrl(), false)
                            .create(NikePlusMigrationNetworkApi.class);
                }
            }
        }
        return sNikePlusMigrationClient;
    }

    static SwooshLoginNetworkAPI getSwooshLoginApi(Context context)
            throws PinCertsException {
        if (sSwooshClient == null) {
            synchronized (RestClient.class) {
                if (sSwooshClient == null) {
                    sSwooshClient = getJsonNoAuthRxRetrofit(context,
                            EnvironmentManager.getCurrentEnvironment(context)
                                    .getSecureStoreDomain())
                            .create(SwooshLoginNetworkAPI.class);
                }
            }
        }
        return sSwooshClient;
    }

    /**
     * Not using the nike oauth2 access tokens
     */
    private static Retrofit getJsonNoAuthRetrofit(Context context, String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .client(getJsonNoOAuthClient(context))
                .callbackExecutor(NetworkExecutor.NETWORK)
                .build();
    }

    private static Retrofit getJsonNoAuthRxRetrofit(Context context, String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .client(getJsonNoOAuthClient(context))
                .build();
    }

    private static void addHttpAuthInterceptor(OkHttpClient.Builder clientBuilder,
            Context context) {
        if (BuildConfig.DEBUG && !EnvironmentManager.getCurrentEnvironment(context).getTitle()
                .equals(Constants.PRODUCTION_ENV)) {
            clientBuilder.addInterceptor(
                    new BasicAuthInterceptor(new OmegaAuthProvider(context)));
        }
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
            throws PinCertsException {
        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(requiresCookies ? getJsonCookieClient(context) :
                        getJsonClient(context))
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .callbackExecutor(NetworkExecutor.NETWORK)
                .build();
    }

    /**
     * Create a Retrofit REST adapter with specified timeout for handling Json responses.
     *
     * @param requiresCookies           endpoint requires cookies passed in header for
     *                                  authentication for
     *                                  commerce endpoints
     *                                  (i.e. jCart addItems to cart,order history, cart
     *                                  summary)
     * @param connectionsTimeoutSeconds use to avoid SocketTimeoutExceptions
     */
    private static Retrofit getJsonRetrofitWithTimeout(Context context, String baseURL,
            boolean requiresCookies, int connectionsTimeoutSeconds)
            throws PinCertsException {
        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(getJsonCookieClientWithTimeout(context,
                        connectionsTimeoutSeconds))
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .callbackExecutor(NetworkExecutor.NETWORK)
                .build();
    }

    private static OkHttpClient getJsonClient(Context context)
            throws PinCertsException {
        if (sJsonClient == null) {
            synchronized (RestClient.class) {
                if (sJsonClient == null) {
                    OkHttpClient.Builder builder = getJsonClientBuilder(context, getCache(context),
                            false);
                    if (BuildConfig.PIN_CERTS) {
                        pinCertificates(context, builder);
                    }
                    sJsonClient = builder.build();
                }
            }
        }
        return sJsonClient;
    }

    /**
     * we create a new json cookie client each time for now since
     * there is an issue with how we are handling the cookies and
     * having a singleton client
     * TODO - fix cookie interceptor
     */
    private static OkHttpClient getJsonCookieClient(Context context)
            throws PinCertsException {
        OkHttpClient.Builder builder = getJsonClientBuilder(context, getCache(context),
                true);
        if (BuildConfig.PIN_CERTS) {
            pinCertificates(context, builder);
        }
        return builder.build();
    }

    /**
     * We create a new json cookie client with specified timeout each time for now since
     * there is an issue with how we are handling the cookies and
     * having a singleton client
     * The timeout is added to avoid SocketTimeoutExceptions
     */
    private static OkHttpClient getJsonCookieClientWithTimeout(Context context, int
            connectionsTimeoutSeconds)
            throws PinCertsException {
        OkHttpClient.Builder builder = getJsonClientBuilder(context, getCache(context),
                true);
        if (BuildConfig.PIN_CERTS) {

            pinCertificates(context, builder);
        }
        builder.connectTimeout(connectionsTimeoutSeconds, TimeUnit.SECONDS);
        builder.writeTimeout(connectionsTimeoutSeconds, TimeUnit.SECONDS);
        builder.readTimeout(connectionsTimeoutSeconds, TimeUnit.SECONDS);
        return builder.build();
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
                    addHttpAuthInterceptor(clientBuilder, context);
                    clientBuilder.addInterceptor(sLoggingInterceptor);
                    //StethoUtils.addInterceptor is a noop for release
                    StethoUtils.addInterceptor(clientBuilder);
                    sJsonNoOAuthClient = clientBuilder.build();
                }
            }
        }
        return sJsonNoOAuthClient;
    }

    private static OkHttpClient.Builder getJsonClientBuilder(Context context, Cache cache,
            boolean hasCookies) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .cache(cache);
        addHttpAuthInterceptor(clientBuilder, context);
        if (hasCookies) {
            addCookieInterceptor(context, clientBuilder);
        }
        clientBuilder.addInterceptor(new OAuthRefreshInterceptor(new OmegaAuthProvider(context)));
        clientBuilder.addInterceptor(sLoggingInterceptor);
        //StethoUtils.addInterceptor is a noop for release
        StethoUtils.addInterceptor(clientBuilder);
        return clientBuilder;
    }

    /**
     * pin certificates to this client
     */
    private static void pinCertificates(Context context, OkHttpClient.Builder builder)
            throws PinCertsException {
        try {
            CertificatePinning.pin(builder, context.getResources(), R.raw.nike, STORE_PASS);
        } catch (CertificateException | UnrecoverableKeyException | KeyStoreException |
                NoSuchAlgorithmException | IOException | KeyManagementException e) {
            throw new PinCertsException(e);
        }
    }

    private static void addCookieInterceptor(Context context, OkHttpClient.Builder clientBuilder) {
        // Add interceptor to captures cookies from responses coming in.
        // Cookies are use later passed in headers of commerce endpoint calls
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        clientBuilder.cookieJar(new JavaNetCookieJar(cookieManager));
        clientBuilder.addInterceptor(new CookieInterceptor(context, cookieManager));
    }

    /**
     * Create a Retrofit REST adapter for handling XML responses.
     */
    private static Retrofit getXmlRetrofit(Context context, String baseUrl)
            throws PinCertsException {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(getXmlClient(context))
                .callbackExecutor(NetworkExecutor.NETWORK)
                .build();
    }

    /**
     * @return singleton xml client
     */
    private static OkHttpClient getXmlClient(Context context)
            throws PinCertsException {
        if (sXmlClient == null) {
            synchronized (RestClient.class) {
                if (sXmlClient == null) {
                    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                            .cache(getCache(context));
                    addHttpAuthInterceptor(clientBuilder, context);
                    addCookieInterceptor(context, clientBuilder);
                    clientBuilder.addInterceptor(
                            new OAuthRefreshInterceptor(new OmegaAuthProvider(context)));
                    clientBuilder.addInterceptor(sLoggingInterceptor);
                    if (BuildConfig.PIN_CERTS) {
                        pinCertificates(context, clientBuilder);
                    }
                    //StethoUtils.addInterceptor is a noop for release
                    StethoUtils.addInterceptor(clientBuilder);
                    sXmlClient = clientBuilder.build();
                }
            }
        }
        return sXmlClient;
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

    public static final class PinCertsException extends Exception {

        public PinCertsException(Exception exception) {
            super(exception.toString(), exception);
        }
    }

}
