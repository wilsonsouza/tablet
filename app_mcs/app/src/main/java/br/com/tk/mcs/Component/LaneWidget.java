/*

   Sistema de Gestão de Pistas

   (C) 2016 TecSidel

   Created by wilson.souza (WR DevInfo)

   Description: vcs revision 925
 */
package br.com.tk.mcs.Component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import java.util.Locale;

import br.com.tk.mcs.Generic.Company;
import br.com.tk.mcs.Generic.ConfigDisplayMetrics;
import br.com.tk.mcs.Lane.Lane;
import br.com.tk.mcs.Lane.State.BarreraState;
import br.com.tk.mcs.Lane.State.LaneState;
import br.com.tk.mcs.Lane.State.TrafficLightState;
import br.com.tk.mcs.R;
import br.com.tk.mcs.Remote.Response.GetLongStatusResponse;

/**
 * Created by wilsonsouza on 21/12/16.
 */

public class LaneWidget extends LinearVertical
{
   public RadioButtonEx Caption = null;
   public ImageViewEx Semaphore = null;
   public ImageViewEx Vehicle = null;
   public ImageViewEx Traffic = null;
   public ImageViewEx Barrer = null;
   public Lane LaneHandle = null;
   protected Point m_imagesize = ConfigDisplayMetrics.LaneIcon;
   protected Rect m_margins = new Rect(8, 8, 8, 8);
   protected long m_time = System.currentTimeMillis();
   //-----------------------------------------------------------------------------------------------------------------//
   public LaneWidget(Context pWnd)
   {
      super(pWnd);
      /* set main layout parameters */
      //this.m_params.width = 110;
      this.SetMargins(new Rect(8, 8, 8, 8));

      //this.SetPadding(this.m_imagemargins);
      /* set direction of layout to vertical */
      //this.setGravity(Gravity.CENTER_VERTICAL|Gravity.TOP);
      /* create components of lane box */
      this.Vehicle = new ImageViewEx(this.getContext(), this.m_imagesize, R.drawable.area_blank, false, null, m_margins);
      this.Vehicle.SetColor(Color.WHITE);
      this.Vehicle.setGravity(Gravity.CENTER);
      this.Barrer = new ImageViewEx(this.getContext(), this.m_imagesize, R.drawable.ln_barrer_blank_hd, false, null, m_margins);
      this.Barrer.SetColor(Color.WHITE);
      this.Barrer.setGravity(Gravity.CENTER);
      this.Semaphore = new ImageViewEx(this.getContext(), this.m_imagesize,R.drawable.traffic_blank, false, null, m_margins);
      this.Semaphore.SetColor(Color.WHITE);
      this.Semaphore.setGravity(Gravity.CENTER);
      this.Traffic = new ImageViewEx(this.getContext(), this.m_imagesize, R.drawable.light_blank, false, null, m_margins);
      this.Traffic.SetColor(Color.WHITE);
      this.Traffic.setGravity(Gravity.CENTER);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void Create(final Lane pLane, final int nID, View.OnClickListener pClick)
   {
      /* put caption */
      LinearHorizontal pCaption = new LinearHorizontal(this.getContext());
      {
         this.Caption = new RadioButtonEx(this.getContext(), pLane.getLaneName(), true, false, RadioButtonEx.CAPTION);
         this.Caption.setId(nID);
         this.Caption.SetPadding(new Rect(1, 1, 1, 1));
         this.Caption.Params.gravity = Gravity.TOP;
         this.Caption.setBackgroundColor(Color.GRAY);
         pCaption.SetBorder(Color.GRAY);
         pCaption.addView(this.Caption, this.Caption.Params);
      }
      /* put semaphore */
      LinearHorizontal pHeader = new LinearHorizontal(this.getContext());
      {
         this.Semaphore.Data.setId(nID);
         pHeader.addView(this.Semaphore, this.Semaphore.Params);

         /* put vehicle */
         //this.Vehicle.Params.gravity = Gravity.RIGHT | Gravity.TOP;
         this.Vehicle.Data.setId(nID);
         pHeader.addView(this.Vehicle, this.Vehicle.Params);
      }
      /* put traffic */
      LinearHorizontal pFoot = new LinearHorizontal(this.getContext());
      {
         //this.Traffic.Params.gravity = Gravity.LEFT | Gravity.TOP;
         this.Traffic.Data.setId(nID);
         pFoot.addView(this.Traffic, this.Traffic.Params);

         /* put barrier */
         //this.Barrer.Params.gravity = Gravity.RIGHT | Gravity.TOP;
         this.Barrer.Data.setId(nID);
         pFoot.addView(this.Barrer, this.Barrer.Params);
      }

      /* set lane */
      this.LaneHandle = pLane;
      this.setId(pLane.getId());

      /* put all images in contaner*/
      this.addView(pCaption, pCaption.Params);
      this.addView(pHeader, pHeader.Params);
      this.addView(pFoot, pFoot.Params);

      /* disable all images */
      this.Semaphore.setEnabled(false);
      this.Vehicle.setEnabled(false);
      this.Barrer.setEnabled(false);
      this.Traffic.setEnabled(false);

      /* set listener */
      this.Caption.setOnClickListener(pClick);
      this.Vehicle.Data.setOnClickListener(pClick);
      this.Barrer.Data.setOnClickListener(pClick);
      this.Semaphore.Data.setOnClickListener(pClick);
      this.Traffic.Data.setOnClickListener(pClick);

      /* recalc */
      this.Invalidate(true);
      this.Caption.Params.width = this.getWidth();

      /* draw border */
      this.SetBorder(true);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void EnableSemaphore(LaneState state, boolean bOperator, final int nSemaphoreState)
   {
      final int nID = this.Semaphore.getId();

      this.Semaphore.setEnabled(state == LaneState.Opened && bOperator);

      switch (nSemaphoreState)
      {
         case -1:
            if(nID != R.drawable.traffic_black)
            {
               this.Semaphore.SetImage(R.drawable.traffic_black);
            }
            return;
         case 1:
            if(nID != R.drawable.traffic_green)
            {
               this.Semaphore.SetImage(R.drawable.traffic_green);
            }
            return;
         case 2:
            if(nID != R.drawable.traffic_red)
            {
               this.Semaphore.SetImage(R.drawable.traffic_red);
            }
            return;
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetBarrer(BarreraState bs)
   {
      if (bs == BarreraState.SensorON)
      {
         this.Barrer.SetImage(R.drawable.ln_barrer_up_hd);
      }
      else if (bs == BarreraState.SensorOFF)
      {
         this.Barrer.SetImage(R.drawable.ln_barrer_down_hd);
      }
      else if (bs == BarreraState.SensorUnknown)
      {
         this.Barrer.SetImage(R.drawable.ln_barrer_blank_hd);
      }
      else
      {
         this.Barrer.SetImage(R.drawable.ln_barrer_blank_hd);
      }

      this.LaneHandle.setBarreraState(bs);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetLightEntry(TrafficLightState ts)
   {
      if(ts == TrafficLightState.LightGreen)
      {
         this.Traffic.SetImage(R.drawable.light_green);
      }
      else if(ts == TrafficLightState.LightRed)
      {
         this.Traffic.SetImage(R.drawable.light_red);
      }
      else if(ts == TrafficLightState.LightOff)
      {
         this.Traffic.SetImage(R.drawable.light_black);
      }
      else
      {
         this.Traffic.SetImage(R.drawable.light_blank);
      }

      this.LaneHandle.setMarquiseState(ts);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetLightExit(TrafficLightState ts)
   {
      if(ts == TrafficLightState.LightGreen)
      {
         this.Traffic.SetImage(R.drawable.light_green);
      }
      else if(ts == TrafficLightState.LightRed)
      {
         this.Traffic.SetImage(R.drawable.light_red);
      }
      else if(ts == TrafficLightState.LightOff)
      {
         this.Traffic.SetImage(R.drawable.light_black);
      }
      else
      {
         this.Traffic.SetImage(R.drawable.light_blank);
      }

      this.LaneHandle.setPassageState(ts);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetVehicle(LaneState state, int nVehicles, int nStopped, boolean bOperator)
   {
      /* verify if have vehicle stopped on the lane */
      boolean bSuccess = (state != this.LaneHandle.getLaneState() ||
         nVehicles != this.LaneHandle.getTotalVehicles() ||
         nStopped != this.LaneHandle.getVehicleStopped());

      if(Company.IsDebug)
      {
         String fmt = String.format(Locale.FRANCE, "Name %s, State %s, Vehicles %d, Stopped %d BVehicle %s",
            this.LaneHandle.getLaneName(), state.toString(), nVehicles, nStopped, Boolean.toString(this.Vehicle.isEnabled()));
         String fmt1 = String.format(Locale.FRANCE, "Name %s, State %s, Vehicles %d, Stopped %d Success %s",
            this.LaneHandle.getLaneName(), this.LaneHandle.getLaneState().toString(), this.LaneHandle.getTotalVehicles(),
            this.LaneHandle.getVehicleStopped(), Boolean.toString(bSuccess));
         Log.e(this.getClass().getName(), fmt);
         Log.e(this.getClass().getName(), fmt1);
      }

      if(bSuccess)
      {
         /* disable or enable vehicle image */
         boolean bEnabled = (state == LaneState.Opened && bOperator && nStopped == 1);
         this.Vehicle.setEnabled(bEnabled);
         //
         switch (nVehicles)
         {
            case 1:
               this.Vehicle.SetImage(R.drawable.ln_vehicle1_hd);
               break;
            case 2:
               this.Vehicle.SetImage(R.drawable.ln_vehicle2_hd);
               break;
            case 3:
               this.Vehicle.SetImage(R.drawable.ln_vehicle3_hd);
               break;
            case 4:
               this.Vehicle.SetImage(R.drawable.ln_vehicle4_hd);
               break;
            default:
               this.Vehicle.SetImage(R.drawable.area_blank);
               break;
         }

         /* has vehicle stopped on the lane */
         if(nStopped > 0)
         {
            Long value = (System.currentTimeMillis() - this.m_time) / 1000;
            int nClr = Color.WHITE;

            if (value > 3)
            {
               nClr = Color.RED;
               this.m_time = System.currentTimeMillis();
            }

            this.Vehicle.Data.setBackgroundColor(nClr);
         }

         this.LaneHandle.setTotalVehicles(nVehicles);
         this.LaneHandle.setVehicleStopped(nStopped);
         this.Vehicle.Data.setBackgroundColor(Color.WHITE);
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
}
