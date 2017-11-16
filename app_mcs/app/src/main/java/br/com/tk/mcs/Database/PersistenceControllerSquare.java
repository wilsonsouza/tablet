/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Created by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by wilsonsouza on 11/17/16.
 */

public class PersistenceControllerSquare extends Persistence
{
   private SQLiteDatabase m_db;
   //-----------------------------------------------------------------------------------------------------------------//
   public PersistenceControllerSquare(Context context)
   {
      super(context);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public boolean Update(String szValue)
   {
      String aFields[] = new String[]{Persistence.ip};
      Cursor pCursor = null;
      boolean bSuccess = false;
      long lResult = -1;
      //
      m_db = this.getWritableDatabase();
      OnCreateSquareTable(m_db);
      pCursor = m_db.query(Persistence.SquareTableName, aFields, null, null, null, null, null, null);
      //
      if(pCursor.getCount() > 0)
      {
         String szIP = new String();
         //
         pCursor.moveToFirst();
         szIP = pCursor.getString(pCursor.getColumnIndex(Persistence.ip));

         if(!szIP.equals(szValue.trim()))
         {
            ContentValues pValues = new ContentValues();
            //
            try
            {
               pValues.put(Persistence.ip, szValue.trim());
               lResult = m_db.update(Persistence.SquareTableName, pValues, null, null);
               bSuccess = (lResult != -1);
            }
            catch (SQLException e)
            {
               e.printStackTrace();
            }
         }
      }
      else
      {
         ContentValues pValues = new ContentValues();
         //
         try
         {
            pValues.put(Persistence.ip, szValue.trim());
            lResult = m_db.insert(Persistence.SquareTableName, null, pValues);
            bSuccess = (lResult != -1);
         }
         catch(SQLException e)
         {
            e.printStackTrace();
         }
      }
      //
      m_db.close();
      return bSuccess;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public String Get()
   {
      String szIP = new String();
      String aFields[] = new String[]{Persistence.ip};
      Cursor pCursor = null;
      //
      m_db = this.getReadableDatabase();
      OnCreateSquareTable(m_db);
      pCursor = m_db.query(Persistence.SquareTableName, aFields, null, null, null, null, null, null);
      //
      if(pCursor.getCount() > 0)
      {
         pCursor.moveToFirst();
         szIP = pCursor.getString(pCursor.getColumnIndex(Persistence.ip));
         //
         if(!Character.isDigit(szIP.charAt(0)))
         {
            szIP = "";
         }
      }
      //
      m_db.close();
      return szIP;
   }
}
