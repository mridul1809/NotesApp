package com.example.roronoa.trialapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Starter extends Activity {

    @Override
    public void onCreate(Bundle x) {

        super.onCreate(x);
        try {

            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        catch (Exception e) {

        }
        Intent intent;
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
            intent = new Intent(this , Home.class);
        else
            intent = new Intent(this , MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
