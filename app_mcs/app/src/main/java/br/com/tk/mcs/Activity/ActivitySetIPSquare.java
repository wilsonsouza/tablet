/*

   Sistema de Gestão de Pistas

   (C) 2016 TecSidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.tk.mcs.Component.DisplayMessage;
import br.com.tk.mcs.Component.MessageBox;
import br.com.tk.mcs.Database.PersistenceControllerSquare;
import br.com.tk.mcs.Generic.INetAddressValidate;
import br.com.tk.mcs.Generic.Ping;
import br.com.tk.mcs.Generic.Utils;
import br.com.tk.mcs.R;

public class ActivitySetIPSquare extends AppCompatActivity implements View.OnClickListener
{
   static int WRAP = android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;
   static int MATCH = android.widget.LinearLayout.LayoutParams.MATCH_PARENT;
   static int SIZE = 0xdc;

   private android.widget.EditText m_IP;
   private Button m_Update;
   private Button m_Verify;
   private PersistenceControllerSquare m_Controller = null;
   private android.widget.LinearLayout m_body = null;
   private DisplayMessage m_dialog = null;
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      //setContentView(R.layout.activity_set_ipsquare2);
      this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
      this.m_Controller = new br.com.tk.mcs.Database.PersistenceControllerSquare(this);
      this.m_body = new android.widget.LinearLayout(this);
      {
         android.widget.LinearLayout.LayoutParams p = br.com.tk.mcs.Generic.ILayoutParameters.Linear(MATCH, WRAP);
         {
            p.gravity = android.view.Gravity.CENTER_HORIZONTAL| android.view.Gravity.CENTER_VERTICAL;
            p.setMargins(8, 8, 8, 8);
         }

         this.m_body.setLayoutParams(p);
         this.m_body.setOrientation(android.widget.LinearLayout.VERTICAL);
         this.setContentView(this.m_body, p);
      }
      //
      android.widget.LinearLayout.LayoutParams p = br.com.tk.mcs.Generic.ILayoutParameters.Linear(WRAP, WRAP);
      {
         p.gravity = android.view.Gravity.CENTER;
         p.setMargins(8, 16, 8, 8);

         TextView pCaption = new android.widget.TextView(this);
         {
            android.widget.LinearLayout.LayoutParams pv = br.com.tk.mcs.Generic.ILayoutParameters.Linear(WRAP, WRAP);
            {
               pv.gravity = android.view.Gravity.CENTER;
               pv.setMargins(8, 8, 8, 8);
            }

            pCaption.setLayoutParams(pv);
            pCaption.setText(br.com.tk.mcs.R.string.manager_set_ip_square);
            pCaption.setTextSize(24);
            this.m_body.addView(pCaption);
         }
      }
      this.m_IP = new android.widget.EditText(this);
      {
         br.com.tk.mcs.Generic.ISetTextMaxLength.SetMaxLength(this.m_IP, 0x10, 0x10);
         this.m_IP.setLayoutParams(p);
         this.m_IP.setCursorVisible(true);
         this.m_IP.setGravity(android.view.View.TEXT_ALIGNMENT_CENTER);
         this.m_IP.setHint(br.com.tk.mcs.R.string.ip_address);
         this.m_IP.setInputType(android.text.InputType.TYPE_CLASS_NUMBER| android.text.InputType.TYPE_CLASS_TEXT);
         this.m_IP.setSingleLine();
         this.m_IP.setText(this.m_Controller.Get());
         this.m_IP.setMinimumWidth(SIZE);
         this.m_IP.setCursorVisible(true);
         //
         this.m_IP.addTextChangedListener(new TextWatcher()
         {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
               boolean bOK = INetAddressValidate.getInstance().isValid(s.toString());
               //
               m_Update.setEnabled(bOK);
               m_Verify.setEnabled(bOK);
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
         });

         this.m_body.addView(this.m_IP, p);
      }
      android.widget.LinearLayout pBtn = new android.widget.LinearLayout(this);
      {
         android.widget.LinearLayout.LayoutParams pp = br.com.tk.mcs.Generic.ILayoutParameters.Linear(WRAP, WRAP);
         {
            pp.gravity = android.view.Gravity.CENTER;
            pp.setMargins(8, 8, 8, 8);
            pBtn.setLayoutParams(pp);
         }
         m_Update = new android.widget.Button(this);
         {
            m_Update.setText(br.com.tk.mcs.R.string.button_ok);
            m_Update.setBackgroundResource(br.com.tk.mcs.R.drawable.button_selector);
         }
         m_Verify = new android.widget.Button(this);
         {
            m_Verify.setText(br.com.tk.mcs.R.string.manager_set_ip_square_tester_button);
            m_Verify.setBackgroundResource(br.com.tk.mcs.R.drawable.button_selector);
         }

         pBtn.setOrientation(android.widget.LinearLayout.HORIZONTAL);
         pBtn.addView(this.m_Update, pp);
         pBtn.addView(this.m_Verify, pp);

         this.m_body.addView(pBtn, pp);
      }
      //
      boolean bOK = INetAddressValidate.getInstance().isValid(this.m_IP.getText().toString());
      {
         m_Update.setEnabled(bOK);
         m_Verify.setEnabled(bOK);
         m_Update.setOnClickListener(this);
         m_Verify.setOnClickListener(this);
      }

      this.m_dialog = new DisplayMessage(this, R.string.ids_wait, -1);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onDestroy()
   {
      super.onDestroy();
      m_Controller = null;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(View v)
   {
      final Context context = this;

      if(v == this.m_Update)
      {
         String szIP = m_IP.getText().toString().trim();
         //
         m_Controller.Update(szIP);
         this.finish();
         return;
      }

      if(v == this.m_Verify)
      {
         String m_szIP = m_IP.getText().toString().trim();
         String m_szFmt = String.format("Verificando IP %s...", m_szIP);
         boolean m_bSuccess = false;

         m_dialog.show();
         m_dialog.setMessage(m_szFmt);
         Utils.hideSoftKeyboard(this);

         /* try to connect on msc system */
         m_bSuccess = Ping.IsOnLine(m_szIP);
         m_dialog.dismiss();

         if (m_bSuccess)
         {
            new MessageBox(context, "Resultado...", String.format("IP %s OK!", m_szIP), MessageBox.IDOK);
         }
         else
         {
            new MessageBox(context, "Resultado...", String.format("IP %s sem resposta!", m_szIP), MessageBox.IDWARNING);
         }
      }
   }
}
