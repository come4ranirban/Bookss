package com.nayakaurn.bookss;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.transform.Result;

public class LandingPage extends AppCompatActivity{

    GoogleSignInAccount account;
    FirebaseAuth.AuthStateListener  mAuthListner;
    FrameLayout bookFrame;
    static BottomNavigationView navigationView;
    static LandingPage landingPage;
    static boolean setfragment= false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);

        if(StaticVariableClass.loadsplashscreen){
            setContentView(R.layout.booksdisplay);
            initialize();
        }else {
            signInCheck();
        }
    }

    public void signInCheck(){
        StaticVariableClass.loadsplashscreen = true;
        setContentView(R.layout.coverpage);
        StaticVariableClass.mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                    setContentView(R.layout.signed_in);
            }
        };

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        account = GoogleSignIn.getLastSignedInAccount(this);

        new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if(account!=null){
                    setContentView(R.layout.booksdisplay);
                    initialize();
                }
                else
                {
                    Intent intent= new Intent(landingPage, SignInPage.class);
                    startActivityForResult(intent, 111);
                }
            }
        }.start();
    }

    public void initialize(){
        bookFrame = (FrameLayout)findViewById(R.id.frame);
        navigationView = (BottomNavigationView)findViewById(R.id.bottomnav);
        StaticVariableClass.toolbartxt= (TextView)findViewById(R.id.toolbartxt);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.library:
                        item.setChecked(true);
                        StaticVariableClass.fragmentTransaction= getFragmentManager().beginTransaction().replace(R.id.frame, new BooksLibrary());
                        StaticVariableClass.fragmentTransaction.commit();
                        break;

                    case R.id.personalize:
                        item.setChecked(true);
                        StaticVariableClass.fragmentTransaction= getFragmentManager().beginTransaction().replace(R.id.frame, new Personalize());
                        StaticVariableClass.fragmentTransaction.commit();
                        break;

                    default:
                        item.setChecked(false);
                        break;
                }
                return false;
            }
        });


        StaticVariableClass.fragmentTransaction= getFragmentManager().beginTransaction().add(R.id.frame, new BooksLibrary());
        StaticVariableClass.fragmentTransaction.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        landingPage = this;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(setfragment){
            if(StaticVariableClass.resumefragment.size()>0){
                StaticVariableClass.fragmentTransaction= getFragmentManager().beginTransaction().replace(R.id.frame, StaticVariableClass.resumefragment.get(StaticVariableClass.resumefragment.size()-1));
                StaticVariableClass.fragmentTransaction.commit();
            }

        }
        setfragment= false;
    }

    @Override
    public void onBackPressed() {

        if(StaticVariableClass.lastfragment.size()==0)
            super.onBackPressed();
        else {
            if(StaticVariableClass.lastfragment.size()==1) {
                if(navigationView.getVisibility()==View.GONE)
                    navigationView.setVisibility(View.VISIBLE);
            }

            StaticVariableClass.fragmentTransaction= getFragmentManager().beginTransaction().replace(R.id.frame, StaticVariableClass.lastfragment.get(StaticVariableClass.lastfragment.size()-1));
            StaticVariableClass.lastfragment.remove(StaticVariableClass.lastfragment.size()-1);
            StaticVariableClass.resumefragment.remove(StaticVariableClass.resumefragment.size()-1);
            StaticVariableClass.fragmentTransaction.commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        setfragment= true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==100){
            if(resultCode== RESULT_OK){
                Toast.makeText(getApplicationContext(), "payment sucess", Toast.LENGTH_SHORT).show();
                StaticVariableClass.fragmentTransaction= LandingPage.landingPage.getFragmentManager().beginTransaction().replace(R.id.frame, new Choise());
                StaticVariableClass.fragmentTransaction.commit();
            }
            if(resultCode== RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "Payment Failed\n Try again", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode==111){
            if(resultCode== RESULT_OK){
                setContentView(R.layout.booksdisplay);
                initialize();
            }
            if(resultCode== RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "Payment Failed\n Try again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
