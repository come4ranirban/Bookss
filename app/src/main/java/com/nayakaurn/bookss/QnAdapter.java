package com.nayakaurn.bookss;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.text.Html.fromHtml;

public class QnAdapter extends RecyclerView.Adapter <QnAViewHolder>{

    int count;
    DataSnapshot dataSnapshot;
    String qhtml,ahtml;
    View v;

    QnAdapter(int count, DataSnapshot dataSnapshot){
        this.dataSnapshot = dataSnapshot;
        this.count = count;
    }

    @NonNull
    @Override
    public QnAViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(LandingPage.landingPage);
        v= inflater.inflate(R.layout.questionwithans, parent, false);
        QnAViewHolder qnAViewHolder= new QnAViewHolder(v);
        return qnAViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final QnAViewHolder holder, final int count) {

        final String position= StaticVariableClass.selectedQnA.get(count);
        qhtml= dataSnapshot.child(position).child("q").getValue().toString();
        ahtml= dataSnapshot.child(position).child("a").getValue().toString();
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.answer.setText(Html.fromHtml(ahtml, Html.FROM_HTML_MODE_LEGACY)) ;
        } else {
            holder.answer.setText(Html.fromHtml(ahtml));
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            holder.question.setText(Html.fromHtml(qhtml, Html.FROM_HTML_MODE_LEGACY)) ;
        }else
            holder.question.setText(Html.fromHtml(qhtml));

        holder.answer.loadUrl(ahtml);

        holder.ansclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StaticVariableClass.ansvisible){
                    holder.anscheck.setImageResource(R.drawable.contrap);
                    StaticVariableClass.ansvisible= false;
                    //holder.answer.setText(dataSnapshot.child(""+position).child("a").getValue().toString());
                    holder.answer.setVisibility(View.VISIBLE);
                }else {
                    holder.anscheck.setImageResource(R.drawable.expand);
                    StaticVariableClass.ansvisible= true;
                    holder.answer.setVisibility(View.GONE);

                }
            }
        });

        holder.discuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticVariableClass.menu.setVisibility(View.GONE);
                StaticVariableClass.dques =  dataSnapshot.child(position).child("q").getValue().toString();
                StaticVariableClass.dans= dataSnapshot.child(position).child("a").getValue().toString();
                StaticVariableClass.discussdataSnapshot= dataSnapshot;
                StaticVariableClass.discussposition=Integer.parseInt(position);
                StaticVariableClass.lastfragment.add(QnA.qnA);
                StaticVariableClass.fragmentTransaction= LandingPage.landingPage.getFragmentManager().beginTransaction().add(R.id.frame, new DiscussForum());
                StaticVariableClass.fragmentTransaction.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return StaticVariableClass.selectedQnA.size();
    }
}

class QnAViewHolder extends RecyclerView.ViewHolder{

    TextView question;
    WebView answer;
    ImageView anscheck;
    LinearLayout ansclick,discuss;

    public QnAViewHolder(View itemView) {
        super(itemView);

        question= (TextView)itemView.findViewById(R.id.question);
        anscheck= (ImageView)itemView.findViewById(R.id.anscheck);
        //answer= (TextView)itemView.findViewById(R.id.answer);
        answer= (WebView)itemView.findViewById(R.id.answer);
        ansclick= (LinearLayout)itemView.findViewById(R.id.ansclick);
        discuss= (LinearLayout)itemView.findViewById(R.id.discuss);
    }
}
