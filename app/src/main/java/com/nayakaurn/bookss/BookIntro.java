package com.nayakaurn.bookss;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BookIntro extends AppCompatActivity implements PaymentResultListener{

    TextView bookname,intro, bookprice;
    DatabaseReference booklistreference;
    BookIntro bookIntro;
    SimpleDraweeView bookcoverpage;
    DataSnapshot bookintrodataSnapshot;
    ValueEventListener valueEventListener;
    Button buynow;
    String email, userId, orderId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookoverview);
        Fresco.initialize(this);
        bookname= (TextView)findViewById(R.id.bookname);
        intro= (TextView)findViewById(R.id.intro);
        bookprice= (TextView)findViewById(R.id.bookprice);
        bookcoverpage= (SimpleDraweeView)findViewById(R.id.bookimage);
        buynow= (Button)findViewById(R.id.buynow);
        booklistreference= FirebaseDatabase.getInstance().getReference("Books");

        bookIntro= this;
    }

    @Override
    public void onStart() {
        super.onStart();

        booklistreference.child(""+StaticVariableClass.cardposition).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bookintrodataSnapshot= dataSnapshot;
                bookcoverpage.setImageURI(dataSnapshot.child("coverpageurl").getValue().toString());
                bookname.setText(dataSnapshot.child("bookname").getValue().toString());
                intro.setText(dataSnapshot.child("intro").getValue().toString());
                bookprice.setText(dataSnapshot.child("price").getValue().toString());
                valueEventListener= this;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //LandingPage.navigationView.setVisibility(View.GONE);

        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });
    }

    public void startPayment() {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        final Activity activity= this;

        /**
         * Set your logo here
         */
        //checkout.setImage(R.drawable.bookizz);


        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: Rentomojo || HasGeek etc.
             */
            FirebaseUser user= StaticVariableClass.mAuth.getCurrentUser();
            email= user.getEmail().substring(0,user.getEmail().indexOf("@"));
            userId= user.getUid();
            options.put("name", ""+email);

            /**
             * Description can be anything
             * eg: Order #123123
             *     Invoice Payment
             *     etc.
             */
            String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(
                    Calendar.getInstance().getTime());
            orderId= bookintrodataSnapshot.child("productid").getValue().toString()+timeLog;
            options.put("description", orderId);
            options.put("currency", "INR");

            /**
             * Amount is always passed in PAISE
             * Eg: "500" = Rs 5.00
             */
            options.put("amount", ""+Integer.parseInt(bookintrodataSnapshot.child("price").getValue().toString())*100);
            //options.put("amount", "500");
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("dsnjc", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {

        bookintrodataSnapshot.child("subscribers").getRef().child(userId).child("username").setValue(email);
        bookintrodataSnapshot.child("subscribers").getRef().child(userId).child("transactionID").setValue(s);
        booklistreference.child(""+StaticVariableClass.cardposition).removeEventListener(valueEventListener);
        Intent resultintent= new Intent();
        resultintent.setComponent(new ComponentName(BookIntro.this, LandingPage.class));
        setResult(Activity.RESULT_OK, resultintent);
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        booklistreference.child(""+StaticVariableClass.cardposition).removeEventListener(valueEventListener);
        Intent resultintent= new Intent();
        resultintent.setComponent(new ComponentName(BookIntro.this, LandingPage.class));
        setResult(Activity.RESULT_CANCELED, resultintent);
        finish();
    }
}
