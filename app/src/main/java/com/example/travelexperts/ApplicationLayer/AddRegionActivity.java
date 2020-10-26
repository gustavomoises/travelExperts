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
import com.example.travelexperts.BusinessLayer.Region;
import com.example.travelexperts.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executors;

public class AddRegionActivity extends AppCompatActivity {
    SharedPreferences prefs;
    ConstraintLayout clAddRegion;
    Button btnAddRegionCancel,btnAddRegionSave, btnAddRegionDelete;
    String mode;
    Region region;
    EditText etAddRegionRegionName, etAddRegionRegionId;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_region);
        requestQueue = Volley.newRequestQueue(this);
        //Set background color form Settings
        clAddRegion= findViewById(R.id.clAddRegion);
        btnAddRegionCancel=findViewById(R.id.btnAddRegionCancel);
        btnAddRegionDelete=findViewById(R.id.btnAddRegionDelete);
        btnAddRegionSave=findViewById(R.id.btnAddRegionSave);
        etAddRegionRegionId=findViewById(R.id.etAddRegionRegionId);
        etAddRegionRegionName=findViewById(R.id.etAddRegionRegionName);

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        if (mode.equals("update")) {
            btnAddRegionDelete.setEnabled(true);
            region = (Region) intent.getSerializableExtra("Region");
            etAddRegionRegionName.setText(region.getRegionName()+"");
            etAddRegionRegionId.setText(region.getRegionId()+"");

        }
        else
        {
            btnAddRegionDelete.setEnabled(false);
            region=new Region();
            etAddRegionRegionName.setText("");
            etAddRegionRegionId.setText("");
        }
        btnAddRegionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegionActivity.class);
                startActivity(intent);

            }
        });

        btnAddRegionSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeId=etAddRegionRegionId.getText()+"";
                if (typeId.length()==0)
                    Toast.makeText(getApplicationContext(), " Region Id is required.", Toast.LENGTH_LONG).show();
                else
                {
                    if (typeId.length()>5)
                        Toast.makeText(getApplicationContext(), " Maximum of 5 characters is required for the trip Id", Toast.LENGTH_LONG).show();
                    else
                        Executors.newSingleThreadExecutor().execute(new AddRegionActivity.VerifyRegionId(typeId));
                }
            }
        });

        btnAddRegionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new AddRegionActivity.DeleteRegion(region.getRegionId()));
            }
        });



        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");
        switch (basicColor){
            case "White":
                clAddRegion.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddRegion.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddRegion.setBackgroundColor(Color.GREEN);
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
        clAddRegion= findViewById(R.id.clAddRegion);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clAddRegion.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddRegion.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddRegion.setBackgroundColor(Color.GREEN);
                break;
        }
    }


    class VerifyRegionId implements Runnable {
        private String typeId;

        public VerifyRegionId(String typeId) {
            this.typeId = typeId;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/region/getregions";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of Agents
                    final ArrayList<Region> regions = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject agt = jsonArray.getJSONObject(i);
                            Region region = new Region(agt.getString("RegionId"), agt.getString("RegionName"));
                            regions.add(region);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //display result message
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            boolean exist = false;
                            int k=0;
                            for (Region r : regions) {
                                if (mode.equals("update"))
                                {
                                    if (r.getRegionId().equals(typeId) && !(region.getRegionId().equals(typeId)))
                                        exist = true;
                                }
                                else
                                {
                                    if (r.getRegionId().equals(typeId))
                                        exist = true;
                                }

                            }
                            if (exist)
                                Toast.makeText(getApplicationContext(), " Id associated to another region. Please, choose another Id!!!", Toast.LENGTH_LONG).show();
                            else
                            {
                                region.setRegionName(etAddRegionRegionName.getText() + "");
                                String oldRegionId=region.getRegionId();
                                region.setRegionId(typeId);

                                if (mode.equals("update"))
                                    Executors.newSingleThreadExecutor().execute(new AddRegionActivity.PostRegion(region,oldRegionId));
                                else
                                    Executors.newSingleThreadExecutor().execute(new AddRegionActivity.PutRegion(region));
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


    class PostRegion implements Runnable {
        private Region region;
        private String oldRegionId;

        public PostRegion(Region region, String oldRegionId) {
            this.region = region;
            this.oldRegionId = oldRegionId;
        }

        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/region/postregion/"+oldRegionId;
            JSONObject obj = new JSONObject();
            try {
                obj.put("RegionId", region.getRegionId()+ "");
                obj.put("RegionName", region.getRegionName() + "");
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
                                        if(response.getString("message").equals("Region updated successfully"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(), RegionActivity.class);
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

    class DeleteRegion implements Runnable {
        private String regionId;

        public DeleteRegion(String regionId) {
            this.regionId = regionId;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/region/deleteregion/" + regionId;
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    VolleyLog.wtf(response, "utf-8");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            if(response.equals("Region Deleted Successfully"))
                            {
                                Intent intent = new Intent(getApplicationContext(), RegionActivity.class);
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
    class PutRegion implements Runnable {
        private Region region;

        public PutRegion(Region region) {
            this.region = region;
        }

        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/region/putregion";
            JSONObject obj = new JSONObject();
            try {
                obj.put("RegionId", region.getRegionId() + "");
                obj.put("RegionName", region.getRegionName() + "");
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
                                        if(response.getString("message").equals("Region inserted successfully"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(),RegionActivity.class);
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
}