package com.example.stockit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Overview extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private GoogleSignInClient mGoogleSignInClient;
    private ArrayList<Artikel> mArtikel = new ArrayList<>();
    private RecyclerView recyclerView;
    private String user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        recyclerView = findViewById(R.id.recyclerview);

        user = FirebaseAuth.getInstance().getCurrentUser().getUid();


        mDatabaseReference = FirebaseDatabase.getInstance().getReference("artikel");




        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         //Floating Action Button
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Overview.this, PopupEintreage.class));
            }
        });


        //Nav Drawer
            drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new PersonalFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_personal);


        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mArtikel.clear();

                for(DataSnapshot artikelsnapshot: dataSnapshot.getChildren()){

                    Artikel artikel = artikelsnapshot.getValue(Artikel.class);
                    if(artikel.getUserId().equals(user)) {
                        mArtikel.add(artikel);
                    }
                }
               initRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_personal:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PersonalFragment()).commit();
                break;
            case R.id.nav_logout:
                signOut();
                Toast.makeText(this, "Logged out from StockIT", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void signOut(){
        //Firebase Sign Out
        mAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity();
                    }
                });

    }



    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
    public void startActivity() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }



    private void initRecyclerView(){

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mArtikel,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



}
