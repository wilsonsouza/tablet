/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import android.util.Log;

import java.util.InputMismatchException;
import java.util.Vector;

/**
 * Created by wilsonsouza on 16/02/17.
 */

public class PhysicalPersonalCode extends Object
{
   static char ZERO = '0';
   //-----------------------------------------------------------------------------------------------------------------//
   static String CreateSequence(int nValue, int nOffset)
   {
      String szData = "";

      for(int i = 0; i < nOffset; i++)
      {
         szData += Integer.toString(nValue);
      }
      return szData;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   static Vector<String> GenerateSequencialCode()
   {
      Vector<String> pQueue = new Vector<>();

      for(int i = 0; i < 0xa; i++)
      {
         pQueue.add(PhysicalPersonalCode.CreateSequence(i, 0xb));
      }
      return pQueue;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public static boolean IsOK(String szData)
   {
      char c10 = 0;
      char c11 = 0;
      int  num = 0;
      int  i = 0;
      int  r = 0;
      int  weight = 0xa;
      int  sum = 0;

      try
      {
         Vector<String> pItems = PhysicalPersonalCode.GenerateSequencialCode();
         int nIndex = pItems.indexOf(szData.trim());

         if(szData.trim().length() != 0xb || nIndex != -1)
         {
            return false;
         }

         /* calcule first digit */
         for(; i < 9; i++)
         {
            num = (int)(szData.charAt(i) - 0x30);
            sum += (num * weight);
            weight--;
         }

         r = 0xb - (sum % 0xb);
         if ((r == 0xa) || (r == 0xb))
         {
            c10 = ZERO;
         }
         else
         {
            /* convert character to integer */
            c10 = (char)(r + 0x30);
         }

         // calcule next digit
         sum = 0;
         weight = 0xb;

         for(i = 0; i < 0xa; i++)
         {
            num = (int)(szData.charAt(i) - 0x30);
            sum += (num * weight);
            weight--;
         }

         r = 0xb - (sum % 0xb);
         if ((r == 0xa) || (r == 0xb))
         {
            c11 = ZERO;
         }
         else
         {
            /* convert character to integer */
            c11 = (char) (r + 0x30);
         }

         /* verify all digits */
         if ((c10 == szData.charAt(9)) && (c11 == szData.charAt(0xa)))
         {
            return true;
         }
      }
      catch (InputMismatchException e)
      {
         Log.e(PhysicalPersonalCode.class.getName(), e.getMessage());
      }
      return false;
   }
}
