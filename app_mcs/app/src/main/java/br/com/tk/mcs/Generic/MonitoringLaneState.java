/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import br.com.tk.mcs.Activity.ManageLanes;
import br.com.tk.mcs.Component.LaneWidget;
import br.com.tk.mcs.Lane.State.LaneState;

/**
 * Created by wilsonsouza on 3/30/17.
 */

public class MonitoringLaneState extends VerifyLaneState
{
   //-----------------------------------------------------------------------------------------------------------------//
   public MonitoringLaneState(final String szUser, final LaneWidget pView, final ManageLanes pWnd, final BuilderManager pManager)
   {
      super(szUser, pWnd, pManager);
      super.m_pView = pView;
      super.m_pLane = super.m_pView.LaneHandle;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void run()
   {
      while (m_bWhile)
      {
         try
         {
            m_pLong = m_pView.LaneHandle.getOperations().getLongStatus();
            SetState(false);

            this.m_pView.postDelayed(new Runnable()
            {
               @Override
               public void run()
               {
                  VerifySensors();
                  UpdateStateOperator();
               }
            }, 0x64);
            /* suspend thread by 100ms and 100ns */
            Thread.sleep(0xFF);
         }
         catch (Exception e)
         {
            //Log.e(this.getClass().getName(), e.getMessage());
            e.printStackTrace();
         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   void VerifySensors()
   {
      try
      {
         /* verify if have vehicle stopped on the lane */
         this.m_pView.SetVehicle(this.m_state, this.m_nTotalVehicles, this.m_nStoppedVehicle, this.m_bOperator);
         this.m_pView.EnableSemaphore(this.m_state, this.m_bOperator, this.m_nSemaphoreOfMarquise);

         if (this.m_LightEntry != this.m_pLane.getMarquiseState())
         {
            this.m_pView.SetLightEntry(this.m_LightEntry);
         }

         if (this.m_LightExit != this.m_pLane.getPassageState())
         {
            this.m_pView.SetLightExit(this.m_LightExit);
         }

         if (this.m_BarreraState != this.m_pLane.getBarreraState())
         {
            this.m_pView.SetBarrer(this.m_BarreraState);
         }
      }
      catch (Exception e)
      {
         //Log.e(this.getClass().getName(), e.getMessage());
         e.printStackTrace();
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   void UpdateStateOperator()
   {
      final LaneState nState = this.m_pView.LaneHandle.getLaneState();

      if (this.m_state != nState || this.m_bOperator)
      {
         this.m_pView.Caption.Data.setBackgroundColor(LaneState.getColor(this.m_state));
         this.m_pView.LaneHandle.setLaneState(this.m_state);
      }
   }
 }
