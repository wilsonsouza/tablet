/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Vector;

import br.com.tk.mcs.Activity.ManageLanes;
import br.com.tk.mcs.Component.TableWidget;
import br.com.tk.mcs.Lane.Lane;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 3/30/17.
 */

class BuilderTableView extends TableWidget
{
   protected Vector<FieldData> Items = new Vector<>();
   protected BuilderManager Manager = null;
   protected int nRound = 0;
   //-----------------------------------------------------------------------------------------------------------------//
   class OnTableViewClickListener implements View.OnClickListener
   {
      @Override
      public void onClick(View view)
      {
         final Lane pLane = Manager.VerifyLanes.m_pLane;
         final TableItem pItem = (TableItem)view;
         final String szTransactionId = pItem.GetItems ().get(3).Name.trim ();
         final TableWidget.DetailsArray pDetails = Manager.TableView.GetDataBackup ( szTransactionId );
         new DialogTableViewItem(getContext(), pItem, pLane, Manager.Printer, pDetails );
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public BuilderTableView(ManageLanes pWnd, BuilderMainWindow pOwner)
   {
      super(pWnd);
      int n = 0x00be;
      int nphoto = 0x96;

      this.Manager = pWnd.Manager;
      pOwner.addView(this, this.Params);
      //
      if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && this.Params.Display.densityDpi >= DisplayMetrics.DENSITY_HIGH)
      {
         nRound = 0x50 + n;
         nphoto += 0x50;
      }
      else
      {
         nRound = n + 0x10;
      }

      this.Items.add(new FieldData(R.string.tbl_datetime, nRound, R.id.tbl_datetime, DEFAULT));
      this.Items.add(new FieldData(R.string.tbl_via, nRound, R.id.tbl_via, DEFAULT));
      this.Items.add(new FieldData(R.string.tbl_tag, nRound, R.id.tbl_tag, DEFAULT));
      this.Items.add(new FieldData(R.string.tbl_photo, nphoto, R.id.tbl_frontphoto, BITMAP));
      this.Items.add(new FieldData(R.string.tbl_down, nRound, R.id.tbl_down, DEFAULT));
         /* create header */
      super.CreateHeader(this.Items);
         /* display content */
      super.Create(new OnTableViewClickListener());
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetManagerPointer(BuilderManager pManager)
   {
      this.Manager = pManager;
   }
   //-----------------------------------------------------------------------------------------------------------------//
}
