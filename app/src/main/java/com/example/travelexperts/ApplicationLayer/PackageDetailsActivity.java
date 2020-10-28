package com.example.travelexperts.ApplicationLayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.example.travelexperts.BusinessLayer.ProdPackage;
import com.example.travelexperts.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

public class PackageDetailsActivity extends AppCompatActivity {

    EditText etPackageId, etCommission, etBasePrice, etDescription, etEndDate, etName, etStartDate;
    TextView lbId;
    SharedPreferences prefs;
    Button btnConfirm, btnDelete;
    ConstraintLayout clPackage;
    RequestQueue requestQueue;

    class PutPostDeletePackage implements Runnable {
        private ProdPackage prodPackage;
        String mode;

        public PutPostDeletePackage(ProdPackage prodPackage, String mode) {
            this.prodPackage = prodPackage; this.mode = mode;
        }

        @Override
        public void run() {
            if (mode.equals("add")) {
                //send JSON data to REST service for insert
                String url = "http://192.168.1.71:8080/JSPDay3RESTExample/rs/package/putpackage";
                JSONObject obj = new JSONObject();
                try {
                    obj.put("packName", prodPackage.getPkgName() + "");
                    obj.put("packStartDate", prodPackage.getPkgStartDate() + "");
                    obj.put("packEndDate", prodPackage.getPkgEndDate() + "");
                    obj.put("packDesc", prodPackage.getPkgDec() + "");
                    obj.put("packPrice", prodPackage.getPkgBasePrice() + "");
                    obj.put("packCommission", prodPackage.getPkgAgencyCommission() + "");
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
            else if (mode.equals("delete"))
            {
                String packageId = etPackageId.getText().toString();
                StringBuffer buffer = new StringBuffer();
                String url = "http://192.168.1.71:8080/JSPDay3RESTExample/rs/package/deletepackage/" + packageId;
                StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        VolleyLog.wtf(response, "utf-8");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
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
            else
            {
                //send JSON data to REST service for update
                String url = "http://192.168.1.71:8080/JSPDay3RESTExample/rs/package/postpackage";
                JSONObject obj = new JSONObject();
                try {
                    obj.put("packageId", prodPackage.getPackageId());
                    obj.put("packName", prodPackage.getPkgName() + "");
                    obj.put("packStartDate", prodPackage.getPkgStartDate() + "");
                    obj.put("packEndDate", prodPackage.getPkgEndDate() + "");
                    obj.put("packDesc", prodPackage.getPkgDec() + "");
                    obj.put("packPrice", prodPackage.getPkgBasePrice() + "");
                    obj.put("packCommission", prodPackage.getPkgAgencyCommission() + "");
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details);
        requestQueue = Volley.newRequestQueue(this);

        Intent receive = getIntent();

        // receives agent from sending activity
        final ProdPackage prodPackage = (ProdPackage) receive.getSerializableExtra("package");
        // receives mode from sending activity
        final String mode = (String) receive.getStringExtra("mode");
        // reference for texts
        etPackageId = findViewById(R.id.etPackageId);
        etCommission = findViewById(R.id.etCommission);
        etBasePrice = findViewById(R.id.etBasePrice);
        etDescription = findViewById(R.id.etDescription);
        etEndDate = findViewById(R.id.etEndDate);
        etName = findViewById(R.id.etName);
        etStartDate = findViewById(R.id.etStartDate);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnDelete = findViewById(R.id.btnDelete);
        lbId = findViewById(R.id.lbId);


        //Set background color form Settings
        clPackage= findViewById(R.id.clPackage);
        prefs = getSharedPreferences("myprefs",Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clPackage.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clPackage.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clPackage.setBackgroundColor(Color.GREEN);
                break;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDateStr = null;
        String endDateStr = null;

        try {
            // convert from provided format into date variable
            Date startDate = new Date(prodPackage.getPkgStartDate() + "");
            // convert into fromatted string
            startDateStr = sdf.format(startDate);
            Date endDate = new Date(prodPackage.getPkgEndDate() + "");
            endDateStr = sdf.format(endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mode.equals("edit")) {
            // pre-populate fields
            etPackageId.setText(prodPackage.getPackageId() + "");
            etCommission.setText(prodPackage.getPkgAgencyCommission() + "");
            etBasePrice.setText(prodPackage.getPkgBasePrice() + "");
            etDescription.setText(prodPackage.getPkgDec());
            etEndDate.setText(endDateStr);
            etName.setText(prodPackage.getPkgName());
            etStartDate.setText(startDateStr);
            // enable confirm button
            btnConfirm.setVisibility(View.VISIBLE);
            // enable delete button (edit mode includes deletion case)
            btnDelete.setVisibility(View.VISIBLE);
        }
        else {
            // enable confirm button
            btnConfirm.setVisibility(View.VISIBLE);
            // disable delete button
            btnDelete.setVisibility(View.INVISIBLE);
            // disable Id field since populated in database (autoincrement enabled in database)
            etPackageId.setVisibility(View.INVISIBLE);
            lbId.setVisibility(View.INVISIBLE);
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            // button enabled in edit case which includes deletion
            String mode = "delete";
            public void onClick(View v) {
                ProdPackage prodPackage = new ProdPackage(
                        etName.getText().toString(),
                        etStartDate.getText().toString(),
                        etEndDate.getText().toString(),
                        etDescription.getText().toString(),
                        Double.parseDouble(etBasePrice.getText().toString()),
                        Double.parseDouble(etCommission.getText().toString()));

                Executors.newSingleThreadExecutor().execute(new PackageDetailsActivity.PutPostDeletePackage(prodPackage, mode));

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProdPackage prodPackage = null;

                // innocent until proven guilty
                boolean lv_validated = true;

                // blank field validation
                if (etName.getText().toString().equals("") ||
                        etStartDate.getText().toString().equals("") ||
                        etEndDate.getText().toString().equals("") ||
                        etDescription.getText().toString().equals("") ||
                        etBasePrice.getText().toString().equals("") ||
                        etCommission.getText().toString().equals(""))
                {
                        Toast.makeText(getApplicationContext(), "Blank field", Toast.LENGTH_LONG).show();
                        lv_validated = false;
                }

                // numeric validation
                if (lv_validated) {
                    try {
                        double price = Double.parseDouble(etBasePrice.getText().toString());
                        double commission = Double.parseDouble(etCommission.getText().toString());
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Wrong number format", Toast.LENGTH_LONG).show();
                        lv_validated = false;
                    }
                }


                // date validation
                if (lv_validated) {
                    try {
                        java.sql.Date startDate = java.sql.Date.valueOf(etStartDate.getText().toString());
                        java.sql.Date endDate = java.sql.Date.valueOf(etEndDate.getText().toString());
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Wrong date format", Toast.LENGTH_LONG).show();
                        lv_validated = false;
                    }
                }

                if (lv_validated) {
                    // if user input is correct, post/put user entry
                    if (!mode.equals("edit")) {
                        prodPackage = new ProdPackage(
                                etName.getText().toString(),
                                etStartDate.getText().toString(),
                                etEndDate.getText().toString(),
                                etDescription.getText().toString(),
                                Double.parseDouble(etBasePrice.getText().toString()),
                                Double.parseDouble(etCommission.getText().toString())
                        );
                        Executors.newSingleThreadExecutor().execute(new PackageDetailsActivity.PutPostDeletePackage(prodPackage, mode));
                    }
                    else
                    {
                        prodPackage = new ProdPackage(
                                Integer.parseInt(etPackageId.getText().toString()),
                                etName.getText().toString(),
                                etStartDate.getText().toString(),
                                etEndDate.getText().toString(),
                                etDescription.getText().toString(),
                                Double.parseDouble(etBasePrice.getText().toString()),
                                Double.parseDouble(etCommission.getText().toString())
                        );
                        Executors.newSingleThreadExecutor().execute(new PackageDetailsActivity.PutPostDeletePackage(prodPackage, mode));
                    }
                }
            }
        });
    }
}