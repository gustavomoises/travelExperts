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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.travelexperts.BusinessLayer.TripType;
import com.example.travelexperts.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executors;

public class AddTripTypeActivity extends AppCompatActivity {
    SharedPreferences prefs;
    ConstraintLayout clAddSTrip;
    Button btnAddTripTypeCancel,btnAddTripTypeSave, btnAddTripTypeDelete;
    String mode;
    TripType tripType;
    EditText etAddTripTypeTripTypeName, etAddTripTypeTripTypeId;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        requestQueue = Volley.newRequestQueue(this);
        //Set background color form Settings
        clAddSTrip= findViewById(R.id.clAddTrip);
        btnAddTripTypeDelete=findViewById(R.id.btnAddTripTypeDelete);
        btnAddTripTypeSave=findViewById(R.id.btnAddTripTypeSave);
        btnAddTripTypeCancel=findViewById(R.id.btnAddTripTypeCancel);
        etAddTripTypeTripTypeId=findViewById(R.id.etAddTripTypeTripId);
        etAddTripTypeTripTypeName=findViewById(R.id.etAddTripTypeTripTypeName);

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        if (mode.equals("update")) {
            btnAddTripTypeDelete.setEnabled(true);
            tripType = (TripType) intent.getSerializableExtra("TripType");
            etAddTripTypeTripTypeName.setText(tripType.gettTName()+"");
            etAddTripTypeTripTypeId.setText(tripType.getTripTypeId()+"");

        }
        else
        {
            btnAddTripTypeDelete.setEnabled(false);
            tripType=new TripType();
            etAddTripTypeTripTypeName.setText("");
            etAddTripTypeTripTypeId.setText("");
        }


        btnAddTripTypeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TripTypeActivity.class);
                startActivity(intent);

            }
        });

        btnAddTripTypeSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeId=etAddTripTypeTripTypeId.getText()+"";
                if (typeId.length()==0)
                    Toast.makeText(getApplicationContext(), " One character is required for the trip Id", Toast.LENGTH_LONG).show();
                else
                {
                    if (typeId.length()>1)
                        Toast.makeText(getApplicationContext(), " Only character is required for the trip Id", Toast.LENGTH_LONG).show();
                    else {
                        Executors.newSingleThreadExecutor().execute(new AddTripTypeActivity.VerifyTripTypeId(typeId));
                    }
                }
            }
        });

        btnAddTripTypeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new AddTripTypeActivity.DeleteTripType(tripType.getTripTypeId()));
            }
        });

        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");
        switch (basicColor){
            case "White":
                clAddSTrip.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddSTrip.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddSTrip.setBackgroundColor(Color.GREEN);
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
        clAddSTrip= findViewById(R.id.clAddTrip);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clAddSTrip.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddSTrip.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddSTrip.setBackgroundColor(Color.GREEN);
                break;
        }
    }

    class VerifyTripTypeId implements Runnable {
        private String typeId;

        public VerifyTripTypeId(String typeId) {
            this.typeId = typeId;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/triptype/gettriptypes";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of Agents
                    final ArrayList<TripType> tripTypes = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject agt = jsonArray.getJSONObject(i);
                            TripType tripType = new TripType(agt.getString("TripTypeId").charAt(0), agt.getString("TTName"));
                            tripTypes.add(tripType);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //display result message
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ///Verify if Affiliation Id is already in use by another Affiliation
                            boolean exist = false;
                            int k=0;
                            for (TripType tt : tripTypes) {
                                if (mode.equals("update"))
                                {
                                    if (tt.getTripTypeId() == typeId.charAt(0) && tripType.getTripTypeId()!=typeId.charAt(0))
                                        exist = true;
                                }
                                else
                                {
                                    if (tt.getTripTypeId() == typeId.charAt(0))
                                        exist = true;
                                }

                            }
                            if (exist)
                                Toast.makeText(getApplicationContext(), " Character associated to another trip type. Please, choose another character!!!", Toast.LENGTH_LONG).show();
                            else {

                                tripType.settTName(etAddTripTypeTripTypeName.getText() + "");
                                char oldTripTypeId = tripType.getTripTypeId();
                                tripType.setTripTypeId(typeId.charAt(0));


                                if (mode.equals("update")) {
                                    Executors.newSingleThreadExecutor().execute(new AddTripTypeActivity.PostTripType(tripType,oldTripTypeId));
                                }
                                else {
                                    Executors.newSingleThreadExecutor().execute(new AddTripTypeActivity.PutTripType(tripType));
                                }
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

    class PostTripType implements Runnable {
        private TripType tripType;
        private char oldTripTypeId;

        public PostTripType(TripType tripType, char oldTripTypeId) {
            this.tripType = tripType;
            this.oldTripTypeId = oldTripTypeId;
        }

        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/tritype/posttriptype/"+oldTripTypeId;
            JSONObject obj = new JSONObject();
            try {
                obj.put("TripTypeId", tripType.getTripTypeId()+ "");
                obj.put("TTName", tripType.gettTName() + "");
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
                                        if(response.getString("message").equals("Trip Type updated successfully"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(), TripTypeActivity.class);
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

    class PutTripType implements Runnable {
        private TripType tripType;

        public PutTripType(TripType tripType) {
            this.tripType = tripType;
        }

        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/triptype/puttriptype";
            JSONObject obj = new JSONObject();
            try {
                obj.put("TripTypeId", tripType.getTripTypeId() + "");
                obj.put("TTName", tripType.gettTName() + "");
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
                                        if(response.getString("message").equals("Trip Type inserted successfully"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(),TripTypeActivity.class);
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

    class DeleteTripType implements Runnable {
        private char tripTypeId;

        public DeleteTripType(char tripTypeId) {
            this.tripTypeId = tripTypeId;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/triptype/deletetriptype/" + tripTypeId;
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    VolleyLog.wtf(response, "utf-8");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            if(response.equals("Trip Type Deleted Successfully"))
                            {
                                Intent intent = new Intent(getApplicationContext(), TripTypeActivity.class);
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