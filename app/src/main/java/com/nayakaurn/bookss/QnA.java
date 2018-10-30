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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//Calls QnA adapter for QnA
public class QnA extends Fragment {

    RecyclerView recyclerViewqna;
    static QnA qnA;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.qna, container, false);
        StaticVariableClass.toolbartxt.setText("QnA");
        recyclerViewqna= (RecyclerView)v.findViewById(R.id.qna);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewqna.setLayoutManager(new LinearLayoutManager(LandingPage.landingPage));
        qnA= this;

        if(!StaticVariableClass.resumefragment.contains(qnA))
            StaticVariableClass.resumefragment.add(qnA);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        StaticVariableClass.booklistrefrence.child(""+StaticVariableClass.cardposition).child("choice").child(""+StaticVariableClass.choosecardposition).child("qna").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0) {
                    recyclerViewqna.setAdapter(new QnAdapter((int)dataSnapshot.getChildrenCount(), dataSnapshot));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}