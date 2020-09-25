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
}
