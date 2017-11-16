/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by revolution on 31/01/16.
 */

public class PersistenceController extends Persistence
{
   private SQLiteDatabase db;

   public PersistenceController(Context context)
   {
      super(context);
   }

   public boolean insertLane(String name, String ip, String sentido)
   {
      db = this.getWritableDatabase();
      ContentValues values = new ContentValues();
      values.put(this.ip, ip.trim());
      values.put(this.name, name.trim());
      values.put(this.sen, sentido.trim());

      long res = -1;

      try
      {
         res = db.insert(this.lane, null, values);
      }
      catch (SQLiteException e)
      {
         e.printStackTrace();
      }

      db.close();
      return (res != -1);
   }

   public boolean deleteLane(String ip, String name)
   {
      String where = this.ip + " = " + "\"" + ip.trim() + "\"" + " and " + this.name + " = " + "\"" + name.trim() + "\"";
      db = this.getReadableDatabase();

      int rows = 0;

      try
      {
         rows = db.delete(this.lane, where, null);
      }
      catch (SQLiteException e)
      {
         e.printStackTrace();
      }

      db.close();
      return (rows > 0);
   }

   public boolean updateLane(String oldIP, String ip, String name)
   {
      db = this.getWritableDatabase();
      String where = this.ip + " = " + "\"" + oldIP.trim() + "\"";
      ContentValues values = new ContentValues();
      values.put(this.ip, ip.trim());
      values.put(this.name, name.trim());

      int rows = 0;

      try
      {
         rows = db.update(this.lane, values, where, null);
      }
      catch (SQLiteException e)
      {
         e.printStackTrace();
      }

      db.close();
      return (rows > 0);
   }

   public ArrayAdapter<String> loadLane(Context context)
   {
      Cursor cursor;
      String[] fields = {this.id, this.name, this.ip};
      String where = this.name + " = " + "\"" + name.trim() + "\" order by name";
      db = this.getReadableDatabase();
      cursor = db.query(this.lane, fields, where, null, null, null, null, null);
      ArrayAdapter<String> adp = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_checked);

      if (cursor.getCount() > 0)
      {

         cursor.moveToFirst();

         do
         {
            adp.add(cursor.getString(cursor.getColumnIndex(this.ip)) + " (" + cursor.getString(cursor.getColumnIndex(this.name)) + ")");
         } while (cursor.moveToNext());
      }

      db.close();
      return adp;
   }

   public ArrayList<String> getLanesAddress()
   {
      Cursor cursor;
      String[] fields = {this.ip};
      db = this.getReadableDatabase();
      cursor = db.query(this.lane, fields, null, null, null, null, null, null);
      ArrayList<String> arr = new ArrayList<String>();

      if (cursor.getCount() > 0)
      {

         cursor.moveToFirst();

         do
         {
            arr.add(cursor.getString(cursor.getColumnIndex(this.ip)));
         } while (cursor.moveToNext());
      }

      db.close();
      return arr;
   }

   public ArrayList<String> getLanesNames()
   {
      Cursor cursor;
      String where = this.name + " = " + "\"" + name.trim() + "\" order by name";
      String[] fields = {this.name};
      db = this.getReadableDatabase();
      cursor = db.query(this.lane, fields, where, null, null, null, null, null);
      ArrayList<String> arr = new ArrayList<String>();

      if (cursor.getCount() > 0)
      {

         cursor.moveToFirst();

         do
         {
            arr.add(cursor.getString(cursor.getColumnIndex(this.name)));
         } while (cursor.moveToNext());
      }

      db.close();
      return arr;
   }

   public String getLaneAddressByName(String name)
   {
      Cursor cursor;
      String[] fields = {this.ip};
      String where = this.name + " = " + "\"" + name.trim() + "\" order by name";
      db = this.getReadableDatabase();
      cursor = db.query(this.lane, fields, where, null, null, null, null, null);
      if (cursor.getCount() > 0)
      {
         cursor.moveToFirst();
      }
      db.close();
      return cursor.getString(cursor.getColumnIndex(this.ip));
   }

   public String getLaneNameByIP(String ip)
   {
      Cursor cursor;
      String where = this.ip + " = " + "\"" + ip.trim() + "\"";
      String[] fields = {this.name};
      db = this.getReadableDatabase();
      cursor = db.query(this.lane, fields, where, null, null, null, null, null);
      if (cursor.getCount() > 0)
      {
         cursor.moveToFirst();
      }
      db.close();
      return cursor.getString(cursor.getColumnIndex(this.name));
   }

   public int getLanesCount()
   {
      int i = 0;
      Cursor cursor;
      String[] fields = {this.name};
      db = this.getReadableDatabase();
      cursor = db.query(this.lane, fields, null, null, null, null, null, null);
      if (cursor.getCount() > 0)
      {
         cursor.moveToFirst();
         do
         {
            i++;
         } while (cursor.moveToNext());
      }
      db.close();
      return i;
   }
}