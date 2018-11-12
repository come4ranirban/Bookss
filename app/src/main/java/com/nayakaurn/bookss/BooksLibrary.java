package com.nayakaurn.bookss;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.app.Activity.RESULT_OK;

//Calls BooksAdapter for recyclerview
public class BooksLibrary extends Fragment{

    int count;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    static BooksLibrary booksLibrary;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.bookslabrary, container, false);
        recyclerView= (RecyclerView)v.findViewById(R.id.bookrecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(StaticVariableClass.landingPage,3));
        StaticVariableClass.booklistrefrence = FirebaseDatabase.getInstance().getReference("Books");
        StaticVariableClass.toolbartxt.setText("Library");
        progressBar= (ProgressBar)v.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        booksLibrary= this;

        if(!StaticVariableClass.resumefragment.contains(booksLibrary))
            StaticVariableClass.resumefragment.add(booksLibrary);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        StaticVariableClass.booklistrefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count= (int) dataSnapshot.getChildrenCount();
                if(count>0){
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setAdapter(new BooksAdapter(count, StaticVariableClass.booklistrefrence));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

