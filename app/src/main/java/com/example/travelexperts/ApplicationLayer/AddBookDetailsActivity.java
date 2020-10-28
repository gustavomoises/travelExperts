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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.example.travelexperts.BusinessLayer.BookClass;
import com.example.travelexperts.BusinessLayer.Booking;
import com.example.travelexperts.BusinessLayer.BookingDetail;
import com.example.travelexperts.BusinessLayer.Fee;
import com.example.travelexperts.BusinessLayer.Product;
import com.example.travelexperts.BusinessLayer.Region;
import com.example.travelexperts.BusinessLayer.Supplier;
import com.example.travelexperts.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;

import static java.lang.Math.ceil;


public class AddBookDetailsActivity extends AppCompatActivity {
    //Local Variables
    SharedPreferences prefs;
    ConstraintLayout clUpdateBookingDetail;
    EditText etBookingDetailTripStart,etBookingDetailTripEnd, etBookingDetailDesc,etBookingDetailDestination,etBookingDetailBasePrice,etBookingDetailAgCom;
    TextView tvBookingDetailBookingNo,tvBookingDetailItinerary;
    Spinner spBookingDetailRegion,spBookingDetailClass,spBookingDetailFee,spBookingDetailProduct,spBookingDetailSupplier;
    Button btnBookingDetailCancel,btnBookingDetailSave, btnBookingDetailDelete;
    String mode;
    BookingDetail bookingDetail;
    Booking booking;
    Product product;
    RequestQueue requestQueue;
    boolean SupBoolean;
    boolean ProdBoolean;
    boolean ClassBoolean;
    boolean FeeBoolean;
    boolean RegionBoolean;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_details);

        //Volley Request Line
        requestQueue = Volley.newRequestQueue(this);
        //Id Association
        clUpdateBookingDetail=findViewById(R.id.clUpdateBookingDetail);
        etBookingDetailTripStart=findViewById(R.id.etBookingDetailTripStart);
        etBookingDetailTripEnd=findViewById(R.id.etBookingDetailTripEnd);
        etBookingDetailDesc=findViewById(R.id.etBookingDetailDesc);
        etBookingDetailDestination=findViewById(R.id.etBookingDetailDestination);
        etBookingDetailBasePrice=findViewById(R.id.etBookingDetailBasePrice);
        etBookingDetailAgCom=findViewById(R.id.etBookingDetailAgCom);
        tvBookingDetailBookingNo=findViewById(R.id.tvBookingDetailBookingNo);
        tvBookingDetailItinerary=findViewById(R.id.tvBookingDetailItinerary);
        spBookingDetailRegion=findViewById(R.id.spBookingDetailRegion);
        spBookingDetailClass=findViewById(R.id.spBookingDetailClass);
        spBookingDetailFee=findViewById(R.id.spBookingDetailFee);
        spBookingDetailProduct=findViewById(R.id.spBookingDetailProduct);
        spBookingDetailSupplier=findViewById(R.id.spBookingDetailSupplier);
        btnBookingDetailCancel=findViewById(R.id.btnBookingDetailCancel);
        btnBookingDetailSave=findViewById(R.id.btnBookingDetailSave);
        btnBookingDetailDelete=findViewById(R.id.btnBookingDetailDelete);
        //Spinner first entry
        SupBoolean =true;
        ProdBoolean=true;
        ClassBoolean=true;
        FeeBoolean=true;
        RegionBoolean=true;

        //Get mode from intent
        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        booking=(Booking)intent.getSerializableExtra("Booking");

        //Verify mode
        if (mode.equals("update"))
        {
            btnBookingDetailDelete.setEnabled(true);
            bookingDetail =(BookingDetail) intent.getSerializableExtra("BookingDetail");
            //Set Booking details
            tvBookingDetailBookingNo.setText(booking.getBookingNo());
            tvBookingDetailItinerary.setText(String.format("%.2f",bookingDetail.getItineraryNo()));
            @SuppressLint("SimpleDateFormat") DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
            etBookingDetailTripStart.setText(bookingDetail.getTripStart()==null?"":df.format(bookingDetail.getTripStart()));
            etBookingDetailTripEnd.setText(bookingDetail.getTripEnd()==null?"":df.format(bookingDetail.getTripEnd()));
            etBookingDetailDesc.setText(bookingDetail.getDescription());
            etBookingDetailDestination.setText(bookingDetail.getDestination());
            etBookingDetailBasePrice.setText(String.format("%.2f",bookingDetail.getBasePrice()));
            etBookingDetailAgCom.setText(String.format("%.2f",bookingDetail.getAgencyCommission()));

        }
        else
        {
            //New Booking Detail setup
            btnBookingDetailDelete.setEnabled(false);
            spBookingDetailSupplier.setEnabled(false);
            tvBookingDetailBookingNo.setText(booking.getBookingNo());
            tvBookingDetailItinerary.setText("*");
            etBookingDetailTripStart.setText("");
            etBookingDetailTripEnd.setText("");
            etBookingDetailDesc.setText("");
            etBookingDetailDestination.setText("");
            etBookingDetailBasePrice.setText("");
            etBookingDetailAgCom.setText("");
            //Create Booking Detail
            bookingDetail = new BookingDetail();
            bookingDetail.setRegionId("");
            bookingDetail.setClassId("");
            bookingDetail.setFeedId("");
        }

        //Get Regions from database
        Executors.newSingleThreadExecutor().execute(new AddBookDetailsActivity.RegionId());

        //Get fees from database
        Executors.newSingleThreadExecutor().execute(new AddBookDetailsActivity.FeeId());

        //Get classes from database
        Executors.newSingleThreadExecutor().execute(new AddBookDetailsActivity.BookClassId());

        //Get products and suppliers from database
        if ( bookingDetail.getProductSupplierId()==0)
        {
            Executors.newSingleThreadExecutor().execute(new AddBookDetailsActivity.ProductId(0));
            Executors.newSingleThreadExecutor().execute(new AddBookDetailsActivity.SupplierId(0,0));
        }
        else
        //Get ProductId and SupplierId  from ProductSupplierId from booking detail
        Executors.newSingleThreadExecutor().execute(new AddBookDetailsActivity.GetProductIdSupplierId(bookingDetail.getProductSupplierId()));


        //Class Select Combo Box
        spBookingDetailClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Don't do anything if it is the first time
                if(ClassBoolean)
                    ClassBoolean=false;
                else
                {
                    //Set booking detail Class Id
                    BookClass bookClass = (BookClass)spBookingDetailClass.getSelectedItem();
                    bookingDetail.setClassId(bookClass.getClassId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Fee Selection Combo Box
        spBookingDetailFee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Don't do anything if it is the first time
                if(FeeBoolean)
                    FeeBoolean=false;
                else
                {
                    //Set booking detail Fee Id
                    Fee fee = (Fee)spBookingDetailFee.getSelectedItem();
                    bookingDetail.setFeedId(fee.getFeeId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Region Selection Combo Box
        spBookingDetailRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Don't do anything if it is the first time
                if (RegionBoolean)
                    RegionBoolean=false;
                else
                {
                    //Set booking detail Region Id
                    Region region = (Region)spBookingDetailRegion.getSelectedItem();
                    bookingDetail.setRegionId(region.getRegionId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Product Selection Combo Box
        spBookingDetailProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Don't do anything if it is the first time
                if (ProdBoolean)
                    ProdBoolean=false;
                else
                {
                    //Verify selected product
                    if (position==0)
                    {
                        bookingDetail.setProductSupplierId(0);
                        spBookingDetailSupplier.setEnabled(false);
                        //Change supplier's list
                        Executors.newSingleThreadExecutor().execute(new AddBookDetailsActivity.SupplierId(0,0));
                    }

                    else{
                        //Enable Supplier combo box
                        spBookingDetailSupplier.setEnabled(true);
                        //Set booking detail product
                        product = (Product)spBookingDetailProduct.getSelectedItem();
                        //Change supplier's list based on the product selected
                        Executors.newSingleThreadExecutor().execute(new AddBookDetailsActivity.SupplierId(0,product.getProductId()));
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Supplier Selection Combo Box
        spBookingDetailSupplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Don't do anything if it is the first time
                if(SupBoolean)
                    SupBoolean=false;
                else
                {
                    //Verify selected supplier
                    if (position==0)
                        bookingDetail.setProductSupplierId(0);
                    else
                    {
                        //Set booking detail supplier
                        Supplier supplier = (Supplier) spBookingDetailSupplier.getSelectedItem();
                        //Find productSupplierId based on the ProductId and SupplierId
                        Executors.newSingleThreadExecutor().execute(new AddBookDetailsActivity.ProductSupplierId(supplier.getSupplierId(),product.getProductId()));
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Cancel Button
        btnBookingDetailCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddBookingActivity.class);
                intent.putExtra("mode","update");
                intent.putExtra("Booking",booking);
                startActivity(intent);
            }
        });

        //Save Button
        btnBookingDetailSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Verify if Product and supplier were selected
                if (bookingDetail.getProductSupplierId()==0) {
                    Toast.makeText(getApplicationContext(), " Product and Supplier must be selected!!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    //INSERT DATA FORMAT EXCEPTION

                    //Set booking details from interface
                    @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    ParsePosition pos = new ParsePosition(0);
                    ParsePosition pos1 = new ParsePosition(0);
                    bookingDetail.setTripStart(etBookingDetailTripStart.getText() == null ? null : df.parse(etBookingDetailTripStart.getText() + "", pos));
                    bookingDetail.setTripEnd(etBookingDetailTripEnd.getText() == null ? null : df.parse(etBookingDetailTripEnd.getText() + "", pos1));
                    bookingDetail.setDescription(etBookingDetailDesc.getText()==null?null: etBookingDetailDesc.getText()+ "");
                    bookingDetail.setDestination(etBookingDetailDestination.getText() ==null?null : etBookingDetailDestination.getText()+ "");
                    bookingDetail.setBookingId(booking.getBookingId());
                    //Agency Commission Exception Handler
                    try
                    {
                        bookingDetail.setAgencyCommission(Double.parseDouble(etBookingDetailAgCom.getText() + ""));
                    } catch (NumberFormatException e) {
                        bookingDetail.setAgencyCommission(0);
                    }
                    //Base Price Exception Handler
                    try
                    {
                        bookingDetail.setBasePrice(Double.parseDouble(etBookingDetailBasePrice.getText() + ""));
                    } catch (NumberFormatException e) {
                        bookingDetail.setBasePrice(0);
                    }

                    // Define Itinerary number from booking detail
                    Random random = new Random();
                    if (bookingDetail.getItineraryNo()==0)
                        bookingDetail.setItineraryNo(ceil(1000*random.nextDouble()));

                    //Verify mode
                    if (mode.equals("update"))
                        Executors.newSingleThreadExecutor().execute(new AddBookDetailsActivity.PostBookingDetail(bookingDetail));
                    else
                    {
                        Executors.newSingleThreadExecutor().execute(new AddBookDetailsActivity.PutBookingDetail(bookingDetail));
                    }
                }
            }
        });

        //Delete Button
        btnBookingDetailDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Executors.newSingleThreadExecutor().execute(new AddBookDetailsActivity.DeleteBookingDetail(bookingDetail.getBookingDetailId()));

            }
        });

        //Set background color from Settings
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");
        switch (basicColor){
            case "White":
                clUpdateBookingDetail.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clUpdateBookingDetail.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clUpdateBookingDetail.setBackgroundColor(Color.GREEN);
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
        clUpdateBookingDetail= findViewById(R.id.clUpdateBookingDetail);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        assert basicColor != null;
        switch (basicColor){
            case "White":
                clUpdateBookingDetail.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clUpdateBookingDetail.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clUpdateBookingDetail.setBackgroundColor(Color.GREEN);
                break;
        }
    }

    // Define Region Combo box parameters
    class RegionId implements Runnable {

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://192.168.1.81:8080/JSPDay3RESTExample/rs/region/getregions";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of Regions
                    final ArrayList<Region> regions = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        //Insert fake Region option
                        Region fakeRegion = new Region("","");
                        regions.add(fakeRegion);
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

                            //Set Adapter to combo box
                            ArrayAdapter<Region> regionAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,regions );
                            regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spBookingDetailRegion.setAdapter(regionAdapter);
                            //Select combo box item for the first time
                            if(mode.equals("update"))
                            {
                                int help=0;
                                for (Region tt : regions)
                                {
                                    if (bookingDetail.getRegionId().equals(tt.getRegionId()))
                                        break;
                                    else
                                        help++;
                                }
                                spBookingDetailRegion.setSelection(help==regions.size()?0:help);
                            }
                            else
                                spBookingDetailRegion.setSelection(0);
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

    // Define Fee Combo box parameters
    class FeeId implements Runnable {

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://192.168.1.81:8080/JSPDay3RESTExample/rs/fee/getfees";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of Fees
                    final ArrayList<Fee> fees = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        //Insert fake Fee option
                        Fee fakeFee = new Fee("","",0,"");
                        fees.add(fakeFee);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject agt = jsonArray.getJSONObject(i);
                            Fee fee = new Fee(agt.getString("FeeId"), agt.getString("FeeName"),agt.getDouble("FeeAmt"),agt.getString("FeeDesc"));
                            fees.add(fee);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //display result message
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //Set combo box Adapter
                            ArrayAdapter<Fee> feeAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,fees );
                            feeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spBookingDetailFee.setAdapter(feeAdapter);
                            //Select combo box item for the first time
                            if(mode.equals("update"))
                            {
                                int help=0;
                                for (Fee tt : fees)
                                {
                                    if (bookingDetail.getFeedId().equals(tt.getFeeId()))
                                        break;
                                    else
                                        help++;
                                }
                                spBookingDetailFee.setSelection(help==fees.size()?0:help);
                            }
                            else
                                spBookingDetailFee.setSelection(0);
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

    // Define Class Combo box parameters
    class BookClassId implements Runnable {

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://192.168.1.81:8080/JSPDay3RESTExample/rs/class/getclasses";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of BookClasses
                    final ArrayList<BookClass> bookClasses = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        //Insert fake Class option
                        BookClass fakeBookClass = new BookClass("","","");
                        bookClasses.add(fakeBookClass);
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

                            //Set Combo Box Adapter
                            ArrayAdapter<BookClass> bookClassAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,bookClasses );
                            bookClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spBookingDetailClass.setAdapter(bookClassAdapter);
                            //Select combo box item for the first time
                            if(mode.equals("update"))
                            {
                                int help=0;
                                for (BookClass tt : bookClasses)
                                {
                                    if (bookingDetail.getClassId().equals(tt.getClassId()))
                                        break;
                                    else
                                        help++;
                                }
                                spBookingDetailClass.setSelection(help==bookClasses.size()?0:help);
                            }
                            else
                                spBookingDetailClass.setSelection(0);
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

    // Define Product Combo box parameters
    class ProductId implements Runnable {
        private int productId;

        public ProductId(int productId) {
            this.productId = productId;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://192.168.1.81:8080/JSPDay3RESTExample/rs/product/getproducts";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of Products
                    final ArrayList<Product> products= new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        //Insert fake Product option
                        Product fakeProduct = new Product(0,"");
                        products.add(fakeProduct);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject agt = jsonArray.getJSONObject(i);
                            Product product = new Product(agt.getInt("ProductId"), agt.getString("ProdName"));
                            products.add(product);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //display result message
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //Set Combo Box Adapter
                            ArrayAdapter<Product> productAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,products );
                            productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spBookingDetailProduct.setAdapter(productAdapter);
                            //Select combo box item for the first time
                            if(mode.equals("update"))
                            {
                                int help=0;
                                for (Product tt : products)
                                {
                                    if (productId == tt.getProductId())
                                        break;
                                    else
                                        help++;
                                }
                                spBookingDetailProduct.setSelection(help==products.size()?0:help);
                                product = (Product)spBookingDetailProduct.getSelectedItem();
                            }
                            else
                                spBookingDetailProduct.setSelection(0);
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

    // Define Supplier Combo box parameters
    class SupplierId implements Runnable {
        private int supplierId;
        private int productId;

        public SupplierId(int supplierId, int productId) {
            this.supplierId = supplierId;
            this.productId = productId;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url;
            //Verify if there is any product selected
            if(productId==0)
                url = "http://192.168.1.81:8080/JSPDay3RESTExample/rs/supplier/getsupplierswithproducts";
                else
                url = "http://192.168.1.81:8080/JSPDay3RESTExample/rs/supplier/getsupplierswithproducts/"+productId;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of Suppliers
                    final ArrayList<Supplier> suppliers = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        //Insert fake Supplier option
                        Supplier fakeSupplier = new Supplier(0,"");
                        suppliers.add(fakeSupplier);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject agt = jsonArray.getJSONObject(i);
                            Supplier supplierAux = new Supplier(agt.getInt("SupplierId"), agt.getString("SupName"));
                            suppliers.add(supplierAux);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //display result message
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //Set Combo Box Adapter
                            ArrayAdapter<Supplier> supplierAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, suppliers );
                            supplierAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spBookingDetailSupplier.setAdapter(supplierAdapter);
                            //Select combo box item for the first time
                            if(mode.equals("update"))
                            {
                                int help=0;
                                for (Supplier tt : suppliers)
                                {
                                    if (supplierId == tt.getSupplierId())
                                        break;
                                    else
                                        help++;
                                }
                                spBookingDetailSupplier.setSelection(help==suppliers.size()?0:help);
                            }
                            else
                                spBookingDetailSupplier.setSelection(0);
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

    // Get ProductId and SupplierId from productSupplierId from booking detail
    class GetProductIdSupplierId implements Runnable {
        private int productSupplierId;

        public GetProductIdSupplierId(int productSupplierId) {
            this.productSupplierId = productSupplierId;
        }
        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://192.168.1.81:8080/JSPDay3RESTExample/rs/productsupplier/getproductidsupplieridbypsid/"+productSupplierId;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter with productId and supplierId
                    final ArrayList<Integer> productSupplier = new ArrayList<>();

                    try {
                        JSONObject json = new JSONObject(response);
                        productSupplier.add(json.getInt("ProductId")) ;
                        productSupplier.add( json.getInt("SupplierId"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //display result message
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Set Product Combo Box Parameters
                            Executors.newSingleThreadExecutor().execute(new AddBookDetailsActivity.ProductId(productSupplier.get(0)));
                            //Set Supplier Combo Box Parameters
                            Executors.newSingleThreadExecutor().execute(new AddBookDetailsActivity.SupplierId(productSupplier.get(1),productSupplier.get(0)));
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

    //Get the productSupplierId based on the selected productId and supplierId
    class ProductSupplierId implements Runnable {
        private int supplierId;
        private int productId;

        public ProductSupplierId(int supplierId, int productId) {
            this.supplierId = supplierId;
            this.productId = productId;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://192.168.1.81:8080/JSPDay3RESTExample/rs/productsupplier/getProdSupIdByIds/"+supplierId+"/"+productId;
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
                                //Add productSupplierId to the booking detail
                                bookingDetail.setProductSupplierId(Integer.parseInt(response));
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Product-supplier not found", Toast.LENGTH_LONG).show();
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

    //Update Booking Detail
    class PostBookingDetail implements Runnable {
        private BookingDetail bookingDetail;

        public PostBookingDetail(BookingDetail bookingDetail) {
            this.bookingDetail = bookingDetail;
        }

        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://192.168.1.81:8080/JSPDay3RESTExample/rs/bookingdetail/postbookingdetail";
            JSONObject obj = new JSONObject();
            try {
                obj.put("TripStart",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bookingDetail.getTripStart())+"");
                obj.put("TripEnd",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bookingDetail.getTripEnd())+"");
                obj.put("Description", bookingDetail.getDescription()+"");
                obj.put("Destination", bookingDetail.getDestination()+"");
                obj.put("BasePrice", bookingDetail.getBasePrice()+"");
                obj.put("AgencyCommission", bookingDetail.getAgencyCommission()+"");
                obj.put("RegionId", bookingDetail.getRegionId()+"");
                obj.put("ClassId", bookingDetail.getClassId()+"");
                obj.put("FeeId", bookingDetail.getFeedId()+"");
                obj.put("ProductSupplierId", bookingDetail.getProductSupplierId()+"");
                obj.put("BookingId", bookingDetail.getBookingId()+"");
                obj.put("ItineraryNo", bookingDetail.getItineraryNo()+"");
                obj.put("BookingDetailId", bookingDetail.getBookingDetailId()+"");

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
                                        if(response.getString("message").equals("Booking Detail updated successfully"))
                                        {
                                            //Go to Add Booking Activity
                                            Intent intent = new Intent(getApplicationContext(), AddBookingActivity.class);
                                            intent.putExtra("mode","update");
                                            intent.putExtra("Booking",booking);
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

    //Insert a new booking detail
    class PutBookingDetail implements Runnable {
        private BookingDetail bookingDetail;

        public PutBookingDetail(BookingDetail bookingDetail) {
            this.bookingDetail = bookingDetail;
        }

        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://192.168.1.81:8080/JSPDay3RESTExample/rs/bookingdetail/putbookingdetail";
                        JSONObject obj = new JSONObject();
            try {
                obj.put("TripStart",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bookingDetail.getTripStart())+"");
                obj.put("TripEnd",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bookingDetail.getTripEnd())+"");
                obj.put("Description", bookingDetail.getDescription()+"");
                obj.put("Destination", bookingDetail.getDestination()+"");
                obj.put("BasePrice", bookingDetail.getBasePrice()+"");
                obj.put("AgencyCommission", bookingDetail.getAgencyCommission()+"");
                obj.put("RegionId", bookingDetail.getRegionId()+"");
                obj.put("ClassId", bookingDetail.getClassId()+"");
                obj.put("FeeId", bookingDetail.getFeedId()+"");
                obj.put("ProductSupplierId", bookingDetail.getProductSupplierId()+"");
                obj.put("BookingId", bookingDetail.getBookingId()+"");
                obj.put("ItineraryNo", bookingDetail.getItineraryNo()+"");
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
                                        if(response.getString("message").equals("Booking Detail inserted successfully"))
                                        {
                                            //Go to Add Booking Activity
                                            Intent intent = new Intent(getApplicationContext(), AddBookingActivity.class);
                                            intent.putExtra("mode", "update");
                                            intent.putExtra("Booking", booking);
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

//Delete Booking Detail
    class DeleteBookingDetail implements Runnable {
        private int bookingDetailId;

        public DeleteBookingDetail(int bookingDetailId) {
            this.bookingDetailId = bookingDetailId;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://192.168.1.81:8080/JSPDay3RESTExample/rs/bookingdetail/deletebookingdetail/" + bookingDetailId;
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    VolleyLog.wtf(response, "utf-8");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            if(response.equals("Booking Detail Deleted Successfully"))
                            {
                                //Go to Add Booking Activity
                                Intent intent = new Intent(getApplicationContext(), AddBookingActivity.class);
                                intent.putExtra("mode","update");
                                intent.putExtra("Booking",booking);
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