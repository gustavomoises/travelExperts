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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.travelexperts.BusinessLayer.Affiliation;
import com.example.travelexperts.BusinessLayer.Reward;
import com.example.travelexperts.DatabaseLayer.DataSource;
import com.example.travelexperts.R;

import java.util.ArrayList;

public class AffiliationActivity extends AppCompatActivity {
    SharedPreferences prefs;
    ConstraintLayout clAffiliation;
    ListView lvListAffiliation;
    DataSource dataSource;
    ArrayList<Affiliation> affiliations;
    ArrayAdapter<Affiliation> adapterAffiliation;
    Button btnAddAffiliation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiliation);
        //Set background color form Settings
        clAffiliation= findViewById(R.id.clAffiliation);
        btnAddAffiliation=findViewById(R.id.btnAddAffiliation);
        lvListAffiliation=findViewById(R.id.lvListAffiliation);
        //Get Fees from database
        dataSource = new DataSource(this);
        affiliations=dataSource.getAffiliations();
        adapterAffiliation=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,affiliations);
        lvListAffiliation.setAdapter(adapterAffiliation);

        btnAddAffiliation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddAffiliationActivity.class);
                intent.putExtra("mode","insert");
                startActivity(intent);
            }
        });

        lvListAffiliation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AddAffiliationActivity.class);
                intent.putExtra("mode","update");
                intent.putExtra("Affiliation",affiliations.get(position));
                startActivity(intent);
            }
        });


        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");



        switch (basicColor){
            case "White":
                clAffiliation.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAffiliation.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAffiliation.setBackgroundColor(Color.GREEN);
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
        clAffiliation= findViewById(R.id.clAffiliation);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clAffiliation.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAffiliation.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAffiliation.setBackgroundColor(Color.GREEN);
                break;
        }
    }
}