package com.example.tacademy.finalproject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.tacademy.finalproject.com.db.dto.SmsInfoListVO;
import com.example.tacademy.finalproject.helper.PersonHelper;
import com.example.tacademy.finalproject.parser.SmsInfoParser;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

    WebView webView;
    private final Handler handler = new Handler();/*1*/

    /*SqlLiteDB START*/
    PersonHelper helper = null;
    SQLiteDatabase liteDB;
    /*SqlLiteDB END*/

    /*카메라 START*/
    private static final int FILECHOOSER_RESULTCODE = 2888;

    final Activity activity = this;
    public Uri imageUri;

    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    /*카메라 END*/


    String userNum;

    AQuery aq = null;

    SmsInfoListVO smsinfoListVO;

    String location;

    int keyCode;

    private static MainActivity inst;

    public static MainActivity instance(){
        return inst;
    }

    public void updateList(){
        aquery();
    }

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    @JavascriptInterface/*1*/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 현재 유효한 위치정보 공급자 명을 얻는다
        String providers = android.provider.Settings.Secure.getString(
                getContentResolver(),
                android.provider.Settings.Secure.
                        LOCATION_PROVIDERS_ALLOWED);

        // 공급자 이름에 GPS가 포함되어 있지 않으면
        if (providers.indexOf("gps", 0) == -1){
            // 위치 정보 설정을 호출하는 인텐트 설정
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            // 활동 시작
            startActivity(intent);
        }

        internetConnectionCheck();

        startActivity(new Intent(this, SplashActivity.class));
        backPressCloseHandler = new BackPressCloseHandler(this);
        /*SqlLiteDBSTART(fileName); sqlLiteInsert문 봉인*/
        select();
        LM.v(LM.SERVER+"/app/index");

        aquery();


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        webView();


    }


    private class AndroidBridge { /*1*/
        @JavascriptInterface
        public void test(){
            Intent intent = new Intent(MainActivity.this,MapActivity.class);
            startActivity(intent);
        }
        @JavascriptInterface
        public void test1(){
            userNumHandler.sendEmptyMessage(100);
        }
        @JavascriptInterface
        public void test2(){
            userNumHandler.sendEmptyMessage(200);
        }

    }
    Handler userNumHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100 :
                    webView.loadUrl("javascript:setMessage('"+userNum+"')");/*자바스크립트에서 안드로이드를 호출*/
                    break;
                case 200 :
                    webView.loadUrl("javascript:setMessage('"+smsinfoListVO.getListVO().get(0).getS_location()+"')");/*자바스크립트에서 안드로이드를 호출*/
                    break;
            }
        }
    };



    public void webView(){
        webView = (WebView)findViewById(R.id.webView);
//        webView.loadUrl("file:///android_asset/index.html");

        /*webView.setWebViewClient(new WebViewClient());*/
        webView.getSettings().setJavaScriptEnabled(true); /*2*/
        webView.addJavascriptInterface(new AndroidBridge(), "HybridApp");/*1*/
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);

        /*수정부분 START */
        /*webView.setWebChromeClient(new WebChromeClient());*/
        /*수정부분 END*/

        webView.loadUrl(LM.SERVER+"/app/index");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String overrideUrl) {
                view.stopLoading();
                view.loadUrl(overrideUrl);
                return false;
            }

            public void onReceivedError(WebView view, int errorCode, String description, String fallingUrl) {
                view.loadData("<html><body></body></html>", "text/html", "UTF-8");
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Alert title")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            }
        });
    }


    public void SqlLiteDBSTART(String name){
        dbOpen();
        ContentValues values = new ContentValues();/*values('"+name+"',"+age+","+type+") 이걸 해주는 안드로이드 메소드*/  /*업데이트할때도 사용하는 메소드*/
        values.put("name", name);

        try{
            long id = liteDB.insert("person", null, values);/*3개의 인자가 필요함, 첫번째는 insert하고자하는 table명*/

            if(id > 0){/*성공하면 무조건 0이상의 숫자가 나온다*/
                LM.v("insert success");
            }else{
                LM.v("insert fail");
            }
        }catch (SQLException e){
            LM.v("insert e"+e);
        }
        dbClose();
    }

    public void select(){
        dbOpen();
        String sql = "select * from person";
        Cursor c = null;
        try{
            c = liteDB.rawQuery(sql.toString(), null);/*select 실행시키는 메소드*/
            while(c.moveToNext()){/*rs.next 랑 비슷*/
                LM.v("select : "+c.getCount());/*총row의 수*/
                LM.v(c.getString(c.getColumnIndex("name")));/*컬럼의 숫자를 적는다*/

                userNum = c.getString(c.getColumnIndex("name"));
            }

        }catch (SQLException e){
            LM.v("select e : "+e);
        }finally {
            c.close();
        }
        dbClose();
    }

    void dbOpen(){
        if(helper == null){
            helper = new PersonHelper(this, "info.db",null,2);
        }
        liteDB = helper.getWritableDatabase();
    }
    void dbClose(){
        if(liteDB != null){
            if(liteDB.isOpen()){
                liteDB.close();
            }
        }
        if(helper != null){
            helper.close();
        }
        helper = null;
    }

    void aquery(){
        aq = new AQuery(MainActivity.this);/*무조건 생성*/
        String url = LM.SERVER+"/app/smsinfoselect1";
        final Map<String, Object> params = new HashMap<String, Object>();

        aq.ajax(url, params, String.class, new AjaxCallback<String>() {/*두번째 인자는 리턴받을 클레스 , 세번째는 콜백*/
            @Override
            public void callback(String url, String object, AjaxStatus status) { /*두번째 인자는 리턴되는*/
                SmsInfoListVO(object);
            }
        });
    }
    void SmsInfoListVO(String string){
        smsinfoListVO = SmsInfoParser.parse(string);
    }

    @Override
    protected void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this.keyCode = keyCode;
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
    protected boolean internetConnectionCheck(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        if(connectivityManager != null){
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if(netInfo != null && netInfo.isConnected()){
                LM.v("BaseActivity_internetConnectionCheck netInfo.isConnected() : "+ netInfo.isConnected());
                Toast.makeText(this, "인터넷 연결이 되어 있습니다", Toast.LENGTH_SHORT).show();
                return true;
            }else{
                Toast.makeText(this, "인터넷 연결을 해주시기 바랍니다", Toast.LENGTH_SHORT).show();
                return false;
                /** 인터넷 미연결*/
            }
        }else{
            Toast.makeText(this, "connectivityManage =null", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
