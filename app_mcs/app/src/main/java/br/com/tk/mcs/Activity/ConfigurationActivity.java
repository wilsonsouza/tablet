/*

   Sistema de Gestão de Pistas

   (C) 2016 TecSidel

   Updated by wilson.souza (WR DevInfo)

   Description:
   Fixed: 09-20-2016
   event menu_config_update error StringIndexOutOfBoundsException
   event menu_config_delete error StringIndexOutOfBoundsException
 */
package br.com.tk.mcs.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import br.com.tk.mcs.Component.MessageBox;
import br.com.tk.mcs.Database.PersistenceController;
import br.com.tk.mcs.R;

public class ConfigurationActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
   private String m_current;
   private MenuItem m_update;
   private MenuItem m_delete;
   private MenuItem m_ip_square;
   private ListView m_view;
   private PersistenceController m_controller;
   private static final int m_LIMIT = 30;
   private static final String m_IP = "IP";
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   protected void onResume()
   {
      super.onResume();
      if (m_delete != null && m_update != null)
      {
         setEnable(false);
      }

      if (m_view != null)
      {
         m_view.setAdapter(m_controller.loadLane(this));
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_configuration);
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
      m_controller = new PersistenceController(this);
      m_view = (ListView) findViewById(R.id.lstLane);
      m_view.setOnItemClickListener(this);
      m_view.setAdapter(m_controller.loadLane(this));
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public boolean onCreateOptionsMenu(Menu menu)
   {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.menu_config, menu);
      m_delete = menu.findItem(R.id.menu_config_delete);
      m_update = menu.findItem(R.id.menu_config_update);
      m_ip_square = menu.findItem(R.id.menu_config_ip_square);
      //add menu item to teste
/*
      menu.add(0, R.id.menu_config_ip_square + 1, 0, "Teste MSC");
      menu.add(0, R.id.menu_config_ip_square + 3, 0, "Start scanner");
*/

      setEnable(false);
      return super.onCreateOptionsMenu(menu);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public boolean onOptionsItemSelected(MenuItem item)
   {
      Intent it = null;

      switch (item.getItemId())
      {
         case R.id.menu_config_insert:
         {
            if (m_controller.getLanesCount() < m_LIMIT)
            {
               it = new Intent(ConfigurationActivity.this, RegisterLaneActivity.class);
               startActivity(it);
               setEnable(false);
            }
            else
            {
               //Toast.makeText(getApplicationContext(), getString(R.string.config_msg_error), Toast.LENGTH_SHORT).show();
               new MessageBox(this, R.string.ids_warning, R.string.config_msg_error, MessageBox.IDWARNING);
            }
            return true;
         }
         case R.id.menu_config_update:
         {
            String szKey = m_current;
            it = new Intent(ConfigurationActivity.this, RegisterLaneActivity.class);
            //fix StringIndexOutOfBoundsException
            if(m_current.length() > 0)
            {
               szKey = m_current.substring(0, m_current.length() - 0xa);
            }
            //it.putExtra(IP, current.substring(0, current.length() - 30).trim());
            it.putExtra(m_IP, szKey.trim());
            startActivity(it);
            setEnable(false);
            return true;
         }
         case R.id.menu_config_delete:
         {
            String szKey = m_current;
            //fix StringIndexOutOfBoundsException
            if(m_current.length() > 0)
            {
               szKey = m_current.substring(0, m_current.length() - 0xa);
            }
            //String ip = current.substring(0, current.length() - 30).trim();
            m_controller.deleteLane(szKey.trim(), m_controller.getLaneNameByIP(szKey.trim()));
            m_view.setAdapter(m_controller.loadLane(ConfigurationActivity.this));
            setEnable(false);
            return true;
         }
         case R.id.menu_config_ip_square:
         {
            it = new Intent(ConfigurationActivity.this, ActivitySetIPSquare.class);
            startActivity(it);
            return true;
         }
      }

      return super.onOptionsItemSelected(item);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void setEnable(boolean status)
   {
      m_delete.setEnabled(status);
      m_update.setEnabled(status);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void onItemClick(AdapterView parent, View v, int position, long id)
   {
      setEnable(true);
      m_current = (String) (m_view.getItemAtPosition(position));
   }
}

