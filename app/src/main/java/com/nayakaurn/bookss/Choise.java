package com.nayakaurn.bookss;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


//for book and QnA choice
//Calls QnA frame
public class Choise extends Fragment {

    RecyclerView recyclerViewchoice;
    DatabaseReference booklistreference;
    static Choise choise;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.choice, container, false);
        StaticVariableClass.toolbartxt.setText("Chapters");
        choise= this;

        if(!StaticVariableClass.resumefragment.contains(choise))
            StaticVariableClass.resumefragment.add(choise);

        recyclerViewchoice= (RecyclerView)v.findViewById(R.id.choicerecyclerview);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewchoice.setLayoutManager(new LinearLayoutManager(LandingPage.landingPage));
        booklistreference = FirebaseDatabase.getInstance().getReference("Books");

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        LandingPage.navigationView.setVisibility(View.GONE);
        DatabaseReference booklistreference= FirebaseDatabase.getInstance().getReference("Books");
        booklistreference.child(""+StaticVariableClass.cardposition).child("choice").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0) {
                    recyclerViewchoice.setAdapter(new ChoiceAdapter((int)dataSnapshot.getChildrenCount(), dataSnapshot));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
