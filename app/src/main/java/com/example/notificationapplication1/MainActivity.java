package com.example.notificationapplication1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;

public class MainActivity extends AppCompatActivity {

    //Notification channel
    //Notification builder
    //Notification manager

    //For notification channel
        //We need channel Id, Name, Desc

    private static final String CHANNEL_ID = "sampa";
    private static final String CHANNEL_NAME = "sam pa";
    private static final String CHANNEL_DESC = "sam pa notification";

    private TextView textViewToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewToken = findViewById(R.id.textViewToken);

        // android > 8
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //then create a notification channel
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        //getting the registration token

        FirebaseInstallations.getInstance().getToken(true)
                .addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstallationTokenResult> task) {
                        if (task.isSuccessful()){
                            String token = task.getResult().getToken();
                            textViewToken.setText("Token" + token);
                        }else{
                            textViewToken.setText(task.getException().getMessage());
                        }
                    }
                });
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