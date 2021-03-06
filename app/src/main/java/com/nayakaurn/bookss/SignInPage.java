package com.nayakaurn.bookss;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static com.nayakaurn.bookss.StaticVariableClass.mAuth;

public class SignInPage extends AppCompatActivity {

    private final int RC_SIGN_IN = 1;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton button;
    public static SignInPage signInPage;

    public static SignInPage getInstance() {
        signInPage= new SignInPage();
        return signInPage;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);

        // Configure Google Sign In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //inapp permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS)!= PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{android.Manifest.permission.GET_ACCOUNTS},1);
            //if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED)
            //    requestPermissions(new String[]{Manifest.permission.READ_SMS},2);

        }


        button = (SignInButton)findViewById(R.id.signin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Mainactivity", "onSignInClick");
                signIn();
            }
        });

    }

    private void signIn() {
        // Build a GoogleSignInClient with the options specified by gso.
        Log.i("Mainactivity", "inside SignINmethod");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Mainactivity", "Google sign in failed", e);
                // ...
            }
        }
    }


    public void firebaseAuthWithGoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent= new Intent();
                            intent.setComponent(new ComponentName(SignInPage.this, LandingPage.class));
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "LogIn Failed", Toast.LENGTH_SHORT).show();
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==1){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_DENIED) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                finish();
            }
        }

        /*if(requestCode==2){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_DENIED) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                finish();
            }
        }*/
    }
}
