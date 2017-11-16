/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import java.util.Locale;
import java.util.Vector;

/**
 * Created by wilsonsouza on 01/03/17.
 */

public class Company
{
   public static boolean IsArteris  = false;
   public static boolean IsCRO      = true;
   public static boolean IsTamoios  = false;
   public static boolean IsTecsidel = false;
   public static boolean IsViaRio   = false;
   public static boolean Is040      = false;
   /* internal */
   public static boolean IsDebug    = false;
   public static boolean IsScanner  = true;

   public static class Plaza
   {
      public String Name;
      public String Alias;
      public String IP;
      public Plaza(){}
      public Plaza(final String name, final String alias, final String ip)
      {
         this.Name = name;
         this.Alias = alias;
         this.IP = ip;
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static Vector<Plaza> GetPlazas ( )
   {
      Vector<Plaza> data = new Vector<> (  );

      if ( Company.IsTamoios )
      {
         data.add(new Plaza ( "Praça Jambeiro", "P1 Tamoios", "172.16.62.62" ));
         data.add(new Plaza ( "Praça Paraibuna", "P2 Tamoios", "172.16.102.62" ));
      }
      else if ( Company.IsViaRio )
      {
         int ip = 0xbe;

         for(int i = 1; i < 4; i++)
         {
            data.add ( new Plaza ( String.format ( Locale.FRANCE, "Praça %d" ),
                                   String.format ( Locale.FRANCE, "P%d VIARIO", i ),
                                   String.format ( Locale.FRANCE, "192.168.42.%d", ip )));
            ip += 0xA;
         }
      }
      else if ( Company.Is040 )
      {
         int ip = 0x90;

         for(int i = 1; i < 8; i++ )
         {
            data.add(new Plaza ( String.format ( Locale.FRANCE, "Praça %d", i ),
                                 String.format ( Locale.FRANCE, "P%d 040", i  ),
                                 String.format ( Locale.FRANCE, "10.50.%d.134", ip )));
            ip += 0x10;
         }
         //
         ip = 0;
         //
         for(int i = 8; i < 12; i++ )
         {
            data.add(new Plaza ( String.format ( Locale.FRANCE, "Praça %d", i ),
                                 String.format ( Locale.FRANCE, "P%d 040", i  ),
                                 String.format ( Locale.FRANCE, "10.51.%d.134", ip )));
            ip += 0x10;
         }
      }
      else if ( Company.IsCRO )
      {
         for ( int i = 1; i < 0xA; i++ )
         {
            data.add ( new Plaza ( String.format ( Locale.FRANCE, "Praça %d", i ),
                                   String.format ( Locale.FRANCE, "P%d Rota do Oeste", i ),
                                   String.format ( Locale.FRANCE, "10.200.%d.20", 10 * i )));
         }
      }
      return data;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public final Vector<DbAccess> GetDatabaseList ( )
   {
      Vector<DbAccess> pQueue = new Vector<> ( );
      {
         pQueue.add (
            new DbAccess ( "USRTOLLHOST", "USRTOLLHOST", "192.168.1.7", "TOLLPLAZA", "TecSidel" ) );
         pQueue.add (
            new DbAccess ( "USRTOLLPLAZA", "USRTOLLPLAZA", "10.100.0.11", "master", "Arteris" ) );
      }
      return pQueue;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public class DbAccess
   {
      public String User;
      public String Password;
      public String IP;
      public String DatabaseName;
      public String Company;

      public DbAccess ( String szUser, String szPassword, String szIP, String szDatabaseName,
                        String szCompany
                      )
      {
         this.User = szUser;
         this.DatabaseName = szDatabaseName;
         this.IP = szIP;
         this.Password = szPassword;
         this.Company = szCompany;
      }
   }
}
