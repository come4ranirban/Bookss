package com.nayakaurn.bookss;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class StaticVariableClass {

    public static FirebaseAuth mAuth;
    public static LandingPage landingPage;
    public static FragmentTransaction fragmentTransaction;
    public static int cardposition, choosecardposition, discussposition;
    public static DatabaseReference booklistrefrence;
    public static DataSnapshot discussdataSnapshot,readsnapshot;
    public static boolean loadsplashscreen=false, ansvisible=false;
    public static ArrayList<Fragment> lastfragment= new ArrayList<>();
    public static ArrayList<Fragment> resumefragment= new ArrayList<>();
    public static TextView toolbartxt;
    public static String dques,dans;
}
