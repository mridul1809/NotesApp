package com.example.roronoa.trialapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class ListNotes extends Fragment {


    public ListNotes() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    FirebaseRecyclerAdapter adapter;
    FirebaseUser currentUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.list_notes_fragment, container, false);
        recyclerView = layout.findViewById(R.id.listNotes);
        return layout;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }


    private void fillRecyclerView() {

        Query query = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(currentUser.getUid())
                .child("notes");
        FirebaseRecyclerOptions<Note> options = new FirebaseRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Note, NoteHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull Note model) {

                holder.content.setText(model.getContent());
                holder.subject.setText(model.getSubject());
            }


            @NonNull
            @Override
            public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view, parent, false);
                return new NoteHolder(view);
            }
        };


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
    }


    @Override
    public void onStart() {

        super.onStart();
        fillRecyclerView();
        adapter.startListening();
    }


    @Override
    public void onStop() {

        super.onStop();
        adapter.stopListening();
    }

}

