/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Created by wilson.souza (WR DevInfo)

   Description: managed bluetooth device node.
 */
package br.com.tk.mcs.Drivers;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wilsonsouza on 10/6/16.
 */

public class DeviceAdapter extends BaseAdapter
{
   private java.util.List<DeviceNode> m_NodeList;
   //-----------------------------------------------------------------------------------------------------------------//
   public DeviceAdapter()
   {
      m_NodeList = new ArrayList<DeviceNode>();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public int getCount()
   {
      return m_NodeList.size();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public Object getItem(int position)
   {
      return m_NodeList.get(position);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public long getItemId(int position)
   {
      return position;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public View getView(int position, View convertView, ViewGroup parent)
   {
      return convertView;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void add(String szName, String szAddress, boolean bPaired)
   {
      m_NodeList.add( new DeviceNode(szName, szAddress, bPaired));
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void clear()
   {
      m_NodeList.clear();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public DeviceNode find(String szAddress)
   {
      for(DeviceNode i: m_NodeList)
      {
         if(szAddress.equals(i.GetAddress()))
         {
            return i;
         }
      }
      return null;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public DeviceNode search(String szName)
   {
      for(DeviceNode i: m_NodeList)
      {
         if(szName.equals(i.GetName()))
         {
            return i;
         }
      }
      return null;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public List<DeviceNode> getItems()
   {
      return m_NodeList;
   }
}
