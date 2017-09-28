package com.example.tacademy.finalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class RoadView extends AppCompatActivity {
    WebView webView;
    BroadcastReceiver receiver = new  BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(RoadView.this,"위치값 변경"+intent.getDoubleExtra("lati", 0.0), Toast.LENGTH_SHORT).show();
            LM.v(intent.getDoubleExtra("lati", 0.0)+"위도");
            LM.v(intent.getDoubleExtra("longi", 0.0)+"경도");

            webView.loadUrl("javascript:aaa("+intent.getDoubleExtra("lati", 0.0)+','+intent.getDoubleExtra("longi", 0.0)+")");

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadview);
        webView = (WebView)findViewById(R.id.webView3);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(LM.SERVER+"/app/roadView");

    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.tacademy.finalproject.intent.action.dd");
        /*Toast.makeText(RoadView.this,"로드뷰 시작",Toast.LENGTH_SHORT).show();*/
        /*시작*/
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
         /*종료*/
        unregisterReceiver(receiver);
    }
}
