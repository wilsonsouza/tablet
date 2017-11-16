/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import java.util.ArrayList;
import java.util.Vector;

import br.com.tk.mcs.Activity.ManageLanes;
import br.com.tk.mcs.Component.DisplayMessage;
import br.com.tk.mcs.Component.HorizontalScrollViewEx;
import br.com.tk.mcs.Component.LaneWidget;
import br.com.tk.mcs.Lane.Lane;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 3/30/17.
 */

public class BuilderLanesView extends HorizontalScrollViewEx implements View.OnClickListener
{
   public Vector<Lane> Items = new Vector<>();
   protected int CurrentLane = -1;
   protected DisplayMessage WarningMessage = null;
   protected BuilderManager Manager = null;
   private String m_caption = getContext().getString(R.string.ids_wait);
   //-----------------------------------------------------------------------------------------------------------------//
   public BuilderLanesView(ManageLanes pWnd, BuilderMainWindow pOwner)
   {
      super(pWnd, false, Color.BLACK, new Rect(0, 4, 0, 4), null);
      this.Params.gravity = Gravity.CENTER|Gravity.TOP;
      this.Manager = pWnd.Manager;
      pOwner.addView(this, this.Params);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public BuilderLanesView Create(final ArrayList<Lane> pLanes)
   {
      int nID = 0;
      /* process all lanes */
      for(Lane p: pLanes)
      {
         LaneWidget pView = new LaneWidget(this.getContext());
         {
            pView.Create(p, nID, this);
            pView.setId(nID);
            this.Contaneir.addView(pView);
            this.Items.add(p);
            nID++;
         }
      }
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetManagerPointer(BuilderManager pManager)
   {
      this.Manager = pManager;
   }
   //-----------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(final View pView)
   {
      try
      {
         String bitmap = Manager.VerifyLanes.CurrentVechicleImage;
         LaneWidget pWnd = (LaneWidget) Contaneir.getChildAt(pView.getId());
         Lane pLane = pWnd.LaneHandle;
         String message = String.format(getContext().getString(R.string.ids_syncronize), pLane.getLaneName());

         if (pView == pWnd.Caption.Data)
         {
            if (CurrentLane != pView.getId())
            {
               if (CurrentLane != -1)
               {
                  final LaneWidget p = (LaneWidget) Contaneir.findViewById(CurrentLane);

                  if (p.Caption.isChecked())
                  {
                     p.Caption.setChecked(false);
                  }
                  CurrentLane = pView.getId();
               }
               else
               {
                  CurrentLane = pView.getId();
               }
               /* enable select lane */
               pWnd.Caption.setChecked(true);
               Manager.VerifyLanes.Next = pWnd.Caption.getId();
               /* display message */
               WarningMessage = new DisplayMessage(getContext(), m_caption, message);
               WarningMessage.show();
            }
         }
         else if (pView == pWnd.Vehicle.Data)
         {
            new DialogReleaseVehicle(this.getContext(), pLane, bitmap, Manager.Printer);
         }
         else if (pView == pWnd.Barrer.Data)
         {
            new DialogChangeGateState(this.getContext(), pLane);
         }
         else if (pView == pWnd.Semaphore.Data)
         {
            new DialogChangeMarquiseState(this.getContext(), pLane);
         }
      }
      catch (Exception e)
      {
         Log.e(this.getClass().getName(), e.getMessage());
      }
   }
}
