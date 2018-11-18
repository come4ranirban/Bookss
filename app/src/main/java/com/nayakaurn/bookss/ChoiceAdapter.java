package com.nayakaurn.bookss;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class ChoiceAdapter extends RecyclerView.Adapter<ChoiceViewHolder> {

    int count;
    DataSnapshot dataSnapshot;

    ChoiceAdapter(int count, DataSnapshot dataSnapshot){
        this.count= count;
        this.dataSnapshot= dataSnapshot;
    }

    @NonNull
    @Override
    public ChoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater= LayoutInflater.from(LandingPage.landingPage);
        View v= inflater.inflate(R.layout.choicebutton, parent, false);
        ChoiceViewHolder viewHolder= new ChoiceViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChoiceViewHolder holder, final int position) {

        holder.choose.setText(dataSnapshot.child(""+position).child("choicename").getValue().toString());
        holder.choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticVariableClass.choosecardposition = position;

                if(position == 0){
                    StaticVariableClass.lastfragment.add(Choise.choise);
                    StaticVariableClass.fragmentTransaction= LandingPage.landingPage.getFragmentManager().beginTransaction().replace(R.id.frame, new QnA());
                    StaticVariableClass.fragmentTransaction.commit();
                }else {
                    if(StaticVariableClass.purchasestatus){
                        StaticVariableClass.lastfragment.add(Choise.choise);
                        StaticVariableClass.fragmentTransaction= LandingPage.landingPage.getFragmentManager().beginTransaction().replace(R.id.frame, new QnA());
                        StaticVariableClass.fragmentTransaction.commit();
                    }else {
                        Intent intent= new Intent(LandingPage.landingPage, BookIntro.class);
                        LandingPage.landingPage.startActivityForResult(intent, 100);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return count;
    }
}

class ChoiceViewHolder extends RecyclerView.ViewHolder{

    Button choose;

    public ChoiceViewHolder(View itemView) {
        super(itemView);
        choose= (Button)itemView.findViewById(R.id.choose);
    }
}
