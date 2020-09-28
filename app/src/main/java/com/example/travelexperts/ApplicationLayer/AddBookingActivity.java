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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelexperts.BusinessLayer.Booking;
import com.example.travelexperts.BusinessLayer.BookingDetail;
import com.example.travelexperts.BusinessLayer.Customer;
import com.example.travelexperts.BusinessLayer.ProdPackage;
import com.example.travelexperts.BusinessLayer.ProductSupplier;
import com.example.travelexperts.BusinessLayer.TripType;
import com.example.travelexperts.DatabaseLayer.DataSource;
import com.example.travelexperts.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
    DataSource dataSource;
    ArrayList<TripType> tripTypes;
    ArrayList<Customer> customers;
    ArrayList<ProdPackage> packages;
    ArrayList<BookingDetail> bookingDetails;
    ListView lvBookingProducts;
    ArrayAdapter<BookingDetail> adapterBookingDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirstTimeTrip=true;
        FirstTimeCustomer=true;
        FirstTimePkg=true;
        NewBooking=true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_booking);
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
        dataSource = new DataSource(this);

        //Get TripTypes from database
        tripTypes = new ArrayList<>();
        TripType fakeTripType = new TripType('0',"");
        tripTypes.add(fakeTripType);

        for (TripType tt : dataSource.getTripTypes())
        {
            tripTypes.add(tt);
        }
        ArrayAdapter<TripType> tripTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,tripTypes );
        tripTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTripType.setAdapter(tripTypeAdapter);
        spTripType.setSelection(0);

        //Get Customers from database
        customers = new ArrayList<>();

        Customer fakeCustomer = new Customer();
        fakeCustomer.setCustFirstName("");
        fakeCustomer.setCustLastName("");
        customers.add(fakeCustomer);
        for (Customer c : dataSource.getCustomers())
        {
            customers.add(c);
        }

        ArrayAdapter<Customer> customerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,customers);
        customerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBookingDetailCustomer.setAdapter(customerAdapter);
        spBookingDetailCustomer.setSelection(0);

        //Get Packages from database


        packages = new ArrayList<>();

        ProdPackage fakePackage = new ProdPackage();
        fakePackage.setPkgDec("");
        fakePackage.setPkgName("");
        packages.add(fakePackage);
        for (ProdPackage p : dataSource.getPackages())
        {
            packages.add(p);
        }
        ArrayAdapter<ProdPackage> packageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,packages);
        packageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBookingPackage.setAdapter(packageAdapter);
        spBookingPackage.setSelection(0);

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");

        if (mode.equals("update"))
        {
            btnAddBookingDetail.setEnabled(true);
            booking =(Booking) intent.getSerializableExtra("Booking");
            tvBookingNo.setText(booking.getBookingNo());
            DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
            tvAddBookingDate.setText(df.format(booking.getBookingDate()));
            etTravelerCount.setText(String.format("%.0f",booking.getTravelerCount()));
            int help=0;
            for (TripType tt : tripTypes)
            {
                if (booking.getTripTypeId() == tt.getTripTypeId())
                    break;
                else
                    help++;

            }
            spTripType.setSelection(help==tripTypes.size()?0:help);

            help=0;
            for (Customer c : customers)
            {
                if (booking.getCustomerId()==c.getCustomerId())
                    break;
                else
                    help++;

            }
            spBookingDetailCustomer.setSelection(help);

            help=0;
            for (ProdPackage p : packages)
            {
                if (booking.getPackageId()==p.getPackageId())
                    break;
                else
                    help++;

            }
            spBookingPackage.setSelection(help);
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
                if (mode.equals("insert")&&NewBooking)
                {
                    booking.setTravelerCount(Double.parseDouble(etTravelerCount.getText().toString()));
                    //save in the database
                    booking=dataSource.insertBooking(booking);

                    //update fields
                    tvBookingNo.setText(booking.getBookingNo());
                    DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
                    tvAddBookingDate.setText(df.format(booking.getBookingDate()));
                    btnAddBookingDetail.setEnabled(true);
                    NewBooking=false;
                }
                else
                {
                    //update
                    booking.setTravelerCount(Double.parseDouble(etTravelerCount.getText().toString()));
                    dataSource.updateBooking(booking);
                    Intent intent2 = new Intent(getApplicationContext(), BookingDetailsActivity.class);
                    intent2.putExtra("Booking",booking);
                    startActivity(intent2);
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
        loadBookingDetails();
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
                Intent intent4 = new Intent(this, MiscelaneousActivity.class);
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
    //Get all the agents in the database and associate to the Listview
    private void loadBookingDetails() {
        bookingDetails=dataSource.getBookingDetailByBookingId(booking.getBookingId());
        adapterBookingDetails=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,dataSource.getBookingDetailByBookingId(booking.getBookingId()));
        lvBookingProducts.setAdapter(adapterBookingDetails);
    }
}