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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travelexperts.BusinessLayer.Affiliation;
import com.example.travelexperts.BusinessLayer.BookClass;
import com.example.travelexperts.BusinessLayer.TripType;
import com.example.travelexperts.DatabaseLayer.DataSource;
import com.example.travelexperts.R;

public class AddAffiliationActivity extends AppCompatActivity {
    SharedPreferences prefs;
    ConstraintLayout clAddAffiliation;
    Button btnAddAffiliationCancel,btnAddAffiliationSave, btnAddAffiliationDelete;
    DataSource dataSource;
    String mode;
    Affiliation affiliation;
    EditText etAddAffiliationAffiliationId, etAddAffiliationAffName,etAddAffiliationAffDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_affiliation);
        //Set background color form Settings
        clAddAffiliation= findViewById(R.id.clAddAffiliation);
        btnAddAffiliationSave=findViewById(R.id.btnAddAffiliationSave);
        btnAddAffiliationCancel=findViewById(R.id.btnAddAffiliationCancel);
        btnAddAffiliationDelete=findViewById(R.id.btnAddAffiliationDelete);
        etAddAffiliationAffiliationId=findViewById(R.id.etAddAffiliationAffiliationId);
        etAddAffiliationAffName=findViewById(R.id.etAddAffiliationAffName);
        etAddAffiliationAffDesc=findViewById(R.id.etAddAffiliationAffDesc);
        dataSource = new DataSource(this);

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        if (mode.equals("update")) {
            btnAddAffiliationDelete.setEnabled(true);
            affiliation = (Affiliation) intent.getSerializableExtra("Affiliation");
            etAddAffiliationAffName.setText(affiliation.getAffName()+"");
            etAddAffiliationAffiliationId.setText(affiliation.getAffiliationId()+"");
            etAddAffiliationAffDesc.setText(affiliation.getAffDesc()+"");
        }
        else
        {
            btnAddAffiliationDelete.setEnabled(false);
            affiliation=new Affiliation();
            etAddAffiliationAffName.setText("");
            etAddAffiliationAffiliationId.setText("");
            etAddAffiliationAffDesc.setText("");
        }
        btnAddAffiliationCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AffiliationActivity.class);
                startActivity(intent);

            }
        });

        btnAddAffiliationSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeId=etAddAffiliationAffiliationId.getText()+"";
                if (typeId.length()==0)
                    Toast.makeText(getApplicationContext(), " Affiliation Id is required.", Toast.LENGTH_LONG).show();
                else
                {
                    if (typeId.length()>10)
                        Toast.makeText(getApplicationContext(), " Maximum of 10 characters is required for the Affiliation Id", Toast.LENGTH_LONG).show();
                    else {
                        boolean exist = false;
                        int k=0;
                        for (Affiliation r : dataSource.getAffiliations()) {
                            if (mode.equals("update"))
                            {
                                if (r.getAffiliationId().equals(typeId) && !(affiliation.getAffiliationId().equals(typeId)))
                                    exist = true;
                            }
                            else
                            {
                                if (r.getAffiliationId().equals(typeId))
                                    exist = true;
                            }

                        }
                        if (exist)
                            Toast.makeText(getApplicationContext(), " Id associated to another affiliation. Please, choose another Id!!!", Toast.LENGTH_LONG).show();
                        else {
                            if (mode.equals("update")) {
                                affiliation.setAffName(etAddAffiliationAffName.getText() + "");
                                affiliation.setAffiliationId(typeId);
                                affiliation.setAffDesc(etAddAffiliationAffDesc.getText() + "");
                                if (dataSource.updateAffiliation(affiliation)) {
                                    Toast.makeText(getApplicationContext(), " Affiliation Updated!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), AffiliationActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), " Affiliation Update Failed!", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                affiliation.setAffName(etAddAffiliationAffName.getText() + "");
                                affiliation.setAffiliationId(typeId);
                                affiliation.setAffDesc(etAddAffiliationAffDesc.getText() + "");
                                if (dataSource.insertAffiliation(affiliation)) {
                                    Toast.makeText(getApplicationContext(), " Affiliation Inserted!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(),AffiliationActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), " Affiliation Insertion Failed!", Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    }
                }
            }
        });

        btnAddAffiliationDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.deleteAffiliation(affiliation);
                Intent intent = new Intent(getApplicationContext(), AffiliationActivity.class);
                startActivity(intent);
            }
        });








        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");



        switch (basicColor){
            case "White":
                clAddAffiliation.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddAffiliation.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddAffiliation.setBackgroundColor(Color.GREEN);
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
        clAddAffiliation= findViewById(R.id.clAddAffiliation);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clAddAffiliation.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddAffiliation.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddAffiliation.setBackgroundColor(Color.GREEN);
                break;
        }
    }

}