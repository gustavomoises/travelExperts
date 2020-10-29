package com.example.travelexperts.ApplicationLayer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.travelexperts.BusinessLayer.Product;
import com.example.travelexperts.BusinessLayer.Supplier;
import com.example.travelexperts.DatabaseLayer.DBHelper;
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
    Product product;
    Supplier supplier;
    DBHelper dbHelper;
    List<Product> productList;
    ArrayAdapter<Product> productArrayAdapter;
    List<Supplier> supplierList;
    ArrayAdapter<Supplier> supplierArrayAdapter;
    int pId, sId;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        queue = Volley.newRequestQueue(getApplicationContext());

        //adding the back button in the activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Add Product");

        //dbHelper = DBHelper.getInstance(getApplicationContext());

        productList = new ArrayList<>();
        supplierList = new ArrayList<>();

        selectProduct = findViewById(R.id.spinnerSelectProduct);
        getProductData();

        btnSaveProduct = findViewById(R.id.btnSaveProduct);
        selectSupplier = findViewById(R.id.spinnerSelectSupplier);

        // Creating adapter for spinner
        supplierArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, supplierList);
        selectSupplier.setAdapter(supplierArrayAdapter);


        if (getPId()==0){
            selectSupplier.setEnabled(false);
        }else {
            selectSupplier.setEnabled(true);
            getSupplierData(getPId());
        }

        //getSId();
        selectSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                supplier = supplierList.get(position);

                sId = supplier.getSupplierId();
                Log.d("pos", "sId "+ sId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData(getPId(),getSId());
                Restart();
            }
        });
    }

    //back button pressed
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    public void Restart()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Data Added Successfully!!");
        dialog.setMessage("Do you want to add another data?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.show();
    }

    //fetch Products data from server
    private void getProductData() {
        dbHelper = new DBHelper(getApplicationContext());

        // Creating adapter for spinner
        productArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productList);

        // Drop down layout
        productArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        String url = "http://192.168.0.22:8081/JSPDay3RESTExample/rs/product/getproducts";
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

        // Drop down layout
        supplierArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Log.d("pos", "sup "+ prodId);
        String url = "http://192.168.0.22:8081/JSPDay3RESTExample/rs/supplier/getsupplierswithoutproduct/"+prodId;
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

    private int getSId(){
        selectSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                supplier = supplierList.get(position);

                sId = supplier.getSupplierId();
                Log.d("pos", "sId "+ sId);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return sId;
    }

    //fetch Suppliers data from server
    private void insertData(final int prodId, final int supId) {
        getSId();
        //dbHelper = new DBHelper(getApplicationContext());

        Log.d("pos", prodId +" " + supId);

        String url = "http://192.168.0.22:8081/JSPDay3RESTExample/rs/productssuppliers/putproductsandsuppliers";

        JSONObject object = new JSONObject();
        try {
            object.put("ProductId", String.valueOf(prodId));
            object.put("SupplierId", String.valueOf(supId));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Data Added", Snackbar.LENGTH_SHORT);
                        snackbar.show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Data Not Added", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(jsonObjectRequest);
    }
}