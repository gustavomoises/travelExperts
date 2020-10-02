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

import com.example.travelexperts.BusinessLayer.Product;
import com.example.travelexperts.BusinessLayer.Supplier;
import com.example.travelexperts.DatabaseLayer.DataSource;
import com.example.travelexperts.R;

public class AddProductItemActivity extends AppCompatActivity {
    SharedPreferences prefs;
    ConstraintLayout clAddProductItem;
    Button btnAddProductItemCancel,btnAddProductItemSave, btnAddProductItemDelete;
    DataSource dataSource;
    String mode;
    Product product;
    TextView tvAddProductItemProductId;
    EditText etAddProductItemProdName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_item);
        //Set background color form Settings
        clAddProductItem= findViewById(R.id.clAddProductItem);
        btnAddProductItemSave=findViewById(R.id.btnAddProductItemSave);
        btnAddProductItemCancel=findViewById(R.id.btnAddProductItemCancel);
        btnAddProductItemDelete=findViewById(R.id.btnAddProductItemDelete);
        tvAddProductItemProductId=findViewById(R.id.tvAddProductItemProductId);
        etAddProductItemProdName=findViewById(R.id.etAddProductItemProdName);
        dataSource = new DataSource(this);

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        if (mode.equals("update")) {
            btnAddProductItemDelete.setEnabled(true);
            product = (Product) intent.getSerializableExtra("ProductItem");
            tvAddProductItemProductId.setText(product.getProductId()+"");
            etAddProductItemProdName.setText(product.getProdName());

        }
        else
        {
            btnAddProductItemDelete.setEnabled(false);
            product=new Product();
            tvAddProductItemProductId.setText("*");
            etAddProductItemProdName.setText("");


        }


        btnAddProductItemCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProductItemActivity.class);
                startActivity(intent);

            }
        });

        btnAddProductItemSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode.equals("update"))
                {
                    product.setProdName(etAddProductItemProdName.getText()+"");
                    if(dataSource.updateProductItem(product))
                    {
                        Toast.makeText(getApplicationContext(), " Product Item Updated!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), ProductItemActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), " Product Item Update Failed!", Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    product.setProdName(etAddProductItemProdName.getText()+"");
                    if(dataSource.insertProductItem(product))
                    {
                        Toast.makeText(getApplicationContext(), " Product Item Inserted!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), ProductItemActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), " Product Item Insertion Failed!", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        btnAddProductItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.deleteProductItem(product);
                Intent intent = new Intent(getApplicationContext(), ProductItemActivity.class);
                startActivity(intent);
            }
        });




        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");



        switch (basicColor){
            case "White":
                clAddProductItem.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddProductItem.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddProductItem.setBackgroundColor(Color.GREEN);
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
        clAddProductItem= findViewById(R.id.clAddProductItem);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clAddProductItem.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddProductItem.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddProductItem.setBackgroundColor(Color.GREEN);
                break;
        }
    }

}