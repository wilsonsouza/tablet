/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

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
import br.com.tk.mcs.Remote.Response.RemotePaymentResponse;

/**
 * Created by wilsonsouza on 23/01/17.
 */

public class DialogPaymentMoney extends DialogEx implements TextWatcher, ScannerBarcodeEx.OnScannerBarcodeListener
{
   static final int RECIPT = 0xC;
   static final int CATEGORY = 0x2;

   protected EditTextEx m_pRecipt = null;
   protected EditTextEx m_pCategory = null;
   protected Lane m_pLane = null;
   protected PrinterManager m_pPrinter = null;
   protected DisplayMessage m_dialog = null;
   protected ScannerBarcodeEx m_barcode = null;
   //-----------------------------------------------------------------------------------------------------------------//
   class Launcher extends Thread
   {
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
            ShowDialog(m_dialog);
            final Operations p = m_pLane.getOperations();
            final String szRecipt = m_pRecipt.GetData().toUpperCase().trim();
            final String szCat = m_pCategory.GetData().toUpperCase().trim();
            final RemotePaymentResponse pOK = p.paymentRDCash(szRecipt, szCat);
            //
            CloseDialog(m_dialog);
            //
            if (pOK != RemotePaymentResponse.ResponseOK)
            {
               final String szMessage = RemotePaymentResponse.FormatMessage(getContext(), pOK, szRecipt);
               final String szCaption = m_pWnd.getString(R.string.manager_rdmoney_title);

               //Log.e(getClass().getName(), String.format("Result %s, %s", pOK, szMessage));
               ShowMsgBox(szCaption, szMessage, RemotePaymentResponse.FormatMessageID(pOK));
            }
            else
            {
               final String szMsg = RemotePaymentResponse.FormatMessage(getContext(), pOK, szRecipt);
               final String szCaption = m_pWnd.getString(R.string.manager_rdmoney_title);

               ShowMsgBox(szCaption, szMsg, RemotePaymentResponse.FormatMessageID(pOK));
               dismiss();
               boolean bOnline = m_pPrinter.IsOnLine();
               /* check if printer is online */
               if (bOnline)
               {
                  m_pWnd.runOnUiThread(new Runnable()
                  {
                     protected String m_szBuffer = p.GetCupom();
                     protected StringBuffer m_pBuffer = CreateTicket.Builder(m_szBuffer);

                     @Override
                     public void run()
                     {
                        /* send ticket to printer */
                        m_pPrinter.SendCupomToPrinter(m_pBuffer);
                     }
                  });
               }
            }
         }
         catch (final Exception e)
         {
            dismiss();
            ShowMsgBox(getContext().getString(R.string.ids_caption_error), e.getMessage(), MessageBox.IDERROR);
            //Log.e(getClass().getName(), e.getMessage());
         }
      }
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public DialogPaymentMoney(Context pWnd, final Lane pLane, final PrinterManager pPrinter)
   {
      super(pWnd, R.string.manager_rdmoney_title, LM_HORIZONTAL);
      try
      {
         this.m_pPrinter = pPrinter;
         this.m_pLane = pLane;
         this.Create();
      }
      catch (Exception e)
      {
         e.printStackTrace();
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
         this.m_barcode.setPadding(8, 8, 8, 8);
         body.setGravity(Gravity.CENTER);
      }
      LinearHorizontal edit = new LinearHorizontal(getContext());
      {
         String szCategory = getContext().getString(R.string.ids_category);
         this.m_pRecipt = new EditTextEx(getContext(), R.string.ids_do_recibo, RECIPT, true, -1, -1, true);
         this.m_pCategory = new EditTextEx(getContext(), szCategory, CATEGORY, true, -1, 0x50, true);
         this.m_pRecipt.Data.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
         this.m_pCategory.Data.setInputType(InputType.TYPE_CLASS_NUMBER);

         this.m_pCategory.Data.addTextChangedListener(this);
         this.m_pCategory.Data.setCursorVisible(true);
         this.m_pRecipt.Data.addTextChangedListener(this);
         this.m_pRecipt.Data.setCursorVisible(true);

         edit.addView(this.m_pRecipt);
         edit.addView(this.m_pCategory);
         edit.SetMargins(new Rect(8, 8, 8, 8));
         edit.setGravity(Gravity.CENTER);

         edit.Params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL;

         if (!Company.IsScanner)
         {
            edit.setOrientation(LinearLayout.VERTICAL);
         }
      }
      body.addView(new SpaceEx(this.getContext(), 0, 8, false));

      if (Company.IsScanner)
      {
         body.addView(m_barcode, m_barcode.Params);
      }

      body.addView(edit);

      this.m_dialog = new DisplayMessage(getContext(), R.string.manager_rdmoney_title, R.string.manager_assign_process);
      this.SetButtons(new int[]{R.string.manager_button_confirm, R.string.manager_button_cancel});
      this.SetIcon(R.drawable.ic_paymoney);
      this.View.addView(body, body.Params);
      super.Create();
      super.SetKeyboard(false, this.m_pRecipt.Data);
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
         m_pRecipt.GetData().length() == RECIPT &&
         m_pCategory.GetData().length() == CATEGORY);
   }

   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void afterTextChanged(Editable s)
   {

   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   protected void onStart()
   {
      super.onStart();
      this.SetKeyboard(false, this.m_pRecipt.Data);
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
   public void OnHasData(Result pResult)
   {
      this.m_pRecipt.Data.setText(pResult.getText());
      this.m_pCategory.Data.requestFocus();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(android.view.View v)
   {
      int which = v.getId();

      switch (which)
      {
         case R.string.manager_button_confirm:
            new Launcher().start();
            break;
         case R.string.manager_button_cancel:
            dismiss();
            break;
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
}
