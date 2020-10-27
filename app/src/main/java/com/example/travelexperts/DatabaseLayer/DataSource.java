//Author: Gustavo Lourenco Moises
//Thread Project - Group 1
//OOSD Program Spring 2020
//Date:9/30/2020
//Travel Agency Application

package com.example.travelexperts.DatabaseLayer;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.travelexperts.BusinessLayer.Affiliation;
import com.example.travelexperts.BusinessLayer.Agency;
import com.example.travelexperts.BusinessLayer.Agent;
import com.example.travelexperts.BusinessLayer.BookClass;
import com.example.travelexperts.BusinessLayer.Booking;
import com.example.travelexperts.BusinessLayer.BookingDetail;
import com.example.travelexperts.BusinessLayer.Customer;
import com.example.travelexperts.BusinessLayer.Fee;
import com.example.travelexperts.BusinessLayer.ProdPackage;
import com.example.travelexperts.BusinessLayer.Product;
import com.example.travelexperts.BusinessLayer.ProductSupplier;
import com.example.travelexperts.BusinessLayer.Region;
import com.example.travelexperts.BusinessLayer.Reward;
import com.example.travelexperts.BusinessLayer.Supplier;
import com.example.travelexperts.BusinessLayer.TripType;

import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.ceil;


public class DataSource {
    //Local Variables
    private Context context;
    private SQLiteDatabase db;
    private DBHelper helper;

    public DataSource(Context context) {
        this.context=context;
        Log.d("harv","context: "+context);
        //Create Helper and open database
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    //AFFILIATION -----------------------------------------------------------------------------------------------

    //Update Affiliation in the database
    // http://localhost:8080/JSPDay3RESTExample/rs/affiliation/updateaffiliation/{oldAffiliationId}
    //INSERT THE OLD Primary KEY to Be Updated
    public boolean updateAffiliation(Affiliation affiliation, String oldAffiliationId){
        ContentValues cv = new ContentValues();
        cv.put("AffilitationId",affiliation.getAffiliationId()+"");
        cv.put("AffName", affiliation.getAffName());
        cv.put("AffDesc", affiliation.getAffDesc());
        String [] args = {oldAffiliationId+""};
        String where = "AffilitationId=?";
        return db.update("Affiliations", cv, where, args) != -1;
    }

    //Insert Affiliation in the database
    // http://localhost:8080/JSPDay3RESTExample/rs/affiliation/putaffiliation
    public boolean insertAffiliation(Affiliation affiliation)
    {
        ContentValues cv = new ContentValues();
        cv.put("AffilitationId",affiliation.getAffiliationId()+"");
        cv.put("AffName", affiliation.getAffName());
        cv.put("AffDesc", affiliation.getAffDesc());
        return db.insert("Affiliations", null, cv) != -1;
    }

    //Delete Affiliation from Database
    //http://localhost:8080/JSPDay3RESTExample/rs/affiliation/deleteaffiliation/{AffiliationId}
    public boolean deleteAffiliation(Affiliation affiliation){
        String [] args = {affiliation.getAffiliationId()+""};
        String where = "AffilitationId=?";
        return db.delete("Affiliations", where, args) != -1;
    }

    //Get all Affiliations from the database
    //http://localhost:8080/JSPDay3RESTExample/rs/affiliation/getaffiliations
    public ArrayList<Affiliation> getAffiliations()
    {
        ArrayList<Affiliation> affiliations = new ArrayList<>();
        String [ ] columns = {"AffilitationId","AffName","AffDesc"};
        Cursor cursor = db.query("Affiliations",columns,null,null,null,null,"AffilitationId");

        while (cursor.moveToNext())
        {
            affiliations.add(new Affiliation(cursor.getString(0),cursor.getString(1),cursor.getString(2)));
        }
        cursor.close();
        return affiliations;
    }
    //AGENCY-----------------------------------------------------------------------------------------------------------------
    //Get all the agencies from database
    // http://localhost:8080/JSPDay3RESTExample/rs/agency/getagencies
    public ArrayList<Agency> getAllAgencies()
    {
        ArrayList<Agency> agencies = new ArrayList<>();
        String [ ] columns = {"AgencyId"};
        Cursor cursor = db.query("Agencies",columns,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            agencies.add(new Agency(cursor.getInt(0)));
        }
        cursor.close();
        return  agencies;
    }


    //AGENT --------------------------------------------------------------------------------------------------------------
    // Get Agent by id
    // http://localhost:8080/JSPDay3RESTExample/rs/agent/getagent/{agentId}
    public Agent getAgent(int agentId)
    {
        String sql = "SELECT * FROM Agents WHERE AgentId=?";
        String [] args = {agentId+ ""};
        Cursor cursor = db.rawQuery(sql, args);
        //position the cursor on the next/first row
        cursor.moveToNext();
        //create a product using this row
        Agent agent = new Agent(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getInt(7),cursor.getString(8),cursor.getString(9));
        cursor.close();

        return agent;
    }

    //Get all agents from database
    // http://localhost:8080/JSPDay3RESTExample/rs/agent/getagents
    public ArrayList<Agent> getAllAgents()
    {
        ArrayList<Agent> products = new ArrayList<>();
        String [ ] columns = {"AgentId","AgtFirstName","AgtMiddleInitial","AgtLastName","AgtBusPhone", "AgtEmail","AgtPosition","AgencyId","userid","password"};
        Cursor cursor = db.query("Agents",columns,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            products.add(new Agent(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getInt(7),cursor.getString(8),cursor.getString(9)));
        }
        cursor.close();
        return  products;
    }

    //Insert agent in the database
    // http://localhost:8080/JSPDay3RESTExample/rs/agent/putagent
    public boolean insertAgent(Agent agent)
    {
        ContentValues cv = new ContentValues();
        cv.put("AgtFirstName",agent.getAgtFirstName().equals("")?null:agent.getAgtFirstName());
        cv.put("AgtMiddleInitial", agent.getAgtMiddleInitial().equals(null)?null:agent.getAgtMiddleInitial());
        cv.put("AgtLastName", agent.getAgtLastName().equals(null)?null:agent.getAgtLastName());
        cv.put("AgtBusPhone", agent.getAgtBusPhone().equals(null)?null:agent.getAgtBusPhone());
        cv.put("AgtEmail",agent.getAgtEmail().equals(null)?null:agent.getAgtEmail());
        cv.put("AgtPosition",agent.getAgtPosition().equals(null)?null:agent.getAgtPosition());
        cv.put("AgencyId", agent.getAgencyId()==0?null:agent.getAgencyId());

        return db.insert("Agents", null, cv) != -1;
    }

    //Update Agent in the database
    // http://localhost:8080/JSPDay3RESTExample/rs/updateagent
    public boolean updateAgent(Agent agent){
        ContentValues cv = new ContentValues();
        cv.put("AgtFirstName",agent.getAgtFirstName().equals("")?null:agent.getAgtFirstName());
        cv.put("AgtMiddleInitial", agent.getAgtMiddleInitial().equals(null)?null:agent.getAgtMiddleInitial());
        cv.put("AgtLastName", agent.getAgtLastName().equals(null)?null:agent.getAgtLastName());
        cv.put("AgtBusPhone", agent.getAgtBusPhone().equals(null)?null:agent.getAgtBusPhone());
        cv.put("AgtEmail",agent.getAgtEmail().equals(null)?null:agent.getAgtEmail());
        cv.put("AgtPosition",agent.getAgtPosition().equals(null)?null:agent.getAgtPosition());
        cv.put("AgencyId", agent.getAgencyId()==0?null:agent.getAgencyId());
        String [] args = {agent.getAgentId()+""};
        String where = "AgentId=?";
        return db.update("Agents", cv, where, args) != -1;
    }
    //Delete Agent from Database
    // http://localhost:8080/JSPDay3RESTExample/rs/agent/deleteagent/{ agentId }
    public boolean deleteAgent(Agent agent){
        String [] args = {agent.getAgentId()+""};
        String where = "AgentId=?";
        return db.delete("Agents", where, args) != -1;
    }

     //BOOKING------------------------------------------------------------------------------------------------------------------

    //Update Booking in the database
    // http://localhost:8080/JSPDay3RESTExample/rs/booking/updatebooking
    public boolean updateBooking(Booking booking){
        ContentValues cv = new ContentValues();
        @SuppressLint("SimpleDateFormat") DateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        cv.put("BookingDate",df.format(booking.getBookingDate()));
        cv.put("TravelerCount",booking.getTravelerCount());
        cv.put("BookingNo", booking.getBookingNo().equals(null)?null:booking.getBookingNo());
        cv.put("CustomerId", booking.getCustomerId()==0?null:booking.getCustomerId());
        cv.put("TripTypeId", Character.toString(booking.getTripTypeId()).equals("0")?null:Character.toString(booking.getTripTypeId()));
        cv.put("PackageId",booking.getPackageId()==0?null:booking.getPackageId());
        String [] args = {booking.getBookingId()+""};
        String where = "BookingId=?";
        return db.update("Bookings", cv, where, args) != -1;
    }

    //Insert Booking in the database
    // http://localhost:8080/JSPDay3RESTExample/rs/booking/putbooking
    public Booking insertBooking(Booking booking)
    {
        ContentValues cv = new ContentValues();
        Date currentDate = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") DateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        booking.setBookingDate(currentDate);
        cv.put("BookingDate",df.format(booking.getBookingDate()));
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
        cv.put("BookingNo", booking.getBookingNo());
        cv.put("TravelerCount",booking.getTravelerCount());
        cv.put("CustomerId", booking.getCustomerId()==0?null:booking.getCustomerId());
        cv.put("TripTypeId", Character.toString(booking.getTripTypeId()).equals("0")?null:Character.toString(booking.getTripTypeId()));
        cv.put("PackageId",booking.getPackageId()==0?null:booking.getPackageId());

        if(db.insert("Bookings",null,cv)!=-1)
        {
            booking.setBookingId(findBookingIdByBookingNo(booking.getBookingNo()));
        }
        else
        {
            //If an error occurs, set booking id to 0
            booking.setBookingId(0);
        }
        return booking;
    }

    //Find BookingId by BookingNo
    // http://localhost:8080/JSPDay3RESTExample/rs/booking/findBookingIdByBookingNo/{ bookingNo }
    private int findBookingIdByBookingNo(String bookingNo) {
        String sql = "SELECT BookingId FROM Bookings WHERE BookingNo=?";
        String [] args = {bookingNo+ ""};
        Cursor cursor = db.rawQuery(sql, args);
        cursor.moveToNext();
        int bookingId = cursor.getInt(0);
        cursor.close();
        return bookingId;
    }

    //Get all Bookings from database
    // http://localhost:8080/JSPDay3RESTExample/rs/booking/getbookings
    public ArrayList<Booking> getBookings()
    {
        ArrayList<Booking> bookings = new ArrayList<>();
        String [ ] columns = {"BookingId","BookingDate","BookingNo","TravelerCount","CustomerId", "tripTypeId","packageId"};
        Cursor cursor = db.query("Bookings",columns,null,null,null,null,"BookingDate"+" DESC",null);

        while (cursor.moveToNext())
        {
            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date =new Date();
            try {
                date = dateFormat.parse(cursor.getString(1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            char tripType;
            if (cursor.getString(5)==null)
                tripType='0';
            else
                tripType=cursor.getString(5).charAt(0);


            bookings.add(new Booking(cursor.getInt(0), date,cursor.getString(2),cursor.getDouble(3),cursor.getInt(4),tripType,cursor.getInt(6)));
        }
        cursor.close();
        return  bookings;
    }


    //BOOKING DETAIL -------------------------------------------------------------------------------------------------------------

    //Insert Booking Detail in the database
   // http://localhost:8080/JSPDay3RESTExample/rs/bookingdetail/putbookingdetail
    public boolean insertBookingDetail(BookingDetail bookingDetail)
    {
        ContentValues cv = new ContentValues();
        Random random = new Random();

        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cv.put("TripStart",bookingDetail.getTripStart()==null?null:df.format(bookingDetail.getTripStart()));
        cv.put("TripEnd",bookingDetail.getTripEnd()==null?null:df.format(bookingDetail.getTripEnd()));
        cv.put("Description", bookingDetail.getDescription().equals(null)?null:bookingDetail.getDescription());
        cv.put("Destination", bookingDetail.getDestination().equals(null)?null:bookingDetail.getDestination());
        cv.put("BasePrice", bookingDetail.getBasePrice()==0?null:bookingDetail.getBasePrice());
        cv.put("AgencyCommission", bookingDetail.getAgencyCommission()==0?null:bookingDetail.getAgencyCommission());
        cv.put("RegionId", bookingDetail.getRegionId());
        cv.put("ClassId", bookingDetail.getClassId());
        cv.put("FeeId", bookingDetail.getFeedId());
        cv.put("ProductSupplierId", bookingDetail.getProductSupplierId()==0?null:bookingDetail.getProductSupplierId());
        cv.put("BookingId", bookingDetail.getBookingId()==0?null:bookingDetail.getBookingId());
        cv.put("ItineraryNo", ceil(1000*random.nextDouble()));

        return db.insert("BookingDetails", null, cv) != -1;
    }

    //Update Booking Detail in the database
    //http://localhost:8080/JSPDay3RESTExample/rs/bookingdetail/updatebookingdetail
    public boolean updateBookingDetail(BookingDetail bookingDetail){
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ContentValues cv = new ContentValues();
        cv.put("TripStart",bookingDetail.getTripStart()==null?null:df.format(bookingDetail.getTripStart()));
        cv.put("TripEnd",bookingDetail.getTripEnd()==null?null:df.format(bookingDetail.getTripEnd()));
        cv.put("Description", bookingDetail.getDescription().equals(null)?null:bookingDetail.getDescription());
        cv.put("Destination", bookingDetail.getDestination().equals(null)?null:bookingDetail.getDestination());
        cv.put("BasePrice", bookingDetail.getBasePrice()==0?null:bookingDetail.getBasePrice());
        cv.put("AgencyCommission", bookingDetail.getAgencyCommission()==0?null:bookingDetail.getAgencyCommission());
        cv.put("RegionId", bookingDetail.getRegionId());
        cv.put("ClassId", bookingDetail.getClassId());
        cv.put("FeeId", bookingDetail.getFeedId());
        cv.put("ProductSupplierId", bookingDetail.getProductSupplierId()==0?null:bookingDetail.getProductSupplierId());
        String [] args = {bookingDetail.getBookingDetailId()+""};
        String where = "BookingDetailId=?";
        return db.update("BookingDetails", cv, where, args) != -1;
    }
    //Delete Booking Detail from Database
    //http://localhost:8080/JSPDay3RESTExample/rs/bookingdetail/deletebookingdetail/{bookingdetailid}
    public boolean deleteBookingDetail(BookingDetail bookingDetail){
        ContentValues cv = new ContentValues();
        //Instead of deleting the booking detail, the bookingId is multiplied by -1
        cv.put("BookingId",-bookingDetail.getBookingId());
        String [] args = {bookingDetail.getBookingDetailId()+""};
        String where = "BookingDetailId=?";
        return db.update("BookingDetails", cv, where, args) != -1;
    }

      //Get List of Booking Details by BookingId
      // http://localhost:8080/JSPDay3RESTExample/rs/bookingdetail/getnookingdetailbybookingid/{bookingId}
    public ArrayList<BookingDetail> getBookingDetailByBookingId(int bookingId)
    {
        ArrayList<BookingDetail> bookingDetails = new ArrayList<>();
        String sql = "SELECT * FROM BookingDetails WHERE BookingId=?";
        String [] args = {bookingId+ ""};
        Cursor cursor = db.rawQuery(sql, args);

        while (cursor.moveToNext())
        {
            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateStart;
            Date dateEnd;

            try {
                     dateStart = dateFormat.parse(cursor.getString(2));
            } catch (Exception e) {
                dateStart=null;
            }
            try
            {
                dateEnd = dateFormat.parse(cursor.getString(3));

            } catch (Exception e) {
                dateEnd=null;
            }
            bookingDetails.add(new BookingDetail(cursor.getInt(0), cursor.getDouble(1),dateStart,dateEnd,cursor.getString(4),cursor.getString(5),cursor.getDouble(6),cursor.getDouble(7),cursor.getInt(8),cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getInt(12)));
        }
        cursor.close();
        return  bookingDetails;

    }

    //CLASS (BOOK CLASS) -------------------------------------------------------------------------------------------------------------


       //Update Class in the database
       //INSERT THE OLD Primary KEY to Be Updated
        // http://localhost:8080/JSPDay3RESTExample/rs/class/updateclass/{oldBookClassId}

    public boolean updateBookClass(BookClass bookClass){
        ContentValues cv = new ContentValues();
        cv.put("ClassId",bookClass.getClassId()+"");
        cv.put("ClassName", bookClass.getClassName());
        cv.put("ClassDesc", bookClass.getClassDes());
        String [] args = {bookClass.getClassId()+""};
        String where = "ClassId=?";
        return db.update("Classes", cv, where, args) != -1;
    }

    //Insert Class in the database
    // http://localhost:8080/JSPDay3RESTExample/rs/class/putclass
    public boolean insertBookClass(BookClass bookClass)
    {
        ContentValues cv = new ContentValues();
        cv.put("ClassId",bookClass.getClassId()+"");
        cv.put("ClassName", bookClass.getClassName());
        cv.put("ClassDesc", bookClass.getClassDes());
        return db.insert("Classes", null, cv) != -1;
    }
    //Delete Class from Database
    // http://localhost:8080/JSPDay3RESTExample/rs/class/deleteclass/{ bookclassId }
    public boolean deleteBookClass(BookClass bookClass){
        String [] args = {bookClass.getClassId()+""};
        String where = "ClassId=?";
        return db.delete("Classes", where, args) != -1;
    }

    //Get all classes from database
    // http://localhost:8080/JSPDay3RESTExample/rs/class/getclasses
    public ArrayList<BookClass> getBookClasses()
    {
        ArrayList<BookClass> bookClasses = new ArrayList<>();
        String [ ] columns = {"ClassId","ClassName", "ClassDesc"};
        Cursor cursor = db.query("Classes",columns,null,null,null,null,"ClassName");
        while (cursor.moveToNext())
        {
            bookClasses.add(new BookClass(cursor.getString(0),cursor.getString(1),cursor.getString(2)));
        }
        cursor.close();
        return  bookClasses;
    }

    //CUSTOMER----------------------------------------------------------------------------------------------------------------------

    //Get Customer by id
    // http://localhost:8080/JSPDay3RESTExample/rs/customer/getcustomer/{ customerId }
    public Customer getCustomerById (int customerId)
    {
        String sql = "SELECT * FROM Customers WHERE CustomerId=?";
        String [] args = {customerId+ ""};
        Cursor cursor = db.rawQuery(sql, args);
        //position the cursor on the next/first row
        cursor.moveToNext();
        //create a product using this row
        Customer customer= new Customer(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getInt(11));
        cursor.close();
        return  customer;   }

    //Get all Customers
    // http://localhost:8080/JSPDay3RESTExample/rs/customer/getcustomers
    public ArrayList<Customer> getCustomers()
    {
        ArrayList<Customer> customers = new ArrayList<>();
        String [ ] columns = {"CustomerId","CustFirstName","CustLastName", "CustAddress", "CustCity","CustProv","CustPostal","CustCountry","CustHomePhone","CustBusPhone","CustEmail", "AgentId"};
        Cursor cursor = db.query("Customers",columns,null,null,null,null,"CustFirstName");

        while (cursor.moveToNext())
        {
            customers.add(new Customer(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getInt(11)));
        }
        cursor.close();
        return  customers;
    }

    //FEE ----------------------------------------------------------------------------------------------------------------------
    //Get all fees from the database
    // http://localhost:8080/JSPDay3RESTExample/rs/fee/getfees
    public ArrayList<Fee> getFees()
    {
        ArrayList<Fee> fees = new ArrayList<>();
        String [ ] columns = {"FeeId","FeeName","FeeAmt", "FeeDesc"};
        Cursor cursor = db.query("Fees",columns,null,null,null,null,"FeeName");

        while (cursor.moveToNext())
        {
            fees.add(new Fee(cursor.getString(0),cursor.getString(1),cursor.getDouble(2),cursor.getString(3)));
        }
        cursor.close();
        return  fees;
    }

    //PACKAGE--------------------------------------------------------------------------------------------------------------------------

    //Get Package by id
    // http://localhost:8080/JSPDay3RESTExample/rs/package/getpackage/{ packageId }
    public ProdPackage getPackageById(int packageId)
    {
        String sql = "SELECT * FROM Packages WHERE PackageId=?";
        String [] args = {packageId+ ""};
        Cursor cursor = db.rawQuery(sql, args);
        //position the cursor on the next/first row
        cursor.moveToNext();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date pkgStartDate =new Date();
        Date pkgEndDate =new Date();

        try {
            pkgStartDate = dateFormat.parse(cursor.getString(2));
            pkgEndDate = dateFormat.parse(cursor.getString(3));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ProdPackage prodPackage= new ProdPackage(cursor.getInt(0),cursor.getString(1),pkgStartDate.toString(),pkgEndDate.toString(),cursor.getString(4),cursor.getDouble(5),cursor.getDouble(6)) ;
        cursor.close();
        return prodPackage;
    }

    //Get All Packages
    //http://localhost:8080/JSPDay3RESTExample/rs/package/getpackages
    public ArrayList<ProdPackage> getPackages()
    {
        ArrayList<ProdPackage> packages = new ArrayList<>();
        String [ ] columns = {"PackageId","PkgName","PkgStartDate", "PkgEndDate", "PkgDesc","PkgBasePrice","PkgAgencyCommission"};
        Cursor cursor = db.query("Packages",columns,null,null,null,null,"PkgName");
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date pkgStartDate =new Date();
        Date pkgEndDate =new Date();

        while (cursor.moveToNext())
        {
            try {
                pkgStartDate = dateFormat.parse(cursor.getString(2));
                pkgEndDate = dateFormat.parse(cursor.getString(3));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            packages.add(new ProdPackage(cursor.getInt(0),cursor.getString(1),pkgStartDate.toString(),pkgEndDate.toString(),cursor.getString(4),cursor.getDouble(5),cursor.getDouble(6)));
        }
        cursor.close();
        return  packages;
    }

    //PRODUCT (PRODUCT ITEM)---------------------------------------------------------------------------------------------------------

    //Update Product in the database
    // http://localhost:8080/JSPDay3RESTExample/rs/product/updateproduct
    public boolean updateProductItem(Product product){
        ContentValues cv = new ContentValues();
        cv.put("ProdName", product.getProdName());
        String [] args = {product.getProductId()+""};
        String where = "ProductId=?";
        return db.update("Products", cv, where, args) != -1;
    }

    //Insert Product in the database
    // http://localhost:8080/JSPDay3RESTExample/rs/product/putproduct
    public boolean insertProductItem(Product product)
    {
        ContentValues cv = new ContentValues();
        cv.put("ProdName", product.getProdName());
        return db.insert("Products", null, cv) != -1;
    }

    //Delete Product from Database
    // http://localhost:8080/JSPDay3RESTExample/rs/product/deleteproduct/{ productId }
    public boolean deleteProductItem(Product product){
        String [] args = {product.getProductId()+""};
        String where = "ProductId=?";
        return db.delete("Products", where, args) != -1;
    }

    //Get all products
    //http://localhost:8080/JSPDay3RESTExample/rs/product/getproducts
    public ArrayList<Product> getProducts()
    {
        ArrayList<Product> products = new ArrayList<>();
        String MY_QUERY = "SELECT ProductId, ProdName FROM Products  ORDER BY ProdName";
        Cursor cursor =  db.rawQuery(MY_QUERY,null);
        while (cursor.moveToNext())
        {
            products.add(new Product(cursor.getInt(0),cursor.getString(1)));
        }
        cursor.close();
        return  products;
    }

    //Get Products with Suppliers
    // http://localhost:8080/JSPDay3RESTExample/rs/product/getproductswithsuppliers
    public ArrayList<Product> getProductsWithSuppliers()
    {
        ArrayList<Product> products = new ArrayList<>();
        String MY_QUERY = "SELECT DISTINCT b.ProductId, b.ProdName FROM Products_suppliers a INNER JOIN Products  b ON a.ProductId=b.ProductId ORDER BY b.ProdName";
        Cursor cursor =  db.rawQuery(MY_QUERY,null);

        while (cursor.moveToNext())
        {
            products.add(new Product(cursor.getInt(0),cursor.getString(1)));
        }
        cursor.close();
        return  products;
    }

    //Get Product by product-supplierID
    // http://localhost:8080/JSPDay3RESTExample/rs/product/getproductbypsid/{ productSupplierId }
    public Product getProductByPSId(int productSupplierId)
    {
        String MY_QUERY = "SELECT DISTINCT b.ProductId, b.ProdName FROM Products_suppliers a INNER JOIN Products  b ON a.ProductId=b.ProductId WHERE a.ProductSupplierId=? ORDER BY b.ProdName";
        String [] args = {productSupplierId+ ""};
        Cursor cursor =  db.rawQuery(MY_QUERY,args);
        cursor.moveToNext();
        Product product =new Product(cursor.getInt(0),cursor.getString(1));
        cursor.close();
        return product;
    }
    //PRODUCT-SUPPLIER-----------------------------------------------------------------------------------------------------
    // Get all supplier-Products of a BookingId
    // http://localhost:8080/JSPDay3RESTExample/rs/productsupplier/getpkgproductsbypkgId/{ packageId }
    public ArrayList<ProductSupplier> getPkgProductsByPkgId(int packageId)
    {
        ArrayList<ProductSupplier> packageProducts = new ArrayList<>();
        String sql = "SELECT * FROM packages_products_suppliers WHERE PackageId=?";
        String [] args = {packageId+ ""};
        Cursor cursor = db.rawQuery(sql, args);

        while (cursor.moveToNext())
        {
            packageProducts.add(new ProductSupplier(cursor.getInt(0), cursor.getInt(1),cursor.getInt(2)));
        }
        cursor.close();
        return  packageProducts;
    }

    //Get product-supplierId by product Id and supplier Id
    // http://localhost:8080/JSPDay3RESTExample/rs/productsupplier/getProdSupIdByIds/{supplierId}/{productId}
    public int getProdSupIdByIds(int supplierId, int productId)
    {
        String MY_QUERY = "SELECT DISTINCT a.ProductSupplierId FROM Products_suppliers a INNER JOIN Suppliers  b ON a.SupplierId=b.SupplierId WHERE a.ProductId=? and b.SupplierId=?";
        String [] args = {productId+ "",supplierId+""};
        Cursor cursor =  db.rawQuery(MY_QUERY,args);
        cursor.moveToNext();
        int a =cursor.getInt(0);
        cursor.close();
        return  a;
    }

    //REGION---------------------------------------------------------------------------------------------------------------------------

    //Update Region in the database
    //INSERT THE OLD Primary KEY to Be Updated
    // http://localhost:8080/JSPDay3RESTExample/rs/region/updateregion/{oldRegionId}
    public boolean updateRegion(Region region){
        ContentValues cv = new ContentValues();
        cv.put("RegionId",region.getRegionId()+"");
        cv.put("RegionName", region.getRegionName());
        String [] args = {region.getRegionId()+""};
        String where = "RegionId=?";
        return db.update("Regions", cv, where, args) != -1;
    }

    //Insert Region in the database
    // http://localhost:8080/JSPDay3RESTExample/rs/region/putregion
    public boolean insertRegion(Region region)
    {
        ContentValues cv = new ContentValues();
        cv.put("RegionId",region.getRegionId()+"");
        cv.put("RegionName", region.getRegionName());
        return db.insert("Regions", null, cv) != -1;
    }

    //Delete Region from Database
    // http://localhost:8080/JSPDay3RESTExample/rs/region/deleteregion/{regionId}
    public boolean deleteRegion(Region region){
        String [] args = {region.getRegionId()+""};
        String where = "RegionId=?";
        return db.delete("Regions", where, args) != -1;
    }

    //Get all regions from database
    // http://localhost:8080/JSPDay3RESTExample/rs/region/getregions
    public ArrayList<Region> getRegions()
    {
        ArrayList<Region> regions = new ArrayList<>();
        String [ ] columns = {"RegionId","RegionName"};
        Cursor cursor = db.query("Regions",columns,null,null,null,null,"RegionName");

        while (cursor.moveToNext())
        {
            regions.add(new Region(cursor.getString(0),cursor.getString(1)));
        }
        cursor.close();
        return  regions;
    }

    //REWARD---------------------------------------------------------------------------------------------------------------------------

    //Update Reward in the database
    // http://localhost:8080/JSPDay3RESTExample/rs/reward/updatereward/{rewardId}
    public boolean updateReward(Reward reward){
        ContentValues cv = new ContentValues();
        cv.put("RwdName", reward.getRwdName());
        cv.put("RwdDesc", reward.getRwdDesc());
        String [] args = {reward.getRewardId()+""};
        String where = "RewardId=?";
        return db.update("Rewards", cv, where, args) != -1;
    }

    //Insert Reward in the database
    // http://localhost:8080/JSPDay3RESTExample/rs/reward/putreward
    public boolean insertReward(Reward reward)
    {
        ContentValues cv = new ContentValues();
        Cursor cursor =db.rawQuery("Select RewardId from Rewards order by RewardId DESC limit 1",null);
        cursor.moveToNext();
        int a =cursor.getInt(0);
        cursor.close();
        cv.put("RewardId",a+1);
        cv.put("RwdName", reward.getRwdName());
        cv.put("RwdDesc", reward.getRwdDesc());
        return db.insert("Rewards", null, cv) != -1;
    }

    //Delete Reward from Database
    // http://localhost:8080/JSPDay3RESTExample/rs/reward/deletereward/{ rewardId }
    public boolean deleteReward(Reward reward){
        String [] args = {reward.getRewardId()+""};
        String where = "RewardId=?";
        return db.delete("Rewards", where, args) != -1;
    }

    //Get all Rewards from database
    // http://localhost:8080/JSPDay3RESTExample/rs/reward/getrewards
    public ArrayList<Reward> getRewards()
    {
        ArrayList<Reward> rewards = new ArrayList<>();
        String [ ] columns = {"RewardId","RwdName","RwdDesc"};
        Cursor cursor = db.query("Rewards",columns,null,null,null,null,"RwdName");

        while (cursor.moveToNext())
        {
            rewards.add(new Reward(cursor.getInt(0),cursor.getString(1),cursor.getString(2)));
        }
        cursor.close();
        return rewards;
    }

    //SUPPLIER---------------------------------------------------------------------------------------------------------------------------

    //Update Supplier in the database
    // http://localhost:8080/JSPDay3RESTExample/rs/supplier/updatesupplier
    public boolean updateSupplier(Supplier supplier){
        ContentValues cv = new ContentValues();
        cv.put("SupName", supplier.getSupName());
        String [] args = {supplier.getSupplierId()+""};
        String where = "SupplierId=?";
        return db.update("Suppliers", cv, where, args) != -1;
    }

    //Insert Supplier in the database
    // http://localhost:8080/JSPDay3RESTExample/rs/supplier/putsupplier
    public boolean insertSupplier(Supplier supplier)
    {
        ContentValues cv = new ContentValues();
        cv.put("SupName", supplier.getSupName());
        Cursor cursor =db.rawQuery("Select SupplierId from Suppliers order by SupplierId DESC limit 1",null);
        cursor.moveToNext();
        int a =cursor.getInt(0);
        cursor.close();
        cv.put("SupplierId",a+1);
        return db.insert("Suppliers", null, cv) != -1;
    }

    //Delete Supplier from Database
    // http://localhost:8080/JSPDay3RESTExample/rs/supplier/deletesupplier/{ supplierId }
    public boolean deleteSupplier(Supplier supplier){
        String [] args = {supplier.getSupplierId()+""};
        String where = "SupplierId=?";
        return db.delete("Suppliers", where, args) != -1;
    }

    //Get all Suppliers
    // http://localhost:8080/JSPDay3RESTExample/rs/supplier/getsuppliers
    public ArrayList<Supplier> getSuppliers()
    {
        ArrayList<Supplier> suppliers = new ArrayList<>();
        String MY_QUERY = "SELECT SupplierId, SupName FROM Suppliers  ORDER BY SupName";
        Cursor cursor =  db.rawQuery(MY_QUERY,null);
        while (cursor.moveToNext())
        {
            suppliers.add(new Supplier(cursor.getInt(0),cursor.getString(1)));
        }
        cursor.close();
        return  suppliers;
    }

    //Get Supplier with Products
    // http://localhost:8080/JSPDay3RESTExample/rs/supplier/getsupplierswithproducts
    public ArrayList<Supplier> getSuppliersWithProducts()
    {
        ArrayList<Supplier> suppliers = new ArrayList<>();
        String MY_QUERY = "SELECT DISTINCT b.SupplierId, b.SupName FROM Products_suppliers a INNER JOIN Suppliers  b ON a.SupplierId=b.SupplierId ORDER BY b.SupName";
        Cursor cursor = db.rawQuery(MY_QUERY,null);

        while (cursor.moveToNext())
        {
            suppliers.add(new Supplier(cursor.getInt(0),cursor.getString(1)));
        }
        cursor.close();
        return  suppliers;
    }

    //Get Supplier by product-supplier Id
    // http://localhost:8080/JSPDay3RESTExample/rs/supplier/getsupplierbypsid/{ productSupplierId }
    public Supplier getSupplierByPSId(int productSupplierId)
    {
        String MY_QUERY = "SELECT DISTINCT b.SupplierId, b.SupName FROM Products_suppliers a INNER JOIN Suppliers  b ON a.SupplierId=b.SupplierId WHERE a.ProductSupplierId=? ORDER BY b.SupName";
        String [] args = {productSupplierId+ ""};
        Cursor cursor =  db.rawQuery(MY_QUERY,args);
        cursor.moveToNext();
        Supplier supplier=new Supplier(cursor.getInt(0),cursor.getString(1));
        cursor.close();
        return supplier;
    }

    //Get Suppliers by Product Id
    // http://localhost:8080/JSPDay3RESTExample/rs/supplier/getsupplierswithproducts/{ productId }
    public ArrayList<Supplier> getSupplierByProductId(int productId)
    {
        ArrayList<Supplier> suppliers = new ArrayList<>();
        String MY_QUERY = "SELECT DISTINCT b.SupplierId, b.SupName FROM Products_suppliers a INNER JOIN Suppliers  b ON a.SupplierId=b.SupplierId WHERE a.ProductId=? ORDER BY b.SupName";
        String [] args = {productId+ ""};
        Cursor cursor =  db.rawQuery(MY_QUERY,args);
        while (cursor.moveToNext())
        {
            suppliers.add(new Supplier(cursor.getInt(0),cursor.getString(1)));
        }
        cursor.close();
        return  suppliers;
    }

    //TRIP TYPE--------------------------------------------------------------------------------------------------------------------------

    //Update Trip Type in the database
    //INSERT THE OLD Primary KEY to Be Updated
    //http://localhost:8080/JSPDay3RESTExample/rs/tritype/updatetruptype/{oldTripTypeId}
    public boolean updateTripType(TripType tripType){
        ContentValues cv = new ContentValues();
        cv.put("TripTypeId",tripType.getTripTypeId()+"");
        cv.put("tTName", tripType.gettTName());
        String [] args = {tripType.getTripTypeId()+""};
        String where = "TripTypeId=?";
        return db.update("TripTypes", cv, where, args) != -1;
    }

    //Insert Trip Type in the database
    // http://localhost:8080/JSPDay3RESTExample/rs/triptype/puttriptype
    public boolean insertTripType(TripType tripType)
    {
        ContentValues cv = new ContentValues();
        cv.put("TripTypeId",tripType.getTripTypeId()+"");
        cv.put("tTName", tripType.gettTName());
        return db.insert("TripTypes", null, cv) != -1;
    }

    //Delete Trip Trip from Database
    // http://localhost:8080/JSPDay3RESTExample/rs/triptype/deletetriptype/{ tripTypeId }
    public boolean deleteTripType(TripType tripType){
        String [] args = {tripType.getTripTypeId()+""};
        String where = "TripTypeId=?";
        return db.delete("TripTypes", where, args) != -1;
    }

    //Get all TripTypes from database
    // http://localhost:8080/JSPDay3RESTExample/rs/triptype/gettriptypes
    public ArrayList<TripType> getTripTypes()
    {
        ArrayList<TripType> tripTypes = new ArrayList<>();
        String [ ] columns = {"TripTypeId","TTName"};
        Cursor cursor = db.query("TripTypes",columns,null,null,null,null,null);

        while (cursor.moveToNext())
        {
            tripTypes.add(new TripType(cursor.getString(0).charAt(0),cursor.getString(1)));
        }
        cursor.close();
        return  tripTypes;
    }

}
