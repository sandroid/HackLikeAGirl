package com.nike.hacklikeagirl.ui;

import com.nike.hacklikeagirl.R;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class WebviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("https://wecode-nov2016s.splashthat.com/#settings");
    }
}
