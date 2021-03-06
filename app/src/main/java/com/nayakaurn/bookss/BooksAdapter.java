package com.nayakaurn.bookss;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nayakaurn.bookss.LandingPage;
import com.nayakaurn.bookss.R;

import java.net.MalformedURLException;
import java.net.URL;

import static com.nayakaurn.bookss.StaticVariableClass.landingPage;

public class BooksAdapter extends RecyclerView.Adapter<BooksViewHolder>{


    int count;
    DatabaseReference booklistreference;
    public static BooksAdapter booksAdapter;


    BooksAdapter(int count, DatabaseReference booklistreference){
        this.count =count;
        booksAdapter= this;
    }

    @NonNull
    @Override
    public BooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater= LayoutInflater.from(LandingPage.landingPage);
        View v= inflater.inflate(R.layout.bookcoverpage, parent, false);
        BooksViewHolder booksViewHolder= new BooksViewHolder(v);
        return booksViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BooksViewHolder holder, final int position) {

        holder.bookcoverpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticVariableClass.cardposition = position;
                StaticVariableClass.lastfragment.add(BooksLibrary.booksLibrary);
                final DatabaseReference booklistreference= FirebaseDatabase.getInstance().getReference("Books");
                FirebaseUser user= StaticVariableClass.mAuth.getCurrentUser();
                final String uid=  user.getUid();
                //final String email= user.getEmail().substring(0,user.getEmail().indexOf("@"));
                booklistreference.child(""+position).child("subscribers").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(uid))
                         {
                            StaticVariableClass.purchasestatus = true;
                            StaticVariableClass.fragmentTransaction= LandingPage.landingPage.getFragmentManager().beginTransaction().replace(R.id.frame, new Choise());
                            StaticVariableClass.fragmentTransaction.commit();
                        }else {
                            booklistreference.child(""+position).child("subscribers").removeEventListener(this);
                            StaticVariableClass.purchasestatus = false;
                            StaticVariableClass.fragmentTransaction= LandingPage.landingPage.getFragmentManager().beginTransaction().replace(R.id.frame, new Choise());
                            StaticVariableClass.fragmentTransaction.commit();
                            /*Intent intent= new Intent(LandingPage.landingPage, BookIntro.class);
                            LandingPage.landingPage.startActivityForResult(intent, 100);*/
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        StaticVariableClass.booklistrefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(position+"")){
                        holder.bookcoverpage.setImageURI(dataSnapshot.child(""+position).child("coverpageurl").getValue().toString());
                }else
                    holder.itemView.setBackgroundResource(R.drawable.cove);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return count;
    }

}

class BooksViewHolder extends RecyclerView.ViewHolder {

    SimpleDraweeView bookcoverpage;
    public BooksViewHolder(View itemView) {
        super(itemView);
        bookcoverpage= (SimpleDraweeView) itemView.findViewById(R.id.bookcover);
    }
}