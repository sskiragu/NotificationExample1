package com.example.notificationapplication1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
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

    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressbar);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        // android > 8
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //then create a notification channel
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        findViewById(R.id.buttonSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }

    private void createUser() {
        String email  = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (email.isEmpty()){
            editTextEmail.setError("Email required");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            editTextPassword.setError("Password required");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editTextPassword.setError("Password should be atleast 6 char long");
            editTextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startProfileActivity();
                        }else{
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                userLogin(email, password);

                            }else{
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(MainActivity.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void userLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startProfileActivity();
                        }else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null) {
            startProfileActivity();
        }
    }

    private void startProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class);
        //So that someone does not use back button to go back to main activity using back button
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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