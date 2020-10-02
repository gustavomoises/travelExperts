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

import com.example.travelexperts.BusinessLayer.TripType;
import com.example.travelexperts.DatabaseLayer.DataSource;
import com.example.travelexperts.R;

public class AddTripTypeActivity extends AppCompatActivity {
    SharedPreferences prefs;
    ConstraintLayout clAddSTrip;
    Button btnAddTripTypeCancel,btnAddTripTypeSave, btnAddTripTypeDelete;
    DataSource dataSource;
    String mode;
    TripType tripType;
    EditText etAddTripTypeTripTypeName, etAddTripTypeTripTypeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        //Set background color form Settings
        clAddSTrip= findViewById(R.id.clAddTrip);
        btnAddTripTypeDelete=findViewById(R.id.btnAddTripTypeDelete);
        btnAddTripTypeSave=findViewById(R.id.btnAddTripTypeSave);
        btnAddTripTypeCancel=findViewById(R.id.btnAddTripTypeCancel);
        etAddTripTypeTripTypeId=findViewById(R.id.etAddTripTypeTripId);
        etAddTripTypeTripTypeName=findViewById(R.id.etAddTripTypeTripTypeName);
        dataSource = new DataSource(this);

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        if (mode.equals("update")) {
            btnAddTripTypeDelete.setEnabled(true);
            tripType = (TripType) intent.getSerializableExtra("TripType");
            etAddTripTypeTripTypeName.setText(tripType.gettTName()+"");
            etAddTripTypeTripTypeId.setText(tripType.getTripTypeId()+"");

        }
        else
        {
            btnAddTripTypeDelete.setEnabled(false);
            tripType=new TripType();
            etAddTripTypeTripTypeName.setText("");
            etAddTripTypeTripTypeId.setText("");


        }


        btnAddTripTypeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TripTypeActivity.class);
                startActivity(intent);

            }
        });

        btnAddTripTypeSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeId=etAddTripTypeTripTypeId.getText()+"";
                if (typeId.length()==0)
                    Toast.makeText(getApplicationContext(), " One character is required for the trip Id", Toast.LENGTH_LONG).show();
                else
                {
                    if (typeId.length()>1)
                        Toast.makeText(getApplicationContext(), " Only character is required for the trip Id", Toast.LENGTH_LONG).show();
                    else {
                        boolean exist = false;
                        int k=0;
                        for (TripType tt : dataSource.getTripTypes()) {
                            if (mode.equals("update"))
                            {
                                if (tt.getTripTypeId() == typeId.charAt(0) && tripType.getTripTypeId()!=typeId.charAt(0))
                                    exist = true;
                            }
                            else
                            {
                                if (tt.getTripTypeId() == typeId.charAt(0))
                                    exist = true;
                            }

                        }
                        if (exist)
                            Toast.makeText(getApplicationContext(), " Character associated to another trip type. Please, choose another character!!!", Toast.LENGTH_LONG).show();
                        else {
                            if (mode.equals("update")) {
                                tripType.settTName(etAddTripTypeTripTypeName.getText() + "");
                                tripType.setTripTypeId(typeId.charAt(0));
                                if (dataSource.updateTripType(tripType)) {
                                    Toast.makeText(getApplicationContext(), " Trip Type Updated!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), TripTypeActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), " TripType Update Failed!", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                tripType.settTName(etAddTripTypeTripTypeName.getText() + "");
                                tripType.setTripTypeId(typeId.charAt(0));
                                if (dataSource.insertTripType(tripType)) {
                                    Toast.makeText(getApplicationContext(), " TripType Inserted!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), TripTypeActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), " Trip Type Insertion Failed!", Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    }
                }
            }
        });

        btnAddTripTypeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.deleteTripType(tripType);
                Intent intent = new Intent(getApplicationContext(), TripTypeActivity.class);
                startActivity(intent);
            }
        });






        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");



        switch (basicColor){
            case "White":
                clAddSTrip.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddSTrip.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddSTrip.setBackgroundColor(Color.GREEN);
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
                Intent intent4 = new Intent(this, MiscelaneousActivity.class);
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
        clAddSTrip= findViewById(R.id.clAddTrip);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clAddSTrip.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddSTrip.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddSTrip.setBackgroundColor(Color.GREEN);
                break;
        }
    }
}