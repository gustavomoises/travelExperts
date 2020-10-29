package com.example.travelexperts.ApplicationLayer;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.travelexperts.BusinessLayer.Product;
import com.example.travelexperts.BusinessLayer.RecyclerViewData;
import com.example.travelexperts.BusinessLayer.Supplier;
import com.example.travelexperts.DatabaseLayer.DBHelper;
import com.example.travelexperts.DatabaseLayer.DataSource;
import com.example.travelexperts.R;
import com.google.android.material.snackbar.Snackbar;

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
public class AddProductActivity extends AppCompatActivity{

    private Spinner selectProduct;
    private Spinner selectSupplier;
    private ImageButton btnSaveProduct;
    DataSource dataSource;
    Product product;
    Supplier supplier;
    DBHelper dbHelper;
    RecyclerViewData recyclerViewData;
    List<Product> productList;
    ArrayAdapter<Product> productArrayAdapter;
    List<Supplier> supplierList;
    ArrayAdapter<Supplier> supplierArrayAdapter;
    int pId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        //adding the back button in the activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Add Product");

        dbHelper = DBHelper.getInstance(getApplicationContext());

        productList = new ArrayList<>();
        supplierList = new ArrayList<>();

        selectProduct = findViewById(R.id.spinnerSelectProduct);
        getProductData();

        btnSaveProduct = findViewById(R.id.btnSaveProduct);


        /*selectProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                product = productList.get(position);

                pId = product.getProductId();
                Log.d("pos", "pId "+pId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/

        selectSupplier = findViewById(R.id.spinnerSelectSupplier);
        //getPId();

        if (getPId()==0){
            selectSupplier.setEnabled(false);
        }else {
            selectSupplier.setEnabled(true);
            getSupplierData(getPId());
        }


        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = new Product();

                /*if (!etProductName.getText().toString().isEmpty()){
                    product.ProdName = etProductName.getText().toString();
                } else {
                    product.ProdName = "";
                }*/

                //dbHelper.insertProduct(product);

                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Product Added", Snackbar.LENGTH_SHORT);
                snackbar.show();

                //Intent intent=new Intent(AddProductActivity.this, ProductActivity.class);
                //startActivity(intent);
            }
        });
    }

    //back button pressed
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    //fetch Products data from server
    private void getProductData() {
        dbHelper = new DBHelper(getApplicationContext());

        // Creating adapter for spinner
        productArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productList);

        // Drop down layout
        productArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        String url = "http://192.168.0.23:8081/JSPDay3RESTExample/rs/product/getproducts";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Product product = new Product();
                        product.setProductId(jsonObject.getInt("ProductId"));
                        product.setProdName(jsonObject.getString("ProdName"));

                        productList.add(product);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                productArrayAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }

        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

        // attaching data adapter to spinner
        selectProduct.setAdapter(productArrayAdapter);
    }

    //fetch Suppliers data from server
    private void getSupplierData(int prodId) {
        dbHelper = new DBHelper(getApplicationContext());

        // Creating adapter for spinner
        supplierArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, supplierList);

        // Drop down layout
        supplierArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Log.d("pos", "sup "+ prodId);
        String url = "http://192.168.0.23:8081/JSPDay3RESTExample/rs/supplier/getsupplierswithoutproduct/"+prodId;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                //clearing the list
                while(supplierList.size()>0){
                    supplierList.remove(0);
                }

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Supplier supplier = new Supplier();
                        supplier.setSupplierId(jsonObject.getInt("SupplierId"));
                        supplier.setSupName(jsonObject.getString("SupName"));

                        supplierList.add(supplier);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                supplierArrayAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }

        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

        // attaching data adapter to spinner
        selectSupplier.setAdapter(supplierArrayAdapter);
    }

    private int getPId(){
        selectProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                product = productList.get(position);

                pId = product.getProductId();
                Log.d("pos", "pId "+ pId);

                if (getPId()==0){
                    selectSupplier.setEnabled(false);
                }else {
                    selectSupplier.setEnabled(true);
                    getSupplierData(getPId());
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return pId;
    }

}