package com.example.stockit;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.EventLogTags;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity implements View.OnClickListener, TextWatcher {


    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("(?=.*[A-Z])" + ".{6,}");


    private FirebaseAuth Auth;
    private GoogleSignInClient GoogleSignInClient;


    private EditText et_EmailSignIn;
    private EditText et_PasswordSignIn;
    private Button btn_SignIn, btn_CreateAccount;

    Intent intent = getIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Button listeners sign in Google and id identifier

        findViewById(R.id.signInButton).setOnClickListener(this);
        findViewById(R.id.btn_CreateAccount).setOnClickListener(this);
        findViewById(R.id.btn_SignIn).setOnClickListener(this);

        // identifizieren von Email und Password
        et_EmailSignIn = findViewById(R.id.et_EmailSignIn);
        et_PasswordSignIn = findViewById(R.id.et_PasswordSignIn);

        String email = et_EmailSignIn.getText().toString();
        String password = et_PasswordSignIn.getText().toString();

        //Google Sign in Config
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        GoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Auth = FirebaseAuth.getInstance();

    }


    @Override
    public void onStart() {
        super.onStart();

    }


    //Create Account
    private void createAccount() {
        String email = et_EmailSignIn.getText().toString();
        String password = et_PasswordSignIn.getText().toString();

/*
     if (email.isEmpty()) {
            et_EmailSignIn.setError("Please enter Email");
        } else {

*/
        if (emailValid() == false | passwordValid() == false) {
            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
        } else {

            Auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                showProgressDialog();
                                Toast.makeText(SignUp.this, "Erfolgreich angemeldet", Toast.LENGTH_SHORT).show();

                                FirebaseUser user = Auth.getCurrentUser();
                                updateUI(user);


                            } else {

                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignUp.this, "Anmeldung Fehlgeschlagen", Toast.LENGTH_SHORT).show();
                                // updateUI(null);

                            }

                            // [START_EXCLUDE]
                            hideProgressDialog();
                            // [END_EXCLUDE]
                        }
                    });
        }
    }


    private void emailSignIn() {
        String email = et_EmailSignIn.getText().toString();
        String password = et_PasswordSignIn.getText().toString();

        if (emailValid() == false | passwordValid() == false) {
            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
        } else {

            Auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                showProgressDialog();
                                Toast.makeText(SignUp.this, "Authentication Successful", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = Auth.getCurrentUser();
                                updateUI(user);


                            } else {

                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignUp.this, "Authentication Faild", Toast.LENGTH_SHORT).show();
                                updateUI(null);

                            }
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignUp.this, " Email oder Password ist Falsch", Toast.LENGTH_SHORT).show();
                            }

                            // [START_EXCLUDE]
                            hideProgressDialog();
                            // [END_EXCLUDE]
                        }
                    });
        }
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
                firebaseAuth(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                updateUI(null);


            }

        }

    }

    // [END onactivityresult]
    // [START auth_with_google]
    private void firebaseAuth(GoogleSignInAccount acct) {
        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        Auth.signInWithCredential(credential)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignUp.this, "Angemeldet mit Google", Toast.LENGTH_SHORT).show();


                            FirebaseUser user = Auth.getCurrentUser();
                            updateUI(user);


                        } else {

                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUp.this, "Google anmeldung Fehlgeschlagen", Toast.LENGTH_SHORT).show();

                            updateUI(null);


                        }
                    }
                });
    }


    // [START signin]
    private void firebaseSignIn() {
        Intent signInIntent = GoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    // [END signin]


    @Override
    public void onClick(View v) {
        String email = et_EmailSignIn.getText().toString();
        String password = et_PasswordSignIn.getText().toString();

        int i = v.getId();
//firebase sign in funktioniert einwandfrei
        if (i == R.id.signInButton) {
            firebaseSignIn();


        } else if (i == R.id.btn_SignIn) {

            emailSignIn();

        } else if (i == R.id.btn_CreateAccount) {

            createAccount();

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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    public boolean emailValid() {
        String email = et_EmailSignIn.getText().toString();

        if (email.isEmpty()) {
            et_EmailSignIn.setError("Please enter a Email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_EmailSignIn.setError("Please enter a Valid Email Address");
            return false;
        } else {
            et_EmailSignIn.setError(null);
            return true;
        }
    }

    public boolean passwordValid() {
        String password = et_PasswordSignIn.getText().toString();
        if (TextUtils.isEmpty(password)) {
            et_PasswordSignIn.setError("Please enter a Password");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            et_PasswordSignIn.setError("The password should be at least 6 charachters and Include a Capital Letter");
            return false;

        } else {
            et_PasswordSignIn.setError(null);
            return true;
        }

    }

}



