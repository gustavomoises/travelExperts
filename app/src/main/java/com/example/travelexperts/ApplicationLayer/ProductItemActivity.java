//Author: Gustavo Lourenco Moises
//Thread Project - Group 1
//OOSD Program Spring 2020
//Date:9/30/2020
//Travel Agency Application
//
package com.example.travelexperts.ApplicationLayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.travelexperts.BusinessLayer.Product;
import com.example.travelexperts.R;
import java.util.concurrent.Executors;

public class ProductItemActivity extends AppCompatActivity {
    SharedPreferences prefs;
    ConstraintLayout clProductItem;
    ListView lvListProductItem;
    Button btnAddProductItem;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_item);
        requestQueue = Volley.newRequestQueue(this);
        //Set background color form Settings
        clProductItem= findViewById(R.id.clProductItem);
        btnAddProductItem=findViewById(R.id.btnAddProductItem);
        lvListProductItem=findViewById(R.id.lvListProductItem);

        Executors.newSingleThreadExecutor().execute(new ProductItemActivity.GetProductItems());



        btnAddProductItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddProductItemActivity.class);
                intent.putExtra("mode","insert");
                startActivity(intent);
            }
        });

        lvListProductItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product= (Product) lvListProductItem.getAdapter().getItem(position);
                Intent intent = new Intent(getApplicationContext(), AddProductItemActivity.class);
                intent.putExtra("mode","update");
                intent.putExtra("ProductItem",product);
                startActivity(intent);
            }
        });

        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clProductItem.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clProductItem.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clProductItem.setBackgroundColor(Color.GREEN);
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
        clProductItem= findViewById(R.id.clProductItem);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clProductItem.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clProductItem.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clProductItem.setBackgroundColor(Color.GREEN);
                break;
        }
    }
    class GetProductItems implements Runnable {
        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://10.0.1.33:8081/JSPDay3RESTExample/rs/product/getproducts";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of Agents
                    ArrayAdapter<Product> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject agt = jsonArray.getJSONObject(i);
                            Product product = new Product(agt.getInt("ProductId"), agt.getString("ProdName"));
                            adapter.add(product);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //update ListView with the adapter of Agents
                    final ArrayAdapter<Product> finalAdapter = adapter;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lvListProductItem.setAdapter(finalAdapter);
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