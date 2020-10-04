package com.example.travelexperts.ApplicationLayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;

import com.example.travelexperts.BusinessLayer.ProdPackage;
import com.example.travelexperts.R;

public class PackageDetailsActivity extends AppCompatActivity {

    EditText etPackageId, etCommission, etBasePrice, etDescription, etEndDate, etName, etStartDate;
    SharedPreferences prefs;
    ConstraintLayout clPackage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details);

        Intent receive = getIntent();

        // receives agent from sending activity
        ProdPackage prodPackage = (ProdPackage) receive.getSerializableExtra("package");
        // reference for texts
        etPackageId = findViewById(R.id.etPackageId);
        etCommission = findViewById(R.id.etCommission);
        etBasePrice = findViewById(R.id.etBasePrice);
        etDescription = findViewById(R.id.etDescription);
        etEndDate = findViewById(R.id.etEndDate);
        etName = findViewById(R.id.etName);
        etStartDate = findViewById(R.id.etStartDate);

        //Set background color form Settings
        clPackage= findViewById(R.id.clPackage);
        prefs = getSharedPreferences("myprefs",Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clPackage.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clPackage.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clPackage.setBackgroundColor(Color.GREEN);
                break;
        }

        etPackageId.setText(prodPackage.getPackageId() + "");
        etCommission.setText(prodPackage.getPkgAgencyCommission() + "");
        etBasePrice.setText(prodPackage.getPkgBasePrice() + "");
        etDescription.setText(prodPackage.getPkgDec());
        etEndDate.setText(prodPackage.getPkgEndDate() + "");
        etName.setText(prodPackage.getPkgName());
        etStartDate.setText(prodPackage.getPkgStartDate() + "");

    }


}