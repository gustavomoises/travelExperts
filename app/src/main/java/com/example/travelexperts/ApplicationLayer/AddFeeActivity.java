//Author: Lisa Saffel
//Threaded Project - Group 1
//OOSD Program Spring 2020
//Date: October 28, 2020
//Travel Agency Application - Fees
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
import com.example.travelexperts.BusinessLayer.Fee;
import com.example.travelexperts.DatabaseLayer.DataSource;
import com.example.travelexperts.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executors;

public class AddFeeActivity extends AppCompatActivity {
    SharedPreferences prefs;
    ConstraintLayout clAddFee;
    Button btnAddFeeCancel,btnAddFeeSave, btnAddFeeDelete;
//    DataSource dataSource;      // LV ???
    String mode;
    Fee fee;
    EditText etAddFeeFeeName, etAddFeeFeeId,etAddFeeFeeDesc,etAddFeeFeeAmt;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fee);
        requestQueue = Volley.newRequestQueue(this);

        //Set background color form Settings
        clAddFee= findViewById(R.id.clAddFee);
        btnAddFeeCancel=findViewById(R.id.btnAddFeeCancel);
        btnAddFeeDelete=findViewById(R.id.btnAddFeeDelete);
        btnAddFeeSave=findViewById(R.id.btnAddFeeSave);
        etAddFeeFeeId=findViewById(R.id.etAddFeeFeeId);
        etAddFeeFeeName=findViewById(R.id.etAddFeeFeeName);
        etAddFeeFeeAmt=findViewById(R.id.etAddFeeFeeAmt);
        etAddFeeFeeDesc=findViewById(R.id.etAddFeeFeeDesc);
     //   dataSource = new DataSource(this);


        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        assert mode != null;
        if (mode.equals("update")) {
            btnAddFeeDelete.setEnabled(true);
            fee = (Fee) intent.getSerializableExtra("Fee");
            assert fee != null;
            etAddFeeFeeName.setText(fee.getFeeName()==null?"":String.format("%s", fee.getFeeName()));
            etAddFeeFeeId.setText(String.format("%s", fee.getFeeId()));
            etAddFeeFeeDesc.setText(fee.getFeeDesc()==null?"":String.format("%s", fee.getFeeDesc()));
        }
        else
        {
            btnAddFeeDelete.setEnabled(false);
            fee=new Fee();
            etAddFeeFeeName.setText("");
            etAddFeeFeeId.setText("");
            etAddFeeFeeDesc.setText("");
        }
        btnAddFeeCancel.setOnClickListener(new View.OnClickListener() {     // LV imported View
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FeeActivity.class); // LV is this .class correct ?
                startActivity(intent);

            }
        });

        btnAddFeeSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeId=etAddFeeFeeId.getText()+"";
                if (typeId.length()==0)
                    Toast.makeText(getApplicationContext(), " Fee Id is required.", Toast.LENGTH_LONG).show();
                else
                {
                    if (typeId.length()>10)
                        Toast.makeText(getApplicationContext(), " Maximum of 5 characters is required for the Fee Id", Toast.LENGTH_LONG).show();
                    else {
                        Executors.newSingleThreadExecutor().execute(new AddFeeActivity.VerifyFeeId(typeId));
                    }
                }
            }
        });

        btnAddFeeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new AddFeeActivity.DeleteFee(fee.getFeeId()));
            }
        });
//-------------- LV marker



        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");



        switch (basicColor){
            case "White":
                clAddFee.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddFee.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddFee.setBackgroundColor(Color.GREEN);
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
        clAddFee= findViewById(R.id.clAddFee);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clAddFee.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddFee.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddFee.setBackgroundColor(Color.GREEN);
                break;
        }
    }

    // LV new code added here until closing } below

    class VerifyFeeId implements Runnable {
        private String typeId;

        public VerifyFeeId(String typeId) {
            this.typeId = typeId;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/fee/getfees";    // LV URL changed here to fee/getfees. Correct?
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of Agents   // LV Agents ???
                    final ArrayList<Fee> fees = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject agt = jsonArray.getJSONObject(i);   // LV line below - added double feeAmt - correct?
                            Fee fee = new Fee(agt.getString("FeeId"), agt.getString("FeeName"),agt.getDouble("FeeAmt") ,agt.getString("FeeDesc"));
                            fees.add(fee);
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
                            for (Fee r : fees) {
                                if (mode.equals("update"))
                                {
                                    if (r.getFeeId().equals(typeId) && !(fee.getFeeId().equals(typeId)))
                                        exist = true;
                                }
                                else
                                {
                                    if (r.getFeeId().equals(typeId))
                                        exist = true;
                                }

                            }
                            if (exist)
                                Toast.makeText(getApplicationContext(), " Id associated to another fee. Please, choose another Id!!!", Toast.LENGTH_LONG).show();
                            else {
                                fee.setFeeName(etAddFeeFeeName.getText() + "");
                                fee.setFeeDesc(etAddFeeFeeDesc.getText() + "");
                                String oldFeeId=fee.getFeeId();
                                fee.setFeeId(typeId);

                                if (mode.equals("update"))
                                    Executors.newSingleThreadExecutor().execute(new AddFeeActivity.PostFee(fee,oldFeeId));
                                else
                                    Executors.newSingleThreadExecutor().execute(new AddFeeActivity.PutFee(fee));
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


    class PostFee implements Runnable {
        private Fee fee;
        private String oldFeeId;

        public PostFee(Fee fee, String oldFeeId) {
            this.fee = fee;
            this.oldFeeId = oldFeeId;
        }

        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/fee/postfee/"+oldFeeId;   // LV /fee/postfee < correct?
            JSONObject obj = new JSONObject();
            try {
                obj.put("FeeId", fee.getFeeId()+ "");
                obj.put("FeeName", fee.getFeeName() + "");
                obj.put("FeeDesc", fee.getFeeDesc() + "");
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
                                        if(response.getString("message").equals("Fee updated successfully"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(), FeeActivity.class);   // LV correct ?
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

    class DeleteFee implements Runnable {
        private String feeId;

        public DeleteFee(String feeId) {
            this.feeId = feeId;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/fee/deletefee/" + feeId;
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    VolleyLog.wtf(response, "utf-8");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            if(response.equals("Fee Deleted Successfully"))
                            {
                                Intent intent = new Intent(getApplicationContext(), FeeActivity.class); // LV correct ?
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
    class PutFee implements Runnable {
        private Fee fee;

        public PutFee(Fee fee) {
            this.fee = fee;
        }

        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/fee/putfee";
            JSONObject obj = new JSONObject();
            try {
                obj.put("FeeId", fee.getFeeId() + "");
                obj.put("FeeName", fee.getFeeName() + "");
                obj.put("FeeAmt", fee.getFeeAmt() + "");     // LV I added this line
                obj.put("FeeDesc", fee.getFeeDesc() + "");
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
                                        if(response.getString("message").equals("Fee inserted successfully"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(),FeeActivity.class);  // LV correct?
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
                            Log.d("harv", "error=" + error);        // LV I do have a harv user
                            VolleyLog.wtf(error.getMessage(), "utf-8");
                        }
                    });

            requestQueue.add(jsonObjectRequest);
        }
    }

}