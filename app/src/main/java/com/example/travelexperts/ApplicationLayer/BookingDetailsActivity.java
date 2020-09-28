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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;

public class BookingDetailsActivity extends AppCompatActivity {
    SharedPreferences prefs;
    ConstraintLayout clBookingDetails;
    Booking booking;
    Button btnEditBookingDetail;
    ArrayList<BookingDetail> bookingDetails;
    Customer customer;
    ProdPackage prodPackage;
    DataSource dataSource;
    TextView tvBookingNo,tvBookingDate, tvTravelerCount,tvCustomerBookingDetail,tvTripType, tvBookingSubTotal, tvBookingAgencyCommission,tvBookingTotal;
    ListView lvBookingPackage, lvBookingProducts;
    ArrayList<BookingDetail> bookingProducts;
    ArrayList<ProductSupplier> packageProducts;
    ArrayList<ProdPackage> packages;
    ArrayList<Double> costProducts;
    ArrayAdapter<ProductSupplier> adapterPackageProducts;
    ArrayAdapter<BookingDetail>adapterBookingProducts;
    ArrayAdapter<ProdPackage> adapterPackage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        tvBookingNo=findViewById(R.id.tvBookingNo);
        tvBookingDate=findViewById(R.id.tvAddBookingDate);
        tvTravelerCount=findViewById(R.id.tvTravelerCount);
        tvCustomerBookingDetail=findViewById(R.id.tvCustomerBookingDetail);
        tvTripType=findViewById(R.id.tvTripType);
        tvBookingSubTotal=findViewById(R.id.tvBookingSubTotal);
        tvBookingAgencyCommission=findViewById(R.id.tvBookingAgencyCommission);
        tvBookingTotal=findViewById(R.id.tvBookingTotal);
        lvBookingPackage=findViewById(R.id.lvBookingPackage);
        lvBookingProducts=findViewById(R.id.lvBookingProducts);
        btnEditBookingDetail= findViewById(R.id.btnEditBookingDetail);

        dataSource= new DataSource(this);
        Intent intent = getIntent();
        booking =(Booking) intent.getSerializableExtra("Booking");
        tvBookingNo.setText(booking.getBookingNo());
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        tvBookingDate.setText(df.format(booking.getBookingDate()));
        tvTravelerCount.setText(String.format("%.0f",booking.getTravelerCount()));

        for (TripType tt: dataSource.getTripTypes())
            if (tt.getTripTypeId()==booking.getTripTypeId())
                tvTripType.setText(tt.gettTName());


        costProducts = new ArrayList<>();
        Double bookProd=0.0;
        Double agencyCommission=0.0;
        customer=dataSource.getCustomerById(booking.getCustomerId());
        tvCustomerBookingDetail.setText(customer.getCustFirstName()+" "+customer.getCustLastName());
        bookingDetails= dataSource.getBookingDetailByBookingId(booking.getBookingId());
        for (BookingDetail bk :bookingDetails )
        {
            costProducts.add(booking.getTravelerCount()*bk.getBasePrice());
            bookProd+= booking.getTravelerCount()*bk.getBasePrice();
            agencyCommission += booking.getTravelerCount()*bk.getAgencyCommission();

        }
        if (booking.getPackageId()!=0){
            prodPackage=dataSource.getPackageById(booking.getPackageId());
            packages=new ArrayList<>();
            packages.add(prodPackage);

            String [] from = {"desc","unitPrice","qty","total"};
            int [] to = {R.id.tvBookingPackageDesc, R.id.tvBookingPckUnitPrice,R.id.tvBookingPkgQty,R.id.tvBookingPkgTotal};
            ArrayList<HashMap<String,String>> data = new ArrayList<>();
            for (ProdPackage p :packages){
                HashMap<String,String> map = new HashMap<>();
                map.put("desc",p.getPkgDec());
                map.put("unitPrice", String.format("$ %.2f",p.getPkgBasePrice()));
                map.put("qty",String.format("%.0f",booking.getTravelerCount()));
                map.put("total",String.format("$ %.2f",booking.getTravelerCount()*prodPackage.getPkgBasePrice()));
                data.add(map);
            }
            SimpleAdapter adapterPackage = new SimpleAdapter(this,data,R.layout.bookingdetailpackagelayout,from, to);

            //adapterPackage=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,packages);
            lvBookingPackage.setAdapter(adapterPackage);
            bookProd +=  booking.getTravelerCount()*prodPackage.getPkgBasePrice();
            agencyCommission +=booking.getTravelerCount()*prodPackage.getPkgAgencyCommission();
        }
        tvBookingSubTotal.setText(String.format("$ %.2f",bookProd));
        tvBookingAgencyCommission.setText(String.format("$ %.2f",agencyCommission));

        Double bookTotal = bookProd + agencyCommission;
        tvBookingTotal.setText(String.format("$ %.2f",bookTotal));

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
        loadBookingDetails(booking.getBookingId());

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
    //Get all the agents in the database and associate to the Listview
    private void loadBookingDetails(int bookingId) {
        bookingProducts=dataSource.getBookingDetailByBookingId(bookingId);

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
        SimpleAdapter adapter = new SimpleAdapter(this,data,R.layout.bookingdetailproductlayout,from, to);
        //adapterBookingProducts=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,dataSource.getBookingDetailByBookingId(bookingId));
        lvBookingProducts.setAdapter(adapter);
    }



}