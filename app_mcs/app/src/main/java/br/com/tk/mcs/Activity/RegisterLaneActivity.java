/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.tk.mcs.Component.MessageBox;
import br.com.tk.mcs.Database.PersistenceController;
import br.com.tk.mcs.Generic.INetAddressValidate;
import br.com.tk.mcs.R;

public class RegisterLaneActivity extends AppCompatActivity implements View.OnClickListener
{
   static int WRAP = android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;
   static int MATCH = android.widget.LinearLayout.LayoutParams.MATCH_PARENT;
   static int SIZE = 0xdc;
   //
   private boolean m_bUpdate = false;
   private String m_ip;
   private Button m_btUpAdd;
   private EditText m_edtIP;
   private Spinner m_spName;
   private Spinner m_sentido;
   private List<String> m_list;
   private List<String> m_list2;
   private static final String m_IP = "IP";
   private PersistenceController m_controller;
   android.widget.LinearLayout m_Main = null;
   android.widget.TextView m_TextView = null;
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      //setContentView(R.layout.activity_register_lane);
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
      //update = false;
      m_Main = new android.widget.LinearLayout(this);
      {
         android.widget.LinearLayout.LayoutParams pLay = this.SetParameters(MATCH, MATCH);
         {
            pLay.gravity = android.view.Gravity.CENTER;
            m_Main.setLayoutParams(pLay);
         }
         this.m_Main.setOrientation(android.widget.LinearLayout.VERTICAL);
      }
      m_controller = new PersistenceController(this);
      this.m_TextView = new android.widget.TextView(this);
      {
         android.widget.LinearLayout.LayoutParams p = this.SetParameters(WRAP, WRAP);
         {
            p.gravity = android.view.Gravity.CENTER;
            p.setMargins(8, 8, 8, 8);
         }
         this.m_TextView.setText(br.com.tk.mcs.R.string.register_title);
         this.m_TextView.setTextSize(24);
         this.m_TextView.setSingleLine();
         this.m_Main.addView(this.m_TextView, p);
      }
      this.m_sentido = new android.widget.Spinner(this);
      {
         android.widget.LinearLayout.LayoutParams p = this.SetParameters(WRAP, WRAP);
         {
            p.gravity = android.view.Gravity.CENTER;
            p.setMargins(8, 16, 8, 8);
         }
         this.m_sentido.setMinimumWidth(SIZE);
         this.m_Main.addView(this.m_sentido, p);
      }
      this.m_spName = new android.widget.Spinner(this);
      {
         android.widget.LinearLayout.LayoutParams p = this.SetParameters(WRAP, WRAP);
         {
            p.gravity = android.view.Gravity.CENTER;
            p.setMargins(8, 16, 8, 8);
         }
         this.m_spName.setMinimumWidth(SIZE);
         this.m_Main.addView(this.m_spName, p);
      }
      this.m_edtIP = new android.widget.EditText(this);
      {
         br.com.tk.mcs.Generic.ISetTextMaxLength.SetMaxLength(this.m_edtIP, 0x10, 0x10);
         this.m_edtIP.setMinimumWidth(SIZE);
         this.m_edtIP.setHint("Endereço IP");
         this.m_edtIP.setCursorVisible(true);
         this.m_edtIP.setInputType(android.text.InputType.TYPE_CLASS_NUMBER| android.text.InputType.TYPE_CLASS_TEXT);
         //
         m_edtIP.addTextChangedListener(new TextWatcher()
         {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
               boolean bOK = INetAddressValidate.getInstance().isValid(s.toString());
               m_btUpAdd.setEnabled(bOK);
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
         });

         android.widget.LinearLayout.LayoutParams p = this.SetParameters(WRAP, WRAP);
         {
            p.gravity = android.view.Gravity.CENTER;
            p.setMargins(8, 16, 8, 8);
         }
         this.m_Main.addView(this.m_edtIP, p);
      }
      //
      android.widget.Space pSep = new android.widget.Space(this);
      {
         pSep.setLayoutParams(this.SetParameters(0, 8));
      }
      m_btUpAdd = new android.widget.Button(this);
      {
         android.widget.LinearLayout.LayoutParams p = this.SetParameters(WRAP, WRAP);
         {
            p.gravity = android.view.Gravity.CENTER;
            p.setMargins(8, 8, 8, 8);
         }
         this.m_btUpAdd.setEnabled(false);
         m_btUpAdd.setText(br.com.tk.mcs.R.string.button_ok);
         m_btUpAdd.setBackgroundResource(R.drawable.button_selector);
         m_btUpAdd.setGravity(Gravity.CENTER);
         m_btUpAdd.setOnClickListener(this);

         this.m_Main.addView(this.m_btUpAdd, p);
      }
      //m_sentido = (Spinner) findViewById(R.id.spinner2);
      m_list2 = new ArrayList<>();
      {
         m_list2.add("P");
         m_list2.add("S");
      }
      ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, m_list2);
      dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      m_sentido.setAdapter(dataAdapter2);
      //
      //m_spName = (Spinner) findViewById(R.id.spinner);
      m_list = new ArrayList<>();
      //
      for (int i = 1; i < 30; i++)
      {
         m_list.add(getString(R.string.manager_lane_name) + String.format(" %02d", i));
      }
      //
      ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, m_list);
      dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      m_spName.setAdapter(dataAdapter);
      //
      Bundle bundle = getIntent().getExtras();

      if (bundle != null)
      {
         if (bundle.containsKey(m_IP))
         {
            m_ip = bundle.getString(m_IP);
            m_edtIP.setText(m_ip);
            int pos = dataAdapter.getPosition(m_controller.getLaneNameByIP(m_ip));
            m_spName.setSelection(pos);
            m_bUpdate = !m_bUpdate;
         }
      }

      this.setContentView(this.m_Main);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(View view)
   {
      String szIP = m_edtIP.getText().toString().trim();
      String szName = m_spName.getSelectedItem().toString().trim();
      String szDirection = m_sentido.getSelectedItem().toString().trim();

      if (!szIP.isEmpty() && !szName.isEmpty())
      {
         if(!INetAddressValidate.getInstance().isValid(szIP))
         {
            String szFmt = String.format(getString(R.string.alert_ip_message), szIP);
            //Toast.makeText(getApplicationContext(), szFmt, Toast.LENGTH_LONG).show();
            new MessageBox(this, getString(R.string.ids_warning), szFmt, MessageBox.IDWARNING);
            return;
         }

         if (!m_bUpdate)
         {
            if (!m_controller.insertLane(szName, szIP, szDirection))
            {
               //Toast.makeText(getApplicationContext(), getString(R.string.register_msg_error), Toast.LENGTH_SHORT).show();
               new MessageBox(this, R.string.ids_warning, R.string.register_msg_error, MessageBox.IDWARNING);
            }
            else
            {
               Toast.makeText(this, getString(R.string.register_msg_add), Toast.LENGTH_SHORT).show();
               this.finish();
            }
         }
         else
         {
            if (!m_controller.updateLane(m_ip, szIP, szName))
            {
               //Toast.makeText(getApplicationContext(), getString(R.string.register_msg_error), Toast.LENGTH_SHORT).show();
               new MessageBox(this, R.string.ids_warning, R.string.register_msg_error, MessageBox.IDWARNING);
            }
            else
            {
               Toast.makeText(this, getString(R.string.register_msg_up), Toast.LENGTH_SHORT).show();
               this.finish();
            }
         }
      }
      else
      {
         //Toast.makeText(getApplicationContext(), getString(R.string.register_msg_error),  Toast.LENGTH_SHORT).show();
         new MessageBox(this, R.string.ids_warning, R.string.register_msg_error, MessageBox.IDWARNING);
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   android.widget.LinearLayout.LayoutParams SetParameters(int nWidth, int nHeight)
   {
      return new android.widget.LinearLayout.LayoutParams(nWidth, nHeight);
   }
}
