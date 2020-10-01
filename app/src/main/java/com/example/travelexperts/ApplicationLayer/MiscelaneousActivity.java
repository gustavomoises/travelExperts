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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.example.travelexperts.R;
import java.util.ArrayList;

public class MiscelaneousActivity extends AppCompatActivity {
    SharedPreferences prefs;
    ConstraintLayout clMiscelaneous;
    ListView lvMiscList;
    ArrayAdapter<String> adapterMisc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miscelaneous);

        lvMiscList = findViewById(R.id.lvMiscList);
        final ArrayList<String> miscList =new ArrayList<>();
        miscList.add("Class");
        miscList.add("Fee");
        miscList.add("Product Item");
        miscList.add("Region");
        miscList.add("Supplier");
        miscList.add("Trip Type");


        adapterMisc=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,miscList);
        lvMiscList.setAdapter(adapterMisc);
        lvMiscList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {

                    case 0:
                        Toast.makeText(getApplicationContext(),"Class was clicked", Toast.LENGTH_LONG).show();
                        Intent intent0 = new Intent(getApplicationContext(), ClassActivity.class);
                        startActivity(intent0);
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "Fee was clicked", Toast.LENGTH_LONG).show();
                        Intent intent1 = new Intent(getApplicationContext(), FeeActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "Product Item was clicked", Toast.LENGTH_LONG).show();
                        Intent intent2 = new Intent(getApplicationContext(), ProductItemActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                    Toast.makeText(getApplicationContext(), "Region was clicked", Toast.LENGTH_LONG).show();
                    Intent intent3 = new Intent(getApplicationContext(), RegionActivity.class);
                    startActivity(intent3);
                    break;
                    case 4:
                        Toast.makeText(getApplicationContext(), "Supplier was clicked", Toast.LENGTH_LONG).show();
                        Intent intent4 = new Intent(getApplicationContext(), SupplierActivity.class);
                        startActivity(intent4);
                        break;
                    case 5:
                        Toast.makeText(getApplicationContext(), "Trip was clicked", Toast.LENGTH_LONG).show();
                        Intent intent5 = new Intent(getApplicationContext(), TripTypeActivity.class);
                        startActivity(intent5);
                        break;
                }

            }
        });


        //Set background color form Settings
        clMiscelaneous= findViewById(R.id.clMiscelaneous);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clMiscelaneous.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clMiscelaneous.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clMiscelaneous.setBackgroundColor(Color.GREEN);
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
        clMiscelaneous= findViewById(R.id.clMiscelaneous);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clMiscelaneous.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clMiscelaneous.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clMiscelaneous.setBackgroundColor(Color.GREEN);
                break;
        }
    }


}