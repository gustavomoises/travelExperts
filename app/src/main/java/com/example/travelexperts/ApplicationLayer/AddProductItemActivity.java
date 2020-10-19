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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.travelexperts.BusinessLayer.Product;
import com.example.travelexperts.BusinessLayer.Supplier;
import com.example.travelexperts.DatabaseLayer.DataSource;
import com.example.travelexperts.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;

public class AddProductItemActivity extends AppCompatActivity {
    SharedPreferences prefs;
    ConstraintLayout clAddProductItem;
    Button btnAddProductItemCancel,btnAddProductItemSave, btnAddProductItemDelete;
    String mode;
    Product product;
    TextView tvAddProductItemProductId;
    EditText etAddProductItemProdName;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_item);
        requestQueue = Volley.newRequestQueue(this);
        //Set background color form Settings
        clAddProductItem= findViewById(R.id.clAddProductItem);
        btnAddProductItemSave=findViewById(R.id.btnAddProductItemSave);
        btnAddProductItemCancel=findViewById(R.id.btnAddProductItemCancel);
        btnAddProductItemDelete=findViewById(R.id.btnAddProductItemDelete);
        tvAddProductItemProductId=findViewById(R.id.tvAddProductItemProductId);
        etAddProductItemProdName=findViewById(R.id.etAddProductItemProdName);

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
                product.setProdName(etAddProductItemProdName.getText()+"");
                if (mode.equals("update"))
                    Executors.newSingleThreadExecutor().execute(new AddProductItemActivity.PostProduct(product));
                else
                    Executors.newSingleThreadExecutor().execute(new AddProductItemActivity.PutProduct(product));

            }
        });

        btnAddProductItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new AddProductItemActivity.DeleteProduct(product.getProductId()));
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
    class PostProduct implements Runnable {
        private Product product;

        public PostProduct(Product product) {
            this.product = product;
        }

        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://192.168.1.64:8080/JSPDay3RESTExample/rs/product/postproduct";
            JSONObject obj = new JSONObject();
            try {
                obj.put("ProductId", product.getProductId()+ "");
                obj.put("ProdName", product.getProdName() + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d("harv", "response=" + response);
                            VolleyLog.wtf(response.toString(), "utf-8");

                            //display result message
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                                        if(response.getString("message").equals("Product updated successfully"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(), ProductItemActivity.class);
                                            startActivity(intent);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("harv", "error=" + error);
                            VolleyLog.wtf(error.getMessage(), "utf-8");
                        }
                    });

            requestQueue.add(jsonObjectRequest);
        }
    }

    class PutProduct implements Runnable {
        private Product product;

        public PutProduct(Product product) {
            this.product = product;
        }

        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://192.168.1.64:8080/JSPDay3RESTExample/rs/product/putproduct";
            JSONObject obj = new JSONObject();
            try {
                obj.put("ProdName", product.getProdName()+ "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d("harv", "response=" + response);
                            VolleyLog.wtf(response.toString(), "utf-8");

                            //display result message
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                                        if(response.getString("message").equals("Product inserted successfully"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(),ProductItemActivity.class);
                                            startActivity(intent);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("harv", "error=" + error);
                            VolleyLog.wtf(error.getMessage(), "utf-8");
                        }
                    });

            requestQueue.add(jsonObjectRequest);
        }
    }

    class DeleteProduct implements Runnable {
        private int productId;

        public DeleteProduct(int productId) {
            this.productId = productId;
    }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://192.168.1.64:8080/JSPDay3RESTExample/rs/product/deleteproduct/" + productId;
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    VolleyLog.wtf(response, "utf-8");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            if(response.equals("Product Deleted Successfully"))
                            {
                                Intent intent = new Intent(getApplicationContext(), ProductItemActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.wtf(error.getMessage(), "utf-8");
                }
            });

            requestQueue.add(stringRequest);
        }
    }

}