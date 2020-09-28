//Author: Gustavo Lourenco Moises
//CPMPP 264 - Java Programming - ANDROID
//Date: 9/24/2020
//Lab 12

package com.example.travelexperts.DatabaseLayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.travelexperts.BusinessLayer.Agency;
import com.example.travelexperts.BusinessLayer.Agent;
import com.example.travelexperts.BusinessLayer.Booking;
import com.example.travelexperts.BusinessLayer.BookingDetail;
import com.example.travelexperts.BusinessLayer.Customer;
import com.example.travelexperts.BusinessLayer.ProdPackage;
import com.example.travelexperts.BusinessLayer.ProductSupplier;
import com.example.travelexperts.BusinessLayer.TripType;

import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;


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

    // Get Agent by id
    public Agent getAgent(int agentId)
    {
        String sql = "SELECT * FROM Agents WHERE AgentId=?";
        String [] args = {agentId+ ""};
        Cursor cursor = db.rawQuery(sql, args);
        //position the cursor on the next/first row
        cursor.moveToNext();
        //create a product using this row
       return  new Agent(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getInt(7));
    }

    //Get all agents from database
    public ArrayList<Agent> getAllAgents()
    {
        ArrayList<Agent> products = new ArrayList<>();
        String [ ] columns = {"AgentId","AgtFirstName","AgtMiddleInitial","AgtLastName","AgtBusPhone", "AgtEmail","AgtPosition","AgencyId"};
        Cursor cursor = db.query("Agents",columns,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            products.add(new Agent(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getInt(7)));
        }
        return  products;
    }

    //Insert agent in the database
    public boolean insertAgent(Agent agent)
    {
        ContentValues cv = new ContentValues();
        cv.put("AgtFirstName",agent.getAgtFirstName().equals("")?null:agent.getAgtFirstName());
        cv.put("AgtMiddleInitial", agent.getAgtMiddleInitial().equals(null)?null:agent.getAgtMiddleInitial());
        cv.put("AgtLastName", agent.getAgtLastName().equals(null)?null:agent.getAgtLastName());
        cv.put("AgtBusPhone", agent.getAgtBusPhone().equals(null)?null:agent.getAgtBusPhone());
        cv.put("AgtEmail",agent.getAgtEmail().equals(null)?null:agent.getAgtEmail());
        cv.put("AgtPosition",agent.getAgtPosition().equals(null)?null:agent.getAgtPosition());
        cv.put("AgencyId", agent.getAgtAgency()==0?null:agent.getAgtAgency());

        if(db.insert("Agents",null,cv)!=-1)
            return true;
        else return false;
    }

    //Update Agent in the database
    public boolean updateAgent(Agent agent){
        ContentValues cv = new ContentValues();
        cv.put("AgtFirstName",agent.getAgtFirstName().equals("")?null:agent.getAgtFirstName());
        cv.put("AgtMiddleInitial", agent.getAgtMiddleInitial().equals(null)?null:agent.getAgtMiddleInitial());
        cv.put("AgtLastName", agent.getAgtLastName().equals(null)?null:agent.getAgtLastName());
        cv.put("AgtBusPhone", agent.getAgtBusPhone().equals(null)?null:agent.getAgtBusPhone());
        cv.put("AgtEmail",agent.getAgtEmail().equals(null)?null:agent.getAgtEmail());
        cv.put("AgtPosition",agent.getAgtPosition().equals(null)?null:agent.getAgtPosition());
        cv.put("AgencyId", agent.getAgtAgency()==0?null:agent.getAgtAgency());
        String [] args = {agent.getAgentId()+""};
        String where = "AgentId=?";
        if(db.update("Agents",cv,where,args)!=-1)
            return true;
        else return false;
    }
    //Delete Agent from Database
    public boolean deleteAgent(Agent agent){
        String [] args = {agent.getAgentId()+""};
        String where = "AgentId=?";
        if(db.delete("Agents",where,args)!=-1)
            return true;
        else return false;
    }

    //Get all the agencies from database
    public ArrayList<Agency> getAllAgencies()
    {
        ArrayList<Agency> agencies = new ArrayList<>();
        String [ ] columns = {"AgencyId"};
        Cursor cursor = db.query("Agencies",columns,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            agencies.add(new Agency(cursor.getInt(0)));
        }
        return  agencies;
    }

    //Booking

    //Get all Booking from database
    public ArrayList<Booking> getBookings()
    {
        ArrayList<Booking> bookings = new ArrayList<>();
        String [ ] columns = {"BookingId","BookingDate","BookingNo","TravelerCount","CustomerId", "tripTypeId","packageId"};
        Cursor cursor = db.query("Bookings",columns,null,null,null,null,"BookingDate"+" DESC",null);

            while (cursor.moveToNext())
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date =new Date();
            try {
                date = dateFormat.parse(cursor.getString(1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Character tripType;
            if (cursor.getString(5)==null)
                tripType='0';
            else
                tripType=cursor.getString(5).charAt(0);


            bookings.add(new Booking(cursor.getInt(0), date,cursor.getString(2),cursor.getDouble(3),cursor.getInt(4),tripType,cursor.getInt(6)));
        }
        return  bookings;
    }

    //Get all supplier-Products of a BookingId

    public ArrayList<BookingDetail> getBookingDetailByBookingId(int bookingId)
    {
        ArrayList<BookingDetail> bookingDetails = new ArrayList<>();

        String sql = "SELECT * FROM BookingDetails WHERE BookingId=?";
        String [] args = {bookingId+ ""};
        Cursor cursor = db.rawQuery(sql, args);

        //String [ ] columns = {"BookingDetailId","ItineraryNo","TripStart","TripEnd","Description", "Destination","BasePrice","AgencyCommission","BookingId","RegionId","ClassId","FeedId","ProductSupplierId"};
        //Cursor cursor = db.query("BookingDetails",columns,null,null,null,null,null);

        while (cursor.moveToNext())
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateStart =new Date();
            Date dateEnd =new Date();

            try {
                if(cursor.getString(2)==null)
                     dateStart = dateFormat.parse(cursor.getString(2));
                else
                    dateStart = dateFormat.parse("1900-01-01 00:00:00");

                if(cursor.getString(3)==null)
                    dateEnd = dateFormat.parse(cursor.getString(3));
                    else
                        dateEnd = dateFormat.parse("1900-01-01 00:00:00");

            } catch (ParseException e) {
                e.printStackTrace();
            }
            bookingDetails.add(new BookingDetail(cursor.getInt(0), cursor.getDouble(1),dateStart,dateEnd,cursor.getString(4),cursor.getString(5),cursor.getDouble(6),cursor.getDouble(7),cursor.getInt(8),cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getInt(12)));
        }
        return  bookingDetails;

    }

    //Get all supplier-Products of a BookingId

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
        return  packageProducts;

    }

    //Get Package by id
    public ProdPackage getPackageById(int packageId)
    {
        String sql = "SELECT * FROM Packages WHERE PackageId=?";
        String [] args = {packageId+ ""};
        Cursor cursor = db.rawQuery(sql, args);
        //position the cursor on the next/first row
        cursor.moveToNext();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date pkgStartDate =new Date();
        Date pkgEndDate =new Date();

        try {
            pkgStartDate = dateFormat.parse(cursor.getString(2));
            pkgEndDate = dateFormat.parse(cursor.getString(3));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //create a product using this row
        return  new ProdPackage(cursor.getInt(0),cursor.getString(1),pkgStartDate,pkgEndDate,cursor.getString(4),cursor.getDouble(5),cursor.getDouble(6)) ;
    }

    //Get Customer by id
    public Customer getCustomerById (int customerId)
    {
        String sql = "SELECT * FROM Customers WHERE CustomerId=?";
        String [] args = {customerId+ ""};
        Cursor cursor = db.rawQuery(sql, args);
        //position the cursor on the next/first row
        cursor.moveToNext();
        //create a product using this row
        return  new Customer(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getInt(11));
    }

    public ArrayList<TripType> getTripTypes()
    {
        ArrayList<TripType> tripTypes = new ArrayList<>();
        String [ ] columns = {"TripTypeId","TTName"};
        Cursor cursor = db.query("TripTypes",columns,null,null,null,null,null);

        while (cursor.moveToNext())
        {
            tripTypes.add(new TripType(cursor.getString(0).charAt(0),cursor.getString(1)));        }
        return  tripTypes;
    }

    public ArrayList<Customer> getCustomers()
    {
        ArrayList<Customer> customers = new ArrayList<>();
        String [ ] columns = {"CustomerId","CustFirstName","CustLastName", "CustAddress", "CustCity","CustProv","CustPostal","CustCountry","CustHomePhone","CustBusPhone","CustEmail", "AgentId"};
        Cursor cursor = db.query("Customers",columns,null,null,null,null,"CustFirstName");

        while (cursor.moveToNext())
        {
            customers.add(new Customer(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getInt(11)));
        }
        return  customers;
    }
    public ArrayList<ProdPackage> getPackages()
    {
        ArrayList<ProdPackage> packages = new ArrayList<>();
        String [ ] columns = {"PackageId","PkgName","PkgStartDate", "PkgEndDate", "PkgDesc","PkgBasePrice","PkgAgencyCommission"};
        Cursor cursor = db.query("Packages",columns,null,null,null,null,"PkgName");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

            packages.add(new ProdPackage(cursor.getInt(0),cursor.getString(1),pkgStartDate,pkgEndDate,cursor.getString(4),cursor.getDouble(5),cursor.getDouble(6)));
        }
        return  packages;
    }

    //Update Agent in the database
    public boolean updateBooking(Booking booking){
        ContentValues cv = new ContentValues();


        DateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        cv.put("BookingDate",df.format(booking.getBookingDate()));
        cv.put("TravelerCount",booking.getTravelerCount());
        cv.put("BookingNo", booking.getBookingNo().equals(null)?null:booking.getBookingNo());
        cv.put("CustomerId", booking.getCustomerId()==0?null:booking.getCustomerId());
        cv.put("TripTypeId", Character.toString(booking.getTripTypeId()).equals("0")?null:Character.toString(booking.getTripTypeId()));
        cv.put("PackageId",booking.getPackageId()==0?null:booking.getPackageId());
         String [] args = {booking.getBookingId()+""};
        String where = "BookingId=?";
        if(db.update("Bookings",cv,where,args)!=-1)
            return true;
        else return false;
    }

    //Insert agent in the database
    public Booking insertBooking(Booking booking)
    {
        ContentValues cv = new ContentValues();
        Date currentDate = Calendar.getInstance().getTime();
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        booking.setBookingDate(currentDate);
        cv.put("BookingDate",df.format(booking.getBookingDate()));

        Random random = new Random();
        Random random1 = new Random();
        Random random2 = new Random();
        char[] numbers = {'0', '1','2','3','4','5','6','7','8','9'};
        char[] letters = {'A', 'B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','X','Z','W','Y'};

        int max=random.nextInt(10);
        if (max<4)
            max=4;
        char[] bookingNo = new char[max];
        for (int i=0;i<max;i++)
        {
            if(i<3)
            {
                bookingNo[i] = letters[random2.nextInt(10)];
            }
            else {
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
            return booking;
        }

        else
            {
            booking.setBookingId(0);
            return booking;
        }
    }

    private int findBookingIdByBookingNo(String bookingNo) {
        String sql = "SELECT BookingId FROM Bookings WHERE BookingNo=?";
        String [] args = {bookingNo+ ""};
        Cursor cursor = db.rawQuery(sql, args);
        //position the cursor on the next/first row
        cursor.moveToNext();
        return cursor.getInt(0);

    }

}
