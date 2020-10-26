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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
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
    SharedPreferences prefs;
    Button btnConfirm;
    ConstraintLayout clPackage;
    RequestQueue requestQueue;

    class PutPackage implements Runnable {
        private ProdPackage prodPackage;

        public PutPackage(ProdPackage prodPackage) {
            this.prodPackage = prodPackage;
        }

        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://192.168.1.71:8080/JSPDay3RESTExample/rs/package/putpackage";
            JSONObject obj = new JSONObject();
            try {
                obj.put("packageId", prodPackage.getPackageId() + "");
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details);

        Intent receive = getIntent();

        // receives agent from sending activity
        final ProdPackage prodPackage = (ProdPackage) receive.getSerializableExtra("package");
        // receives mode from sending activity
        String mode = (String) receive.getStringExtra("mode");
        // reference for texts
        etPackageId = findViewById(R.id.etPackageId);
        etCommission = findViewById(R.id.etCommission);
        etBasePrice = findViewById(R.id.etBasePrice);
        etDescription = findViewById(R.id.etDescription);
        etEndDate = findViewById(R.id.etEndDate);
        etName = findViewById(R.id.etName);
        etStartDate = findViewById(R.id.etStartDate);
        btnConfirm = findViewById(R.id.btnConfirm);

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

        if (mode.equals("edit")) {
            etPackageId.setText(prodPackage.getPackageId() + "");
            etCommission.setText(prodPackage.getPkgAgencyCommission() + "");
            etBasePrice.setText(prodPackage.getPkgBasePrice() + "");
            etDescription.setText(prodPackage.getPkgDec());
            etEndDate.setText(prodPackage.getPkgEndDate() + "");
            etName.setText(prodPackage.getPkgName());
            etStartDate.setText(prodPackage.getPkgStartDate() + "");
            btnConfirm.setVisibility(View.INVISIBLE);
        }
        else
            btnConfirm.setVisibility(View.VISIBLE);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    ProdPackage prodPackage = new ProdPackage(
                            Integer.parseInt(etPackageId.getText().toString()),
                            etName.getText().toString(),
                            sdf.parse(etStartDate.getText().toString()),
                            sdf.parse(etEndDate.getText().toString()),
                            etDescription.getText().toString(),
                            Double.parseDouble(etBasePrice.getText().toString()),
                            Double.parseDouble(etCommission.getText().toString())
                    );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Executors.newSingleThreadExecutor().execute(new PackageDetailsActivity.PutPackage(prodPackage));
            }
        });
    }
}