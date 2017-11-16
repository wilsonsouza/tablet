/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Created by wilson.souza (WR DevInfo)

   Description: managed bluetooth printer and print ticket about payment.
 */
package br.com.tk.mcs.Drivers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.datecs.api.printer.Printer;
import com.datecs.api.printer.ProtocolAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import br.com.tk.mcs.Component.DisplayMessage;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 10/6/16.
 */
public class PrinterBluetoothControl implements Runnable
{
   //printer name
   public static final String DEVICE_NAME = "DPP-350";
   protected Printer m_Printer = null;
   protected ProtocolAdapter m_ProtocolAdapter = null;
   protected ProtocolAdapter.Channel m_PrinterChannel = null;
   protected BluetoothSocket m_BluetoothSocket = null;
   protected BluetoothAdapter m_BluetoothAdapter = null;
   protected DeviceAdapter m_DeviceAdapter = null;
   protected android.app.Activity m_pWnd = null;
   protected String m_szTitle = null;
   protected BroadcastReceiverImpl m_pReceiver = new BroadcastReceiverImpl();
   //-----------------------------------------------------------------------------------------------------------------//
   class BroadcastReceiverImpl extends BroadcastReceiver
   {
      public BroadcastReceiverImpl()
      {
         super();
      }
      @Override
      public void onReceive(Context context, Intent intent)
      {
         final String szAction = intent.getAction();
         //
         if(BluetoothDevice.ACTION_FOUND.equals(szAction))
         {
            BluetoothDevice pDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            DeviceNode pNode = m_DeviceAdapter.find(pDevice.getAddress());
            //
            if(pNode == null)
            {
               m_DeviceAdapter.add(pDevice.getName(), pDevice.getAddress(), false);
            }
            else
            {
               pNode.SetName(pDevice.getName());
               pNode.SetAddress(pDevice.getAddress());
            }
         }
         else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(szAction))
         {
            //non code here
            m_BluetoothAdapter.cancelDiscovery();
         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   protected class PrinterListener implements ProtocolAdapter.PrinterListener
   {
      public PrinterListener()
      {
         super();
      }
      @Override
      public void onBatteryStateChanged(boolean b)
      {
         if(b)
         {
            Status("Impressora com bateria fraca!");
         }
         else
         {
            UpdateStatus();
         }
      }

      @Override
      public void onThermalHeadStateChanged(boolean b)
      {
         if(b)
         {
            Status("Impressora super aquecida!");
         }
         else
         {
            UpdateStatus();
         }
      }

      @Override
      public void onPaperStateChanged(boolean b)
      {
         if(b)
         {
            Status("Impressora sem papel!");
         }
         else
         {
            UpdateStatus();
         }
      }
   };
   //-----------------------------------------------------------------------------------------------------------------//
   public class ConnectionListener implements Printer.ConnectionListener
   {
      @Override
      public void onDisconnect()
      {
         new Thread(new Runnable()
         {
            @Override
            public void run()
            {
               UpdateStatus();
               //
               if (!((AppCompatActivity)m_pWnd).isFinishing())
               {
                  CloseActiveConnection();
               }
            }
         }).start();
      }
   };
   //-----------------------------------------------------------------------------------------------------------------//
   class PrinterCupom extends Thread
   {
      protected StringBuffer m_pBuffer = new StringBuffer();
      protected DisplayMessage m_pDlg = null;
      //-----------------------------------------------------------------------------------------------------------------//
      public PrinterCupom(StringBuffer pData)
      {
         super();
         this.setName(this.getClass().getName());
         this.m_pBuffer.append(pData);
         this.m_pDlg = new DisplayMessage(m_pWnd, R.string.ids_wait, R.string.print_cupom);
      }
      //-----------------------------------------------------------------------------------------------------------------//
      @Override
      public void run()
      {
         try
         {
            m_pWnd.runOnUiThread(new Runnable()
            {
               @Override
               public void run()
               {
                  m_pDlg.show();
               }
            });

            m_Printer.reset();
            m_pBuffer.append("\n\n");
            /*printer.printSelfTest();*/
            m_Printer.printText(m_pBuffer.toString());
            m_Printer.flush();
         }
         catch (IOException e)
         {
            Log.e(this.getClass().getName(), e.getMessage());
         }
         catch (Exception e)
         {
            Log.e(this.getClass().getName(), e.getMessage());
         }

         m_pWnd.runOnUiThread(new Runnable()
         {
            @Override
            public void run()
            {
               m_pDlg.dismiss();
            }
         });
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public PrinterBluetoothControl(Context pWnd) throws Exception
   {
      super();
      this.m_pWnd = (Activity)pWnd;
      this.m_szTitle = this.m_pWnd.getTitle().toString();
      //this.setPriority(Thread.MIN_PRIORITY);
      //this.setName(this.getClass().getName());
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
   protected void UpdateStatus()
   {
      boolean bIsOnLine = false;
      String szData;
      //
      if(this.m_BluetoothSocket != null)
      {
         bIsOnLine = this.m_BluetoothSocket.isConnected();
      }
      //
      if(bIsOnLine)
      {
         szData = "Impressora conectada.";
      }
      else
      {
         szData = "Impressora disconectada.";
      }
      //
      Status(szData);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @TargetApi(Build.VERSION_CODES.LOLLIPOP)
   public void Initialize() throws Exception
   {
      final BluetoothManager bm = (BluetoothManager)this.m_pWnd.getSystemService(Context.BLUETOOTH_SERVICE);
      this.m_BluetoothAdapter = bm.getAdapter();
      this.m_DeviceAdapter = new DeviceAdapter();
      //
      this.m_pWnd.registerReceiver(this.m_pReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
      this.m_pWnd.registerReceiver(this.m_pReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
      //
      if(!this.m_BluetoothAdapter.isEnabled())
      {
         this.m_BluetoothAdapter.enable();
      }
      //
      if(this.m_BluetoothAdapter.isEnabled())
      {
         java.util.Set<BluetoothDevice> pPairedDevices = this.m_BluetoothAdapter.getBondedDevices();
         //
         if(pPairedDevices.size() > 0)
         {
            for(BluetoothDevice p: pPairedDevices)
            {
               this.m_DeviceAdapter.add(p.getName(), p.getAddress(), true);
            }
         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void Finalize() throws Exception
   {
      if(m_BluetoothAdapter != null)
      {
         m_BluetoothAdapter.cancelDiscovery();
      }
      //
      this.m_pWnd.unregisterReceiver(this.m_pReceiver);
      this.CloseActiveConnection();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private void Discover()
   {
      if(!m_BluetoothAdapter.isEnabled())
      {
         m_BluetoothAdapter.enable();
      }
      //
      if(!m_BluetoothAdapter.isDiscovering())
      {
         m_BluetoothAdapter.startDiscovery();
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private void Toast(final String szData)
   {
      m_pWnd.runOnUiThread(new Runnable()
      {
         @Override
         public void run()
         {
            Toast.makeText(m_pWnd.getApplicationContext(), szData, Toast.LENGTH_LONG).show();
         }
      });
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private void Status(final String szData)
   {
      m_pWnd.runOnUiThread(new Runnable()
      {
         @Override
         public void run()
         {
            if(szData != null)
            {
               /* put text on title of Manager */
               final CharSequence csNewTitle = szData;
               m_pWnd.setTitle(m_szTitle + " - " + csNewTitle);
            }
            else
            {
               m_pWnd.setTitle(m_szTitle);
            }
         }
      });
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
   public void run()
   {
      try
      {
         this.Initialize();

         DeviceNode pNode = m_DeviceAdapter.search(DEVICE_NAME);
         boolean bIsOnLine = false;

         if(m_BluetoothSocket != null)
         {
            bIsOnLine = m_BluetoothSocket.isConnected();
         }

         if (pNode != null && !bIsOnLine && m_BluetoothAdapter.isEnabled())
         {
            m_BluetoothAdapter.cancelDiscovery();
            EstabablishConnection(pNode.GetAddress());
         }
         //
         if (!bIsOnLine && m_BluetoothAdapter.isEnabled())
         {
            this.UpdateStatus();
            Discover();
         }

         if (!m_BluetoothAdapter.isEnabled())
         {
            m_BluetoothAdapter.enable();
         }
      }
      catch (Exception e)
      {
         this.UpdateStatus();
         Log.e(this.getClass().getName(), e.getMessage());
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void start()
   {
      //if(!isAlive())
      {
         try
         {
            this.Initialize();
            //super.start();
         }
         catch (Exception e)
         {
            this.UpdateStatus();
            Log.e(this.getClass().getName(), e.getMessage());
         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   protected void InitializePrinter(InputStream in, OutputStream out) throws IOException
   {
      try
      {
         this.m_ProtocolAdapter = new ProtocolAdapter(in, out);

         if (this.m_ProtocolAdapter.isProtocolEnabled())
         {
            this.m_ProtocolAdapter.setPrinterListener(new PrinterListener()); //this.m_PrinterListener);
            this.m_PrinterChannel = m_ProtocolAdapter.getChannel(ProtocolAdapter.CHANNEL_PRINTER);
            this.m_Printer = new Printer(m_PrinterChannel.getInputStream(), m_PrinterChannel.getOutputStream());
         }
         else
         {
            m_Printer = new Printer(m_ProtocolAdapter.getRawInputStream(), m_ProtocolAdapter.getRawOutputStream());
         }

         m_Printer.setConnectionListener(new ConnectionListener()); //m_ConnectionListener);
         this.UpdateStatus();
      }
      catch(Exception e)
      {
         this.UpdateStatus();
         Log.e(this.getClass().getName(), e.getMessage());
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private void EstabablishConnection(final String szAddress) throws Exception
   {
      try
      {
         /* display dialog to select printer address */
         final BluetoothAdapter pAdapter = BluetoothAdapter.getDefaultAdapter();
         final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
         final BluetoothDevice pDevice = pAdapter.getRemoteDevice(szAddress);
         InputStream in = null;
         OutputStream out = null;
         /**/
         try
         {
            /* create rfcomm connection service */
            BluetoothSocket pSocket = pDevice.createRfcommSocketToServiceRecord(uuid);
            {
               pAdapter.cancelDiscovery();
               pSocket.connect();
            }
            /**/
            m_BluetoothSocket = pSocket;
            in = m_BluetoothSocket.getInputStream();
            out = m_BluetoothSocket.getOutputStream();

            /* start printer */
            InitializePrinter(in, out);
         }
         catch (IOException e)
         {
            CloseActiveConnection();
            Log.e(this.getClass().getName(), e.getMessage());
         }
      }
      catch (Exception e)
      {
         Log.e(this.getClass().getName(), e.getMessage());
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private void CloseBluetoothConnection()
   {
      try
      {
         /* close bluetooth connection */
         BluetoothSocket pBluetoothSocket = m_BluetoothSocket;
         /* free memory */
         if (pBluetoothSocket != null)
         {
            m_BluetoothSocket = null;
            pBluetoothSocket.close();
         }
      }
      catch (IOException e)
      {
         Log.e(this.getClass().getName(), e.getMessage());
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private void ClosePrinterConnection()
   {
      try
      {
         if (this.m_Printer != null)
         {
            this.m_Printer.close();
            this.m_Printer = null;
         }
         //
         if (this.m_ProtocolAdapter != null)
         {
            this.m_ProtocolAdapter.close();
            this.m_ProtocolAdapter = null;
         }
      }
      catch (Exception e)
      {
         Log.e(this.getClass().getName(), e.getMessage());
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private void CloseActiveConnection()
   {
      this.ClosePrinterConnection();
      this.CloseBluetoothConnection();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SendCupomToPrinter(final StringBuffer pBuffer)
   {
      try
      {
         if(pBuffer.length() == 0)
         {
            throw new Exception("no data to printer!");
         }
         //
         PrinterCupom p = new PrinterCupom(pBuffer);
         {
            p.start();
         }
      }
      catch (Exception e)
      {
         Log.e(this.getClass().getName(), e.getMessage());
      }
   }
}
