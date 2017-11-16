/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Lane;

import java.io.Serializable;

import br.com.tk.mcs.Lane.State.BarreraState;
import br.com.tk.mcs.Lane.State.LaneState;
import br.com.tk.mcs.Lane.State.TrafficLightState;

/**
 * Created by revolution on 30/01/16.
 */

public class Lane implements Serializable
{
   private int m_nID = 0;
   private String m_szLaneName = "";
   private LaneState m_LaneState = LaneState.Starting;
   private BarreraState m_BarreraState = BarreraState.SensorUnknown;
   private TrafficLightState m_PassageState = TrafficLightState.LightUnkown;
   private TrafficLightState m_MarquiseState = TrafficLightState.LightUnkown;
   private int m_nVehicleStopped = 0;
   private int m_nTotalVehicles = 0;
   private Operations m_operations = null;

   public Lane(int id, String name, String szOperator, final Operations operations)
   {
      m_nID = id;
      m_szLaneName = name;
      m_operations = operations;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public int getTotalVehicles() //GetTotalOfVehicles
   {
      return m_nTotalVehicles;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public void setTotalVehicles(int nTotalVehicles)
   {
      m_nTotalVehicles = nTotalVehicles;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public int getVehicleStopped()
   {
      return m_nVehicleStopped;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public void setVehicleStopped(int nVehicleStopped)
   {
      m_nVehicleStopped = nVehicleStopped;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public int getId()
   {
      return m_nID;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public void setId(int nID)
   {
      this.m_nID = nID;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public LaneState getLaneState()
   {
      return m_LaneState;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public void setLaneState(LaneState value)
   {
      m_LaneState = value;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public String getLaneName()
   {
      return m_szLaneName;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public void setLaneName(String szLaneName)
   {
      m_szLaneName = szLaneName;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public Operations getOperations()
   {
      return m_operations;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public void setOperations(Operations value)
   {
      m_operations = value;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public BarreraState getBarreraState()
   {
      return m_BarreraState;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public void setBarreraState(BarreraState value)
   {
      m_BarreraState = value;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public TrafficLightState getPassageState()
   {
      return m_PassageState;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void setPassageState(TrafficLightState value)
   {
      m_PassageState = value;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public TrafficLightState getMarquiseState()
   {
      return m_MarquiseState;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void setMarquiseState(TrafficLightState value)
   {
      m_MarquiseState = value;
   }
}