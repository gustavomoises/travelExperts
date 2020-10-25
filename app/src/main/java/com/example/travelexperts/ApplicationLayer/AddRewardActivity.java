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

import org.json.JSONException;
import org.json.JSONObject;

import com.example.travelexperts.BusinessLayer.Reward;
import com.example.travelexperts.R;

import java.util.concurrent.Executors;

public class AddRewardActivity extends AppCompatActivity {
    //Local Variables
    SharedPreferences prefs;
    ConstraintLayout clAddReward;
    Button btnAddRewardCancel,btnAddRewardSave, btnAddRewardDelete;
    String mode;
    Reward reward;
    TextView tvAddRewardId;
    EditText etAddRwdName,etAddRwdDesc;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reward);
        //Service Request Queue
        requestQueue = Volley.newRequestQueue(this);
        btnAddRewardSave=findViewById(R.id.btnAddRewardSave);
        btnAddRewardCancel=findViewById(R.id.btnAddRewardCancel);
        btnAddRewardDelete=findViewById(R.id.btnAddRewardDelete);
        tvAddRewardId=findViewById(R.id.tvAddRewardId);
        etAddRwdDesc=findViewById(R.id.etAddRewardRwdDesc);
        etAddRwdName=findViewById(R.id.etAddRewardRwdName);

        Intent intent = getIntent();
        //Verify if insert or update
        mode = intent.getStringExtra("mode");
        assert mode != null;
        if (mode.equals("update")) {
            //Set fields and enable the Delete button
            btnAddRewardDelete.setEnabled(true);
            reward = (Reward) intent.getSerializableExtra("Reward");
            assert reward != null;
            etAddRwdName.setText(reward.getRwdName()==null?"":String.format("%s", reward.getRwdName()));
            tvAddRewardId.setText(String.format("%s", reward.getRewardId()));
            etAddRwdDesc.setText(reward.getRwdDesc()==null?"":String.format("%s", reward.getRwdDesc()));
        }
        else
        {
            //Empty all fields and disable the Delete button
            btnAddRewardDelete.setEnabled(false);
            //new reward
            reward=new Reward();
            etAddRwdName.setText("");
            tvAddRewardId.setText("*");
            etAddRwdDesc.setText("");
        }
        //Go back to the previous page - Reward Activity
        btnAddRewardCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RewardActivity.class);
                startActivity(intent);
            }
        });

        //Save Button
        btnAddRewardSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Set reward attributes
                reward.setRwdName(etAddRwdName.getText() + "");
                reward.setRwdDesc(etAddRwdDesc.getText() + "");
                if (mode.equals("update"))
                    Executors.newSingleThreadExecutor().execute(new AddRewardActivity.PostReward(reward));
                else
                    Executors.newSingleThreadExecutor().execute(new AddRewardActivity.NewReward(reward));
            }
        });

        //Delete Button
        btnAddRewardDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new AddRewardActivity.DeleteReward(reward.getRewardId()));
            }
        });

        //Set background color form Settings
        clAddReward= findViewById(R.id.clAddReward);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");
        switch (basicColor){
            case "White":
                clAddReward.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddReward.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddReward.setBackgroundColor(Color.GREEN);
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
        clAddReward= findViewById(R.id.clAddReward);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");
        assert basicColor != null;
        switch (basicColor){
            case "White":
                clAddReward.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddReward.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddReward.setBackgroundColor(Color.GREEN);
                break;
        }
    }

    //Find next reward Id from Database
    class NewReward implements Runnable {
        private Reward reward;

        public NewReward(Reward reward) {
            this.reward = reward;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://192.168.1.81:8080/JSPDay3RESTExample/rs/reward/newreward";
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
                                reward.setRewardId(Integer.parseInt(response));
                                Executors.newSingleThreadExecutor().execute(new AddRewardActivity.PutReward(reward));
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Reward insertion is not possible", Toast.LENGTH_LONG).show();
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

    //Insert Reward
    class PutReward implements Runnable {
        private Reward reward;

        public PutReward(Reward reward) {
            this.reward = reward;
        }
        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://192.168.1.81:8080/JSPDay3RESTExample/rs/reward/putreward";
            JSONObject obj = new JSONObject();
            try {
                obj.put("RewardId", reward.getRewardId() + "");
                obj.put("RwdName", reward.getRwdName() + "");
                obj.put("RwdDesc", reward.getRwdDesc() + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //New Request
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d("harv", "response=" + response);
                            VolleyLog.wtf(response.toString(), "utf-8");

                            //display result message and create intent
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                                        if(response.getString("message").equals("Reward inserted successfully"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(),RewardActivity.class);
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

    // Update Reward
    class PostReward implements Runnable {
        private Reward reward;

        public PostReward(Reward reward) {
            this.reward = reward;
        }

        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://192.168.1.81:8080/JSPDay3RESTExample/rs/reward/postreward";
            //new JSON object
            JSONObject obj = new JSONObject();
            try {
                obj.put("RewardId", reward.getRewardId()+ "");
                obj.put("RwdName", reward.getRwdName() + "");
                obj.put("RwdDesc", reward.getRwdDesc() + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //New Request
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d("harv", "response=" + response);
                            VolleyLog.wtf(response.toString(), "utf-8");
                            //display result message and create intent
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                                        if(response.getString("message").equals("Reward updated successfully"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(),RewardActivity.class);
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

    //Delete Reward
    class DeleteReward implements Runnable {
        private int rewardId;

        public DeleteReward(int rewardId) {
            this.rewardId = rewardId;
        }
        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://192.168.1.81:8080/JSPDay3RESTExample/rs/reward/deletereward/" + rewardId;
            //New Request
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    VolleyLog.wtf(response, "utf-8");
                    //display result message and create intent
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            if(response.equals("Reward Deleted Successfully"))
                            {
                                Intent intent = new Intent(getApplicationContext(), RewardActivity.class);
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