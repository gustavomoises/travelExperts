//Author: Gustavo Lourenco Moises
//Thread Project - Group 1
//OOSD Program Spring 2020
//Date:9/30/2020
//Travel Agency Application

package com.example.travelexperts.DatabaseLayer;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.travelexperts.BusinessLayer.Product;
import com.example.travelexperts.BusinessLayer.RecyclerViewData;
import com.example.travelexperts.BusinessLayer.Supplier;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class DBHelper extends SQLiteOpenHelper {
    private static final int version = 1;
    private static final String name= "TravelExpertsSqlLite.db";


    //Variable and Constants for Recyclerview data //By Suvanjan Shrestha
    //Table names
    private static  final String TABLE_PRODUCTS = "products";
    private static  final String TABLE_SUPPLIERS = "suppliers";
    private static  final String TABLE_PRODUCTS_SUPPLIERS = "products_suppliers";
    //Table columns
    private static  final String PRODUCT_ID = "ProductId";
    private static  final String PRODUCT_NAME = "ProdName";
    private static  final String SUPPLIER_ID = "SupplierId";
    private static  final String SUPNAME = "SupName";
    private static  final String PRODUCT_SUPPLIER_ID = "ProductSupplierId";



    private static DBHelper mDbHelper;

    //Application Context   //By Suvanjan Shrestha
    public static synchronized DBHelper getInstance(Context context) {

        if (mDbHelper == null) {
            mDbHelper = new DBHelper(context.getApplicationContext());
        }
        return mDbHelper;
    }


    public DBHelper(@Nullable Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.d("harv","creating database");
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
        //Log.d("harv","upgrading database, old version: "+oldVersion+" new version: "+newVersion);
    /*
        db.execSQL("drop table Products");
        db.execSQL("drop table Agencies");
        onCreate(db);
     */
    }


    /*//function to insert Product    //By Suvanjan Shrestha
    public void insertProduct(Product product){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(PRODUCT_NAME, product.ProdName);

            db.insertOrThrow(TABLE_PRODUCTS, null, values);
            db.setTransactionSuccessful();

        } catch (SQLException e){
            e.printStackTrace();
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }*/

    //function to view Products    //By Suvanjan Shrestha
    public List<RecyclerViewData> getAllData(){
        List<RecyclerViewData> dataList = new ArrayList<>();
        String QUERY = "SELECT ps.ProductSupplierId, ps.ProductId, ps.SupplierId, p.ProdName, s.SupName\n" +
                "FROM Products p\n" +
                "INNER JOIN Products_Suppliers ps\n" +
                "ON p.ProductId = ps.ProductId\n" +
                "INNER JOIN Suppliers s\n" +
                "ON ps.SupplierId = s.SupplierId\n" +
                "Order By p.prodName";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    RecyclerViewData viewData = new RecyclerViewData();
                    viewData.ProdName = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME));
                    viewData.ProductId = cursor.getInt(cursor.getColumnIndex(PRODUCT_ID));
                    viewData.ProductSupplierId = cursor.getInt(cursor.getColumnIndex(PRODUCT_SUPPLIER_ID));
                    viewData.SupName = cursor.getString(cursor.getColumnIndex(SUPNAME));
                    viewData.SupplierId = cursor.getInt(cursor.getColumnIndex(SUPPLIER_ID));
                    dataList.add(viewData);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get data from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return dataList;
    }



    //Delete single row from Products   //By Suvanjan Shrestha
    void deleteProduct(String name) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            db.beginTransaction();
            db.execSQL("delete from Products where ProdName ='" + name + "'");
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.d(TAG, "Error while trying to delete product");
        } finally {
            db.endTransaction();
        }
    }


    public List<Supplier> getSuppliersByProd(int productId){
        List<Supplier> supplierList = new ArrayList<>();

        String QUERY = "SELECT SupName, SupplierId FROM Suppliers WHERE SupplierId NOT IN(SELECT ps.SupplierId \n" +
                "FROM Products_Suppliers ps INNER JOIN Suppliers s ON ps.SupplierId = s.SupplierId WHERE ps.ProductId="+productId+")Order By SupName";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Supplier supplier = new Supplier();
                    supplier.SupName = cursor.getString(cursor.getColumnIndex(SUPNAME));
                    supplier.SupplierId = cursor.getInt(cursor.getColumnIndex(SUPPLIER_ID));
                    supplierList.add(supplier);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get data from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return supplierList;
    }

    public List<Supplier> getAllSuppliers(){
        List<Supplier> supplierList = new ArrayList<>();

        String QUERY = "Select * from Suppliers Order by SupName";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Supplier supplier = new Supplier();
                    supplier.SupName = cursor.getString(cursor.getColumnIndex(SUPNAME));
                    supplier.SupplierId = cursor.getInt(cursor.getColumnIndex(SUPPLIER_ID));
                    supplierList.add(supplier);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get data from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return supplierList;
    }

    public List<Product> getAllProducts(){
        List<Product> productList = new ArrayList<>();

        String QUERY = "SELECT * FROM Products";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Product product = new Product();
                    product.ProdName = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME));
                    product.ProductId = cursor.getInt(cursor.getColumnIndex(PRODUCT_ID));
                    productList.add(product);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get data from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return productList;
    }

    /*class RecyclerData implements Runnable {

        @Override
        public void run() {
            //retrieve JSON data from REST service into StringBuffer
            StringBuffer buffer = new StringBuffer();
            String url = "http://localhost:8081/JSPDay3RESTExample/rs/getproductsandsuppliers";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    VolleyLog.wtf(response, "utf-8");

                    //convert JSON data from response string into an ArrayAdapter of Agents
                    final ArrayList<RecyclerViewData> recyclerViewData = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            RecyclerViewData recyclerViewData1 = new RecyclerViewData(jsonObject.getInt("ProductSupplierId"), jsonObject.getInt("ProductId"),jsonObject.getInt("SupplierId"),jsonObject.getString("ProdName"),jsonObject.getString("SupName"));
                            recyclerViewData.add(recyclerViewData1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //display result message
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            for (RecyclerViewData tt: recyclerViewData)
                                .setText(tt.getCustFirstName()+" "+tt.getCustLastName());
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
    }*/
}
