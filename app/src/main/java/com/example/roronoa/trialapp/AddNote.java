package com.example.roronoa.trialapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNote extends Fragment implements View.OnClickListener{

    private EditText subject , content;
    private Button addNote;
    private DatabaseReference database;

    public AddNote() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout =  inflater.inflate(R.layout.add_note_fragment, container, false);
        subject = layout.findViewById(R.id.subject);
        content = layout.findViewById(R.id.content);
        addNote = layout.findViewById(R.id.add_note);
        addNote.setOnClickListener(this);
        return layout;
    }

    @Override
    public void onClick(View v) {

        if(subject.getText().toString().equals("")) {

            Toast.makeText(getContext() , "Please Enter Subject" , Toast.LENGTH_SHORT).show();
            return;
        }

        if(content.getText().toString().equals("")) {

            Toast.makeText(getContext() , "Please Enter Content" , Toast.LENGTH_SHORT).show();
            return;
        }

        database = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("notes");

        database.child(database.push().getKey())
                .setValue(new Note(subject.getText().toString() , content.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()) {

                            Toast.makeText(getContext() , "Note Added!" , Toast.LENGTH_SHORT).show();
                            content.setText("");
                            subject.setText("");
                        }
                        else
                            Toast.makeText(getContext() , "Failed to Add Note" , Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
