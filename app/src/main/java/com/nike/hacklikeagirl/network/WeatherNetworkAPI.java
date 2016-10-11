package com.nike.hacklikeagirl.network;


import org.json.JSONObject;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Network class to call endopint login with swoosh credentials to get employee prices in
 * Webview Cart
 * Endpoint is called when swoosh pricing is enabled in Settings
 * Swoosh cookies expire every 30 minutes and user will need to re-login upon expiration
 * Swoosh prices will only be visible in Webview Cart, and will not be visible in app
 * (gridwall or PDP will not display employee prices)
 */
public interface WeatherNetworkAPI
{

    String ACTION_LOGIN = "login";
    String ACTION_LOGOUT = "logout";
    String STATUS_SUCCESS = "success";
    String STATUS_FAILURE = "failure";
    String DEFAULT_USER_TYPE = "defaultUser";
    String USER_TYPE_SWOOSH = "swooshUser";
    String RETURN_TYPE_JSON = "json";

    /**
     * Logs in with swoosh credentials in order to get cookies required for
     * endpoint calls (i.e. addToCart).
     * @param login    email address
     * @param password the password
     * @param rt       return-type
     * @param action   what you want to do?
     * @param userType defaultUser or swooshUser
     * @param country  two char, lower case ie. "us"
     */
    @Headers({
            "Accept: application/json",
            "Accept-Charset: utf-8",
            "Content-Type: application/x-www-form-urlencoded"
    })
    @FormUrlEncoded
    @POST("http://api.openweathermap.org/data/2.5/weather?zip=94040,us&APPID=da0578f96b2dbb5b4c4f0d87f0eb1aa3")
    Observable<Response<JSONObject>> swooshLoginObservable(
            @Path("country") String country,
            @Query("login") String login,
            @Query("password") String password,
            @Query("rt") String rt,
            @Field("action") String action,
            @Field("userType") String userType);
}
