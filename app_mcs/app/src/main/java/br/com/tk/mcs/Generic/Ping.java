/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Created by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import java.io.IOException;

/**
 * Created by wilsonsouza on 11/24/16.
 */

public class Ping
{
   static public boolean IsOnLine(String szIP)
   {
      try
      {
         Runtime r = Runtime.getRuntime();
         Process result = r.exec(String.format("system/bin/ping -c 1 %s", szIP));
         //
         if(result.waitFor() == 0)
         {
            return true;
         }
      }
      catch (InterruptedException e)
      {
         e.printStackTrace();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      return false;
   }
}
