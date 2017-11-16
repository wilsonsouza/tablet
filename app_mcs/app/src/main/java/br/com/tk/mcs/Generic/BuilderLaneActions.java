/*

   Sistema de Gestão de Pistas

   (C) 2016 TecSidel

   Created by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import br.com.tk.mcs.Component.ButtonImage;
import br.com.tk.mcs.Component.HorizontalScrollViewEx;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 24/01/17.
 */

public class BuilderLaneActions extends HorizontalScrollViewEx implements View.OnClickListener
{
   public ButtonImage Search = null;
   public ButtonImage OpenLane = null;
   public ButtonImage CloseLane = null;
   public ButtonImage ChangeOperator = null;
   public ButtonImage PaymentMoney = null;
   public ButtonImage PaymentTag = null;
   public ButtonImage Violation = null;
   public ButtonImage Free = null;
   public ButtonImage Simulation = null;
   private int m_Mode = ButtonImage.UP;
   private Point m_offset = new Point(0x80, 0x80);
   //-----------------------------------------------------------------------------------------------------------------//
   public BuilderLaneActions(Context pWnd)
   {
      super(pWnd, false, Color.TRANSPARENT, null, new Rect(8, 8, 8, 8));
      //
      this.Search = new ButtonImage(this.getContext(), R.string.button_search, m_offset, R.drawable.ic_search_plate, false, m_Mode);
      this.OpenLane = new ButtonImage(this.getContext(), R.string.button_open, m_offset, R.drawable.ic_openlane, false, m_Mode);
      this.CloseLane = new ButtonImage(this.getContext(), R.string.button_close, m_offset, R.drawable.ic_closelane, false, m_Mode);
      this.ChangeOperator = new ButtonImage(this.getContext(), R.string.button_responsible, m_offset, R.drawable.ic_operator, false, m_Mode);
      this.PaymentMoney = new ButtonImage(this.getContext(), R.string.button_pay_money, m_offset, R.drawable.ic_paymoney, false, m_Mode);
      this.PaymentTag = new ButtonImage(this.getContext(), R.string.button_pay_tag, m_offset, R.drawable.ic_paytag, false, m_Mode);
      this.Violation = new ButtonImage(this.getContext(), R.string.button_violation, m_offset, R.drawable.violation, false, m_Mode);
      this.Free = new ButtonImage(this.getContext(), R.string.button_isento, m_offset, R.drawable.ic_free, false, m_Mode);
      this.Simulation = new ButtonImage(this.getContext(), R.string.button_passenger_simulation, m_offset, R.drawable.ic_passenger_simulation, false, m_Mode);

      this.Prepare(this.Search);
      this.Prepare(this.OpenLane);
      this.Prepare(this.CloseLane);
      /* remove button change operator resposanble */
      if(Company.IsArteris)
      {
         this.Prepare(this.ChangeOperator);
      }

      this.Prepare(this.PaymentMoney);
      this.Prepare(this.PaymentTag);

      if(Company.IsTamoios || Company.IsTecsidel || Company.IsCRO || Company.IsViaRio)
      {
         this.Prepare(this.Simulation);
      }

      if(Company.IsArteris)
      {
         this.Prepare(this.Violation);
         this.Prepare(this.Free);
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   void Prepare(ButtonImage pView)
   {
      try
      {
         pView.setOnClickListener(this);
         pView.SetMargins(new Rect(8, 0, 0, 8));
         this.Contaneir.addView(pView, pView.Params);
      }
      catch (Exception e)
      {
         Log.e(this.getClass().getName(), e.getMessage());
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(View v)
   {
      /* source code here */
   }
   //-----------------------------------------------------------------------------------------------------------------//
}
