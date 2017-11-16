/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.tk.mcs.Activity.ManageLanes;
import br.com.tk.mcs.Component.CustomerToolBar;
import br.com.tk.mcs.Component.LaneWidget;
import br.com.tk.mcs.Component.MessageBox;
import br.com.tk.mcs.Lane.Lane;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 3/30/17.
 */

public class BuilderManager extends BuilderMainWindow implements View.OnClickListener
{
   public static final int NMAXTASKS = 0xA;
   public BuilderLanesView LanesView = null;
   public BuilderTableView TableView = null;
   public BuilderOperations LanesActions = null;
   public String UserNameLogged = null;
   public VerifyLaneState VerifyLanes = null;
   final public List<MonitoringLaneState> MonitoringLane = new ArrayList<>();
   public PrinterManager Printer = null;
   public CustomerToolBar ToolBar = null;
   public ExecutorService Runnings = null;
   //-----------------------------------------------------------------------------------------------------------------//
   public BuilderManager(ManageLanes pWnd, final ArrayList<Lane> pLanes, final String szUserName) throws Exception
   {
      super(pWnd);
      this.UserNameLogged = szUserName;
      this.LanesView = new BuilderLanesView(pWnd, this);

      this.LanesView.Create(pLanes);
      this.TableView = new BuilderTableView(pWnd, this);
      this.LanesActions = new BuilderOperations(pWnd, this);
      this.VerifyLanes = new VerifyLaneState(this.UserNameLogged, pWnd, this);

      for(int i = 0; i < this.LanesView.Contaneir.getChildCount(); i++)
      {
         final LaneWidget pView = (LaneWidget) this.LanesView.Contaneir.getChildAt(i);
         final MonitoringLaneState p = new MonitoringLaneState(this.UserNameLogged, pView, pWnd, this);
         //
         MonitoringLane.add(p);
      }
      //
      int nCount = this.LanesView.Contaneir.getChildCount() + NMAXTASKS;
      this.Runnings = Executors.newFixedThreadPool(nCount);
      this.ToolBar = new CustomerToolBar(pWnd, R.string.app_name);
      this.Printer = new PrinterManager(pWnd, this.ToolBar);

      /* change caption */
      this.SetCompositeCaption(Utils.USER + this.UserNameLogged);
      /* refresh all components */
      this.TableView.Invalidate(true);
      this.LanesActions.Invalidate(true);

      /* check display height */
      this.UpdateControls();
      /* set pointer */
      this.LanesView.SetManagerPointer(this);
      this.LanesActions.SetManagerPointer(this);
      this.VerifyLanes.SetManagerPointer(this);
      //
      for(MonitoringLaneState p: this.MonitoringLane)
      {
         p.SetManagerPointer(this);
      }
      //
      this.TableView.SetManagerPointer(this);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public BuilderManager SetCompositeCaption(String szData) throws Exception
   {
      int[] paButtons = new int[]{R.drawable.printer_error, R.drawable.cctv_camera_icon, R.drawable.his_operator};

      this.ToolBar.SetIcons(paButtons, this);
      this.Invalidate(true);
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void UpdateControls() throws Exception
   {
      int nAction = this.LanesActions.getHeight() - 0x10;
      Parameters params = this.LanesActions.Build(MATCH, MATCH);

      if(params.height > 500 && params.height < 600)
      {
         this.TableView.Params.height -= nAction;
         this.TableView.setLayoutParams(this.TableView.Params);
      }
      else
      {
         int y = (params.height - (this.TableView.getBottom() + nAction));
         this.TableView.Params.height += (y - this.TableView.Params.GetActionBarHeight());
         this.TableView.setLayoutParams(this.TableView.Params);
      }
      //set scrollview
      this.TableView.SetScrollViewArea();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   protected void ShowCurrentSquare() throws Exception
   {
      String fmt = String.format("Pista corrente %s selecionada.", this.VerifyLanes.m_pLong.getLaneName());
      //new DialogCurrentSquare(this.getContext(), this);
      new MessageBox(getContext(), R.string.ids_warning, fmt, MessageBox.IDOK);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void Finalize() throws Exception
   {
      VerifyLanes.interrupt();
      //
      for (MonitoringLaneState p : this.MonitoringLane)
      {
         p.interrupt();
      }
      //
      Printer.interrupt();
      this.Runnings.shutdown();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void Start()
   {
      try
      {
         /* check company to active printer use */
         if (!Company.IsArteris)
         {
            this.Runnings.execute(Printer);
         }
         else
         {
            this.ToolBar.SetVisible(0, false);
         }

         for(MonitoringLaneState p: this.MonitoringLane)
         {
            //p.start();
            this.Runnings.execute(p);
         }
         //
         this.Runnings.execute(VerifyLanes);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(View v)
   {
      try
      {
         int nID = v.getId();

         switch (nID)
         {
            case R.drawable.printer_ok:
               //new MessageBox(this.getContext(), R.string.ids_warning, R.string.printer_online, MessageBox.IDOK);
               //break;
            case R.drawable.print_superhot:
               //new MessageBox(this.getContext(), R.string.ids_warning, R.string.printer_superhot, MessageBox.IDWARNING);
               //break;
            case R.drawable.printer_error:
               //new MessageBox(this.getContext(), R.string.ids_warning, R.string.printer_offline, MessageBox.IDWARNING);
               //break;
            case R.drawable.printer_nopaper:
               //new MessageBox(this.getContext(), R.string.ids_warning, R.string.printer_nopaper, MessageBox.IDWARNING);
               //break;
            case R.drawable.printer_without_batery:
               //new MessageBox(this.getContext(), R.string.ids_warning, R.string.printer_batery_weak, MessageBox.IDWARNING);
               new DialogPrinterManager ( this.getContext (), this, nID );
               break;
            case R.drawable.her_operator:
            case R.drawable.his_operator:
               new MessageBox(this.getContext(), R.string.current_operator, Utils.USER + this.UserNameLogged, MessageBox.IDOK);
               break;
            case R.drawable.cctv_camera_icon:
               this.ShowCurrentSquare();
               break;
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
}
