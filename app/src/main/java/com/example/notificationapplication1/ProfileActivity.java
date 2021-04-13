package com.example.notificationapplication1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public static final String NODE_USERS = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        //getting the registration token

        FirebaseInstallations.getInstance().getToken(true)
                .addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstallationTokenResult> task) {
                        if (task.isSuccessful()){
                            String token = task.getResult().getToken();
                            saveToken(token);

                        }else{

                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, MainActivity.class);
            //So that someone does not use back button to go back to main activity using back button
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void saveToken(String token) {
        String email = mAuth.getCurrentUser().getEmail();
        User user = new User(email, token);

        DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference(NODE_USERS);
        dbUsers.child(mAuth.getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ProfileActivity.this, "Token saved", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}