//Author: Gustavo Lourenco Moises
//Thread Project - Group 1
//OOSD Program Spring 2020
//Date:9/30/2020
//Travel Agency Application
//
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.travelexperts.R;
public class SettingsActivity extends AppCompatActivity {
    //Local Variables
    Button btnSaveSettings;
    RadioGroup rgBackColor;
    RadioButton rbBackColor1;
    RadioButton rbBackColor2;
    RadioButton rbBackColor3;
    SharedPreferences prefs;
    ConstraintLayout clSettings;

    private String colorPicked;
    public String getColorPicked() {
        return colorPicked;
    }

    public void setColorPicked(String colorPicked) {
        this.colorPicked = colorPicked;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btnSaveSettings= findViewById(R.id.btnSaveSettings);
        rgBackColor= findViewById(R.id.rgBackGroundColor);
        rbBackColor1= findViewById(R.id.rbBackColor1);
        rbBackColor2= findViewById(R.id.rbBackColor2);
        rbBackColor3= findViewById(R.id.rbBackColor3);

        //Set background color form Settings
        clSettings= findViewById(R.id.clSettings);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clSettings.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clSettings.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clSettings.setBackgroundColor(Color.GREEN);
                break;

        }
        //Select Background color
        rgBackColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()            {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int selectedId) {
                selectedId= rgBackColor.getCheckedRadioButtonId();
                RadioButton rbBackColor = (RadioButton) findViewById(selectedId);
                setColorPicked(rbBackColor.getText().toString());
            }
        });

        //Save the color in the shared preferences in the device
        btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("color",getColorPicked());
                editor.commit();
                switch (getColorPicked()){
                    case "White":
                        clSettings.setBackgroundColor(Color.WHITE);
                        break;
                    case "Blue":
                        clSettings.setBackgroundColor(Color.BLUE);
                        break;
                    case "Green":
                        clSettings.setBackgroundColor(Color.GREEN);
                        break;
                }
                Toast.makeText(getApplicationContext(), "Prefs were saved",Toast.LENGTH_LONG).show();
            }
        });
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
        clSettings= findViewById(R.id.clSettings);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clSettings.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clSettings.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clSettings.setBackgroundColor(Color.GREEN);
                break;
        }
    }
}