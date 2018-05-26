package com.example.roronoa.trialapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Home extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);




        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        recyclerView = findViewById(R.id.listNotes);


        setNavigationDrawerItems();


        fillRecyclerView();

    }


    private  void  fillRecyclerView() {

        Query query = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(currentUser.getUid())
                .child("notes");
        FirebaseRecyclerOptions<Note> options = new FirebaseRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        adapter = new FirebaseRecyclerAdapter < Note , NoteHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull Note model) {

                holder.content.setText(model.getContent());
                holder.subject.setText(model.getSubject());
            }


            @NonNull
            @Override
            public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()  ).inflate(R.layout.note_view , parent , false);
                return  new NoteHolder(view);
            }
        };


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }




    private void setNavigationDrawerItems() {


        TextView userName = navigationView.getHeaderView(0).findViewById(R.id.userId);
        userName.setText(currentUser.getEmail());


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {


                    case R.id.logout :
                        logoutUser();
                        break;

                    case R.id.settings :
                        break;

                    case R.id.show_notes :
                        break;

                    case R.id.add_note :
                        break;
                }


                if(item.getItemId() != R.id.logout)
                    item.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });

    }



    private void logoutUser() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(this, gso);

        auth.signOut();

        client.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {

                    Intent intent = new Intent(Home.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext() , "Failed to Logout" , Toast.LENGTH_SHORT).show();
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


        if(item.getItemId() == android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return  super.onOptionsItemSelected(item);
    }




    @Override
    public void onStart() {

        super.onStart();
        adapter.startListening();
    }




    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}
