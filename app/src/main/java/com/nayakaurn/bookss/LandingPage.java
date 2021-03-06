package com.nayakaurn.bookss;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
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

import io.fabric.sdk.android.Fabric;

public class LandingPage extends AppCompatActivity{

    GoogleSignInAccount account;
    FirebaseAuth.AuthStateListener  mAuthListner;
    FrameLayout bookFrame;

    static BottomNavigationView navigationView;
    static LandingPage landingPage;
    static boolean setfragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();

        Fabric.with(fabric);
        Fabric.with(this, new Crashlytics());
        //Crashlytics.getInstance().crash();


        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        account = GoogleSignIn.getLastSignedInAccount(this);

        if(account!=null){
            setContentView(R.layout.booksdisplay);
            initialize();
        }else{
            signInCheck();
        }
    }

    public void signInCheck(){
        StaticVariableClass.loadsplashscreen = true;
        setContentView(R.layout.coverpage);
        StaticVariableClass.mAuth = FirebaseAuth.getInstance();
       /* mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                    setContentView(R.layout.signed_in);
            }
        };*/

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
        StaticVariableClass.menu= (ImageButton)findViewById(R.id.menu);
        StaticVariableClass.menu.setVisibility(View.GONE);
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

        //for Payment call back
        if(requestCode==100){
            if(resultCode== RESULT_OK){
                Toast.makeText(getApplicationContext(), "payment sucess", Toast.LENGTH_SHORT).show();
                StaticVariableClass.purchasestatus=true;
                StaticVariableClass.fragmentTransaction= LandingPage.landingPage.getFragmentManager().beginTransaction().replace(R.id.frame, new Choise());
                StaticVariableClass.fragmentTransaction.commit();
            }
            if(resultCode== RESULT_CANCELED){
                StaticVariableClass.purchasestatus=false;
                Toast.makeText(getApplicationContext(), "Payment Failed\n Try again", Toast.LENGTH_SHORT).show();
            }
        }

        //for signin call back
        if(requestCode==111){
            if(resultCode== RESULT_OK){
                setContentView(R.layout.booksdisplay);
                initialize();
            }
            if(resultCode== RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "SignInFailed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void menuVisibility(){
        StaticVariableClass.menu.setVisibility(View.VISIBLE);
    }

    public void onMenuClick(View v){
        PopupMenu popup = new PopupMenu(this, v);
        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.one:
                        StaticVariableClass.marksselected = 1;
                        StaticVariableClass.fragmentTransaction= LandingPage.landingPage.getFragmentManager().beginTransaction().replace(R.id.frame, new QnA());
                        StaticVariableClass.fragmentTransaction.commit();
                        return true;

                    case R.id.two:
                        StaticVariableClass.marksselected = 2;
                        StaticVariableClass.fragmentTransaction= LandingPage.landingPage.getFragmentManager().beginTransaction().replace(R.id.frame, new QnA());
                        StaticVariableClass.fragmentTransaction.commit();
                        return true;

                    case R.id.five:
                        StaticVariableClass.marksselected = 5;
                        StaticVariableClass.fragmentTransaction= LandingPage.landingPage.getFragmentManager().beginTransaction().replace(R.id.frame, new QnA());
                        StaticVariableClass.fragmentTransaction.commit();
                        return true;

                    case R.id.allq:
                        StaticVariableClass.marksselected = 0;
                        StaticVariableClass.fragmentTransaction= LandingPage.landingPage.getFragmentManager().beginTransaction().replace(R.id.frame, new QnA());
                        StaticVariableClass.fragmentTransaction.commit();
                        return true;

                    default:
                        StaticVariableClass.marksselected = 0;
                        return true;
                }
            }
        });
        popup.inflate(R.menu.marksmenu);
        popup.show();
    }

}
