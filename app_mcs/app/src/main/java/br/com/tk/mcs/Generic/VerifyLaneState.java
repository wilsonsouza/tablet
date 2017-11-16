/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Created by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import br.com.tk.mcs.Activity.ManageLanes;
import br.com.tk.mcs.Component.LaneWidget;
import br.com.tk.mcs.Component.TableWidget;
import br.com.tk.mcs.Lane.Lane;
import br.com.tk.mcs.Lane.State.BarreraState;
import br.com.tk.mcs.Lane.State.LaneState;
import br.com.tk.mcs.Lane.State.TrafficLightState;
import br.com.tk.mcs.Remote.Response.GetLongStatusResponse;

/**
 * Created by wilsonsouza on 3/30/17.
 */

public class VerifyLaneState implements Runnable
{
   protected final int AVI_LANE = 2;
   protected int m_nTotalVehicles = 0;
   protected int m_nStoppedVehicle = 0;
   protected BarreraState m_BarreraState = BarreraState.SensorUnknown;
   protected TrafficLightState m_LightEntry = TrafficLightState.LightUnkown;
   protected TrafficLightState m_LightExit = TrafficLightState.LightUnkown;
   protected String m_szOperator;
   protected boolean m_bOperator = false;
   public LaneState m_state = LaneState.NoSync;
   public String CurrentVechicleImage = null;
   public int Current = 0;
   public int Selected = -1;
   public int Next = 0;
   protected LaneWidget m_pView = null;
   public boolean m_bChangeOperator = false;
   protected boolean m_bWhile = true;
   protected GetLongStatusResponse m_pLong = null;
   public Lane m_pLane = null;
   protected long m_start = System.currentTimeMillis();
   protected BuilderManager Manager = null;
   protected ManageLanes m_pWnd = null;
   protected int m_nLaneMode = 2;
   protected int m_nSemaphoreOfMarquise = 0;
   //-----------------------------------------------------------------------------------------------------------------//
   public VerifyLaneState(String szOperator, ManageLanes pWnd, BuilderManager pManager)
   {
      this.m_szOperator = szOperator;
      this.m_bOperator = this.m_szOperator.equals(szOperator);
      //this.setName(this.getClass().getName());
      //this.setPriority(Thread.NORM_PRIORITY);
      this.Manager = pManager;
      this.m_pWnd = pWnd;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetManagerPointer(BuilderManager pManager)
   {
      this.Manager = pManager;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void run()
   {
      final int nLanesMax = Manager.LanesView.Contaneir.getChildCount();

      while (this.m_bWhile && nLanesMax > 0)
      {
         try
         {
            m_pView = (LaneWidget) Manager.LanesView.Contaneir.findViewById(Current);
            m_pLane = m_pView.LaneHandle;
            m_pLong = m_pView.LaneHandle.getOperations().getLongStatus();
            //
            if (Current != Next)
            {
               SetState(true);
               /* run others method by main thread */
               this.m_pWnd.runOnUiThread(new Runnable()
               {
                  @Override
                  public void run()
                  {
                     VerifySensors();
                     UpdateStateOperator();
                     Current = Next;
                  }
               });
               //
               this.m_pView.postDelayed(new Runnable()
               {
                  @Override
                  public void run()
                  {
                     UpdateTableView();
                  }
               }, 0x400);
            }
            else
            {
               SetState(false);
               /* run others method by main thread */
               this.m_pWnd.runOnUiThread(new Runnable()
               {
                  @Override
                  public void run()
                  {
                     VerifySensors();
                     UpdateStateOperator();
                     Current = Next;
                  }
               });
               //
               this.m_pView.postDelayed(new Runnable()
               {
                  @Override
                  public void run()
                  {
                     UpdateTableView();
                  }
               }, 0x400);
            }
            /* wait 255ms only if Current is different of Next position */
            if(Current == Next)
            {
               Thread.sleep(0xFF);
            }

         }
         catch (Exception e)
         {
            //Log.e(this.getClass().getName(), e.getMessage());
            e.printStackTrace();
         }
      }
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public void start()
   {
      /*
      if (!this.isAlive())
      {
         m_bWhile = true;
         super.start();
      }
      */
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public void interrupt()
   {
      m_bWhile = false;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   LaneState GetLaneState(GetLongStatusResponse pLong, boolean bChanges)
   {
      if (pLong != null)
      {
         this.m_state = LaneState.fromValue(pLong.getLaneState());
      }
      else
      {
         if (bChanges)
         {
            this.m_state = LaneState.Starting;
         }
         else
         {
            this.m_state = LaneState.NoSync;
         }
      }
      return this.m_state;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   BarreraState GetBarreraState(GetLongStatusResponse pLong, boolean bChanges)
   {
      if (pLong != null)
      {
         return BarreraState.fromValue(pLong.getDevice().getBarrierExit());
      }
      else
      {
         if (bChanges)
         {
            return BarreraState.SensorUnknown;
         }
         else
         {
            if (this.m_state == LaneState.NoSync)
            {
               return BarreraState.SensorUnknown;
            }
            else
            {
               return BarreraState.SensorOFF;
            }
         }
      }
   }

   //-----------------------------------------------------------------------------------------------------------------//
   TrafficLightState GetTrafficLightState(GetLongStatusResponse pLong, boolean bChanges, boolean bOnOff)
   {
      if (pLong != null)
      {
         final int nLightStateEntry = pLong.getDevice().getLightStateEntry();
         final int nLightStateExit = pLong.getDevice().getLightStateExit();
         return TrafficLightState.fromValue(bOnOff ? nLightStateEntry : nLightStateExit);
      }
      else
      {
         if (bChanges)
         {
            return TrafficLightState.LightUnkown;
         }
         else
         {
            if (this.m_state == LaneState.NoSync)
            {
               return TrafficLightState.LightUnkown;
            }
            else
            {
               return TrafficLightState.LightOff;
            }
         }
      }
   }

   //-----------------------------------------------------------------------------------------------------------------//
   void SetState(final boolean bChanges)
   {
      try
      {
         this.m_BarreraState = GetBarreraState(this.m_pLong, bChanges);
         this.m_state = GetLaneState(this.m_pLong, bChanges);
         this.m_LightEntry = GetTrafficLightState(this.m_pLong, bChanges, true);
         this.m_LightExit = GetTrafficLightState(this.m_pLong, bChanges, false);

         if (this.m_pLong != null)
         {
            this.m_bChangeOperator = this.m_szOperator.equals(this.m_pLong.getOperatorCode());
            this.m_nTotalVehicles = this.m_pLong.getDevice().getTotalVehicles();
            this.m_nStoppedVehicle = this.m_pLong.getDevice().getVehicleStopped();
            this.m_nLaneMode = this.m_pLong.getLaneMode();

            final String szValue = this.m_pLong.getDevice().getLightStateCanopy();
            {
               if(szValue.trim().isEmpty())
               {
                  this.m_nSemaphoreOfMarquise = -1;
               }
               else
               {
                  this.m_nSemaphoreOfMarquise = Integer.parseInt(szValue);
               }
            }
         }
         else
         {
            this.m_bChangeOperator = false;
            this.m_nStoppedVehicle = 0;
            this.m_nTotalVehicles = 0;
            this.m_nLaneMode = 0;
            this.m_nSemaphoreOfMarquise = -1;
         }
      }
      catch (Exception e)
      {
         //Log.e(this.getClass().getName(), e.getMessage());
         e.printStackTrace();
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
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

         if (this.m_state != LaneState.Starting && this.Current == this.Next)
         {
            if (this.Manager.LanesView.WarningMessage != null)
            {
               this.Manager.LanesView.WarningMessage.dismiss();
               this.Manager.LanesView.WarningMessage = null;
               this.Manager.TableView.Clear();
            }
         }
               /**/
         if (this.m_state == LaneState.Opened && this.m_pLong != null)
         {
            this.CurrentVechicleImage = "";
         }
      }
      catch (Exception e)
      {
         //Log.e(this.getClass().getName(), e.getMessage());
         e.printStackTrace();
      }
   }

   //-----------------------------------------------------------------------------------------------------------------//
   String Formatdate(final String szFmt)
   {
      try
      {
         final String year = String.copyValueOf(szFmt.toCharArray(), 0, 4);
         final String month = String.copyValueOf(szFmt.toCharArray(), 4, 2);
         final String day = String.copyValueOf(szFmt.toCharArray(), 6, 2);
         final String hour = String.copyValueOf(szFmt.toCharArray(), 8, 2);
         final String minute = String.copyValueOf(szFmt.toCharArray(), 10, 2);
         final String seconds = String.copyValueOf(szFmt.toCharArray(), 12, 2);

         return (day + "/" + month + "/" + year + " " + hour + ":" + minute + ":" + seconds);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      return szFmt;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   void UpdateTableView()
   {
      try
      {
         if (this.Current == this.m_pLane.getId())
         {
            if (!Company.IsDebug && m_pLong != null)
            {
               final Vector<GetLongStatusResponse.DetailArray> pQueue = this.m_pLong.GetDetailArray();
               final Vector<List<String>>                      pItems = new Vector<List<String>>();
               final Vector<TableWidget.DetailsArray> pData = new Vector<> (  );
               //
               if (pQueue != null)
               {
                  for (GetLongStatusResponse.DetailArray p : pQueue)
                  {
                     List<String> itens = new ArrayList<String>();
                     {
                        itens.add ( this.Formatdate ( p.Moment ) );
                        itens.add ( m_pLane.getLaneName ( ) + "/" + this.m_pLong.getLaneName ( ) );
                        itens.add ( p.PanNumber );
                        itens.add ( p.TransactionId );
                        itens.add ( p.PaymentMeans );
                        pItems.add ( itens );
                     }
                     /* create a copy of current data like backup */
                     TableWidget.DetailsArray back = new TableWidget.DetailsArray();
                     {
                        back.Moment = this.Formatdate ( p.Moment );
                        back.AliasLaneName = this.m_pLane.getLaneName ();
                        back.OriginalLaneName = this.m_pLong.getLaneName ();
                        back.PanNumber = p.PanNumber;
                        back.PaymentMeans = p.PaymentMeans.trim ();
                        back.TransactionId = p.TransactionId;
                        back.VehicleClass = p.VehicleClass;
                        pData.add(back);
                     }
                  }
                  /* internal backup */
                  this.Manager.TableView.SetDataBackup ( pData );
                  /* put on table view */
                  this.Manager.TableView.UpdateRows(pItems);
               }
               return;
            }
            /* only debug mode */
            if (Company.IsDebug)
            {
               final SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.FRANCE);
               final Vector<List<String>> pItems = new Vector<List<String>>();
               final Vector<TableWidget.DetailsArray> pBack = new Vector<> (  );

               for (int i = 0; i < 0xA; i++)
               {
                  List<String> itens = new ArrayList<String>();
                  {
                     itens.add ( fmt.format ( new Date ( ) ) );
                     itens.add ( this.m_pLane.getLaneName ( ) + "P" );
                     itens.add ( String.format ( Locale.FRANCE, "%010d", i ) );
                     itens.add ( "TRANSACTION-" + Integer.toString ( i ) );
                     itens.add ( "CA" );
                     pItems.add ( itens );
                  }
                  /****/
                  TableWidget.DetailsArray back = new TableWidget.DetailsArray ();
                  {
                     back.Moment = fmt.format ( new Date() );
                     back.AliasLaneName = this.m_pLane.getLaneName ();
                     back.VehicleClass = Integer.toString ( i );
                     back.TransactionId = "TRANSACTION-" + Integer.toString ( i );
                     back.PaymentMeans = "NO";
                     back.PaymentType = "TG";
                     back.PanNumber = "IUO0098";
                     pBack.add ( back );
                  }
               }
               /****/
               this.Manager.TableView.SetDataBackup ( pBack );
               this.Manager.TableView.UpdateRows(pItems);
               /* turn off debug mode */
               Company.IsDebug = false;
            }
         }
      }
      catch (final Exception e)
      {
         //Log.e(getClass().getName(), e.getMessage());
         e.printStackTrace();
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   void UpdateStateOperator()
   {
      try
      {
         if(this.Current == this.m_pLane.getId())
         {
            final boolean bSearch = (m_state != LaneState.NoSync && m_state != LaneState.Starting);
            final boolean bOpened = (m_state == LaneState.Closed);
            final boolean bClosed = (m_state == LaneState.Opened && m_bOperator);
            final boolean bResponsible = (m_state == LaneState.Opened && !m_bOperator);
            final boolean bMoney = (m_state != LaneState.NoSync && m_state != LaneState.Starting && m_bOperator);
            final boolean bTag = (m_state != LaneState.NoSync && m_state != LaneState.Starting && m_bOperator);
            final boolean bSimulation = (bClosed && m_nTotalVehicles > 0 && m_pView.Caption.isChecked());
            final boolean bAllowOpened = (m_pView.Caption.isChecked() && m_nLaneMode == AVI_LANE);

            Manager.LanesActions.Search.setEnabled(bSearch);
            Manager.LanesActions.OpenLane.setEnabled(bOpened && bAllowOpened);
            Manager.LanesActions.CloseLane.setEnabled(bClosed && bAllowOpened);
            Manager.LanesActions.ChangeOperator.setEnabled(bResponsible);
            Manager.LanesActions.PaymentMoney.setEnabled(bMoney);
            Manager.LanesActions.PaymentTag.setEnabled(bTag);
            Manager.LanesActions.Violation.setEnabled(bTag);
            Manager.LanesActions.Free.setEnabled(bTag);
            Manager.LanesActions.Simulation.setEnabled(bSimulation);

            m_pView.Caption.Data.setBackgroundColor(LaneState.getColor(m_state));
            m_pView.LaneHandle.setLaneState(m_state);
            m_pView.Semaphore.setEnabled(bClosed && bAllowOpened);
            m_pView.Barrer.setEnabled(bClosed && bAllowOpened);
            m_pView.Traffic.setEnabled(bClosed && bAllowOpened);
         }
      }
      catch (final Exception e)
      {
         //Log.e(this.getClass().getName(), e.getMessage());
         e.printStackTrace();
      }
   }
}
//-----------------------------------------------------------------------------------------------------------------//
//060115SAU201703311005227722
//060102PAU20170331101228