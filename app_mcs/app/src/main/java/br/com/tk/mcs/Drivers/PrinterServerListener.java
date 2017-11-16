/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Created by wilson.souza (WR DevInfo)

   Description: managed bluetooth device node.
 */
package br.com.tk.mcs.Drivers;

import java.net.Socket;

/**
 * Created by wilsonsouza on 10/10/16.
 */

public interface PrinterServerListener
{
   public void OnConnect(Socket pSocket);
}
