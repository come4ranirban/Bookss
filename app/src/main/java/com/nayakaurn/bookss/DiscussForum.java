package com.nayakaurn.bookss;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DiscussForum extends Fragment {

    TextView dquestion, commenttext;
    //NestedScrollView scrollView;
    WebView danswer;
    EditText writecomment;
    RecyclerView comments;
    int position;
    DiscussForum discussForum;
    ImageButton comment;
    DataSnapshot dataSnapshot;
    View endview,comentline;
    int count;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.discussforum, container, false);
        dquestion= (TextView)v.findViewById(R.id.dquestion);
        //danswer= (TextView)v.findViewById(R.id.danswer);
        danswer= (WebView)v.findViewById(R.id.danswer);
        //scrollView= (NestedScrollView)v.findViewById(R.id.sendlayout);
        commenttext= (TextView)v.findViewById(R.id.comenttext);
        comments= (RecyclerView)v.findViewById(R.id.comments);
        comment= (ImageButton) v.findViewById(R.id.comment);
        comentline= (View)v.findViewById(R.id.commentline);
        endview= (View)v.findViewById(R.id.endview);
        writecomment= (EditText)v.findViewById(R.id.writecomment);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        comments.setLayoutManager(new LinearLayoutManager(LandingPage.landingPage));


        dataSnapshot= StaticVariableClass.discussdataSnapshot;
        position= StaticVariableClass.discussposition;

        discussForum= this;
        if(!StaticVariableClass.resumefragment.contains(discussForum))
            StaticVariableClass.resumefragment.add(discussForum);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dquestion.setText(Html.fromHtml(StaticVariableClass.dques, Html.FROM_HTML_MODE_LEGACY)) ;
        } else {
            dquestion.setText(Html.fromHtml(StaticVariableClass.dques));
        }

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            danswer.setText(Html.fromHtml(StaticVariableClass.dans, Html.FROM_HTML_MODE_LEGACY)) ;
        } else {
            danswer.setText(Html.fromHtml(StaticVariableClass.dans));
        }*/

        danswer.loadUrl(StaticVariableClass.dans);

        updateDatasnapshot();
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSnapshot= StaticVariableClass.discussdataSnapshot;
                position= StaticVariableClass.discussposition;

                if(!writecomment.getText().toString().trim().isEmpty()){
                    if(!dataSnapshot.child(""+position).hasChild("comment") || dataSnapshot.child(""+position).hasChild("comment")) {
                        count= (int)dataSnapshot.child(""+position).child("comment").getChildrenCount();
                        FirebaseUser user= StaticVariableClass.mAuth.getCurrentUser();
                        String email= user.getEmail().substring(0,user.getEmail().indexOf("@"));
                        dataSnapshot.child(""+position).child("comment").getRef().child(""+count).child("user").setValue(email+":");
                        dataSnapshot.child(""+position).child("comment").getRef().child(""+count).child("msg").setValue(writecomment.getText().toString());
                        writecomment.getText().clear();
                    }
                }

                InputMethodManager imm = (InputMethodManager)LandingPage.landingPage.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(writecomment.getWindowToken(), 0);

                //scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    public void updateDatasnapshot(){
        StaticVariableClass.booklistrefrence.child(""+StaticVariableClass.cardposition).child("choice").child(""+StaticVariableClass.choosecardposition).child("qna").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StaticVariableClass.discussdataSnapshot= dataSnapshot;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void updateComment(){
        dataSnapshot= StaticVariableClass.readsnapshot;
        position= StaticVariableClass.discussposition;
        if(dataSnapshot.hasChild("comment")){
            comments.setVisibility(View.VISIBLE);
            commenttext.setVisibility(View.VISIBLE);
            comentline.setVisibility(View.VISIBLE);
            comments.setHasFixedSize(false);
            endview.setVisibility(View.VISIBLE);
            comments.scrollToPosition(c-1);
            comments.setAdapter(new CommentsAdapter(c));
        }else {
            comments.setVisibility(View.GONE);
            commenttext.setVisibility(View.GONE);
            endview.setVisibility(View.GONE);
            comentline.setVisibility(View.GONE);
        }
    }

    int c;

    @Override
    public void onResume() {
        super.onResume();
        LandingPage.navigationView.setVisibility(View.GONE);
        StaticVariableClass.booklistrefrence.child(""+StaticVariableClass.cardposition).child("choice").child(""+StaticVariableClass.choosecardposition).child("qna").child(""+StaticVariableClass.discussposition).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StaticVariableClass.readsnapshot= dataSnapshot;
                c= (int)dataSnapshot.child("comment").getChildrenCount();
                new CountDownTimer(200,100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        updateComment();
                    }
                }.start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        LandingPage.landingPage.menuVisibility();
    }
}
