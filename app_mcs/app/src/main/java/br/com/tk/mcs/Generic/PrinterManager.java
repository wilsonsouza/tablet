/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.os.Build;

import com.datecs.api.printer.Printer;
import com.datecs.api.printer.ProtocolAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import br.com.tk.mcs.Activity.ManageLanes;
import br.com.tk.mcs.Component.CustomerToolBar;
import br.com.tk.mcs.Drivers.PrinterBluetoothControl;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 3/30/17.
 */

public class PrinterManager extends PrinterBluetoothControl
{
   protected boolean m_bWhile = true;
   protected CustomerToolBar Toolbar = null;
   protected boolean m_bIsOnline = false;
   protected boolean m_stop_and_wait = false;
   //-----------------------------------------------------------------------------------------------------------------//
   class PrinterListenerEx implements ProtocolAdapter.PrinterListener
   {
      public PrinterListenerEx()
      {
         super();
      }
      //-----------------------------------------------------------------------------------------------------------------//
      @Override
      public void onBatteryStateChanged(boolean b)
      {
         if(b)
         {
            Toolbar.SetIcon(0, R.drawable.printer_without_batery);
         }
         else
         {
            UpdateStatus();
         }
      }
      //-----------------------------------------------------------------------------------------------------------------//
      @Override
      public void onThermalHeadStateChanged(boolean b)
      {
         if(b)
         {
            Toolbar.SetIcon(0, R.drawable.print_superhot);
         }
         else
         {
            UpdateStatus();
         }
      }
      //-----------------------------------------------------------------------------------------------------------------//
      @Override
      public void onPaperStateChanged(boolean b)
      {
         if(b)
         {
            Toolbar.SetIcon(0, R.drawable.printer_nopaper);
         }
         else
         {
            UpdateStatus();
         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public PrinterManager(ManageLanes pWnd, CustomerToolBar pToolbar) throws Exception
   {
      super(pWnd);
      this.Toolbar = pToolbar;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetManagerPointer(BuilderManager pManager)
   {
      this.Toolbar = pManager.ToolBar;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
   protected void UpdateStatus()
   {
      try
      {
         if(this.m_BluetoothSocket != null)
         {
            this.m_bIsOnline = m_BluetoothSocket.isConnected();
         }
         else
         {
            this.m_bIsOnline = false;
         }

         if (this.m_bIsOnline)
         {
            Toolbar.SetIcon(0, R.drawable.printer_ok);
         }
         else
         {
            Toolbar.SetIcon(0, R.drawable.printer_error);
         }
      }
      catch (Exception e)
      {
         Toolbar.SetIcon(0, R.drawable.printer_error);
         //Log.e(getClass().getName(), e.getMessage());
         e.printStackTrace();
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   protected void InitializePrinter(InputStream in, OutputStream out) throws IOException
   {
      try
      {
         super.m_ProtocolAdapter = new ProtocolAdapter(in, out);

         if (super.m_ProtocolAdapter.isProtocolEnabled())
         {
            super.m_ProtocolAdapter.setPrinterListener(new PrinterManager.PrinterListenerEx());
            super.m_PrinterChannel = super.m_ProtocolAdapter.getChannel(ProtocolAdapter.CHANNEL_PRINTER);
            super.m_Printer = new Printer(super.m_PrinterChannel.getInputStream(), super.m_PrinterChannel.getOutputStream());
         }
         else
         {
            super.m_Printer = new Printer(super.m_ProtocolAdapter.getRawInputStream(), super.m_ProtocolAdapter.getRawOutputStream());
         }

         super.m_Printer.setConnectionListener(new ConnectionListener());
         this.UpdateStatus();
      }
      catch(Exception e)
      {
         Toolbar.SetIcon(0, R.drawable.printer_error);
         //Log.e(this.getClass().getName(), e.getMessage());
         e.printStackTrace();
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void start()
   {
      this.m_bWhile = true;
      super.start();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void interrupt()
   {
      this.m_bWhile = false;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void run()
   {
      while(this.m_bWhile)
      {
         try
         {
            if(!m_stop_and_wait)
            {
               super.run();
            }
            else
            {
               this.m_BluetoothAdapter.cancelDiscovery ();
               this.m_BluetoothAdapter.disable ();
            }
            /* wait 15 seconds */
            Thread.sleep(0x400);
         }
         catch (Exception e)
         {
            this.UpdateStatus();
            //Log.e(this.getClass().getName(), e.getMessage());
            e.printStackTrace();
         }
      }

      try
      {
         this.Finalize();
      }
      catch (Exception e)
      {
         //Log.e(this.getClass().getName(), e.getMessage());
         e.printStackTrace();
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public final boolean IsOnLine()
   {
      return this.m_bIsOnline;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public final BluetoothAdapter GetDeviceAdapter()
   {
      return this.m_BluetoothAdapter;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public final void SetStopAndWait(boolean bValue)
   {
      this.m_stop_and_wait = bValue;
   }
   //-----------------------------------------------------------------------------------------------------------------//
}
