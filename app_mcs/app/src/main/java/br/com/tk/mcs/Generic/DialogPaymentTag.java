/*

   Sistema de Gestão de Pistas

   (C) 2016 TecSidel

   Created by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.zxing.Result;

import br.com.tk.mcs.Component.DialogEx;
import br.com.tk.mcs.Component.DisplayMessage;
import br.com.tk.mcs.Component.EditTextEx;
import br.com.tk.mcs.Component.LinearHorizontal;
import br.com.tk.mcs.Component.LinearVertical;
import br.com.tk.mcs.Component.MessageBox;
import br.com.tk.mcs.Component.ScannerBarcode;
import br.com.tk.mcs.Component.ScannerBarcodeEx;
import br.com.tk.mcs.Component.SpaceEx;
import br.com.tk.mcs.Lane.Lane;
import br.com.tk.mcs.Lane.Operations;
import br.com.tk.mcs.R;
import br.com.tk.mcs.R.string;
import br.com.tk.mcs.Remote.Response.RemotePaymentResponse;

/**
 * Created by wilsonsouza on 05/12/16.
 */

public class DialogPaymentTag extends DialogEx implements TextWatcher, ScannerBarcodeEx.OnScannerBarcodeListener
{
   static final int DOCKET = 0xc;
   static final int BORD = 0x7;
   static final int SCANNER = 0xfff0;

   private EditTextEx m_Docket = null;
   private EditTextEx m_Board = null;
   private Lane m_pLane = null;
   private ScannerBarcodeEx m_barcode = null;
   //-----------------------------------------------------------------------------------------------------------------//
   class Launcher extends Thread
   {
      protected Context m_context = getContext();
      protected RemotePaymentResponse m_success = RemotePaymentResponse.ResponseERROR;
      protected String m_captionerror = m_context.getString(string.ids_caption_error);
      protected String m_caption = m_context.getString(string.manager_rdtag_title);
      protected DisplayMessage m_dialog = new DisplayMessage(m_context, string.manager_rdtag_title, string.manager_assign_process);
      protected String m_board = m_Board.GetData().toUpperCase().trim();
      protected String m_docket = m_Docket.GetData().toUpperCase().trim();

      public Launcher()
      {
         super();
         setName(getClass().getName());
      }
      @Override
      public void run()
      {
         try
         {
            Operations op = m_pLane.getOperations();
            ShowDialog(m_dialog);
            this.m_success = op.paymentRDTag(this.m_docket, this.m_board);
         }
         catch (final Exception e)
         {
            ShowMsgBox(m_captionerror, e.getMessage(), MessageBox.IDERROR);
            //Log.e(getClass().getName(), e.getMessage());
            e.printStackTrace();
         }
         //
         CloseDialog(m_dialog);
         //
         if (this.m_success != RemotePaymentResponse.ResponseOK)
         {
            String fmt = RemotePaymentResponse.FormatMessage(getContext(), m_success, m_board);

            //Log.e(getClass().getName(), String.format("Result %s, %s", m_success, fmt));
            ShowMsgBox(m_caption, fmt, RemotePaymentResponse.FormatMessageID(m_success));
         }
         else
         {
            String fmt = RemotePaymentResponse.FormatMessage(getContext(), m_success, m_board);
            ShowMsgBox(m_caption, fmt, RemotePaymentResponse.FormatMessageID(m_success) );
            dismiss();
         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public DialogPaymentTag(Context pWnd, final Lane pLane)
   {
      super(pWnd, string.ids_pay_caption, LM_HORIZONTAL);

      try
      {
         this.m_pLane = pLane;
         this.Create();
      }
      catch (Exception e)
      {
         new MessageBox(pWnd, string.ids_warning, e, MessageBox.IDERROR);
         //Log.e(getClass().getName(), e.getMessage());
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void Create() throws Exception
   {
      LinearVertical body = new LinearVertical(getContext());
      {
         this.m_barcode = new ScannerBarcodeEx(m_pWnd, ConfigDisplayMetrics.ScannerPoint, false);
         this.m_barcode.SetOnScannerBarcodeListener(this);
         this.m_barcode.setPadding(8, 8, 0, 0);
         body.setGravity(Gravity.CENTER);
      }
      LinearHorizontal edit = new LinearHorizontal(getContext());
      {
         m_Docket = new EditTextEx(getContext(), string.ids_do_recibo, DOCKET, true, -1, -1, true);
         m_Board = new EditTextEx(getContext(), string.ids_tag_placa, BORD, true, -1, 150, true);
         m_Docket.Data.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
         m_Board.Data.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

         m_Docket.Data.addTextChangedListener(this);
         m_Docket.Data.setCursorVisible(true);
         m_Board.Data.addTextChangedListener(this);
         m_Board.Data.setCursorVisible(true);

         edit.addView(m_Docket);
         edit.addView(m_Board);
         edit.SetMargins(new Rect(8, 8, 8, 8));
         edit.setGravity(Gravity.CENTER);

         if(!Company.IsScanner)
         {
            edit.setOrientation(LinearLayout.VERTICAL);
         }
      }
      body.addView(new SpaceEx(this.getContext(), 0, 8, false));

      if(Company.IsScanner)
      {
         body.addView(m_barcode, m_barcode.Params);
      }

      body.addView(edit);
      this.SetIcon(R.drawable.ic_paytag);
      super.View.addView(body);
      this.SetButtons(new int[]{string.manager_button_confirm, string.button_cancel });
      super.Create();
      super.SetKeyboard(false, this.m_Docket.Data);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   protected void onStart()
   {
      super.onStart();
      this.SetKeyboard(false, this.m_Docket.Data);
      this.m_barcode.start();
      this.m_barcode.Data.requestFocus();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   protected void onStop()
   {
      this.m_barcode.stop();
      super.onStop();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void beforeTextChanged(CharSequence s, int start, int count, int after)
   {

   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onTextChanged(CharSequence s, int start, int before, int count)
   {
      SetControlById(R.string.manager_button_confirm,
         this.m_Docket.GetData().length() == DOCKET && this.m_Board.GetData().length() == BORD);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void afterTextChanged(Editable s)
   {

   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void OnHasData(final Result pResult)
   {
      m_Docket.Data.setText(pResult.getText());
      m_Board.Data.requestFocus();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(android.view.View v)
   {
      int which = v.getId();

      switch (which)
      {
         case string.manager_button_confirm:
            new Launcher().start();
            break;
         case string.button_cancel:
            this.dismiss();
            break;
      }
   }
}
