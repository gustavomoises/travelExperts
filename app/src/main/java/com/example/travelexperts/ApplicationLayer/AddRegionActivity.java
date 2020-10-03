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

import com.example.travelexperts.BusinessLayer.Region;
import com.example.travelexperts.DatabaseLayer.DataSource;
import com.example.travelexperts.R;

public class AddRegionActivity extends AppCompatActivity {
    SharedPreferences prefs;
    ConstraintLayout clAddRegion;
    Button btnAddRegionCancel,btnAddRegionSave, btnAddRegionDelete;
    DataSource dataSource;
    String mode;
    Region region;
    EditText etAddRegionRegionName, etAddRegionRegionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_region);
        //Set background color form Settings
        clAddRegion= findViewById(R.id.clAddRegion);
        btnAddRegionCancel=findViewById(R.id.btnAddRegionCancel);
        btnAddRegionDelete=findViewById(R.id.btnAddRegionDelete);
        btnAddRegionSave=findViewById(R.id.btnAddRegionSave);
        etAddRegionRegionId=findViewById(R.id.etAddRegionRegionId);
        etAddRegionRegionName=findViewById(R.id.etAddRegionRegionName);
        dataSource = new DataSource(this);

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        if (mode.equals("update")) {
            btnAddRegionDelete.setEnabled(true);
            region = (Region) intent.getSerializableExtra("Region");
            etAddRegionRegionName.setText(region.getRegionName()+"");
            etAddRegionRegionId.setText(region.getRegionId()+"");

        }
        else
        {
            btnAddRegionDelete.setEnabled(false);
            region=new Region();
            etAddRegionRegionName.setText("");
            etAddRegionRegionId.setText("");
        }
        btnAddRegionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegionActivity.class);
                startActivity(intent);

            }
        });

        btnAddRegionSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeId=etAddRegionRegionId.getText()+"";
                if (typeId.length()==0)
                    Toast.makeText(getApplicationContext(), " Region Id is required.", Toast.LENGTH_LONG).show();
                else
                {
                    if (typeId.length()>5)
                        Toast.makeText(getApplicationContext(), " Maximum of 5 characters is required for the trip Id", Toast.LENGTH_LONG).show();
                    else {
                        boolean exist = false;
                        int k=0;
                        for (Region r : dataSource.getRegions()) {
                            if (mode.equals("update"))
                            {
                                if (r.getRegionId().equals(typeId) && !(region.getRegionId().equals(typeId)))
                                    exist = true;
                            }
                            else
                            {
                                if (r.getRegionId().equals(typeId))
                                    exist = true;
                            }

                        }
                        if (exist)
                            Toast.makeText(getApplicationContext(), " Id associated to another region. Please, choose another Id!!!", Toast.LENGTH_LONG).show();
                        else {
                            if (mode.equals("update")) {
                                region.setRegionName(etAddRegionRegionName.getText() + "");
                                region.setRegionId(typeId);
                                if (dataSource.updateRegion(region)) {
                                    Toast.makeText(getApplicationContext(), " Region Updated!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), RegionActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), " Region Update Failed!", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                region.setRegionName(etAddRegionRegionName.getText() + "");
                                region.setRegionId(typeId);
                                if (dataSource.insertRegion(region)) {
                                    Toast.makeText(getApplicationContext(), " Region Inserted!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(),RegionActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), " Region Insertion Failed!", Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    }
                }
            }
        });

        btnAddRegionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.deleteRegion(region);
                Intent intent = new Intent(getApplicationContext(), RegionActivity.class);
                startActivity(intent);
            }
        });



        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");



        switch (basicColor){
            case "White":
                clAddRegion.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddRegion.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddRegion.setBackgroundColor(Color.GREEN);
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
        clAddRegion= findViewById(R.id.clAddRegion);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clAddRegion.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddRegion.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddRegion.setBackgroundColor(Color.GREEN);
                break;
        }
    }

}