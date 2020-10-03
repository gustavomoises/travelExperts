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
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelexperts.BusinessLayer.Supplier;
import com.example.travelexperts.DatabaseLayer.DataSource;
import com.example.travelexperts.R;

public class AddSupplierActivity extends AppCompatActivity {
    SharedPreferences prefs;
    ConstraintLayout clAddSupplier;
    Button btnAddSupplierCancel,btnAddSupplierSave, btnAddSupplierDelete;
    DataSource dataSource;
    String mode;
    Supplier supplier;
    TextView tvAddSupplierSupplierId;
    EditText etAddSupplierSupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supplier);
        //Set background color form Settings
        clAddSupplier= findViewById(R.id.clAddSupplier);
        btnAddSupplierSave=findViewById(R.id.btnAddSupplierSave);
        btnAddSupplierCancel=findViewById(R.id.btnAddSupplierCancel);
        btnAddSupplierDelete=findViewById(R.id.btnAddSupplierDelete);
        tvAddSupplierSupplierId=findViewById(R.id.tvAddSupplierSupplierId);
        etAddSupplierSupName=findViewById(R.id.etAddSupplierSupName);
        dataSource = new DataSource(this);

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        if (mode.equals("update")) {
            btnAddSupplierDelete.setEnabled(true);
            supplier = (Supplier) intent.getSerializableExtra("Supplier");
            tvAddSupplierSupplierId.setText(supplier.getSupplierId()+"");
            etAddSupplierSupName.setText(supplier.getSupName());

        }
        else
            {
                btnAddSupplierDelete.setEnabled(false);
                supplier=new Supplier();
                tvAddSupplierSupplierId.setText("*");
                etAddSupplierSupName.setText("");


        }


        btnAddSupplierCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SupplierActivity.class);
                startActivity(intent);

            }
        });

        btnAddSupplierSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode.equals("update"))
                {
                    supplier.setSupName(etAddSupplierSupName.getText()+"");
                    if(dataSource.updateSupplier(supplier))
                    {
                        Toast.makeText(getApplicationContext(), " Supplied Updated!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), SupplierActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), " Supplier Update Failed!", Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    supplier.setSupName(etAddSupplierSupName.getText()+"");
                    if(dataSource.insertSupplier(supplier))
                    {
                        Toast.makeText(getApplicationContext(), " Supplied Inserted!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), SupplierActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), " Supplier Insertion Failed!", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        btnAddSupplierDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.deleteSupplier(supplier);
                Intent intent = new Intent(getApplicationContext(), SupplierActivity.class);
                startActivity(intent);
            }
        });

        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");



        switch (basicColor){
            case "White":
                clAddSupplier.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddSupplier.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddSupplier.setBackgroundColor(Color.GREEN);
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
        clAddSupplier= findViewById(R.id.clAddSupplier);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clAddSupplier.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddSupplier.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddSupplier.setBackgroundColor(Color.GREEN);
                break;
        }
    }
}