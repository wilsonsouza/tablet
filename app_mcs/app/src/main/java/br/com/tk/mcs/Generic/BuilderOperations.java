/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import android.view.Gravity;
import android.view.View;

import br.com.tk.mcs.Activity.ManageLanes;
import br.com.tk.mcs.Lane.Lane;
import br.com.tk.mcs.Lane.State.LaneState;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 3/30/17.
 */

class BuilderOperations extends BuilderLaneActions implements View.OnClickListener
{
   protected DialogChangeOperator m_pDlgChangeOp = null;
   protected BuilderManager Manager = null;
   //-----------------------------------------------------------------------------------------------------------------//
   public BuilderOperations(ManageLanes pWnd, BuilderManager pOwner)
   {
      super(pWnd);
      this.Params.gravity = Gravity.BOTTOM|Gravity.CENTER;
      pOwner.addView(this, this.Params);
      this.Manager = pOwner;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetManagerPointer(BuilderManager pManager)
   {
      this.Manager = pManager;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(View pView)
   {
      final Lane pLane = Manager.VerifyLanes.m_pLane;
      final LaneState State = Manager.VerifyLanes.m_state;

      if(pView == super.Search)
      {
         new DialogSearchTag(this.getContext(), Manager.LanesView.Items);
      }
      else if(pView == super.PaymentMoney)
      {
         new DialogPaymentMoney(this.getContext(), pLane, Manager.Printer);
      }
      else if(pView == super.PaymentTag)
      {
         new DialogPaymentTag(this.getContext(), pLane);
      }
      else if(pView == super.ChangeOperator)
      {
         this.m_pDlgChangeOp = new DialogChangeOperator(this.getContext(), pView, pLane, State);
         Manager.VerifyLanes.m_bChangeOperator = this.m_pDlgChangeOp.IsChangeOperator();
         this.m_pDlgChangeOp = null;
      }
      else if(pView == super.OpenLane)
      {
         new DialogOpenLane(this.getContext(), pView, pLane, Manager.UserNameLogged, State);
      }
      else if(pView == super.CloseLane)
      {
         new DialogCloseLane(this.getContext(), pView, pLane, State);
      }
      else if(pView == super.Violation)
      {
         new DialogViolation(this.getContext(), R.string.dialog_violation, pLane);
      }
      else if(pView == super.Free)
      {
         new DialogSearchFreeVehicle(this.getContext(), R.string.dialog_isento, pLane);
      }
      else if(pView == super.Simulation)
      {
         new DialogSimulatePassenger(this.getContext(), R.string.dialog_simulation, pLane);
      }
   }
}
