package com.example.roronoa.trialapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Home extends AppCompatActivity implements View.OnClickListener {

    TextView userId;
    EditText subject , content;
    Button addNote , logout , view;
    FirebaseAuth auth;
    GoogleSignInOptions gso;
    GoogleSignInClient client;
    FirebaseUser currentUser;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);




        subject = findViewById(R.id.subject);
        content = findViewById(R.id.content);
        addNote = findViewById(R.id.addNote);
        logout = findViewById(R.id.logout);
        view = findViewById(R.id.viewNotes);
        view.setOnClickListener(this);
        addNote.setOnClickListener(this);
        logout.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
        userId = findViewById(R.id.userId);
        currentUser = auth.getCurrentUser();
        userId.setText(currentUser.getUid());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);





        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        client = GoogleSignIn.getClient(this , gso);





        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();
                Toast.makeText(getApplicationContext() , item.getTitle() ,Toast.LENGTH_LONG).show();
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        //Toast.makeText(this , item.getItemId(), Toast.LENGTH_LONG).show();
        if(item.getItemId() == android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return  super.onOptionsItemSelected(item);
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

        if(subject.getText().toString().equals("") || content.getText().toString().equals(""))
            return;


        String noteId;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("users").child(currentUser.getUid()).child("notes");
        noteId = ref.push().getKey();
        ref.child(noteId).setValue(new Note(subject.getText().toString() , content.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Toast.makeText(getApplicationContext() , "posted"  , Toast.LENGTH_LONG).show();
            }
        });
        Toast.makeText(this ,noteId, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.addNote)
            addNewNote();
        else if(v.getId() == R.id.logout)
            signOutUser();

    }
}
