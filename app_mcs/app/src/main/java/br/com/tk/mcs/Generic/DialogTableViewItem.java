/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.zxing.Result;

import java.util.Vector;

import br.com.tk.mcs.Component.CheckBoxEx;
import br.com.tk.mcs.Component.DialogEx;
import br.com.tk.mcs.Component.DisplayMessage;
import br.com.tk.mcs.Component.EditTextEx;
import br.com.tk.mcs.Component.ImageViewEx;
import br.com.tk.mcs.Component.LinearHorizontal;
import br.com.tk.mcs.Component.LinearVertical;
import br.com.tk.mcs.Component.MessageBox;
import br.com.tk.mcs.Component.ScannerBarcode;
import br.com.tk.mcs.Component.ScannerBarcodeEx;
import br.com.tk.mcs.Component.SpaceEx;
import br.com.tk.mcs.Component.TableWidget;
import br.com.tk.mcs.Component.TaskDownloadBitmap;
import br.com.tk.mcs.Lane.Lane;
import br.com.tk.mcs.Lane.Operations;
import br.com.tk.mcs.R;
import br.com.tk.mcs.Remote.Response.RemotePaymentResponse;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by wilsonsouza on 30/01/17.
 */

public class DialogTableViewItem extends DialogEx implements TextWatcher
{
   protected static int BOARD = 0x7;
   protected static int TAG = 0xC;
   protected Point m_point = ConfigDisplayMetrics.VehicleImage;
   protected ImageViewEx m_bmp = null;
   protected EditTextEx m_datetime = null;
   protected EditTextEx m_via = null;
   protected EditTextEx m_tag = null;
   protected EditTextEx m_board = null;
   protected CheckBoxEx m_unknow = null;
   protected CheckBoxEx m_bdown = null;
   protected Lane m_pLane = null;
   protected PrinterManager m_printer = null;
   protected TableWidget.TableItem m_item = null;
   protected TableWidget.DetailsArray m_details = null;
   protected DisplayMessage m_dialog = null;
   protected String m_imagelink = null;
   protected CheckBoxEx m_comment = null;
   protected String m_szTransactionId = "";
   protected EditTextEx m_cupom_id = null;
   protected EditTextEx m_category = null;
   protected ImageViewEx m_do_scanner = null;
   protected LinearHorizontal m_body = null;
   //-----------------------------------------------------------------------------------------------------------------//
   class LauncherScanner extends DialogEx implements ScannerBarcodeEx.OnScannerBarcodeListener
   {
      protected ScannerBarcodeEx m_scanner = new ScannerBarcodeEx(getContext(), ConfigDisplayMetrics.ScannerPoint, false);
      public LauncherScanner(Context pWnd, int nID, int nLayoutMode)
      {
         super(pWnd, nID, nLayoutMode);
         try
         {
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
         LinearHorizontal body = new LinearHorizontal(getContext());
         {
            this.m_scanner.SetOnScannerBarcodeListener(this);
            this.m_scanner.setPadding(8, 8, 0, 0);
            body.setGravity(Gravity.CENTER);
            body.addView(this.m_scanner, this.m_scanner.Params);
            body.addView(new SpaceEx(getContext(), 0x32, 8, false));
         }
         this.SetIcon(R.drawable.ic_paytag);
         this.View.addView(body);
         this.SetButtons(new int[]{R.string.button_cancel});
         super.Create();
      }
      //-----------------------------------------------------------------------------------------------------------------//
      @Override
      protected void onStart()
      {
         super.onStart();
         this.m_scanner.start();
      }
      //-----------------------------------------------------------------------------------------------------------------//
      @Override
      protected void onStop()
      {
         this.m_scanner.stop();
         super.onStop();
      }
      //-----------------------------------------------------------------------------------------------------------------//
      @Override
      public void onClick(android.view.View pView)
      {
         final int nId = pView.getId();

         switch (nId)
         {
            case R.string.ids_take_picture:
               //m_scanner.onClick(m_scanner.Data);
               break;
            case R.string.button_cancel:
               this.dismiss();
               break;
         }
      }
      //-----------------------------------------------------------------------------------------------------------------//
      @Override
      public void OnHasData(Result result)
      {
         m_cupom_id.Data.setText(result.getText());
         m_board.Data.requestFocus();
         this.dismiss();
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   class LauncherPayTag extends Thread
   {
      public LauncherPayTag()
      {
         super();
         setName(getClass().getName());
         /* display message */
         m_dialog.setCaption(R.string.manager_rdtag_title);
         m_dialog.setMessage(R.string.manager_assign_process);
         ShowDialog(m_dialog);
      }
      @Override
      public void run()
      {
         try
         {
            RemotePaymentResponse pSuccess = RemotePaymentResponse.ResponseERROR;
            String szCaption = m_pWnd.getString(R.string.manager_rdtag_title);
            String szBoard = m_board.GetData().toUpperCase().trim();
            String szCupom = m_cupom_id.GetData().toUpperCase().trim();
            Operations operations = m_pLane.getOperations();
            /* make payment */
            pSuccess = operations.paymentRDTag(szCupom, szBoard);
            /* close dialog */
            CloseDialog(m_dialog);
            /* check process result */
            final String fmt = RemotePaymentResponse.FormatMessage(getContext(), pSuccess, szBoard);

            if (pSuccess != RemotePaymentResponse.ResponseOK)
            {
               ShowMsgBox(szCaption, fmt, RemotePaymentResponse.FormatMessageID(pSuccess));
            }
            else
            {
               ShowMsgBox(szCaption, fmt, RemotePaymentResponse.FormatMessageID(pSuccess));
               dismiss();
            }
         }
         catch (Exception e)
         {
            CloseDialog(m_dialog);
            ShowMsgBox(getContext().getString(R.string.ids_warning), e.getMessage(), MessageBox.IDERROR);
         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   class LauncherPayMoney extends Thread
   {
      public LauncherPayMoney()
      {
         super();
         setName(getClass().getName());
         /* display message */
         m_dialog.setCaption(R.string.manager_rdmoney_title);
         m_dialog.setMessage(R.string.manager_assign_process);
         ShowDialog(m_dialog);
      }
      @Override
      public void run()
      {
         try
         {
            RemotePaymentResponse pSuccess = RemotePaymentResponse.ResponseERROR;
            final String szCaption = m_pWnd.getString(R.string.manager_rdmoney_title);
            final String szCupom = m_cupom_id.GetData().toUpperCase().trim();
            final String szCategory = m_category.GetData().trim();
            final Operations operations = m_pLane.getOperations();
            //
            Log.e(this.getClass().getName(), String.format("TransactionId %s", m_szTransactionId));
            pSuccess = operations.paymentRDCash(szCupom, szCategory);

            /* hide dialog */
            CloseDialog(m_dialog);
            final String fmt = RemotePaymentResponse.FormatMessage(getContext(), pSuccess, m_szTransactionId);

            /* check response */
            if (pSuccess != RemotePaymentResponse.ResponseOK)
            {
               ShowMsgBox(szCaption, fmt, RemotePaymentResponse.FormatMessageID(pSuccess));
            }
            else
            {
               boolean bOnline = m_printer.IsOnLine();

               ShowMsgBox(szCaption, fmt, RemotePaymentResponse.FormatMessageID(pSuccess));
               dismiss();
               /* check if printer is online */
               if(bOnline)
               {
                  m_pWnd.runOnUiThread(new Runnable()
                  {
                     final String m_szData = operations.GetCupom();
                     final StringBuffer m_pData = CreateTicket.Builder(this.m_szData);

                     @Override
                     public void run()
                     {
                        /* send ticket to printer */
                        m_printer.SendCupomToPrinter(this.m_pData);
                     }
                  });
               }
            }
         }
         catch (Exception e)
         {
            CloseDialog(m_dialog);
            ShowMsgBox(getContext().getString(R.string.ids_warning), e.getMessage(), MessageBox.IDERROR);
         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public DialogTableViewItem(Context pWnd,
                              final TableWidget.TableItem pItem,
                              final Lane pLane,
                              final PrinterManager pCon,
                              final TableWidget.DetailsArray pDetails)
   {
      super(pWnd, R.string.dialog_tag, LM_HORIZONTAL);
      try
      {
         this.m_pLane = pLane;
         this.m_printer = pCon;
         this.m_item = pItem;
         this.m_details = pDetails;
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
      LinearHorizontal cupom = new LinearHorizontal(getContext());
      LinearHorizontal body = new LinearHorizontal(getContext());
      {
         Rect padding = ConfigDisplayMetrics.EditRect;

         this.m_board = new EditTextEx(getContext(), R.string.ids_placa, BOARD, true, -1, -1, true);
         this.m_board.Params.gravity = Gravity.RIGHT;
         this.m_bmp = new ImageViewEx(getContext(), this.m_point, R.drawable.box_error, true, padding, null);
         this.m_bmp.setGravity(Gravity.CENTER);
         this.m_bdown = new CheckBoxEx(getContext(), R.string.ids_bbaixa, false, false, -1);
         this.m_bdown.Params.gravity = Gravity.RIGHT;
         this.m_datetime = new EditTextEx(getContext(), R.string.ids_datetime, 26, false, -1, -1, true);
         this.m_datetime.Params.gravity = Gravity.RIGHT;
         this.m_via = new EditTextEx(getContext(), R.string.ids_via, 0x12, false, -1, -1, true);
         this.m_via.Params.gravity = Gravity.RIGHT;
         this.m_unknow = new CheckBoxEx(getContext(), "None", false, false, -1);
         this.m_unknow.Params.gravity = Gravity.RIGHT;
         this.m_comment = new CheckBoxEx(getContext(), R.string.IDS_VIOLATION, false, false, CheckBoxEx.DEFAULT);
         this.m_comment.Params.gravity = Gravity.RIGHT;
         //
         this.m_cupom_id = new EditTextEx(getContext(), R.string.ids_do_recibo, 0xc, true, -1, -1, true);
         {
            this.m_cupom_id.Invalidate(true);
            Point point = new Point(this.m_cupom_id.getHeight(), this.m_cupom_id.getHeight());
            this.m_do_scanner = new ImageViewEx(getContext(), point, R.drawable.ic_tbl_photo_image, false, null, null);
            this.m_do_scanner.setPadding(8, 8, 0, 0);
            cupom.addView(this.m_cupom_id);
            cupom.addView(this.m_do_scanner);
            cupom.Params.gravity = Gravity.RIGHT;
         }
         this.m_category = new EditTextEx(getContext(), R.string.ids_category, 0x2, false, -1, 0x50, true);
         this.m_category.Params.gravity = Gravity.RIGHT;

         this.m_comment.SetMargins(new Rect(0, 0, 8, 0));
         this.m_bdown.SetMargins(new Rect(0, 0, 8, 0));

         body.SetMargins(ConfigDisplayMetrics.EditRect);

         this.m_datetime.Data.setInputType(InputType.TYPE_DATETIME_VARIATION_NORMAL);
         this.m_via.Data.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
         this.m_board.Data.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
         this.m_cupom_id.Data.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
         this.m_category.Data.setInputType(InputType.TYPE_CLASS_NUMBER);

         this.m_cupom_id.Data.addTextChangedListener(this);
         this.m_cupom_id.Data.setCursorVisible(true);
         this.m_category.Data.addTextChangedListener(this);
         this.m_category.Data.setCursorVisible(true);
         this.m_board.Data.addTextChangedListener(this);
         this.m_board.Data.setCursorVisible(true);
      }
      LinearVertical left = new LinearVertical(getContext());
      {
         left.addView(this.m_datetime);
         left.addView(this.m_via);
         left.addView(cupom);
         left.addView(this.m_board);
         left.addView(this.m_category);
         left.addView(this.m_bdown);
         left.addView(this.m_comment);
         left.SetMargins(ConfigDisplayMetrics.EditRect);

         if(this.m_item != null)
         {
            Vector<TableWidget.FieldData> data = this.m_item.GetItems();
            boolean is_payed = false;

            if(data.size() >= 4)
            {
               this.m_datetime.Data.setText(data.get(0).Name.trim ());
               this.m_via.Data.setText(data.get(1).Name.trim ());
               this.m_imagelink = data.get(3).Name.trim ();
               this.m_szTransactionId = data.get(3).Name.trim ();
               //
               is_payed = !data.get(4).Name.trim ().isEmpty () &&
                  !data.get(4).Name.trim ().equals ( "NO" );
               //
               this.m_bdown.setChecked(is_payed);
               this.m_comment.setChecked(data.get(4).Name.trim ().equals("NO"));
            }
         }

         if(this.m_details != null)
         {
            boolean bdown = (!this.m_details.PaymentMeans.isEmpty () &&
               !this.m_details.PaymentMeans.equals ( "NO" ));
            boolean bcomment = this.m_details.PaymentMeans.equals ( "NO" );

            this.m_datetime.Data.setText ( this.m_details.Moment );
            this.m_via.Data.setText ( this.m_details.AliasLaneName );
            this.m_imagelink = this.m_details.TransactionId;
            this.m_szTransactionId = this.m_details.TransactionId;
            this.m_bdown.setChecked ( bdown );
            this.m_comment.setChecked ( bcomment );
            //this.m_board.Data.setText ( this.m_details.PanNumber );
            this.m_category.Data.setText ( this.m_details.VehicleClass );
         }
      }

      this.m_do_scanner.setOnClickListener(this);
      this.SetButtons(new int[]{R.string.ids_pgto_money, R.string.ids_pgto_tag_placa, R.string.button_cancel});
      this.SetIcon(R.drawable.ic_paytag);
      /* change local to global class variable */
      this.m_body = body;

      this.m_body.addView(left, left.Params);
      this.m_body.addView(this.m_bmp, this.m_bmp.Params);
      super.View.addView(this.m_body, this.m_body.Params);

      new TaskDownloadBitmap(this.m_bmp, this.m_point).execute(Utils.MountImageLink(getContext(), this.m_imagelink));
      this.m_dialog = new DisplayMessage(getContext(), -1, -1);
      this.m_cupom_id.Data.requestLayout();
      this.SetKeyboard(false);
      super.Create();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(android.view.View v) // DialogInterface dialog, int which)
   {
      final int nId = v.getId();

      switch (nId)
      {
         case R.string.ids_pgto_money:
            new LauncherPayMoney().start();
            break;
         case R.string.ids_pgto_tag_placa:
            new LauncherPayTag().start();
            break;
         case R.string.button_cancel:
            this.dismiss();
            break;
         case R.drawable.ic_tbl_photo_image:
            this.DoScanner();
            break;
      }
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
      boolean bId = this.m_cupom_id.GetData().trim().isEmpty();
      boolean bPlate = this.m_board.GetData().trim().isEmpty();
      boolean bCat = this.m_category.GetData().trim().isEmpty();

      SetControlById(R.string.ids_pgto_money, !bId && !bCat);
      SetControlById(R.string.ids_pgto_tag_placa, !bId && !bPlate);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void afterTextChanged(Editable s)
   {

   }
   //-----------------------------------------------------------------------------------------------------------------//
   protected void DoScanner()
   {
      new LauncherScanner(this.m_pWnd, R.string.dlg_do_scanner, DialogEx.LM_HORIZONTAL);
   }
}
