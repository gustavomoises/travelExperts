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
import com.example.travelexperts.BusinessLayer.BookClass;
import com.example.travelexperts.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executors;

public class AddClassActivity extends AppCompatActivity {
    SharedPreferences prefs;
    ConstraintLayout clAddClass;
    Button btnAddClassCancel,btnAddClassSave, btnAddClassDelete;
    String mode;
    BookClass bookClass;
    EditText etAddClassClassName, etAddClassClassId,etAddCLassClassDesc;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        requestQueue = Volley.newRequestQueue(this);
        //Set background color form Settings
        clAddClass= findViewById(R.id.clAddClass);
        btnAddClassSave=findViewById(R.id.btnAddClassSave);
        btnAddClassCancel=findViewById(R.id.btnAddClassCancel);
        btnAddClassDelete=findViewById(R.id.btnAddClassDelete);
        etAddCLassClassDesc=findViewById(R.id.etAddClassClassDesc);
        etAddClassClassId=findViewById(R.id.etAddClassClassId);
        etAddClassClassName=findViewById(R.id.etAddCLassClassName);


        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        assert mode != null;
        if (mode.equals("update")) {
            btnAddClassDelete.setEnabled(true);
            bookClass = (BookClass) intent.getSerializableExtra("Class");
            assert bookClass != null;
            etAddClassClassName.setText(bookClass.getClassName()==null?"":String.format("%s", bookClass.getClassName()));
            etAddClassClassId.setText(String.format("%s", bookClass.getClassId()));
            etAddCLassClassDesc.setText(bookClass.getClassDes()==null?"":String.format("%s", bookClass.getClassDes()));
        }
        else
        {
            btnAddClassDelete.setEnabled(false);
            bookClass=new BookClass();
            etAddClassClassName.setText("");
            etAddClassClassId.setText("");
            etAddCLassClassDesc.setText("");
        }
        btnAddClassCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClassActivity.class);
                startActivity(intent);

            }
        });

        btnAddClassSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeId=etAddClassClassId.getText()+"";
                if (typeId.length()==0)
                    Toast.makeText(getApplicationContext(), " Class Id is required.", Toast.LENGTH_LONG).show();
                else
                {
                    if (typeId.length()>5)
                        Toast.makeText(getApplicationContext(), " Maximum of 5 characters is required for the Class Id", Toast.LENGTH_LONG).show();
                    else {
                        Executors.newSingleThreadExecutor().execute(new AddClassActivity.VerifyBookClassId(typeId));
                    }
                }
            }
        });

        btnAddClassDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new AddClassActivity.DeleteBookClass(bookClass.getClassId()));
            }
        });

        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clAddClass.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddClass.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddClass.setBackgroundColor(Color.GREEN);
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
        clAddClass= findViewById(R.id.clAddClass);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        assert basicColor != null;
        switch (basicColor){
            case "White":
                clAddClass.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddClass.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddClass.setBackgroundColor(Color.GREEN);
                break;
        }
    }

    class VerifyBookClassId implements Runnable {
        private String typeId;

        public VerifyBookClassId(String typeId) {
            this.typeId = typeId;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/class/getclasses";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of Agents
                    final ArrayList<BookClass> bookClasses = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject agt = jsonArray.getJSONObject(i);
                            BookClass bookClass = new BookClass(agt.getString("ClassId"), agt.getString("ClassName"),agt.getString("ClassDesc"));
                            bookClasses.add(bookClass);
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
                            for (BookClass r : bookClasses) {
                                if (mode.equals("update"))
                                {
                                    if (r.getClassId().equals(typeId) && !(bookClass.getClassId().equals(typeId)))
                                        exist = true;
                                }
                                else
                                {
                                    if (r.getClassId().equals(typeId))
                                        exist = true;
                                }

                            }
                            if (exist)
                                Toast.makeText(getApplicationContext(), " Id associated to another class. Please, choose another Id!!!", Toast.LENGTH_LONG).show();
                            else {
                                bookClass.setClassName(etAddClassClassName.getText() + "");
                                bookClass.setClassDes(etAddCLassClassDesc.getText() + "");
                                String oldBookClassId=bookClass.getClassId();
                                bookClass.setClassId(typeId);

                                if (mode.equals("update"))
                                    Executors.newSingleThreadExecutor().execute(new AddClassActivity.PostBookClass(bookClass,oldBookClassId));
                                else
                                    Executors.newSingleThreadExecutor().execute(new AddClassActivity.PutBookClass(bookClass));
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


    class PostBookClass implements Runnable {
        private BookClass bookClass;
        private String oldBookClassId;

        public PostBookClass(BookClass bookClass, String oldBookClassId) {
            this.bookClass = bookClass;
            this.oldBookClassId = oldBookClassId;
        }

        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/class/postclass/"+oldBookClassId;
            JSONObject obj = new JSONObject();
            try {
                obj.put("ClassId", bookClass.getClassId()+ "");
                obj.put("ClassName", bookClass.getClassName() + "");
                obj.put("ClassDesc", bookClass.getClassDes() + "");
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
                                        if(response.getString("message").equals("Class updated successfully"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(), ClassActivity.class);
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

    class DeleteBookClass implements Runnable {
        private String bookClassId;

        public DeleteBookClass(String bookClassId) {
            this.bookClassId = bookClassId;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/class/deleteclass/" + bookClassId;
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    VolleyLog.wtf(response, "utf-8");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            if(response.equals("Class Deleted Successfully"))
                            {
                                Intent intent = new Intent(getApplicationContext(), ClassActivity.class);
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
    class PutBookClass implements Runnable {
        private BookClass bookClass;

        public PutBookClass(BookClass bookClass) {
            this.bookClass = bookClass;
        }

        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/class/putclass";
            JSONObject obj = new JSONObject();
            try {
                obj.put("ClassId", bookClass.getClassId() + "");
                obj.put("ClassName", bookClass.getClassName() + "");
                obj.put("ClassDesc", bookClass.getClassDes() + "");
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
                                        if(response.getString("message").equals("Class inserted successfully"))
                                        {
                                            Intent intent = new Intent(getApplicationContext(),ClassActivity.class);
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