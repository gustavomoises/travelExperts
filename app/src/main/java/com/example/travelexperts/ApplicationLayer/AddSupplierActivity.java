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
import com.example.travelexperts.BusinessLayer.Supplier;
import com.example.travelexperts.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;

public class AddSupplierActivity extends AppCompatActivity {
    SharedPreferences prefs;
    ConstraintLayout clAddSupplier;
    Button btnAddSupplierCancel,btnAddSupplierSave, btnAddSupplierDelete;
    String mode;
    Supplier supplier;
    TextView tvAddSupplierSupplierId;
    EditText etAddSupplierSupName;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supplier);
        requestQueue = Volley.newRequestQueue(this);
        //Set background color form Settings
        clAddSupplier= findViewById(R.id.clAddSupplier);
        btnAddSupplierSave=findViewById(R.id.btnAddSupplierSave);
        btnAddSupplierCancel=findViewById(R.id.btnAddSupplierCancel);
        btnAddSupplierDelete=findViewById(R.id.btnAddSupplierDelete);
        tvAddSupplierSupplierId=findViewById(R.id.tvAddSupplierSupplierId);
        etAddSupplierSupName=findViewById(R.id.etAddSupplierSupName);

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
                supplier.setSupName(etAddSupplierSupName.getText()+"");
                if (mode.equals("update"))
                    Executors.newSingleThreadExecutor().execute(new AddSupplierActivity.PostSupplier(supplier));
                else
                    Executors.newSingleThreadExecutor().execute(new AddSupplierActivity.NewSupplier(supplier));
            }
        });

        btnAddSupplierDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new AddSupplierActivity.DeleteSupplier(supplier.getSupplierId()));
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

    //Find next supplier Id from Database
    class NewSupplier implements Runnable {
        private Supplier supplier;

        public NewSupplier(Supplier supplier) {
            this.supplier = supplier;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/supplier/newsupplier";
            //New Request
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    VolleyLog.wtf(response, "utf-8");
                    //display result message and execute the reward insertion
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!response.equals(""))
                            {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                //Set Reward Id
                                supplier.setSupplierId(Integer.parseInt(response));
                                Executors.newSingleThreadExecutor().execute(new AddSupplierActivity.PutSupplier(supplier));
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Supplier insertion is not possible", Toast.LENGTH_LONG).show();
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


    class PostSupplier implements Runnable {
        private Supplier supplier;

        public PostSupplier(Supplier supplier) {
            this.supplier = supplier;
        }

        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/supplier/postsupplier";
            JSONObject obj = new JSONObject();
            try {
                obj.put("SupplierId", supplier.getSupplierId()+ "");
                obj.put("SupName", supplier.getSupName() + "");
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
                                        if(response.getString("message").equals("Supplier updated successfully"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(), SupplierActivity.class);
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

    class PutSupplier implements Runnable {
        private Supplier supplier;

        public PutSupplier(Supplier supplier) {
            this.supplier = supplier;
        }

        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/supplier/putsupplier";
            JSONObject obj = new JSONObject();
            try {
                obj.put("SupplierId", supplier.getSupplierId() + "");
                obj.put("SupName", supplier.getSupName() + "");
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
                                        if(response.getString("message").equals("Supplier inserted successfully"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(),SupplierActivity.class);
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

    class DeleteSupplier implements Runnable {
        private int supplierId;

        public DeleteSupplier(int supplierId) {
            this.supplierId = supplierId;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/supplier/deletesupplier/" + supplierId;
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    VolleyLog.wtf(response, "utf-8");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            if(response.equals("Supplier Deleted Successfully"))
                            {
                                Intent intent = new Intent(getApplicationContext(), SupplierActivity.class);
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