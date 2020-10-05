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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.travelexperts.BusinessLayer.BookClass;
import com.example.travelexperts.BusinessLayer.Booking;
import com.example.travelexperts.BusinessLayer.BookingDetail;
import com.example.travelexperts.BusinessLayer.Fee;
import com.example.travelexperts.BusinessLayer.Product;
import com.example.travelexperts.BusinessLayer.Region;
import com.example.travelexperts.BusinessLayer.Supplier;
import com.example.travelexperts.DatabaseLayer.DataSource;
import com.example.travelexperts.R;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class AddBookDetailsActivity extends AppCompatActivity {
    //Local Variables
    SharedPreferences prefs;
    ConstraintLayout clUpdateBookingDetail;
    EditText etBookingDetailTripStart,etBookingDetailTripEnd, etBookingDetailDesc,etBookingDetailDestination,etBookingDetailBasePrice,etBookingDetailAgCom;
    TextView tvBookingDetailBookingNo,tvBookingDetailItinerary;
    Spinner spBookingDetailRegion,spBookingDetailClass,spBookingDetailFee,spBookingDetailProduct,spBookingDetailSupplier;
    Button btnBookingDetailCancel,btnBookingDetailSave, btnBookingDetailDelete;
    DataSource dataSource;
    String mode;
    ArrayList<BookClass> bookClasses;
    ArrayList<Fee> fees;
    ArrayList<Region> regions;
    ArrayList<Product> products;
    ArrayList<Supplier> suppliers,productSuppliers;
    BookingDetail bookingDetail;
    Booking booking;
    Product product;
    Supplier supplier;
    ArrayAdapter<Supplier> supplierAdapter;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_details);
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
        dataSource = new DataSource(this);

        //Get Regions from database
        regions = new ArrayList<>();
        //Insert blank Region
        Region fakeRegion = new Region("","");
        regions.add(fakeRegion);
        regions.addAll(dataSource.getRegions());
        ArrayAdapter<Region> regionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,regions );
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBookingDetailRegion.setAdapter(regionAdapter);
        spBookingDetailRegion.setSelection(0);

        //Get Fees from database
        fees = new ArrayList<>();
        //Insert blank Fee
        Fee fakeFee = new Fee("","",0,"");
        fees.add(fakeFee);
        fees.addAll(dataSource.getFees());
        ArrayAdapter<Fee> feeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,fees);
        feeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBookingDetailFee.setAdapter(feeAdapter);
        spBookingDetailFee.setSelection(0);

        //Get classes from database
        bookClasses = new ArrayList<>();
        //Insert blank Class
        BookClass fakeBookClass = new BookClass("","","");
        bookClasses.add(fakeBookClass);
        bookClasses.addAll(dataSource.getBookClasses());
        ArrayAdapter<BookClass> bookClassAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,bookClasses);
        bookClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBookingDetailClass.setAdapter(bookClassAdapter);
        spBookingDetailClass.setSelection(0);

        //Get products with suppliers from database
        products = new ArrayList<>();
        //Insert blank Product
        Product fakeProduct = new Product(0,"");
        products.add(fakeProduct);
        products.addAll(dataSource.getProductsWithSuppliers());
        ArrayAdapter<Product> productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,products);
        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBookingDetailProduct.setAdapter(productAdapter);
        spBookingDetailProduct.setSelection(0);

        //Get suppliers with products from database
        suppliers = new ArrayList<>();
        //Insert blank Supplier
        Supplier fakeSupplier = new Supplier(0,"");
        suppliers.add(fakeSupplier);
        suppliers.addAll(dataSource.getSuppliersWithProducts());
        supplierAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,suppliers);
        supplierAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBookingDetailSupplier.setAdapter(supplierAdapter);
        spBookingDetailSupplier.setSelection(0);

        //Get mode from intent
        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        booking=(Booking)intent.getSerializableExtra("Booking");
        //Verify mode
        if (mode.equals("update"))
        {
            btnBookingDetailDelete.setEnabled(true);
            bookingDetail =(BookingDetail) intent.getSerializableExtra("BookingDetail");
            tvBookingDetailBookingNo.setText(booking.getBookingNo());
            tvBookingDetailItinerary.setText(String.format("%.2f",bookingDetail.getItineraryNo()));
            @SuppressLint("SimpleDateFormat") DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
            etBookingDetailTripStart.setText(bookingDetail.getTripStart()==null?"":df.format(bookingDetail.getTripStart()));
            etBookingDetailTripEnd.setText(bookingDetail.getTripEnd()==null?"":df.format(bookingDetail.getTripEnd()));
            etBookingDetailDesc.setText(bookingDetail.getDescription());
            etBookingDetailDestination.setText(bookingDetail.getDestination());
            etBookingDetailBasePrice.setText(String.format("%.2f",bookingDetail.getBasePrice()));
            etBookingDetailAgCom.setText(String.format("%.2f",bookingDetail.getAgencyCommission()));
            //Region Combo Box
            int help=0;
            for (Region r : regions)
            {
                if (bookingDetail.getRegionId().equals(r.getRegionId()))
                    break;
                else
                    help++;
            }
            spBookingDetailRegion.setSelection(help==regions.size()?0:help);
            //Fee Combo Box
            help=0;
            for (Fee f : fees)
            {
                if (bookingDetail.getFeedId().equals(f.getFeeId()))
                    break;
                else
                    help++;
            }
            spBookingDetailFee.setSelection(help);
            //Class Combo Box
            help=0;
            for (BookClass bc : bookClasses)
            {
                if (bookingDetail.getClassId().equals(bc.getClassId()))
                    break;
                else
                    help++;
            }
            spBookingDetailClass.setSelection(help);

            //Product Combo Box
            product = dataSource.getProductByPSId(bookingDetail.getProductSupplierId());
            help=0;
            for (Product p : products)
            {
                if (product.getProductId()==p.getProductId())
                    break;
                else
                    help++;
            }
            spBookingDetailProduct.setSelection(help);

            //Supplier Combo Box
            supplier = dataSource.getSupplierByPSId(bookingDetail.getProductSupplierId());
            help=0;
            for (Supplier s : suppliers)
            {
                if (supplier.getSupplierId()==s.getSupplierId())
                    break;
                else
                    help++;
            }
            spBookingDetailSupplier.setSelection(help);
        }
        else
        {
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
            bookingDetail = new BookingDetail();

        }

        //Class Select Combo Box
        spBookingDetailClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bookingDetail.setClassId(bookClasses.get(position).getClassId());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Fee Selection Combo Box
        spBookingDetailFee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bookingDetail.setFeedId(fees.get(position).getFeeId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Region Selection Combo Box
        spBookingDetailRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bookingDetail.setRegionId(regions.get(position).getRegionId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Product Selection Combo Box
        spBookingDetailProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0)
                {
                    product = products.get(position);
                    //All suppliers with products
                    supplierAdapter.clear();
                    supplierAdapter.addAll(suppliers);
                    spBookingDetailSupplier.setAdapter(supplierAdapter);
                    spBookingDetailSupplier.setSelection(0);
                    spBookingDetailSupplier.setEnabled(false);
                    bookingDetail.setProductSupplierId(0);
                }

                else{
                    product = products.get(position);
                    //After product selection, display only suppliers of that product
                    productSuppliers= dataSource.getSupplierByProductId(products.get(position).getProductId());
                    if(productSuppliers.size()==1 )
                    {
                        supplier=productSuppliers.get(0);
                        bookingDetail.setProductSupplierId(dataSource.getProdSupIdByIds(supplier.getSupplierId(),product.getProductId()));
                    }

                    supplierAdapter.clear();
                    supplierAdapter.addAll(productSuppliers);
                    spBookingDetailSupplier.setAdapter(supplierAdapter);
                    spBookingDetailSupplier.setSelection(0);
                    spBookingDetailSupplier.setEnabled(true);
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
                if(product.getProductId()!=0)
                {
                    supplier=productSuppliers.get(position);
                    bookingDetail.setProductSupplierId(dataSource.getProdSupIdByIds(supplier.getSupplierId(),product.getProductId()));
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
                //Verify mode
                if (mode.equals("update"))
                {
                    @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    ParsePosition pos = new ParsePosition(0);
                    ParsePosition pos1 = new ParsePosition(0);
                    bookingDetail.setTripStart(etBookingDetailTripStart.getText() == null ? null : df.parse(etBookingDetailTripStart.getText() + "", pos));
                    bookingDetail.setTripEnd(etBookingDetailTripEnd.getText() == null ? null : df.parse(etBookingDetailTripEnd.getText() + "", pos1));
                    bookingDetail.setDescription(etBookingDetailDesc.getText()==null?null: etBookingDetailDesc.getText()+ "");
                    bookingDetail.setDestination(etBookingDetailDestination.getText() ==null?null : etBookingDetailDestination.getText()+ "");
                    bookingDetail.setBasePrice(etBookingDetailBasePrice.getText()==null?null:Double.parseDouble(etBookingDetailBasePrice.getText() + ""));
                    bookingDetail.setAgencyCommission(etBookingDetailAgCom.getText()==null?null:Double.parseDouble(etBookingDetailAgCom.getText() + ""));

                    //Verify with the update was successful
                    if(dataSource.updateBookingDetail(bookingDetail))
                    {
                        Toast.makeText(getApplicationContext(), " Product Updated!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), AddBookingActivity.class);
                        intent.putExtra("mode","update");
                        intent.putExtra("Booking",booking);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), " Product Update FAILED!", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    //Verify if Product and supplier were selected
                    if (bookingDetail.getProductSupplierId()==0) {
                        Toast.makeText(getApplicationContext(), " Product and Supplier must be selected!!", Toast.LENGTH_LONG).show();
                        if (product.getProductId()==0)
                            spBookingDetailProduct.setFocusable(true);
                        else
                            spBookingDetailSupplier.setFocusable(true);

                    }
                    else {
                        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        ParsePosition pos = new ParsePosition(0);
                        ParsePosition pos1 = new ParsePosition(0);
                        bookingDetail.setTripStart(etBookingDetailTripStart.getText() == null ? null : df.parse(etBookingDetailTripStart.getText() + "", pos));
                        bookingDetail.setTripEnd(etBookingDetailTripEnd.getText() == null ? null : df.parse(etBookingDetailTripEnd.getText() + "", pos1));
                        bookingDetail.setDescription(etBookingDetailDesc.getText()==null?null: etBookingDetailDesc.getText()+ "");
                        bookingDetail.setDestination(etBookingDetailDestination.getText() ==null?null : etBookingDetailDestination.getText()+ "");

                        try
                        {
                            bookingDetail.setAgencyCommission(Double.parseDouble(etBookingDetailAgCom.getText() + ""));
                        } catch (NumberFormatException e) {
                            bookingDetail.setAgencyCommission(0);
                        }
                        try
                        {
                            bookingDetail.setBasePrice(Double.parseDouble(etBookingDetailBasePrice.getText() + ""));
                        } catch (NumberFormatException e) {
                            bookingDetail.setBasePrice(0);
                        }
                         bookingDetail.setBookingId(booking.getBookingId());

                        //Verify with the insertion was successful
                        if (dataSource.insertBookingDetail(bookingDetail)) {
                            Toast.makeText(getApplicationContext(), " Product Inserted!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), AddBookingActivity.class);
                            intent.putExtra("mode","update");
                            intent.putExtra("Booking",booking);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), " Product Insertion FAILED!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        //Delete Button
        btnBookingDetailDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataSource.deleteBookingDetail(bookingDetail);
                Intent intent = new Intent(getApplicationContext(), AddBookingActivity.class);
                intent.putExtra("mode","update");
                intent.putExtra("Booking",booking);
                startActivity(intent);
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
}