package com.example.stockit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

public class PopupEintreage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText et_NameArtikel;
    Button btn_addArtikel;
    public static String number;
    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_eintreage);

        //Getting id's
        et_NameArtikel = findViewById(R.id.et_NameArtikel);
        btn_addArtikel = findViewById(R.id.btn_addArtikel);

        //
         String  userId = FirebaseAuth.getInstance().getCurrentUser().getUid();







        //Firebase DB Reference
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("artikel");

        //onclickListener

        btn_addArtikel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addArtikel();
            }
        });





        //PopUp
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int breite = dm.widthPixels;
        int höhe = dm.heightPixels;

        getWindow().setLayout( (int)(breite*.8),(int) (höhe*.7));

        //Spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this,R.array.numbers_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        number = parent.getItemAtPosition(pos).toString();
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public  void addArtikel(){
             String artikelname = et_NameArtikel.getText().toString();
             String  userId = FirebaseAuth.getInstance().getCurrentUser().getUid();



        if(!TextUtils.isEmpty((CharSequence) artikelname)){
           String artikelId = reference.push().getKey();

           Artikel artikel = new Artikel( userId, artikelId, artikelname, number);

           reference.child(artikelId).setValue(artikel);

            Toast.makeText(this, "Artikel added", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(PopupEintreage.this, "Please Enter a Name", Toast.LENGTH_SHORT).show();

        }


    }

}
