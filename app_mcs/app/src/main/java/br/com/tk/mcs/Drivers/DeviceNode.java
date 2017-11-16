/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Created by wilson.souza (WR DevInfo)

   Description: managed bluetooth device list.
 */
package br.com.tk.mcs.Drivers;

/**
 * Created by wilsonsouza on 10/7/16.
 */

public class DeviceNode
{
   private String m_Name;
   private String m_Address;
   private boolean m_IsPaired = false;
   //
   public DeviceNode(String szName, String szAddress, boolean bPaired)
   {
      m_Name = szName;
      m_Address = szAddress;
      m_IsPaired = bPaired;
   }
   //
   public void SetName(String szName)
   {
      m_Name = szName;
   }
   //
   public void SetAddress(String szAddress)
   {
      m_Address = szAddress;
   }
   //
   public void SetPaired(boolean bPaired)
   {
      m_IsPaired = bPaired;
   }
   //
   public String GetName()
   {
      return m_Name;
   }
   //
   public String GetAddress()
   {
      return m_Address;
   }
   //
   public boolean GetPaired()
   {
      return m_IsPaired;
   }
}