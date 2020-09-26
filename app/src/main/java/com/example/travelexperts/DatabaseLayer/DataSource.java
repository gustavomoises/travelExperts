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

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


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

    //Get all the agencues from database
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
        Cursor cursor = db.query("Bookings",columns,null,null,null,null,null);

            while (cursor.moveToNext())
        {
             DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date =new Date();
            try {
                date = dateFormat.parse(cursor.getString(1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            bookings.add(new Booking(cursor.getInt(0), date,cursor.getString(2),cursor.getDouble(3),cursor.getInt(4),cursor.getString(5),cursor.getInt(6)));
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
                dateStart = dateFormat.parse(cursor.getString(2));
                dateEnd = dateFormat.parse(cursor.getString(3));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            bookingDetails.add(new BookingDetail(cursor.getInt(0), cursor.getDouble(1),dateStart,dateEnd,cursor.getString(4),cursor.getString(5),cursor.getDouble(6),cursor.getDouble(7),cursor.getInt(8),cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getInt(12)));
        }
        return  bookingDetails;

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

}
