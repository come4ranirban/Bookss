package com.nayakaurn.bookss;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsViewHolder> {

    int count;
    CommentsAdapter(int count){
        this.count= count;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater= LayoutInflater.from(LandingPage.landingPage);
        View v= inflater.inflate(R.layout.commentsholder, parent, false);
        CommentsViewHolder commentsViewHolder= new CommentsViewHolder(v);
        return commentsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        holder.username.setText(StaticVariableClass.readsnapshot.child("comment").child(""+position).child("user").getValue().toString());
        holder.msg.setText(StaticVariableClass.readsnapshot.child("comment").child(""+position).child("msg").getValue().toString());
        if(StaticVariableClass.readsnapshot.child("comment").child(""+position).child("msg").getValue()==null)
            Toast.makeText(LandingPage.landingPage, position+"->null", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return count;
    }
}

class CommentsViewHolder extends RecyclerView.ViewHolder{

    TextView username,msg;

    public CommentsViewHolder(View itemView) {
        super(itemView);
        username= (TextView)itemView.findViewById(R.id.username);
        msg= (TextView)itemView.findViewById(R.id.msg);
    }
}