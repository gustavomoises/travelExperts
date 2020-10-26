//Author: Gustavo Lourenco Moises
//Thread Project - Group 1
//OOSD Program Spring 2020
//Date:9/30/2020
//Travel Agency Application
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.travelexperts.BusinessLayer.Booking;
import com.example.travelexperts.BusinessLayer.BookingDetail;
import com.example.travelexperts.BusinessLayer.Customer;

import com.example.travelexperts.BusinessLayer.Fee;
import com.example.travelexperts.BusinessLayer.Package;
import com.example.travelexperts.BusinessLayer.TripType;

import com.example.travelexperts.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executors;

public class BookingDetailsActivity extends AppCompatActivity {
    SharedPreferences prefs;
    ConstraintLayout clBookingDetails;
    Booking booking;
    Button btnEditBookingDetail;
    TextView tvBookingNo,tvBookingDate, tvTravelerCount,tvCustomerBookingDetail,tvTripType, tvBookingSubTotal, tvBookingAgencyCommission,tvBookingAgencyFee,tvBookingTotal;
    ListView lvBookingPackage, lvBookingProducts;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        requestQueue = Volley.newRequestQueue(this);

        tvBookingNo=findViewById(R.id.tvBookingNo);
        tvBookingDate=findViewById(R.id.tvAddBookingDate);
        tvTravelerCount=findViewById(R.id.tvTravelerCount);
        tvCustomerBookingDetail=findViewById(R.id.tvCustomerBookingDetail);
        tvTripType=findViewById(R.id.tvTripType);
        tvBookingSubTotal=findViewById(R.id.tvBookingSubTotal);
        tvBookingAgencyCommission=findViewById(R.id.tvBookingAgencyCommission);
        tvBookingAgencyFee=findViewById(R.id.tvBookingAgencyFee);
        tvBookingTotal=findViewById(R.id.tvBookingTotal);
        lvBookingPackage=findViewById(R.id.lvBookingPackage);
        lvBookingProducts=findViewById(R.id.lvBookingProducts);
        btnEditBookingDetail= findViewById(R.id.btnEditBookingDetail);

        Intent intent = getIntent();
        booking =(Booking) intent.getSerializableExtra("Booking");
        tvBookingNo.setText(booking.getBookingNo());
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        tvBookingDate.setText(df.format(booking.getBookingDate()));
        tvTravelerCount.setText(String.format("%.0f",booking.getTravelerCount()));

        Executors.newSingleThreadExecutor().execute(new BookingDetailsActivity.TripTypeId());
        Executors.newSingleThreadExecutor().execute(new BookingDetailsActivity.CustomerId());

        Double bookProd=0.0;
        Double agencyCommission=0.0;
        Double agencyFee=0.0;

        Executors.newSingleThreadExecutor().execute(new BookingDetailsActivity.FeeCalculation(bookProd, agencyCommission,agencyFee));

        btnEditBookingDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddBookingActivity.class);
                intent.putExtra("mode","update");
                intent.putExtra("Booking",booking);
                startActivity(intent);
            }
        });

        //Set background color form Settings
        clBookingDetails= findViewById(R.id.clBookingDetails);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");
        switch (basicColor){
            case "White":
                clBookingDetails.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clBookingDetails.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clBookingDetails.setBackgroundColor(Color.GREEN);
                break;

        }
        //loadBookingDetails(booking.getBookingId());

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
        clBookingDetails= findViewById(R.id.clBookingDetails);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clBookingDetails.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clBookingDetails.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clBookingDetails.setBackgroundColor(Color.GREEN);
                break;
        }
    }


    class TripTypeId implements Runnable {

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://192.168.1.81:8080/JSPDay3RESTExample/rs/triptype/gettriptypes";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of Agents
                    final ArrayList<TripType> tripTypes = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject agt = jsonArray.getJSONObject(i);
                            TripType tripType = new TripType(agt.getString("TripTypeId").charAt(0), agt.getString("TTName"));
                            tripTypes.add(tripType);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //display result message
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            for (TripType tt: tripTypes)
                                if (tt.getTripTypeId()==booking.getTripTypeId())
                                    tvTripType.setText(tt.gettTName());

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


    class CustomerId implements Runnable {

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://192.168.1.81:8080/JSPDay3RESTExample/rs/customer/getcustomers";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of Agents
                    final ArrayList<Customer> customers = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject agt = jsonArray.getJSONObject(i);
                            Customer customer = new Customer(agt.getInt("CustomerId"), agt.getString("CustFirstName"),agt.getString("CustLastName"),agt.getString("CustAddress"),agt.getString("CustCity"), agt.getString("CustProv"),agt.getString("CustPostal"),agt.getString("CustCountry"),agt.getString("CustHomePhone"),agt.getString("CustBusPhone"),agt.getString("CustEmail"),agt.getInt("AgentId"));
                            customers.add(customer);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //display result message
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            for (Customer tt: customers)
                                if (tt.getCustomerId()==booking.getCustomerId())
                                    tvCustomerBookingDetail.setText(tt.getCustFirstName()+" "+tt.getCustLastName());

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

    class FeeCalculation implements Runnable {
        private Double bookProd;
        private Double agencyCommission;
        private Double agencyFee;

        public FeeCalculation(Double bookProd, Double agencyCommission, Double agencyFee) {
            this.bookProd = bookProd;
            this.agencyCommission = agencyCommission;
            this.agencyFee = agencyFee;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://192.168.1.81:8080/JSPDay3RESTExample/rs/fee/getfees";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of Agents
                    final ArrayList<Fee> fees = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
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

                            Executors.newSingleThreadExecutor().execute(new BookingDetailsActivity.BookDetailsCalculation(bookProd, agencyCommission,agencyFee,fees));

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

    class BookDetailsCalculation implements Runnable {
        private Double bookProd;
        private Double agencyCommission;
        private Double agencyFee;
        private ArrayList<Fee> fees;

        public BookDetailsCalculation(Double bookProd, Double agencyCommission, Double agencyFee, ArrayList<Fee> fees) {
            this.bookProd = bookProd;
            this.agencyCommission = agencyCommission;
            this.agencyFee = agencyFee;
            this.fees = fees;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://192.168.1.81:8080/JSPDay3RESTExample/rs/bookingdetail/getbookingdetailsbybookingid/"+booking.getBookingId();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of Agents
                    final ArrayList<BookingDetail> bookingDetails = new ArrayList<>();
                        try {

                            JSONArray jsonArray = new JSONArray(response);
                            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject agt = jsonArray.getJSONObject(i);
                                Date dateStart =new Date();
                                try {
                                    dateStart = dateFormat.parse(agt.getString("TripStart"));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Date dateEnd =new Date();
                                try {
                                    dateEnd = dateFormat.parse(agt.getString("TripEnd"));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                BookingDetail bookingDetail = new BookingDetail(agt.getInt("BookingDetailId"), agt.getDouble("ItineraryNo"),dateStart,dateEnd,agt.getString("Description"),agt.getString("Destination"),agt.getDouble("BasePrice"),agt.getDouble("AgencyCommission"),agt.getInt("BookingId"),agt.getString("RegionId"),agt.getString("ClassId"),agt.getString("FeeId"),agt.getInt("ProductSupplierId"));
                                bookingDetails.add(bookingDetail);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    //display result message
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<Double> costProducts = new ArrayList<>();
                            for (BookingDetail bk :bookingDetails )
                            {
                                costProducts.add(booking.getTravelerCount()*bk.getBasePrice());
                                bookProd+= booking.getTravelerCount()*bk.getBasePrice();
                                agencyCommission += booking.getTravelerCount()*bk.getAgencyCommission();
                                for (Fee f:fees)
                                {
                                    if (f.getFeeId().equals(bk.getFeedId()))
                                    {
                                        agencyFee+= booking.getTravelerCount()*f.getFeeAmt();
                                        break;
                                    }
                                }
                            }

                            String [] from = {"desc","unitPrice","qty","total"};
                            int [] to = {R.id.tvBookingProducDesc, R.id.tvBookingProducUnitPrice,R.id.tvBookingProducDescQty,R.id.tvBookingProducTotal};
                            ArrayList<HashMap<String,String>> data = new ArrayList<>();
                            int k=0;
                            for (BookingDetail bd :bookingDetails){
                                HashMap<String,String> map = new HashMap<>();
                                map.put("desc",bd.getDescription());
                                map.put("unitPrice", String.format("$ %.2f",bd.getBasePrice()));
                                map.put("qty",String.format("%.0f",booking.getTravelerCount()));
                                map.put("total",String.format("$ %.2f",costProducts.get(k)));
                                data.add(map);
                                k++;
                            }
                            SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),data,R.layout.bookingdetailproductlayout,from, to);
                            //adapterBookingProducts=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,dataSource.getBookingDetailByBookingId(bookingId));
                            lvBookingProducts.setAdapter(adapter);
                            if (booking.getPackageId()!=0)
                            Executors.newSingleThreadExecutor().execute(new BookingDetailsActivity.PackageCalculation(bookProd, agencyCommission,agencyFee));
                            else
                            {
                                tvBookingSubTotal.setText(String.format("$ %.2f",bookProd));
                                tvBookingAgencyCommission.setText(String.format("$ %.2f",agencyCommission));
                                tvBookingAgencyFee.setText(String.format("$ %.2f",agencyFee));
                                Double bookTotal = bookProd + agencyCommission+agencyFee;
                                tvBookingTotal.setText(String.format("$ %.2f",bookTotal));
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
    class PackageCalculation implements Runnable {
        private Double bookProd;
        private Double agencyCommission;
        private Double agencyFee;

        public PackageCalculation(Double bookProd, Double agencyCommission, Double agencyFee) {
            this.bookProd = bookProd;
            this.agencyCommission = agencyCommission;
            this.agencyFee = agencyFee;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://192.168.1.81:8080/JSPDay3RESTExample/rs/package/getpackage/"+booking.getPackageId();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of Agents
                    final Package prodPackage = new Package();
                    try {
                        JSONObject agt = new JSONObject(response);
                        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            Date dateStart =new Date();
                            try {
                                dateStart = dateFormat.parse(agt.getString("PkgStartDate"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Date dateEnd =new Date();
                            try {
                                dateEnd = dateFormat.parse(agt.getString("PkgEndDate"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            prodPackage.setPackageId(agt.getInt("PackageId"));
                            prodPackage.setPkgName(agt.getString("PkgName"));
                            prodPackage.setPkgStartDate(dateStart);
                            prodPackage.setPkgEndDate(dateEnd);
                            prodPackage.setPkgDec(agt.getString("PkgDesc"));
                            prodPackage.setPkgBasePrice(agt.getDouble("PkgBasePrice"));
                            prodPackage.setPkgAgencyCommission(agt.getDouble("PkgAgencyCommission"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //display result message
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (booking.getPackageId()!=0){
                                ArrayList<Package> packages=new ArrayList<>();
                                packages.add(prodPackage);

                                String [] from = {"desc","unitPrice","qty","total"};
                                int [] to = {R.id.tvBookingPackageDesc, R.id.tvBookingPckUnitPrice,R.id.tvBookingPkgQty,R.id.tvBookingPkgTotal};
                                ArrayList<HashMap<String,String>> data = new ArrayList<>();
                                for (Package p :packages){
                                    HashMap<String,String> map = new HashMap<>();
                                    map.put("desc",p.getPkgName()+" - "+p.getPkgDec());
                                    map.put("unitPrice", String.format("$ %.2f",p.getPkgBasePrice()));
                                    map.put("qty",String.format("%.0f",booking.getTravelerCount()));
                                    map.put("total",String.format("$ %.2f",booking.getTravelerCount()*prodPackage.getPkgBasePrice()));
                                    data.add(map);
                                }
                                SimpleAdapter adapterPackage = new SimpleAdapter(getApplicationContext(),data,R.layout.bookingdetailpackagelayout,from, to);

                                //adapterPackage=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,packages);
                                lvBookingPackage.setAdapter(adapterPackage);
                                bookProd +=  booking.getTravelerCount()*prodPackage.getPkgBasePrice();
                                agencyCommission +=booking.getTravelerCount()*prodPackage.getPkgAgencyCommission();
                            }
                            tvBookingSubTotal.setText(String.format("$ %.2f",bookProd));
                            tvBookingAgencyCommission.setText(String.format("$ %.2f",agencyCommission));
                            tvBookingAgencyFee.setText(String.format("$ %.2f",agencyFee));
                            Double bookTotal = bookProd + agencyCommission+agencyFee;
                            tvBookingTotal.setText(String.format("$ %.2f",bookTotal));


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