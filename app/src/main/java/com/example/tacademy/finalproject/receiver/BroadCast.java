package com.example.tacademy.finalproject.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsMessage;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.tacademy.finalproject.LM;
import com.example.tacademy.finalproject.MainActivity;
import com.example.tacademy.finalproject.R;
import com.example.tacademy.finalproject.helper.PersonHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tacademy on 2016-10-18.
 */
public class BroadCast extends BroadcastReceiver{
    NotificationManager manager;
    AQuery aq = null;
    Context context;

    String a1;
    String a2;
    String a3;
    String a4;

    PersonHelper helper = null;
    SQLiteDatabase liteDB;
    String user;
    @Override
    public void onReceive(Context context, Intent intent) {
            LM.v("825"+intent.getAction());
            Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
            vibrator.vibrate(500);

        this.context = context;
        manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            LM.v("문자가 수신되었습니다.");
//            abortBroadcast();

            // SMS 메시지를 파싱합니다.
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[])bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for(int i = 0; i < messages.length; i++) {
                // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                smsMessage[i] = SmsMessage.createFromPdu((byte[])messages[i]);
            }

            // SMS 수신 시간 확인
            Date curDate = new Date(smsMessage[0].getTimestampMillis());
            LM.v(curDate.toString());
            // SMS 발신 번호 확인
            String origNumber = smsMessage[0].getOriginatingAddress();

            // SMS 메시지 확인
            String message = smsMessage[0].getMessageBody().toString();
            LM.v("발신자 : "+origNumber+", 내용 : " + message);

            if(smsMessage[0].getMessageBody().substring(0,6).equals("[택배도착]")){
                Notification notification = null;
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo2);
                Intent intent2 = new Intent(context,MainActivity.class);

                PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent2,PendingIntent.FLAG_UPDATE_CURRENT); /*그 노티디테일을 누르면 */ /*FLAG_UPDATE_CURRENT기존껄 지우고 내껄 넣어라*/
                int a = smsMessage[0].getMessageBody().indexOf("[택배도착]");
                int b = smsMessage[0].getMessageBody().indexOf(":");
                int c = smsMessage[0].getMessageBody().indexOf("/");
                int d = smsMessage[0].getMessageBody().indexOf(":",30);
                int e = smsMessage[0].getMessageBody().indexOf(":",45);
                notification = new NotificationCompat.Builder(context)/*이렇게 하면 노티피케이션이 나온다*/
                        .setContentTitle(smsMessage[0].getMessageBody().substring(a+6,b+4))
//                        .setContentText("내용")
                        .setContentText("비밀번호: "+smsMessage[0].getMessageBody().substring(c+3,c+7))
                        .setTicker("안심보관함")
                        .setSmallIcon(R.drawable.logo)
                        .setLargeIcon(bitmap)
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();
                manager.notify(825, notification);/*숫자에 의미 노티는 여러개가 올수 있는데 그중 하나를 구분하기 위해서 쓴다*/;
                a1 = smsMessage[0].getMessageBody().substring(a+7,b);;
                a2 = smsMessage[0].getMessageBody().substring(c+3,c+7);
                a3 = smsMessage[0].getMessageBody().substring(d+1,d+12);
                a4 = smsMessage[0].getMessageBody().substring(e+1,e +12);
                /* 노티가 왔을때 정보 이동 */
                intent2.putExtra("1",a1);


                select();
                aquery();
                /*MainActivity inst = MainActivity.instance();
                inst.updateList();*/
            }

        }
    }

    public void aquery(){
        aq = new AQuery(context);/*무조건 생성*/
        String url = LM.SERVER+"/app/smsinfoinsert";
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("s_location", a1);
        params.put("s_pw", a2);
        params.put("s_receive", a3);
        params.put("s_delivery", a4);
        params.put("s_user", user);
        params.put("s_memo", "");
        params.put("s_link", "");
        LM.v("이거"+user);
        aq.ajax(url, params, String.class, new AjaxCallback<String>() {/*두번째 인자는 리턴받을 클레스 , 세번째는 콜백*/
            @Override
            public void callback(String url, String object, AjaxStatus status) { /*두번째 인자는 리턴되는*/

            }
        });
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
                user = c.getString(c.getColumnIndex("name"));
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
            helper = new PersonHelper(context, "info.db",null,2);
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

}
