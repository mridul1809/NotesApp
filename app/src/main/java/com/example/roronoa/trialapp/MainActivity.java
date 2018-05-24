package com.example.roronoa.trialapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{


    private static final int RC_SIGN_IN = 9001;


    private ProgressDialog progressDialog;


    private FirebaseAuth auth;



    private GoogleSignInOptions gso;
    private GoogleSignInClient client;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        updateUI(FirebaseAuth.getInstance().getCurrentUser());

        setContentView(R.layout.activity_main);
        findViewById(R.id.signIn).setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing In");
        progressDialog.setIndeterminate(true);


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        client = GoogleSignIn.getClient(this , gso);

        auth = FirebaseAuth.getInstance();

    }



    private void updateUI(FirebaseUser firebaseUser) {

        if(firebaseUser == null)
            return;
        Intent intent = new Intent( this , Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }




    @Override
    public void onActivityResult(int requestCode , int resultCode , Intent data) {

        super.onActivityResult(requestCode , resultCode , data);
        if(requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }
            catch (ApiException e) {
                updateUI(null);
            }

        }

    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        progressDialog.show();


        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken() , null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this , new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                            updateUI(auth.getCurrentUser());
                        else
                            Toast.makeText(getApplicationContext() , "Failed to Login" , Toast.LENGTH_LONG).show();

                        progressDialog.dismiss();
                    }
                });

    }



    @Override
    public void onClick(View v) {

        Intent signInIntent = client.getSignInIntent();
        startActivityForResult(signInIntent , RC_SIGN_IN);
    }
}
