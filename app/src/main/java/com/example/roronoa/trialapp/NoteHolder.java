package com.example.roronoa.trialapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class NoteHolder extends RecyclerView.ViewHolder {

    TextView subject , content;

    public NoteHolder(View itemView) {
        super(itemView);

        subject = itemView.findViewById(R.id.subject);
        content = itemView.findViewById(R.id.content);
    }
}
