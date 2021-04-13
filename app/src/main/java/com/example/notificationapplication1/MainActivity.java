package com.example.notificationapplication1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //Notification channel
    //Notification builder
    //Notification manager

    //For notification channel
        //We need channel Id, Name, Desc

    private static final String CHANNEL_ID = "sampa";
    private static final String CHANNEL_NAME = "sam pa";
    private static final String CHANNEL_DESC = "sam pa notification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // android > 8
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //then create a notification channel
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    //Function that will display the notification
    public void displayNotification(View view){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notifications_none_24)
                    .setContentTitle("New notication sent")
                    .setContentText("New marks updated")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                ;

        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(this);
        mNotificationMgr.notify(1, mBuilder.build());
    }

}