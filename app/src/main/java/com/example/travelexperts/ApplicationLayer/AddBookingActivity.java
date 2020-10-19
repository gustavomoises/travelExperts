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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.example.travelexperts.BusinessLayer.Booking;
import com.example.travelexperts.BusinessLayer.BookingDetail;
import com.example.travelexperts.BusinessLayer.Customer;
import com.example.travelexperts.BusinessLayer.ProdPackage;
import com.example.travelexperts.BusinessLayer.TripType;
import com.example.travelexperts.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executors;

public class AddBookingActivity extends AppCompatActivity {
    SharedPreferences prefs;
    ConstraintLayout clAddBooking;
    String mode;
    Booking booking;
    Boolean NewBooking,FirstTimeTrip, FirstTimePkg, FirstTimeCustomer;
    Button btnSaveBooking, btnCancelBooking,btnAddBookingDetail;
    EditText etTravelerCount;
    TextView tvBookingNo, tvAddBookingDate;
    Spinner spTripType,spBookingDetailCustomer, spBookingPackage;
    ListView lvBookingProducts;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirstTimeTrip=true;
        FirstTimeCustomer=true;
        FirstTimePkg=true;
        NewBooking=true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_booking);
        requestQueue = Volley.newRequestQueue(this);
        btnAddBookingDetail=findViewById(R.id.btnAddBookingDetail);
        btnCancelBooking=findViewById(R.id.btnCancelBooking);
        btnSaveBooking=findViewById(R.id.btnSaveBooking);
        tvBookingNo=findViewById(R.id.tvBookingNo);
        tvAddBookingDate=findViewById(R.id.tvAddBookingDate);
        etTravelerCount=findViewById(R.id.etTravelerCount);
        spTripType=findViewById(R.id.spTripType);
        spBookingDetailCustomer=findViewById(R.id.spBookingDetailCustomer);
        spBookingPackage=findViewById(R.id.spBookingPackage);
        lvBookingProducts= findViewById(R.id.lvBookingProducts);

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");

        //Get TripTypes from database
        Executors.newSingleThreadExecutor().execute(new AddBookingActivity.TripTypeId());

        //Get Customers from database
        Executors.newSingleThreadExecutor().execute(new AddBookingActivity.CustomerId());

        //Get Packages from database
        Executors.newSingleThreadExecutor().execute(new AddBookingActivity.PackageId());



        if (mode.equals("update"))
        {
            btnAddBookingDetail.setEnabled(true);
            booking =(Booking) intent.getSerializableExtra("Booking");
            tvBookingNo.setText(booking.getBookingNo());
            DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
            tvAddBookingDate.setText(df.format(booking.getBookingDate()));
            etTravelerCount.setText(String.format("%.0f",booking.getTravelerCount()));
            //Get bookDetails
            Executors.newSingleThreadExecutor().execute(new AddBookingActivity.GetBookingDetails(booking.getBookingId()));
        }
        else
        {
            tvBookingNo.setText("*");
            tvAddBookingDate.setText("*");
            etTravelerCount.setText("1");
            btnAddBookingDetail.setEnabled(false);
            booking = new Booking();

        }
        spTripType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!FirstTimeTrip)
                {
                    TripType tripType = (TripType)spTripType.getSelectedItem();
                    booking.setTripTypeId(tripType.getTripTypeId());
                }
                else
                {
                    FirstTimeTrip=false;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spBookingDetailCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!FirstTimeCustomer)
                {
                    Customer customer = (Customer)spBookingDetailCustomer.getSelectedItem();
                    booking.setCustomerId(customer.getCustomerId());
                }
                else
                {
                    FirstTimeCustomer=false;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spBookingPackage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!FirstTimePkg)
                {
                    ProdPackage prodPackage = (ProdPackage)spBookingPackage.getSelectedItem();
                    booking.setPackageId(prodPackage.getPackageId());
                }
                else
                {
                    FirstTimePkg=false;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnSaveBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                booking.setTravelerCount(Double.parseDouble(etTravelerCount.getText().toString()));
                if (mode.equals("insert")&&NewBooking)
                    Executors.newSingleThreadExecutor().execute(new AddBookingActivity.PutBooking(booking));
                else
                {
                    Executors.newSingleThreadExecutor().execute(new AddBookingActivity.PostBooking(booking));
                }
            }
        });
        btnCancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mode.equals("insert")&&NewBooking)
                {
                    Intent intent2 = new Intent(getApplicationContext(), BookingActivity.class);
                    startActivity(intent2);

                }
                else
                {
                    Intent intent2 = new Intent(getApplicationContext(), BookingDetailsActivity.class);
                    intent2.putExtra("Booking",booking);
                    startActivity(intent2);
                }
            }
        });


        //Set background color form Settings
        clAddBooking= findViewById(R.id.clAddBooking);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");



        switch (basicColor){
            case "White":
                clAddBooking.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddBooking.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddBooking.setBackgroundColor(Color.GREEN);
                break;

        }

        btnAddBookingDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(getApplicationContext(), AddBookDetailsActivity.class);
                intent4.putExtra("mode","insert");
                intent4.putExtra("Booking",booking);
                startActivity(intent4);
            }
        });

        lvBookingProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent5 = new Intent(getApplicationContext(), AddBookDetailsActivity.class);
                intent5.putExtra("mode","update");
                intent5.putExtra("Booking",booking);
                intent5.putExtra("BookingDetail",(BookingDetail) lvBookingProducts.getAdapter().getItem(position));
                startActivity(intent5);
            }
        });

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
        clAddBooking= findViewById(R.id.clAddBooking);
        prefs = getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String basicColor = prefs.getString("color","White");

        switch (basicColor){
            case "White":
                clAddBooking.setBackgroundColor(Color.WHITE);
                break;
            case "Blue":
                clAddBooking.setBackgroundColor(Color.BLUE);
                break;
            case "Green":
                clAddBooking.setBackgroundColor(Color.GREEN);
                break;
        }
    }

    class TripTypeId implements Runnable {

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://192.168.1.64:8080/JSPDay3RESTExample/rs/triptype/gettriptypes";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of Agents
                    final ArrayList<TripType> tripTypes = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        TripType fakeTripType = new TripType('W',"");
                        tripTypes.add(fakeTripType);
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

                            //Get TripTypes from database
                            ArrayAdapter<TripType> tripTypeAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,tripTypes );
                            tripTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spTripType.setAdapter(tripTypeAdapter);
                            if(mode.equals("update"))
                            {
                                int help=0;
                                for (TripType tt : tripTypes)
                                {
                                    if (booking.getTripTypeId() == tt.getTripTypeId())
                                        break;
                                    else
                                        help++;
                                }
                                spTripType.setSelection(help==tripTypes.size()?0:help);
                            }
                                else
                                    spTripType.setSelection(0);
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
            String url = "http://192.168.1.64:8080/JSPDay3RESTExample/rs/customer/getcustomers";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of Agents
                    final ArrayList<Customer> customers = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        Customer fakeCustomer = new Customer();
                        fakeCustomer.setCustFirstName("");
                        fakeCustomer.setCustLastName("");
                        customers.add(fakeCustomer);
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

                            //Get TripTypes from database
                            ArrayAdapter<Customer> customerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,customers );
                            customerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spBookingDetailCustomer.setAdapter(customerAdapter);
                            if(mode.equals("update"))
                            {
                                int help=0;
                                for (Customer tt : customers)
                                {
                                    if (booking.getCustomerId() == tt.getCustomerId())
                                        break;
                                    else
                                        help++;
                                }
                                spBookingDetailCustomer.setSelection(help==customers.size()?0:help);
                            }
                            else
                                spBookingDetailCustomer.setSelection(0);
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

    class PackageId implements Runnable {

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://192.168.1.64:8080/JSPDay3RESTExample/rs/package/getpackages";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of Agents
                    final ArrayList<ProdPackage> prodPackages = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        ProdPackage fakePackage = new ProdPackage();
                        fakePackage.setPkgDec("");
                        fakePackage.setPkgName("");
                        prodPackages.add(fakePackage);
                        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject agt = jsonArray.getJSONObject(i);
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
                            ProdPackage prodPackage = new ProdPackage(agt.getInt("PackageId"),agt.getString("PkgName"),dateStart,dateEnd,agt.getString("PkgDesc"),agt.getDouble("PkgBasePrice"),agt.getDouble("PkgAgencyCommission"));
                            prodPackages.add(prodPackage);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //display result message
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //Get TripTypes from database
                            ArrayAdapter<ProdPackage> prodPackageAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,prodPackages );
                            prodPackageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spBookingPackage.setAdapter(prodPackageAdapter);
                            if(mode.equals("update"))
                            {
                                int help=0;
                                for (ProdPackage tt : prodPackages)
                                {
                                    if (booking.getPackageId() == tt.getPackageId())
                                        break;
                                    else
                                        help++;
                                }
                                spBookingPackage.setSelection(help==prodPackages.size()?0:help);
                            }
                            else
                                spBookingPackage.setSelection(0);
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
    class PostBooking implements Runnable {
        private Booking booking;

        public PostBooking(Booking booking) {
            this.booking = booking;
        }

        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://192.168.1.64:8080/JSPDay3RESTExample/rs/booking/postbooking";
            JSONObject obj = new JSONObject();
            try {
                obj.put("BookingNo", booking.getBookingNo()+"");
                obj.put("TravelerCount", booking.getTravelerCount()+"");
                obj.put("CustomerId",booking.getCustomerId()+"");
                obj.put("TripTypeId",booking.getTripTypeId()+"");
                obj.put("PackageId",booking.getPackageId()+"");
                obj.put("BookingDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(booking.getBookingDate())+"");
                obj.put("BookingId",booking.getBookingId()+"");
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
                                        if(response.getString("message").equals("Booking updated successfully"))
                                        {
                                            Intent intent2 = new Intent(getApplicationContext(), BookingDetailsActivity.class);
                                            intent2.putExtra("Booking", booking);
                                            startActivity(intent2);
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

    class PutBooking implements Runnable {
        private Booking booking;

        public PutBooking(Booking booking) {
            this.booking = booking;
        }

        @Override
        public void run() {
            //send JSON data to REST service
            String url = "http://192.168.1.64:8080/JSPDay3RESTExample/rs/booking/putbooking";
            Date date;
            Date currentDate = Calendar.getInstance().getTime();
            booking.setBookingDate(currentDate);

            //Booking Number definition
            Random random = new Random();
            Random random1 = new Random();
            Random random2 = new Random();
            char[] numbers = {'0', '1','2','3','4','5','6','7','8','9'};
            char[] letters = {'A', 'B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','X','Z','W','Y'};
            // Maximum number of characters
            int max=random.nextInt(10);
            //Minimum number of characters
            if (max<4)
                max=4;
            char[] bookingNo = new char[max];
            for (int i=0;i<max;i++)
            {
                //First 3 characters must be letter
                if(i<3)
                {
                    bookingNo[i] = letters[random2.nextInt(10)];
                }
                else {
                    //The other characters can be numbers and letters
                    if (random1.nextInt(10) > 5)
                        bookingNo[i] = numbers[random2.nextInt(10)];
                    else
                        bookingNo[i] = letters[random2.nextInt(10)];
                }
            }

            booking.setBookingNo(new String(bookingNo));

            JSONObject obj = new JSONObject();
            try {
                obj.put("BookingNo", booking.getBookingNo()+"");
                obj.put("TravelerCount", booking.getTravelerCount()+"");
                obj.put("CustomerId",booking.getCustomerId()+"");
                obj.put("TripTypeId",booking.getTripTypeId()+"");
                obj.put("PackageId",booking.getPackageId()+"");
                obj.put("BookingDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(booking.getBookingDate())+"");
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
                                        if(response.getString("message").equals("Booking inserted successfully"))
                                        {
                                           Toast.makeText(getApplicationContext(), " New booking Created!!!", Toast.LENGTH_LONG).show();
                                            //update fields
                                            tvBookingNo.setText(booking.getBookingNo());
                                            DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
                                            tvAddBookingDate.setText(df.format(booking.getBookingDate()));
                                            btnAddBookingDetail.setEnabled(true);
                                            NewBooking=false;
                                            Executors.newSingleThreadExecutor().execute(new AddBookingActivity.GetBookingId(booking.getBookingNo()));
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



    class DeleteBooking implements Runnable {
        private int bookingId;

        public DeleteBooking(int bookingId) {
            this.bookingId = bookingId;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://192.168.1.64:8080/JSPDay3RESTExample/rs/booking/deletebooking/" + bookingId;
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
                    VolleyLog.wtf(response, "utf-8");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            if(response.equals("Booking Deleted Successfully"))
                            {
                                Intent intent = new Intent(getApplicationContext(), BookingActivity.class);
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

    //Find next reward Id from Database
    class GetBookingId implements Runnable {
        private String bookingNo;

        public GetBookingId(String bookingNo) {
            this.bookingNo = bookingNo;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://192.168.1.64:8080/JSPDay3RESTExample/rs/booking/findBookingIdByBookingNo/"+bookingNo;
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
                                booking.setBookingId(Integer.parseInt(response));
                                Executors.newSingleThreadExecutor().execute(new AddBookingActivity.GetBookingDetails(booking.getBookingId()));
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

    class GetBookingDetails implements Runnable {
        private int bookingId;

        public GetBookingDetails(int bookingId) {
            this.bookingId = bookingId;
        }

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://192.168.1.64:8080/JSPDay3RESTExample/rs/bookingdetail/getbookingdetailsbybookingid/"+bookingId;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of Agents
                    ArrayAdapter<BookingDetail> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject agt = jsonArray.getJSONObject(i);
                                @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date dateStart = new Date();
                                try {
                                    dateStart = dateFormat.parse(agt.getString("TripStart"));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                Date dateEnd = new Date();
                                try {
                                    dateEnd = dateFormat.parse(agt.getString("TripEnd"));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                double itin;
                                try {
                                    itin = Double.parseDouble(agt.getString("ItineraryNo"));
                                } catch (Exception e) {
                                    itin = 0;
                                }

                                double basePrice;
                                try {
                                    basePrice = Double.parseDouble(agt.getString("BasePrice"));
                                } catch (Exception e) {
                                    basePrice = 0;
                                }

                                double agencyPrice;
                                try {
                                    agencyPrice = Double.parseDouble(agt.getString("AgencyCommission"));
                                } catch (Exception e) {
                                    agencyPrice = 0;
                                }

                                BookingDetail bookingDetails = new BookingDetail(agt.getInt("BookingDetailId"), itin, dateStart, dateEnd, agt.getString("Description"), agt.getString("Destination"), basePrice, agencyPrice, agt.getInt("BookingId"), agt.getString("RegionId"), agt.getString("ClassId"), agt.getString("FeeId"), agt.getInt("ProductSupplierId"));
                                adapter.add(bookingDetails);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    //update ListView with the adapter of Agents
                    final ArrayAdapter<BookingDetail> finalAdapter = adapter;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lvBookingProducts.setAdapter(finalAdapter);
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