package com.example.travelexperts.ApplicationLayer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.travelexperts.BusinessLayer.Listener;
import com.example.travelexperts.BusinessLayer.ProductAdapter;
import com.example.travelexperts.BusinessLayer.RecyclerViewData;
import com.example.travelexperts.DatabaseLayer.DBHelper;
import com.example.travelexperts.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 * Author: Suvanjan Shrestha
 * Date: 02/10/2020
 * TravelExperts Android App
 */

public class ProductActivity extends AppCompatActivity implements Listener {
    SharedPreferences prefs;
    RelativeLayout rlProduct;
    FloatingActionButton fab;
    List<RecyclerViewData> dataList;
    RecyclerView mList;
    ProductAdapter adapter;
    DividerItemDecoration dividerItemDecoration;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        setTitle("Products");

        fab = findViewById(R.id.fab);
        mList = findViewById(R.id.recyclerView);
        dataList = new ArrayList<>();
        adapter = new ProductAdapter(getApplicationContext(), dataList);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        //On button Click
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        //get data for recycler view
        getData();

        //Set background color form Settings
        rlProduct = findViewById(R.id.rlProduct);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                rlProduct.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                rlProduct.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                rlProduct.setBackgroundColor(Color.GREEN);
                break;

        }
    }

    //Load Application Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.travelexpertsmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Restart the activity and data
    @Override
    protected void onRestart() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
        super.onRestart();
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
        rlProduct = findViewById(R.id.rlProduct);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                rlProduct.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                rlProduct.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                rlProduct.setBackgroundColor(Color.GREEN);
                break;
        }
    }


    //fetch data from server
    private void getData() {
        String url = "http://192.168.0.22:8081/JSPDay3RESTExample/rs/getproductsandsuppliers";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        RecyclerViewData recyclerViewData = new RecyclerViewData();
                        recyclerViewData.setProductSupplierId(jsonObject.getInt("ProductSupplierId"));
                        recyclerViewData.setProdName(jsonObject.getString("ProdName"));
                        recyclerViewData.setSupName(jsonObject.getString("SupName"));

                        dataList.add(recyclerViewData);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());

            }

        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    //Intent to go to next activity
    public void addItem(){
        Intent intent = new Intent(this, AddProductActivity.class);
        startActivity(intent);
    }


    @Override
    public void nameToChange(String name) {
    }
}