//Author: Gustavo Lourenco Moises
//Thread Project - Group 1
//OOSD Program Spring 2020
//Date:9/30/2020
//Travel Agency Application
package com.example.travelexperts.ApplicationLayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travelexperts.BusinessLayer.Fee;
import com.example.travelexperts.DatabaseLayer.DataSource;
import com.example.travelexperts.R;

public class AddFeeActivity extends AppCompatActivity {
    SharedPreferences prefs;
    ConstraintLayout clAddFee;
    Button btnAddFeeCancel,btnAddFeeSave, btnAddFeeDelete;
    DataSource dataSource;
    String mode;
    Fee fee;
    EditText etAddFeeFeeName, etAddFeeFeeId,etAddFeeFeeDesc,etAddFeeFeeAmt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fee);
        //Set background color form Settings
        clAddFee= findViewById(R.id.clAddFee);
        btnAddFeeCancel=findViewById(R.id.btnAddFeeCancel);
        btnAddFeeDelete=findViewById(R.id.btnAddFeeDelete);
        btnAddFeeSave=findViewById(R.id.btnAddFeeSave);
        etAddFeeFeeId=findViewById(R.id.etAddFeeFeeId);
        etAddFeeFeeName=findViewById(R.id.etAddFeeFeeName);
        etAddFeeFeeAmt=findViewById(R.id.etAddFeeFeeAmt);
        etAddFeeFeeDesc=findViewById(R.id.etAddFeeFeeDesc);
        dataSource = new DataSource(this);





        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");



        switch (basicColor){
            case "White":
                clAddFee.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddFee.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddFee.setBackgroundColor(Color.GREEN);
                break;

        }
    }

    //Load Application Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.travelexpertsmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Go to selected menu item
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {

            case R.id.miBooking:
                Toast.makeText(this, item.getTitle()+" was clicked", Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(this, BookingActivity.class);
                startActivity(intent1);
                break;
            case R.id.miPackage:
                Toast.makeText(this, item.getTitle()+" was clicked", Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(this, PackageActivity.class);
                startActivity(intent2);
                break;
            case R.id.miProduct:
                Toast.makeText(this, item.getTitle()+" was clicked", Toast.LENGTH_LONG).show();
                Intent intent3 = new Intent(this, ProductActivity.class);
                startActivity(intent3);
                break;
            case R.id.miMiscelaneous:
                Toast.makeText(this, item.getTitle()+" was clicked", Toast.LENGTH_LONG).show();
                Intent intent4 = new Intent(this, MiscellaneousActivity.class);
                startActivity(intent4);
                break;
            case R.id.miSettings:
                Toast.makeText(this, item.getTitle()+" was clicked", Toast.LENGTH_LONG).show();
                Intent intent5 = new Intent(this, SettingsActivity.class);
                startActivity(intent5);
                break;
            case R.id.miAbout:
                Toast.makeText(this, item.getTitle()+" was clicked", Toast.LENGTH_LONG).show();
                Intent intent6 = new Intent(this, AboutActivity.class);
                startActivity(intent6);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Set background color form Settings
        clAddFee= findViewById(R.id.clAddFee);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clAddFee.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddFee.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddFee.setBackgroundColor(Color.GREEN);
                break;
        }
    }

}