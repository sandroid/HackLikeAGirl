package com.nike.hacklikeagirl.network;

import org.json.JSONObject;

import android.content.Context;

import rx.Observable;


/**
 * Network class to call login endpoint with swoosh credentials to get employee prices in
 * Webview Cart
 * Endpoint is called when swoosh pricing is enabled in Settings
 * Swoosh cookies expire every 30 minutes and user will need to re-login upon expiration
 * Swoosh prices will only be visible in Webview Cart, and will not be visible in app
 * (gridwall or PDP will not display employee prices)
 */
public final class WeatherNao {

    private static final String TAG = WeatherNao.class.getSimpleName();

    private WeatherNao() {

    }

    /**
     * Logs into with swoosh credentials in order to get cookies required for
     * employee price
     * @param context
     * @param password
     **/
    public static Observable<JSONObject> loginToSwoosh(
            final Context context, final String password) {
//        try {
//            final PreferencesHelper preferencesHelper = PreferencesHelper.getInstance
//                    (context);
//            return RestClient.getSwooshLoginApi(context)
//                    .swooshLoginObservable(Locale.getDefault().getCountry().toLowerCase(),
//                            preferencesHelper.getSwooshEmail(),
//                            password, SwooshLoginNetworkAPI.RETURN_TYPE_JSON,
//                            SwooshLoginNetworkAPI.ACTION_LOGIN,
//                            SwooshLoginNetworkAPI.USER_TYPE_SWOOSH)
//                    .flatMap(new Func1<Response<SwooshLoginResponse>,
//                            Observable<SwooshLoginResponse>>() {
//                        @Override
//                        public Observable<SwooshLoginResponse> call(
//                                Response<SwooshLoginResponse> response) {
//                            if (response.isSuccessful()) {
//                                if (response.body().getStatus()
//                                        .equals(SwooshLoginNetworkAPI.STATUS_SUCCESS)) {
//                                    Log.d(TAG, "SWOOSH_AUTH is successful");
//                                    preferencesHelper.hasSwooshAccount(true);
//                                    preferencesHelper.clearAllSwooshCommerceCookies();
//                                    StringBuilder cookies = new StringBuilder();
//                                    Headers headers = response.headers();
//                                    for (String headerName : headers.names()) {
//                                        if (headerName.equals(Constants.SET_COOKIE)) {
//                                            List<String> cookieValues = headers
//                                                    .values(headerName);
//                                            for (String cookieValue : cookieValues) {
//                                                if (cookieValue.contains("slCheck")) {
//                                                    Log.d(TAG, "SWOOSH_AUTH "
//                                                            + "SLCHECKcookie value = "
//                                                            + cookieValue);
//                                                    String[] splitCookieValues
//                                                            = cookieValue.split(";");
//                                                    cookies.append(
//                                                            splitCookieValues[0] + ";");
//                                                } else if (cookieValue.contains("USID")) {
//                                                    Log.d(TAG, "SWOOSH_AUTH USDI cookie "
//                                                            + "value = " + cookieValue);
//                                                    String[] splitCookieValues
//                                                            = cookieValue.split(";");
//                                                    preferencesHelper
//                                                            .setCommerceCookies(
//                                                                    splitCookieValues[0]
//                                                                            + ";");
//                                                }
//                                            }
//                                        }
//                                    }
//                                    String cookiesString = cookies.toString();
//                                    preferencesHelper.setSlcheckCookie(cookiesString);
//                                    Time cookieTimestamp = new Time();
//                                    cookieTimestamp.setToNow();
//                                    preferencesHelper.setSlcheckCookieTimestamp(
//                                            cookieTimestamp.toMillis(true));
//                                    Log.d(TAG, "SWOOSH_AUTH Save new cookies in "
//                                            + "shared prefs = " + cookieTimestamp);
//                                } else {
//                                    Log.d(TAG, "SWOOSH_AUTH failed");
//                                    //TODO:  handle error?
//                                }
//                                return Observable.just(response.body());
//                            } else {
//                                return Observable
//                                        .error(NetworkResponseFailureException
//                                                .create(response));
//                            }
//                        }
//                    });
//        } catch (RestClient.PinCertsException e) {
//            Log.toExternalCrashReporting(TAG, e.getMessage());
//        }
        return null;
    }
}