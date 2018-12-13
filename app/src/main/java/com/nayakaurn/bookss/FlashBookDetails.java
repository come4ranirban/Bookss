package com.nayakaurn.bookss;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FlashBookDetails extends Fragment {

    //flashbook object
    FlashBookDetails flashBookDetails;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.flashbookdetails, container, false);
        flashBookDetails= this;

        LandingPage.navigationView.setVisibility(View.GONE);

        if(!StaticVariableClass.resumefragment.contains(flashBookDetails))
            StaticVariableClass.resumefragment.add(flashBookDetails);

        DatabaseReference booklistreference= FirebaseDatabase.getInstance().getReference("Books");
        final SimpleDraweeView bookimage= (SimpleDraweeView)v.findViewById(R.id.introbookimage);
        final TextView bookname= (TextView)v.findViewById(R.id.introbookname);
        final TextView bookintro= (TextView)v.findViewById(R.id.introbook);
        Button proceed= (Button)v.findViewById(R.id.proceed);

        booklistreference.child(""+StaticVariableClass.cardposition).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bookimage.setImageURI(dataSnapshot.child("coverpageurl").getValue().toString());
                bookname.setText(dataSnapshot.child("bookname").getValue().toString());
                bookintro.setText(dataSnapshot.child("intro").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticVariableClass.fragmentTransaction= LandingPage.landingPage.getFragmentManager().beginTransaction().replace(R.id.frame, new Choise());
                StaticVariableClass.fragmentTransaction.commit();
            }
        });
        return v;
    }
}
