package com.example.stockit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toSignUp (View view ){
        Intent intent = new Intent(this,GoogleSignInActivity.class);
        startActivity(intent);

    }


    


}

