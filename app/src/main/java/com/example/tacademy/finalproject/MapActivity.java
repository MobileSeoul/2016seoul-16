package com.example.tacademy.finalproject;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.tacademy.finalproject.com.db.dto.MapInfoListDTO;
import com.example.tacademy.finalproject.parser.MapInfoParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.skp.Tmap.TMapData;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapPolyLine;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    WebView webView;
    AQuery aq = null;
    GoogleMap map;
    MapInfoListDTO mapInfoListDTO;
    TextView textView1, textView2,streetText;
    Button button1;
    /*위치 START*/

    LocationManager manager;
    LocationListener listener;

    /*위치 END*/

    LatLng current;

    TMapView tMapView;
    TMapData tMapData;
    TMapPoint start;
    TMapPoint end;
    ArrayList<TMapPoint> linePoint;
    PolylineOptions polylineOptions;

    double markerLati, markerLogi;

    Polyline polyline;
    double street;

    String bb;

    Button button2;

    JobThread jobThread = new JobThread();
    Marker marker;
    @Override
    @JavascriptInterface/*1*/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        streetText = (TextView)findViewById(R.id.street);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        webView();
        aquery();
        tMapView = new TMapView(this);
        tMapView.setSKPMapApiKey("983f86ea-e42b-356c-be63-fe1076b44ff5");

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(marker != null){
                    marker.remove();
                }
                current = new LatLng(location.getLatitude(),location.getLongitude());
                /*Toast.makeText(MapActivity.this,"현재 내위치 "+current,Toast.LENGTH_SHORT).show();*/
                LM.v("내 위치"+current);

                userNumHandler.sendEmptyMessage(400);
                Intent intent = new Intent("com.example.tacademy.finalproject.intent.action.dd");
                intent.putExtra("lati",current.latitude);
                intent.putExtra("longi",current.longitude);
                sendBroadcast(intent);


                marker = map.addMarker(new MarkerOptions().position(current).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.mymarker))));

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
    }

    Handler userNumHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100 :
                    if(polyline != null){
                        polyline.remove();
                    }
                    polyline = map.addPolyline(polylineOptions);
                    break;
                case 200 :
                    streetText.setText(bb);
                    streetText.setVisibility(View.VISIBLE);
                    /*textView3.setText(bb);*/
                    break;
                case 400 :
                    Intent intent = new Intent("com.example.tacademy.finalproject.intent.action.dd");
                    intent.putExtra("lati",current.latitude);
                    intent.putExtra("longi",current.longitude);
                    sendBroadcast(intent);
                    break;

            }
        }
    };


    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        LatLng firstCurrent = new LatLng(37.5679870,126.9771630);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(firstCurrent, 15));
    }

    public void tmapData(){
        start = new TMapPoint(current.latitude,current.longitude);
        end = new TMapPoint(markerLati,markerLogi);
        tMapData = new TMapData();
        tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH,start,end, new TMapData.FindPathDataListenerCallback(){
            @Override
            public void onFindPathData(TMapPolyLine tMapPolyLine) {

                linePoint = tMapPolyLine.getLinePoint();

                street = tMapPolyLine.getDistance()/1000;

                String aa = String.valueOf(street);
                int i = aa.indexOf(".");
                bb = aa.substring(0,i+3)+"km";
                userNumHandler.sendEmptyMessage(200);
                LM.v(bb);

                ArrayPolyline();
            }
        });
    }
    public void ArrayPolyline(){

            polylineOptions = new PolylineOptions();

            for(int i=0;i<linePoint.size();++i){
                polylineOptions.add(new LatLng(linePoint.get(i).getLatitude(),linePoint.get(i).getLongitude()));
            }
            polylineOptions.color(Color.RED);
            userNumHandler.sendEmptyMessage(100);

    }
    class JobThread extends Thread{
        public void run(){
            while (true){
                Intent intent = new Intent("com.example.tacademy.finalproject.intent.action.dd");
                    intent.putExtra("lati",current.latitude);
                    intent.putExtra("longi",current.longitude);
                sendBroadcast(intent);
                SystemClock.sleep(3000);
            }
        }
    }

    public void webView() {

        webView = (WebView) findViewById(R.id.webView2);
//        webView.loadUrl("file:///android_asset/index.html");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new AndroidBridge(), "HybridApp");/*1*/
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(LM.SERVER+"/app/mapHead");
    }

    private class AndroidBridge { /*1*/

        @JavascriptInterface
        public void test3(){
            LM.v("실행돼???");
            finish();
        }
    }

    public void aquery() {
        aq = new AQuery(this);/*무조건 생성*/
        String url = LM.SERVER+"/app/mapinfo";
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("post", "");

        aq.ajax(url, params, String.class, new AjaxCallback<String>() {/*두번째 인자는 리턴받을 클레스 , 세번째는 콜백*/

            @Override
            public void callback(String url, String object, AjaxStatus status) { /*두번째 인자는 리턴되는*/
                mapInfoListDTO = MapInfoParser.parse(object);
                marker();
            }
        });
    }

    public void marker() {
        map.setOnInfoWindowClickListener(null);
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(MapActivity.this, "잠시만 기다려 주시기 바랍니다", Toast.LENGTH_SHORT).show();
                if(current!=null){
                    tmapData();
                }else {
                    Toast.makeText(MapActivity.this,"현재 위치를 받아오고 있습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }
            View.OnClickListener handler = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()){
                        case R.id.button2:

                            Intent intent = new Intent(MapActivity.this,RoadView.class);
                            startActivity(intent);
                           /*jobThread.start();*/
                            LM.v("방송을 보냄");
                            break;

                    }
                }
            };

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.activity_infowindow, null);

                textView1 = (TextView) v.findViewById(R.id.textView2);
                textView2 = (TextView) v.findViewById(R.id.textView3);


                textView1.setText(marker.getTitle());
                textView2.setText(marker.getSnippet());

                markerLati = marker.getPosition().latitude;
                markerLogi = marker.getPosition().longitude;
                button2 = (Button)findViewById(R.id.button2);
                button2.setVisibility(View.VISIBLE);
                button2.setOnClickListener(handler);

                return v;
            }
        });
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){
            @Override
            public boolean onMyLocationButtonClick()
            {
                return false;
            }
        });
        for (int i = 0; i <= mapInfoListDTO.getListVO().size(); ++i) {
            LatLng parcel = new LatLng(mapInfoListDTO.getListVO().get(i).getI_lati(), mapInfoListDTO.getListVO().get(i).getI_longi());
            map.addMarker(new MarkerOptions().position(parcel)
                            .title(mapInfoListDTO.getListVO().get(i).getI_place())
                            .snippet(mapInfoListDTO.getListVO().get(i).getI_useTime())
                            .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.marker2)))
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        manager = (LocationManager)getSystemService(LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,15,listener);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,15,listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.removeUpdates(listener);
    }
}
