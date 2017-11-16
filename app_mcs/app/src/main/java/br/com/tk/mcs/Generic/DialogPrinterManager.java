/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Created by wilson.souza (WR DevInfo)

   Description:
 */

package br.com.tk.mcs.Generic;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import br.com.tk.mcs.Component.DialogEx;
import br.com.tk.mcs.Component.LinearVertical;
import br.com.tk.mcs.Component.RadioButtonEx;
import br.com.tk.mcs.Component.TextViewEx;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 01/08/2017.
 */

public class DialogPrinterManager extends DialogEx
{
   protected BuilderManager m_builder = null;
   protected RadioButtonEx m_printer_enabled = null;
   protected RadioButtonEx m_printer_disabled = null;
   protected TextViewEx m_printer_status = null;
   protected int m_icon_id = 0;
   //-------------------------------------------------------------------------------------------//
   public DialogPrinterManager ( Context pWnd, BuilderManager pBuilder, int nIconId )
   {
      super ( pWnd, "Impressora", LM_HORIZONTAL );
      try
      {
         this.m_icon_id = nIconId;
         this.m_builder = pBuilder;
         this.Create ( );
      }
      catch(Exception e)
      {
         Log.e ( e.getClass ( ).getName ( ), e.getMessage ( ) );
      }
   }
   //-------------------------------------------------------------------------------------------//
   public void Create() throws Exception
   {
      boolean bIsOnline = m_builder.Printer.GetDeviceAdapter ().isEnabled ();
      this.m_printer_enabled = new RadioButtonEx ( this.getContext (),
                                                   R.string.enabled_discover_printer,
                                                   true,
                                                   !bIsOnline,
                                                   RadioButtonEx.DEFAULT );
      this.m_printer_disabled = new RadioButtonEx ( this.getContext (),
                                                    R.string.disable_discover_printer,
                                                    true,
                                                    bIsOnline,
                                                    RadioButtonEx.DEFAULT );
      this.m_printer_status = new TextViewEx ( this.getContext (),
                                               this.m_icon_id,
                                               TextViewEx.DEFAULT,
                                               TextViewEx.DEFCOLOR,
                                               false );

      this.m_printer_disabled.setOnClickListener ( this );
      this.m_printer_enabled.setOnClickListener ( this );
      LinearVertical body = new LinearVertical ( this.getContext () );
      {
         body.addView ( this.m_printer_enabled );
         body.addView ( this.m_printer_disabled );
         body.setGravity ( Gravity.START );
         body.setPadding ( 16, 16, 16, 16 );
      }
      LinearVertical top = new LinearVertical ( this.getContext () );
      {
         int cond = R.string.printer_online;
         //
         switch ( this.m_icon_id )
         {
            case R.drawable.printer_ok:
               cond = R.string.printer_online;
               break;
            case R.drawable.printer_nopaper:
               cond = R.string.printer_nopaper;
               break;
            case R.drawable.print_superhot:
               cond = R.string.printer_superhot;
               break;
            case R.drawable.printer_error:
               cond = R.string.printer_offline;
               break;
            case R.drawable.printer_without_batery:
               cond = R.string.printer_batery_weak;
               break;
         }
         //
         this.m_printer_status.Data.setText ( cond );
         this.m_printer_status.SetColor ( TextViewEx.TRACOLOR );
         top.setGravity ( Gravity.CENTER );
         top.setPadding ( 16, 16, 16, 16 );
         top.addView ( this.m_printer_status );
         top.addView ( body );
      }
      //
      this.View.addView ( top );
      this.SetIcon ( this.m_icon_id );
      this.SetButtons ( new int[]{ R.string.button_ok, R.string.button_cancel} );
      this.SetControlById ( R.string.button_ok, true );
      super.Create ();
   }
   //-------------------------------------------------------------------------------------------//
   @Override
   public void onClick ( android.view.View view )
   {
      switch ( view.getId () )
      {
         case R.string.button_ok:
            this.UpdatePrinterStatus ();
            this.dismiss ();
            break;
         case R.string.button_cancel:
            this.dismiss ();
            break;
         default:
         {
            boolean bEnabled = view == this.m_printer_enabled.Data && this.m_printer_enabled.isChecked ();
            boolean bDisabled = view == this.m_printer_disabled.Data && this.m_printer_disabled.isChecked ();

            this.m_printer_enabled.setChecked ( bEnabled );
            this.m_printer_disabled.setChecked ( bDisabled );
            break;
         }
      }
   }
   //-------------------------------------------------------------------------------------------//
   protected void UpdatePrinterStatus()
   {
      if(this.m_printer_enabled.isChecked ())
      {
         this.m_builder.Printer.SetStopAndWait ( false );
      }
      else if(this.m_printer_disabled.isChecked ())
      {
         this.m_builder.Printer.SetStopAndWait ( true );
      }
   }
   //-------------------------------------------------------------------------------------------//
}
