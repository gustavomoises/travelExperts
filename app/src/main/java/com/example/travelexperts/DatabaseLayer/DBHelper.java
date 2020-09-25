//Author: Gustavo Lourenco Moises
//CPMPP 264 - Java Programming - ANDROID
//Date: 9/24/2020
//Lab 12

package com.example.travelexperts.DatabaseLayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final int version = 1;
    private static final String name= "TravelExpertsSqlLite.db";

    public DBHelper(@Nullable Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("harv","creating database");
        /*
        //Create table Agents
        String sql= "CREATE TABLE Agents(" +
                "`AgentId` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "`AgtFirstName`	VARCHAR(20), "+
                "`AgtMiddleInitial`	VARCHAR(5), "+
                "`AgtLastName`	VARCHAR(20), "+
                "`AgtBusPhone`	VARCHAR(20), "+
                "`AgtEmail`	VARCHAR(50), "+
                "`AgtPosition`	VARCHAR(20), "+
                "`AgencyId`	INT" +
                ")";
        db.execSQL(sql);
        //Add agents to Agents tables
        sql="Insert into Agents (`AgtFirstName`,`AgtMiddleInitial`,`AgtLastName`,`AgtBusPhone`, `AgtEmail`,`AgtPosition`,`AgencyId`) values "+
                "('Janet',null,'Delton','(403) 210-7801','jdelton@gmail.com','Senior Agent',1),"+
                "('Judy',null,'Lisle','(403) 210-7801','jlisle@gmail.com','Intermediate Agent',1),"+
                "('Dennis','C.','Reynolds','(403) 210-7801','dreynolds@gmail.com','Junior Agent',1),"+
                "('John',null,'Coville','(403) 210-7801','jcoville@gmail.com','Intermediate Agent',1),"+
                "('Janice','W.','Dahl','(403) 210-7801','jdahl@gmail.com','Senior Agent',1),"+
                "('Bruce','J.','Dixon','(403) 210-7801','bdixon@gmail.com','Intermediate Agent',2),"+
                "('Beverly','S.','Jones','(403) 210-7801','bjones@gmail.com','Intermediate Agent',2),"+
                "('Jane',null,'Merrill','(403) 210-7801','jmerril@gmail.com','Senior Agent',2),"+
                "('Brian','S.','Peterson','(403) 210-7801','bpeterson@gmail.com','Junior Agent',2);";
        db.execSQL(sql);
        //Create table Agencies
        sql="CREATE TABLE Agencies(" +
                "`AgencyId`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "`AgncyAddress`\tVARCHAR(50)," +
                "`AgncyCity`\tVARCHAR(50)," +
                "`AgncyProv`\tVARCHAR(50)," +
                "`AgncyPostal`\tVARCHAR(50)," +
                "`AgncyCountry`\tVARCHAR(50)," +
                "`AgncyPhone`\tVARCHAR(50)," +
                "`AgncyFax`\tVARCHAR(50)" +
                ")";
        db.execSQL(sql);

        //Add agencies to the Agencies table
        sql="Insert into Agencies (`AgncyAddress`,`AgncyCity`,`AgncyProv`,`AgncyPostal`,`AgncyCountry`,`AgncyPhone`,`AgncyFax`) values "+
                "('1155 8th Ave SW','Calagary','AB','T2P1N3','Canada','4032719873','4032719872'),"+
                "('110 Main Street','Okatokes','AB','T7R3J5','Canada','4035632381','4035632382');";
        db.execSQL(sql);
        */

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Update tables if new version is available
        Log.d("harv","upgrading database, old version: "+oldVersion+" new version: "+newVersion);
    /*
        db.execSQL("drop table Products");
        db.execSQL("drop table Agencies");
        onCreate(db);

     */

    }
}
