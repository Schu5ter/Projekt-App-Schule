package com.example.stockit;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.net.URI;

public class SignUp extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;


    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;


    private EditText et_EmailSignIn;
    private EditText et_PasswordSignIn;
    private Button btn_SignIn;

    Intent intent = getIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        Intent intent = getIntent();


        // Button listeners sign in Google and id identifier
        findViewById(R.id.signInButton).setOnClickListener(this);
        findViewById(R.id.et_EmailSignIn);
        findViewById(R.id.et_PasswordSignIn);
        findViewById(R.id.btn_SignIn).setOnClickListener(this);

        // [START config_signin]
        //Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.toString();

        mAuth = FirebaseAuth.getInstance();

    }

    // [START on_start_check_user]

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        // Check if user is signed in (non-null) and update UI accordingly.
    }
    // [END on_start_check_user]



    //Create Account
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    // Sign in success, update UI with the signed-in user's information

                    Log.d(TAG, "CreateUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);


                } else {

                    // If sign in fails, display a message to the user.

                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    Toast.makeText(SignUp.this, "Authentication Faild", Toast.LENGTH_SHORT).show();
                    updateUI(null);

                }

                // [START_EXCLUDE]
                hideProgressDialog();
                // [END_EXCLUDE]
            }
        });
    }


    private void signIn(String email, String password) {
        Log.d(TAG, "signIn" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    // Sign in success, update UI with the signed-in user's information

                    Log.d(TAG, "CreateUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);


                } else {

                    // If sign in fails, display a message to the user.

                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(SignUp.this, "Authentication Faild", Toast.LENGTH_SHORT).show();
                    updateUI(null);

                }
                if (!task.isSuccessful()) {
                    Toast.makeText(SignUp.this, " Fuck", Toast.LENGTH_SHORT).show();
                }

                // [START_EXCLUDE]
                hideProgressDialog();
                // [END_EXCLUDE]
            }
        });
    }


    // [START onactivityresult]
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
                Log.w(TAG, "Google sign in failed", e);
                updateUI(null);


            }

        }

    }

    // [END onactivityresult]
    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]

        showProgressDialog();


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information

                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);


                        } else {

                            // If sign in fails, display a message to the user.

                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);


                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]

                    }
                });
    }

    // [END auth_with_google]


    // [START signin]
    private void FirebaseSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    // [END signin]


    private void signOut() {

        // Firebase sign out
        mAuth.signOut();
        updateUI(null);

    }


    @Override
    public void onClick(View v) {

        int i = v.getId();

        if (i == R.id.signInButton) {
            showProgressDialog();
            FirebaseSignIn();
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);

        } else if (i == R.id.btn_SignIn) {
           // signIn(et_PasswordSignIn.getText().toString(), et_EmailSignIn.getText().toString());
            createAccount(et_PasswordSignIn.getText().toString(), et_EmailSignIn.getText().toString());
            //checking sign in
        }

    }

    @VisibleForTesting

    public ProgressDialog mProgressDialog;


    public void showProgressDialog() {

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();

    }


    public void hideProgressDialog() {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();

        }

    }

    public void updateUI(FirebaseUser currentUser) {
        hideProgressDialog();
        if (currentUser != null) {  // User is signed in
            Toast.makeText(this, "U Signed in Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Overview.class));

            // go to main page
        } else {
            Toast.makeText(this, "U aren´t signed in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SignUp.class));
            // No user is signed in
            // go to loging page
        }

    }


    @Override

    public void onStop() {
        super.onStop();
        hideProgressDialog();

    }

    public void startActivity() {
        Intent intent = new Intent(this, Overview.class);
        startActivity(intent);
    }

    private boolean validateForm() {

        boolean valid = true;

        String email = et_EmailSignIn.getText().toString();
        if (TextUtils.isEmpty((email)))
        {
            if (email.contains("@") != true ){
            et_EmailSignIn.setError("Requeired.");
            valid = false;}
        } else {
            et_PasswordSignIn.setError(null);
        }    
        return valid;

    }
}



