/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by revolution on 31/01/16.
 */

public class Persistence extends SQLiteOpenHelper
{

   public static final String database = "database.db";
   public static final String lane = "lane";
   public static final String conf = "conf";
   public static final String id = "_id";
   public static final String name = "name";
   public static final String ip = "ip";
   public static final String sen = "sentido";
   public static final int version = 23;
   public static final String SquareTableName = "square";
   //-----------------------------------------------------------------------------------------------------------------//
   public Persistence(Context context)
   {
      super(context, database, null, version);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onCreate(SQLiteDatabase pDb)
   {
      String sql = "CREATE TABLE IF NOT EXISTS " + lane +
         " (" + id + " integer primary key autoincrement, " +
         ip + " text NOT NULL UNIQUE, " +
         name + " text NOT NULL UNIQUE, " +
         sen + " text NOT NULL);";
      Log.d(this.getClass().getName(), sql.toString());
      pDb.execSQL(sql);
      //
      OnCreateSquareTable(pDb);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onUpgrade(SQLiteDatabase pDb, int nOldVersion, int nNewVersion)
   {
      pDb.execSQL("DROP TABLE IF EXISTS " + lane);
      pDb.execSQL("DROP TABLE IF EXISTS " + SquareTableName);
      onCreate(pDb);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void OnCreateSquareTable(SQLiteDatabase pDb)
   {
      String szCmd = "CREATE TABLE IF NOT EXISTS " + SquareTableName + "( " +
         Persistence.id + " integer primary key autoincrement, " +
         Persistence.ip + " text not null unique ); ";

      Log.d(this.getClass().getName(), szCmd.toString());
      pDb.execSQL(szCmd);
   }
}
