package com.example.stockit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    public void toSignUp(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);

    }

    ImageView myImageView = (ImageView) findViewById(R.id.imgview);
    Bitmap myBitmap = BitmapFactory.decodeResource(
            getApplicationContext().getResources(),
            R.drawable.puppy);
        /*myImageView.setImageBitmap(myBitmap);*/


    BarcodeDetector detector =
            new BarcodeDetector.Builder(getApplicationContext())
                    .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                    .build();

    Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
    SparseArray<Barcode> barcodes = detector.detect(frame);
    Barcode thisCode = barcodes.valueAt(0);
    TextView txtView = (TextView) findViewById(R.id.txtContent);
    /*txtView.setText(thisCode.rawValue);*/
            }

