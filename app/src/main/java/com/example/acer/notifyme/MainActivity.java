package com.example.acer.notifyme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    private Button button_notify;
    private static final String primary_channel_id="primary_notification_channel";
    private NotificationManager mnotifyManager;
    private static final int NOTIFICATION_id=0;
    private Button button_update;
    private Button button_cancel;
    private static final String ACTION_UPDATE_NOTIFICATION="com.example.acer.notifyme.ACTION_UPDATE_NOTIFICATION";
    private NotificationReciever mReciever=new NotificationReciever();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_notify=findViewById(R.id.notify);
        button_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                sendNotification();
            }
        });
        button_update=findViewById(R.id.update);
        button_update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                updateNotification();
                //update
            }
        });
        button_cancel=findViewById(R.id.cancel);
        button_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                cancelNotification();
                //cancel
            }
        });
        createNotificationChannel();
        registerReceiver(mReciever,new IntentFilter(ACTION_UPDATE_NOTIFICATION));



    }

    @Override
    protected void onDestroy()
    {
        unregisterReceiver(mReciever);
        super.onDestroy();

    }

    private NotificationCompat.Builder getNotificationBuilder()
    {
        NotificationCompat.Builder notifyBuilder=new NotificationCompat.Builder(this,primary_channel_id).setContentTitle("YOU HAVE BEEN NOTIFIED").setContentText("This is My First Notification").setSmallIcon(R.drawable.ic_android);
        notifyBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        notifyBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
        notifyBuilder.setColor(150);
        return notifyBuilder;
    }
    public void sendNotification()
    {
        Intent updateIntent=new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent=PendingIntent.getBroadcast(this,NOTIFICATION_id,updateIntent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notifyBuilder=getNotificationBuilder();
        Intent notification_intent=new Intent(this,MainActivity.class);
        PendingIntent notificationPendingIntent=
                PendingIntent.getActivity(this,NOTIFICATION_id,notification_intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notifyBuilder.setContentIntent(notificationPendingIntent);
        notifyBuilder.setAutoCancel(true);
        mnotifyManager.notify(NOTIFICATION_id,notifyBuilder.build());
        notifyBuilder.addAction(R.drawable.ic_update,"UpdateNotification",updatePendingIntent);

    }
    public void createNotificationChannel()
    {
        mnotifyManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >=android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel=new NotificationChannel(primary_channel_id,"my_Notification",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Me");
            mnotifyManager.createNotificationChannel(notificationChannel);
        }
    }
    public void updateNotification()
    {
        Bitmap androidImage=BitmapFactory.decodeResource(getResources(),R.drawable.moscot_1);
        NotificationCompat.Builder notifyBuilder=getNotificationBuilder();
        notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(androidImage).setBigContentTitle("Notification updated"));
        mnotifyManager.notify(NOTIFICATION_id,notifyBuilder.build());

    }
    public void cancelNotification()
    {
        mnotifyManager.cancel(NOTIFICATION_id);
    }
    public void setNotificationButtonstate(Boolean isNotifyEnabled,Boolean isUpdateEnabled,Boolean isCancelEnabled)
    {
        setNotificationButtonstate(true,false,false);
        button_notify.setEnabled(isNotifyEnabled);
        setNotificationButtonstate(false,true,true);
        button_update.setEnabled(isUpdateEnabled);
        setNotificationButtonstate(false,false,true);
        button_cancel.setEnabled(isCancelEnabled);
        setNotificationButtonstate(true,false,false);


    }
    public class NotificationReciever extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            updateNotification();

        }
    }
}
