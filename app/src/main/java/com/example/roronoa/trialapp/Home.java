package com.example.roronoa.trialapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Home extends AppCompatActivity implements View.OnClickListener {

    TextView userId;
    EditText subject , content;
    Button addNote , logout;
    FirebaseAuth auth;
    GoogleSignInOptions gso;
    GoogleSignInClient client;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        subject = findViewById(R.id.subject);
        content = findViewById(R.id.content);
        addNote = findViewById(R.id.addNote);
        logout = findViewById(R.id.logout);
        addNote.setOnClickListener(this);
        logout.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        userId = findViewById(R.id.userId);
        currentUser = auth.getCurrentUser();
        userId.setText(currentUser.getUid());


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        client = GoogleSignIn.getClient(this , gso);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        Toast.makeText(this , item.getTitle() , Toast.LENGTH_LONG).show();
        return  true;
    }


    private void signOutUser() {

        auth.signOut();
        client.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext() , MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }


    private void addNewNote() {

        if(subject.getText().equals("") || content.getText().equals(""))
            return;


        String noteId;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("users").child(currentUser.getUid()).child("notes");
        HashMap<String , String> entry = new HashMap<>();
        entry.put("subject" , subject.getText().toString());
        entry.put("content" , content.getText().toString());
        noteId = ref.push().getKey();
        ref.child(noteId).setValue(entry);
        Toast.makeText(this ,noteId, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.addNote)
            addNewNote();
        else
            signOutUser();
    }
}
