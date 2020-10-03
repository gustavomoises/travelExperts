package com.example.travelexperts.ApplicationLayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelexperts.BusinessLayer.Product;
import com.example.travelexperts.DatabaseLayer.DBHelper;
import com.example.travelexperts.R;
import com.google.android.material.snackbar.Snackbar;

public class AddProductActivity extends AppCompatActivity{

    private EditText etProductName;
    private Spinner selectSupplier;
    private ImageButton btnSaveProduct;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        //adding the back button in the activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Add Product");

        dbHelper = DBHelper.getInstance(getApplicationContext());


        etProductName = findViewById(R.id.etProductName);
        selectSupplier = findViewById(R.id.spinnerSelectSupplier);
        btnSaveProduct = findViewById(R.id.btnSaveProduct);

        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = new Product();

                if (!etProductName.getText().toString().isEmpty()){
                    product.ProdName = etProductName.getText().toString();
                } else {
                    product.ProdName = "";
                }

                dbHelper.insertProduct(product);

                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Product Added", Snackbar.LENGTH_SHORT);
                snackbar.show();

                Intent intent=new Intent(AddProductActivity.this, ProductActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }
}