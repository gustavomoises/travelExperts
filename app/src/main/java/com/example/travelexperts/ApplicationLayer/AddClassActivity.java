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

import com.example.travelexperts.BusinessLayer.BookClass;
import com.example.travelexperts.BusinessLayer.Region;
import com.example.travelexperts.DatabaseLayer.DataSource;
import com.example.travelexperts.R;

public class AddClassActivity extends AppCompatActivity {
    SharedPreferences prefs;
    ConstraintLayout clAddClass;
    Button btnAddClassCancel,btnAddClassSave, btnAddClassDelete;
    DataSource dataSource;
    String mode;
    BookClass bookClass;
    EditText etAddClassClassName, etAddClassClassId,etAddCLassClassDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        //Set background color form Settings
        clAddClass= findViewById(R.id.clAddClass);
        btnAddClassSave=findViewById(R.id.btnAddClassSave);
        btnAddClassCancel=findViewById(R.id.btnAddClassCancel);
        btnAddClassDelete=findViewById(R.id.btnAddClassDelete);
        etAddCLassClassDesc=findViewById(R.id.etAddClassClassDesc);
        etAddClassClassId=findViewById(R.id.etAddClassClassId);
        etAddClassClassName=findViewById(R.id.etAddCLassClassName);
        dataSource = new DataSource(this);

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        if (mode.equals("update")) {
            btnAddClassDelete.setEnabled(true);
            bookClass = (BookClass) intent.getSerializableExtra("Class");
            etAddClassClassName.setText(bookClass.getClassName()+"");
            etAddClassClassId.setText(bookClass.getClassId()+"");
            etAddCLassClassDesc.setText(bookClass.getClassDes()+"");
        }
        else
        {
            btnAddClassDelete.setEnabled(false);
            bookClass=new BookClass();
            etAddClassClassName.setText("");
            etAddClassClassId.setText("");
            etAddCLassClassDesc.setText("");
        }
        btnAddClassCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClassActivity.class);
                startActivity(intent);

            }
        });

        btnAddClassSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeId=etAddClassClassId.getText()+"";
                if (typeId.length()==0)
                    Toast.makeText(getApplicationContext(), " Class Id is required.", Toast.LENGTH_LONG).show();
                else
                {
                    if (typeId.length()>5)
                        Toast.makeText(getApplicationContext(), " Maximum of 5 characters is required for the Class Id", Toast.LENGTH_LONG).show();
                    else {
                        boolean exist = false;
                        int k=0;
                        for (BookClass r : dataSource.getBookClasses()) {
                            if (mode.equals("update"))
                            {
                                if (r.getClassId().equals(typeId) && !(bookClass.getClassId().equals(typeId)))
                                    exist = true;
                            }
                            else
                            {
                                if (r.getClassId().equals(typeId))
                                    exist = true;
                            }

                        }
                        if (exist)
                            Toast.makeText(getApplicationContext(), " Id associated to another class. Please, choose another Id!!!", Toast.LENGTH_LONG).show();
                        else {
                            if (mode.equals("update")) {
                                bookClass.setClassName(etAddClassClassName.getText() + "");
                                bookClass.setClassId(typeId);
                                bookClass.setClassDes(etAddCLassClassDesc.getText() + "");
                                if (dataSource.updateBookClass(bookClass)) {
                                    Toast.makeText(getApplicationContext(), " Class Updated!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), ClassActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), " Class Update Failed!", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                bookClass.setClassName(etAddClassClassName.getText() + "");
                                bookClass.setClassId(typeId);
                                bookClass.setClassDes(etAddCLassClassDesc.getText() + "");
                                if (dataSource.insertBookClass(bookClass)) {
                                    Toast.makeText(getApplicationContext(), " Class Inserted!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(),ClassActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), " Class Insertion Failed!", Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    }
                }
            }
        });

        btnAddClassDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.deleteBookClass(bookClass);
                Intent intent = new Intent(getApplicationContext(), ClassActivity.class);
                startActivity(intent);
            }
        });

        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clAddClass.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddClass.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddClass.setBackgroundColor(Color.GREEN);
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
        clAddClass= findViewById(R.id.clAddClass);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clAddClass.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddClass.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddClass.setBackgroundColor(Color.GREEN);
                break;
        }
    }

}