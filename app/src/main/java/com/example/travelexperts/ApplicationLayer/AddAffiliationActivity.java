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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import com.example.travelexperts.BusinessLayer.Affiliation;
import com.example.travelexperts.R;

public class AddAffiliationActivity extends AppCompatActivity {
    //local Variables
    SharedPreferences prefs;
    ConstraintLayout clAddAffiliation;
    Button btnAddAffiliationCancel,btnAddAffiliationSave, btnAddAffiliationDelete;
    String mode;
    Affiliation affiliation;
    EditText etAddAffiliationAffiliationId, etAddAffiliationAffName,etAddAffiliationAffDesc;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_affiliation);
        requestQueue = Volley.newRequestQueue(this);
       //Id Association
        clAddAffiliation= findViewById(R.id.clAddAffiliation);
        btnAddAffiliationSave=findViewById(R.id.btnAddAffiliationSave);
        btnAddAffiliationCancel=findViewById(R.id.btnAddAffiliationCancel);
        btnAddAffiliationDelete=findViewById(R.id.btnAddAffiliationDelete);
        etAddAffiliationAffiliationId=findViewById(R.id.etAddAffiliationAffiliationId);
        etAddAffiliationAffName=findViewById(R.id.etAddAffiliationAffName);
        etAddAffiliationAffDesc=findViewById(R.id.etAddAffiliationAffDesc);

        //Get mode from intent
        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");

        assert mode != null;
        //Verify mode
        if (mode.equals("update")) {
            btnAddAffiliationDelete.setEnabled(true);
            affiliation = (Affiliation) intent.getSerializableExtra("Affiliation");

            assert affiliation != null;
            etAddAffiliationAffName.setText(affiliation.getAffName()==null?"":String.format("%s", affiliation.getAffName()));
            etAddAffiliationAffiliationId.setText(String.format("%s", affiliation.getAffiliationId()));
            etAddAffiliationAffDesc.setText(String.format(affiliation.getAffDesc()==null?"":"%s", affiliation.getAffDesc()));
        }
        else
        {
            btnAddAffiliationDelete.setEnabled(false);
            affiliation=new Affiliation();
            etAddAffiliationAffName.setText("");
            etAddAffiliationAffiliationId.setText("");
            etAddAffiliationAffDesc.setText("");
        }

        //Cancel Button
        btnAddAffiliationCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AffiliationActivity.class);
                startActivity(intent);
            }
        });

        //Add Button
        btnAddAffiliationSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Affiliation Id validation
                String typeId=etAddAffiliationAffiliationId.getText()+"";
                if (typeId.length()==0)
                    Toast.makeText(getApplicationContext(), " Affiliation Id is required.", Toast.LENGTH_LONG).show();
                else
                {
                    if (typeId.length()>10)
                        Toast.makeText(getApplicationContext(), " Affiliation Id cannot have more than 10 characters.", Toast.LENGTH_LONG).show();
                    else {
                        Executors.newSingleThreadExecutor().execute(new VerifyAffiliationId(typeId));
                    }
                }
            }
        });

        //Delete Button
        btnAddAffiliationDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new DeleteAffiliation(affiliation.getAffiliationId()));

            }
        });

        //Set background color from Settings
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");
        switch (basicColor){
            case "White":
                clAddAffiliation.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddAffiliation.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddAffiliation.setBackgroundColor(Color.GREEN);
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
        //Set background color from Settings
        clAddAffiliation= findViewById(R.id.clAddAffiliation);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        assert basicColor != null;
        switch (basicColor){
            case "White":
                clAddAffiliation.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddAffiliation.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddAffiliation.setBackgroundColor(Color.GREEN);
                break;
        }
    }


    class PostAffiliation implements Runnable {
        private Affiliation affiliation;
        private String oldAffiliationId;

        public PostAffiliation(Affiliation affiliation, String oldAffiliationId) {
            this.affiliation = affiliation;
            this.oldAffiliationId = oldAffiliationId;
        }

        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/affiliation/postaffiliation/"+oldAffiliationId;
            JSONObject obj = new JSONObject();
            try {
                obj.put("AffilitationId", affiliation.getAffiliationId()+ "");
                obj.put("AffName", affiliation.getAffName() + "");
                obj.put("AffDesc", affiliation.getAffDesc() + "");
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
                                        if(response.getString("message").equals("Affiliation updated successfully"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(), AffiliationActivity.class);
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

    class DeleteAffiliation implements Runnable {
        private String affiliationId;

        public DeleteAffiliation(String affiliationId) {
            this.affiliationId = affiliationId;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/affiliation/deleteaffiliation/" + affiliationId;
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    VolleyLog.wtf(response, "utf-8");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            if(response.equals("Affiliation Deleted Successfully"))
                            {
                                Intent intent = new Intent(getApplicationContext(), AffiliationActivity.class);
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
    class PutAffiliation implements Runnable {
        private Affiliation affiliation;

        public PutAffiliation(Affiliation affiliation) {
            this.affiliation = affiliation;
        }

        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/affiliation/puttaffiliation";
            JSONObject obj = new JSONObject();
            try {
                obj.put("AffilitationId", affiliation.getAffiliationId() + "");
                obj.put("AffName", affiliation.getAffName() + "");
                obj.put("AffDesc", affiliation.getAffDesc() + "");
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
                                        if(response.getString("message").equals("Affiliation inserted successfully"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(),AffiliationActivity.class);
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

    class VerifyAffiliationId implements Runnable {
        private String typeId;

        public VerifyAffiliationId(String typeId) {
            this.typeId = typeId;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/affiliation/getaffiliations";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of Agents
                    final ArrayList<Affiliation> affiliations = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject agt = jsonArray.getJSONObject(i);
                            Affiliation affiliation = new Affiliation(agt.getString("AffilitationId"), agt.getString("AffName"),
                                    agt.getString("AffDesc"));
                            affiliations.add(affiliation);
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
                            for (Affiliation r : affiliations) {
                                if (mode.equals("update"))
                                {
                                    if (r.getAffiliationId().equals(typeId) && !(affiliation.getAffiliationId().equals(typeId)))
                                        exist = true;
                                }
                                else
                                {
                                    if (r.getAffiliationId().equals(typeId))
                                        exist = true;
                                }
                            }
                                if (exist)
                                    Toast.makeText(getApplicationContext(), " The selected Id is associated to another affiliation. Please, choose another one!!!", Toast.LENGTH_LONG).show();
                                else {
                                    affiliation.setAffName(etAddAffiliationAffName.getText() + "");
                                    String oldAffiliationId = affiliation.getAffiliationId();
                                    affiliation.setAffiliationId(typeId);
                                    affiliation.setAffDesc(etAddAffiliationAffDesc.getText() + "");

                                    if (mode.equals("update")) {
                                        Executors.newSingleThreadExecutor().execute(new PostAffiliation(affiliation,oldAffiliationId));
                                    }
                                    else {
                                        Executors.newSingleThreadExecutor().execute(new PutAffiliation(affiliation));
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
}