/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Created by wilson.souza (WR DevInfo)

   Description: managed bluetooth device node.
 */
package br.com.tk.mcs.Drivers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by wilsonsouza on 10/10/16.
 */

public class PrinterServer implements Runnable
{
   private static final int DEFAULT_PORT = 8006;
   private ServerSocket m_Socket = null;
   private PrinterServerListener m_PrinterServerListener = null;
   private boolean m_bRunning = false;
   //-----------------------------------------------------------------------------------------------------------------//
   public PrinterServer(PrinterServerListener pListener) throws IOException
   {
      this(DEFAULT_PORT, pListener);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public PrinterServer(int nDefaultPort, PrinterServerListener pListener) throws IOException
   {
      if(pListener == null)
      {
         throw new NullPointerException("parameter pListener is null!");
      }
      //
      m_PrinterServerListener = pListener;
      m_Socket = new ServerSocket(nDefaultPort, 1);
      m_bRunning = true;
      //
      Thread pt = new Thread();
      {
         pt.start();
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void run()
   {
      while(m_bRunning)
      {
         Socket ps = null;
         //
         try
         {
            ps = m_Socket.accept();
            ps.setKeepAlive(true);
            ps.setTcpNoDelay(true);
         }
         catch (IOException e)
         {
            break;
         }
         //
         try
         {
            m_PrinterServerListener.OnConnect(ps);
         }
         catch (Exception e)
         {

         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void close() throws IOException
   {
      m_bRunning = false;
      m_Socket.close();
   }
}
