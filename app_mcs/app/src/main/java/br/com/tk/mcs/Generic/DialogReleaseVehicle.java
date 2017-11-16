/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Plates to testes
   | 0618      | 00100    | 1000000004 | DBT0000 | 01           |        | BL       | PRE     | 05     | 01     |           |            |        |
   | 0618      | 00100    | 1000000005 | EJX5458 | 04           |        | BL       | POS     | 00     | 02     |           |            |        |
   | 0618      | 00100    | 1000000006 | CYB6750 | 04           |        | BL       | POS     | 00     | 02     |           |            |        |
   | 0618      | 00100    | 1000000007 | EJX5459 | 04           |        | BL       | POS     | 00     | 02     |           |            |        |
   | 0618      | 00100    | 1000000008 | EJX5456 | 04           |        | BL       | POS     | 00     | 02     |           |            |        |

   View live log file
   tail -f /via/trazas/TRAZA.LIS
   Description:
 */
package br.com.tk.mcs.Generic;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.gms.vision.text.Line;

import java.util.Locale;

import br.com.tk.mcs.Component.DialogEx;
import br.com.tk.mcs.Component.DisplayMessage;
import br.com.tk.mcs.Component.EditTextEx;
import br.com.tk.mcs.Component.ImageViewEx;
import br.com.tk.mcs.Component.LinearHorizontal;
import br.com.tk.mcs.Component.LinearVertical;
import br.com.tk.mcs.Component.MessageBox;
import br.com.tk.mcs.Component.RadioButtonEx;
import br.com.tk.mcs.Component.SpinnerEx;
import br.com.tk.mcs.Component.TaskDownloadBitmap;
import br.com.tk.mcs.Drivers.PrinterBluetoothControl;
import br.com.tk.mcs.Lane.Lane;
import br.com.tk.mcs.Lane.Operations;
import br.com.tk.mcs.R;
import br.com.tk.mcs.Remote.Response.RemotePaymentPermittedResponse;
import br.com.tk.mcs.Remote.Response.RemotePaymentResponse;

/**
 * Created by wilsonsouza on 31/01/17.
 */

public class DialogReleaseVehicle extends DialogEx
   implements DialogEx.IRunnable, TextWatcher, AdapterView.OnItemSelectedListener
{
   protected static int DOCKET = 0xc;
   protected static int BORD = 0x7;
   protected Point m_point = ConfigDisplayMetrics.VehicleImage;
   protected Lane m_pLane = null;
   protected RadioButtonEx m_isento = null;
   protected RadioButtonEx m_tag = null;
   protected EditTextEx m_number_board_0 = null;
   protected EditTextEx m_field_tag = null;
   protected ImageViewEx m_bitmap = null;
   protected SpinnerEx m_group = null;
   protected String m_imagelink = null;
   protected DisplayMessage m_dialog = null;
   protected PrinterBluetoothControl m_printer = null;
   protected ArrayAdapter<String> m_pIsentoList = null;
   protected String[] m_pList = new String[]{
      "Concessionária", "PRF", "Bombeiro", "Forças Armadas", "Órgão Público", "Polícia Militar", "Polícia Cívil", "Ibama", "Isentos Autarquias", "CNO", "Tecsidel"
   };
   protected String m_szIsentoCode = "";

   //-----------------------------------------------------------------------------------------------------------------//
   class LauncherPayTag extends Thread
   {
      public LauncherPayTag()
      {
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
            String szTag = m_field_tag.GetData().toUpperCase().trim();
            Operations operations = m_pLane.getOperations();
            //
            RemotePaymentPermittedResponse rpp = operations.isRemotePaymentPermitted();
            pSuccess = operations.remotePayment(rpp, szTag, null);
            /* close dialog */
            CloseDialog(m_dialog);
            Log.e(this.getClass().getName(), "payment with tag " + pSuccess.toString());
            /* check process result */
            if (pSuccess != RemotePaymentResponse.ResponseOK)
            {
               String fmt = RemotePaymentResponse.FormatMessage(getContext(), pSuccess, szTag);

               //Log.e(this.getName(), String.format("Result %s, %s", pSuccess, fmt));
               ShowMsgBox(szCaption, fmt, RemotePaymentResponse.FormatMessageID(pSuccess));
            }
            else
            {
               dismiss();
            }
         }
         catch (Exception e)
         {
            CloseDialog(m_dialog);
            ShowMsgBox(getContext().getString(R.string.ids_warning), e.getMessage(), MessageBox.IDERROR);
            //Log.e(getClass().getName(), e.getMessage());
            e.printStackTrace();
         }
      }
   }

   //-----------------------------------------------------------------------------------------------------------------//
   class LauncherIsento extends Thread
   {
      public LauncherIsento()
      {
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
            String szCaption = m_pWnd.getString(R.string.manager_rdmoney_title);
            Operations operations = m_pLane.getOperations();
            String szBoard = m_number_board_0.GetData().toUpperCase().trim();

            /* process money */
            RemotePaymentPermittedResponse rpp = operations.isRemotePaymentPermitted();
            pSuccess = operations.remotePayment(rpp, szBoard, m_szIsentoCode);

            /* hide dialog */
            CloseDialog(m_dialog);

            /* check response */
            if (pSuccess != RemotePaymentResponse.ResponseOK)
            {
               String fmt = RemotePaymentResponse.FormatMessage(getContext(), pSuccess, szBoard);

               //Log.e(this.getName(), String.format("Result %s, %s", pSuccess, fmt));
               ShowMsgBox(szCaption, fmt, RemotePaymentResponse.FormatMessageID(pSuccess));
            }
            else
            {
               /* process ok then send ticket to printer */
               //StringBuffer pData = CreateTicket.Builder(m_pLane);
               //m_printer.SendCupomToPrinter(pData);
               dismiss();
            }
         }
         catch (Exception e)
         {
            CloseDialog(m_dialog);
            ShowMsgBox(getContext().getString(R.string.ids_warning), e.getMessage(), MessageBox.IDERROR);
            //Log.e(getClass().getName(), e.getMessage());
            e.printStackTrace();
         }
      }
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public DialogReleaseVehicle(Context pWnd, final Lane pLane, final String szImageLink, PrinterBluetoothControl pCon)
   {
      super(pWnd, R.string.dialog_release_vehicle, LM_HORIZONTAL);
      try
      {
         this.m_pLane = pLane;
         this.m_imagelink = szImageLink;
         this.m_printer = pCon;
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
         body.SetMargins(new Rect(8, 8, 8, 8));
      }
      LinearVertical edit = new LinearVertical(getContext());
      {
         Rect padding = new Rect(8, 8, 8, 8);
         this.m_isento = new RadioButtonEx(getContext(), R.string.ids_isento, true, false, -1);
         this.m_isento.Params.gravity = Gravity.LEFT;
         this.m_group = new SpinnerEx(getContext(), R.string.ids_group, -1, -1, false, true);
         this.m_group.Params.gravity = Gravity.RIGHT;
         this.m_number_board_0 = new EditTextEx(getContext(), R.string.ids_number_id, 0x7, false, -1, -1, true);
         this.m_number_board_0.Params.gravity = Gravity.RIGHT;
         this.m_tag = new RadioButtonEx(getContext(), R.string.ids_tag, true, false, -1);
         this.m_tag.Params.gravity = Gravity.LEFT;
         this.m_field_tag = new EditTextEx(getContext(), R.string.ids_tag_placa, 0xc, false, -1, -1, true);
         this.m_field_tag.Params.gravity = Gravity.RIGHT;
         this.m_bitmap = new ImageViewEx(getContext(), this.m_point, R.drawable.box_error, true, padding, null);
         this.m_bitmap.setGravity(Gravity.CENTER);

         edit.addView(this.m_isento);
         edit.addView(this.m_group);
         edit.addView(this.m_number_board_0);
         edit.addView(this.m_tag);
         edit.addView(this.m_field_tag);
         body.addView(edit);

         {
            this.m_isento.Data.setOnClickListener(this);
            this.m_tag.Data.setOnClickListener(this);
         }
         {
            this.m_field_tag.Data.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            this.m_number_board_0.Data.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
         }

         this.m_field_tag.Data.addTextChangedListener(this);
         this.m_number_board_0.Data.addTextChangedListener(this);
      }
      LinearVertical img = new LinearVertical(getContext());
      {
         img.SetMargins(new Rect(8, 8, 8, 8));
         img.addView(this.m_bitmap);
         body.addView(img);
      }

      this.m_pIsentoList = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_checked);
      this.m_pIsentoList.addAll(this.m_pList);
      this.m_group.Data.setAdapter(this.m_pIsentoList);
      this.m_group.Data.setOnItemSelectedListener(this);

      try
      {
         this.m_imagelink = this.m_pLane.getOperations().isRemotePaymentPermitted().getTransaction().getTransactionid();
         //Log.e(getClass().getName(), "VehiclePhoto " + this.m_imagelink);
      }
      catch (Exception e)
      {
         //Log.e(getClass().getName(), e.getMessage());
         e.printStackTrace();
      }

      this.SetIcon(R.drawable.ic_release_vehicle);
      super.View.addView(body);
      this.SetButtons(new int[]{R.string.manager_button_confirm, R.string.manager_button_cancel});
      new TaskDownloadBitmap(this.m_bitmap, this.m_point).execute(Utils.MountImageLink(getContext(), this.m_imagelink));
      this.m_dialog = new DisplayMessage(getContext(), -1, -1);
      this.SetKeyboard(false);
      super.Create();
   }

   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(android.view.View view)
   {
      int which = view.getId();

      switch (which)
      {
         case R.string.manager_button_confirm:
            this.run(which);
            break;
         case R.string.manager_button_cancel:
            this.dismiss();
            break;
         default:
         {
            boolean bInseto = (view == m_isento.Data && m_isento.isChecked());
            boolean bTag = (view == m_tag.Data && m_tag.isChecked());
            {
               m_isento.setChecked(bInseto);
               m_tag.setChecked(bTag);
               m_group.setEnabled(bInseto);
               m_number_board_0.SetEnabled(bInseto);
               m_number_board_0.Data.setCursorVisible(bInseto);

               m_field_tag.SetEnabled(bTag);
               m_field_tag.Data.setCursorVisible(bTag);

               if (bInseto)
               {
                  this.m_number_board_0.Data.requestFocus();
               }
               if (bTag)
               {
                  this.m_field_tag.Data.requestFocus();
               }
            }
            break;
         }
      }
   }

   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void run(int which)
   {
      String caption_error = m_pWnd.getString(R.string.ids_caption_error);

      try
      {
         if (m_tag.isChecked())
         {
            new LauncherPayTag().start();
         }
         else if (m_isento.isChecked())
         {
            new LauncherIsento().start();
         }
      }
      catch (Exception e)
      {
         this.CloseDialog(this.m_dialog);
         ShowMsgBox(caption_error, e.getMessage(), MessageBox.IDERROR);
         Log.e(getClass().getName(), e.getMessage());
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   protected void onStart()
   {
      this.m_tag.Data.performClick();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   protected void onStop()
   {

   }

   //-----------------------------------------------------------------------------------------------------------------//
   private void DoItFree()
   {
      ShowMsgBox(R.string.ids_warning, R.string.ids_op_isento, MessageBox.IDWARNING);
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
      if (m_isento.isChecked())
      {
         final int nOk = this.m_number_board_0.GetData().trim().length();
         SetControlById(R.string.manager_button_confirm, nOk == BORD && !m_szIsentoCode.isEmpty());
      }
      else if (m_tag.isChecked())
      {
         final int nOk = this.m_field_tag.GetData().trim().length();
         SetControlById(R.string.manager_button_confirm, nOk == DOCKET || nOk == BORD);
      }
   }

   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void afterTextChanged(Editable s)
   {

   }

   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
   {
      m_szIsentoCode = String.format(Locale.FRANCE, "%02d", position + 1);
   }

   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onNothingSelected(AdapterView<?> parent)
   {
      m_szIsentoCode = "";
   }
   //-----------------------------------------------------------------------------------------------------------------//
}
