/*

   Sistema de Gestão de Pistas

   (C) 2016 TecSidel

   Created by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Component;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Vector;

import br.com.tk.mcs.Generic.ConfigDisplayMetrics;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 12/01/17.
 */

public class TableWidget extends LinearVertical
{
   public final static int TEXTVIEW = 0x0010;
   public final static int RADIOBOX = 0x0020;
   public final static int CHECKBOX = 0x0040;
   public final static int BITMAP = 0x0080;
   public final static int BUTTON = 0x0100;
   public final static int BITMAPBUTTON = 0x0200;
   public final static int EDITTEXT = 0x0400;
   public final static int DEFAULT = -1;
   protected final static int DEFAULT_SIZE = 0x00be;
   protected TableHeader ItemsHeader = null;
   protected TableList LinesItems = null;
   protected int Selected = -1;
   protected View.OnClickListener m_OnClick = null;
   protected int m_nID = 0;
   protected Activity m_pWnd = (Activity)getContext();
   /***/
   public static class DetailsArray
   {
      public String TransactionId = "";
      public String Moment = "";
      public String PaymentType = "";
      public String PaymentMeans = "";
      public String PanNumber = "";
      public String VehicleClass = "";
      public String AliasLaneName = "";
      public String OriginalLaneName = "";
      public DetailsArray()
      {}
   };
   protected Vector<DetailsArray> m_backup_data = null;

   /*
   * define width and height on fields
   * */
   public class FieldData
   {
      public int Width;
      public int Height;
      public String Name;
      public ViewGroup Item;
      public int ID;
      public int Kind;

      public FieldData()
      {
         this.Width = DEFAULT_SIZE;
         this.Height = WRAP;
         this.Name = "";
         this.Item = null;
         this.ID = -1;
         this.Kind = TEXTVIEW;
      }
      public FieldData(String szName, TextViewEx pItem)
      {
         this.Name = szName;
         this.Item = pItem;
         this.Width = DEFAULT_SIZE;
      }
      public FieldData(int nNameID, int nWidth)
      {
         this.Item = null;
         this.Width = nWidth == DEFAULT? DEFAULT_SIZE: nWidth;
         this.Name = getContext().getString(nNameID);
      }
      public FieldData(int nNameID, int nWidth, int nID, int nKind)
      {
         this.Item = null;
         this.Width = (nWidth == DEFAULT? DEFAULT_SIZE: nWidth);
         this.Name = getContext().getString(nNameID);
         this.ID = nID;
         this.Kind = (nKind == DEFAULT? TEXTVIEW: nKind);
      }
      public FieldData(String szName, int nWidth, int nID, int nKind)
      {
         this.Item = null;
         this.Width = (nWidth == DEFAULT? DEFAULT_SIZE: nWidth);
         this.Name = szName;
         this.ID = nID;
         this.Kind = (nKind == DEFAULT? TEXTVIEW: nKind);
      }
   }
   /*
   * class resposible by create header caption of table
   * */
   public class TableHeader extends LinearHorizontal
   {
      public Vector<FieldData> Items = new Vector<>();
      //-----------------------------------------------------------------------------------------------------------------//
      public TableHeader(Context pWnd, TableWidget pOwner)
      {
         super(pWnd, null, ConfigDisplayMetrics.CaptionStyle);
         this.Params = this.Build(MATCH, WRAP);
         this.SetMargins(new Rect(1, 0, 2, 8));
         this.SetBorder(true);
         this.setBackgroundResource(R.drawable.window_caption_bar);
         pOwner.addView(this, this.Params);
      }
      //-----------------------------------------------------------------------------------------------------------------//
      public void Create(final Vector<FieldData> pQueue)
      {
         try
         {
            for (FieldData pField : pQueue)
            {
               TextViewEx p = new TextViewEx(getContext(), pField.Name, TextViewEx.CAPTION, TextViewEx.DEFCOLOR, true);
               {
                  p.SetPadding(new Rect(2, 2, 0, 2));
                  p.Data.setGravity(Gravity.LEFT);
                  p.Data.setSingleLine();
                  p.setTag(pField.Name);
                  p.Params.gravity = Gravity.LEFT;
                  pField.Item = p;
                  /* put in back internal stack*/
                  Items.add(pField);
                  p.Params.width = ToDP(pField.Width);
                  p.setLayoutParams(p.Params);
                  addView(p);
               }
            }
            this.Invalidate(true);
         }
         catch (Exception e)
         {
            Log.e(this.getClass().getName(), e.getMessage());
         }
      }
   }
   /*
   * class responsible by display items of table
   * */
   public class TableItem extends LinearHorizontal
   {
      protected Vector<FieldData> m_data = new Vector<>();
      protected int nH = ConfigDisplayMetrics.GetAttrSizeH ( this.getContext (), android.R.attr.actionBarSize );
      //-----------------------------------------------------------------------------------------------------------------//
      public TableItem(Context pWnd)
      {
         super(pWnd);
         this.Params = this.Build(ItemsHeader.Params.width - 0x20, nH - 0xA);
         this.SetMargins(new Rect(0, 2, 0, 2));
      }
      //-----------------------------------------------------------------------------------------------------------------//
      public TableItem(Context pWnd, Vector<FieldData> pItems)
      {
         super(pWnd);
         this.Params = this.Build(ItemsHeader.Params.width - 0x20, nH - 0xA);
         this.Create(pItems);
         this.SetMargins(new Rect(0, 2, 0, 2));
      }
      //-----------------------------------------------------------------------------------------------------------------//
      public void Create(Vector<FieldData> pItems)
      {
         try
         {
            Point area = new Point(ConfigDisplayMetrics.ButtonImageIcon.x - 8, ConfigDisplayMetrics.ButtonImageIcon.y - 8);
            this.setLayoutParams(this.Params);
            this.m_data.clear();

            for (FieldData f : pItems)
            {
               Context context = this.getContext();

               switch (f.Kind)
               {
                  case TEXTVIEW:
                  {
                     TextViewEx p = new TextViewEx(context, f.Name, TextViewEx.DEFAULT, TextViewEx.TRACOLOR, false);

                     p.Params.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
                     p.SetPadding(new Rect(2, 2, 0, 2));
                     p.Data.setSingleLine(true);
                     p.setTag(f.Name);
                     f.Item = p;

                     p.Params.width = ToDP(f.Width);
                     p.Params.height = ToDP(area.y);
                     p.setLayoutParams(p.Params);
                     p.Data.setGravity(p.Params.gravity);

                     this.addView(f.Item);
                     this.m_data.add(f);
                     break;
                  }
                  case BITMAP:
                  {
                     ImageViewEx p = new ImageViewEx(context, area, R.drawable.ic_tbl_photo_image, false, null, null);

                     p.Params.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
                     p.SetPadding(new Rect(2, 2, 0, 2));
                     p.setTag(f.Name);
                     f.Item = p;

                     p.Params.width = ToDP(f.Width);
                     p.setLayoutParams(p.Params);
                     this.addView(f.Item);
                     this.m_data.add(f);
                     break;
                  }
               }
            }

            this.Invalidate(true);
            pItems.clear();
         }
         catch (Exception e)
         {
            Log.e(this.getClass().getName(), e.getMessage());
         }
      }
      //-----------------------------------------------------------------------------------------------------------------//
      public boolean Locate(Object value)
      {
         for (FieldData f : this.m_data)
         {
            if(f.Name.indexOf((String)value) != 0)
            {
               return true;
            }
         }
         return false;
      }
      //-----------------------------------------------------------------------------------------------------------------//
      public Vector<FieldData> GetItems()
      {
         return this.m_data;
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   /* class to display data on screen */
   class TableList extends ScrollViewEx implements View.OnClickListener
   {
      protected Vector<TableItem> m_lines = new Vector<>();
      //-----------------------------------------------------------------------------------------------------------------//
      public TableList(Context pWnd, TableWidget pOwner)
      {
         super(pWnd, false, Color.WHITE, null, null);
         this.Contaneir.SetPadding(new Rect(8, 0, 0, 8));
         this.Params = this.Build(WRAP, WRAP);
         pOwner.addView(this, this.Params);
      }
      //-----------------------------------------------------------------------------------------------------------------//
      public void Create(final Vector<TableItem> pLines)
      {
         try
         {
            m_nID = pLines.size() - 1;
            this.m_lines.clear();
            this.Contaneir.removeAllViews();

            for (TableItem pRow : pLines)
            {
               pRow.setId(m_nID);
               pRow.SetBorder(true);
               pRow.setClickable(true);
               pRow.setOnClickListener(this);

               this.Contaneir.addView(pRow, 0, pRow.Params);
               this.m_lines.add(pRow);
               m_nID--;
            }

            this.Invalidate(true);
         }
         catch (Exception e)
         {
            Log.e(this.getClass().getName(), e.getMessage());
         }
      }
      //-----------------------------------------------------------------------------------------------------------------//
      public boolean Locate(String value)
      {
         for(TableItem item: this.m_lines)
         {
            if(item.Locate(value))
            {
               return true;
            }
         }
         return false;
      }
      //-----------------------------------------------------------------------------------------------------------------//
      @Override
      public void onClick(View v)
      {
         int nKey = v.getId();

         if (Selected != nKey)
         {
            TableItem pView = (TableItem) Contaneir.getChildAt(nKey);
            {
               pView.SetBorder(Color.BLUE);
            }

            if (Selected != -1)
            {
               TableItem p = (TableItem) Contaneir.getChildAt(Selected);
               {
                  p.SetBorder(Color.WHITE);
               }
            }

            Selected = nKey;
         }
         else
         {
            Selected = nKey;
         }

         if (m_OnClick != null)
         {
            m_OnClick.onClick(v);

            this.postDelayed(new Runnable()
            {
               @Override
               public void run()
               {
                  TableItem p = (TableItem) Contaneir.getChildAt(Selected);
                  {
                     p.SetBorder(Color.WHITE);
                  }
               }
            }, 0x400);
         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   //
   //
   //
   //
   // Task to process items on table
   //-----------------------------------------------------------------------------------------------------------------//
   class TaskUpdateRows implements Runnable
   {
      private final Vector<List<String>> m_queue;
      //-----------------------------------------------------------------------------------------------------------------//
      public TaskUpdateRows(final Vector<List<String>> pQueue)
      {
         this.m_queue = pQueue;
      }
      //-----------------------------------------------------------------------------------------------------------------//
      @Override
      public void run()
      {
         try
         {
            final Vector<FieldData> pRow = new Vector<>();
            final Vector<TableItem> pItems = new Vector<>();
            //
            for (List<String> items : this.m_queue)
            {
               int i = 0;

               for (String str : items)
               {
                  FieldData f = (FieldData)ItemsHeader.Items.get(i++);
                  pRow.add(new FieldData(str, f.Width, f.ID, f.Kind));
               }

               pItems.add(new TableItem(getContext(), pRow));
            }

            if (pItems.size() > 0)
            {
               LinesItems.Create(pItems);
            }
         }
         catch (Exception e)
         {
            Log.e(this.getClass().getName(), e.getMessage());
         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//

   //-----------------------------------------------------------------------------------------------------------------//
   /**/
   public TableWidget(Context pWnd)
   {
      super(pWnd);
      this.Params.height = 0x100;
      this.SetMargins(new Rect(8, 0, 8, 8));
      this.ItemsHeader = new TableHeader(this.getContext(), this);
      this.LinesItems = new TableList(this.getContext(), this);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void Create(View.OnClickListener pClick)
   {
      this.m_OnClick = pClick;
      /* create dimension */
      Point point = new Point(this.LinesItems.Contaneir.Params.width - 24, WRAP);
      this.LinesItems.Contaneir.Params.SetDimension(point);
      this.SetBorder(true);
      this.Invalidate(true);
      this.LinesItems.Params.height = this.LinesItems.Contaneir.Params.height - 0x10;
      this.LinesItems.Data.setLayoutParams(this.LinesItems.Params);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void CreateHeader(Vector<FieldData> pHeader)
   {
      this.ItemsHeader.Create(pHeader);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public Vector<FieldData> GetHeaderItems()
   {
      return this.ItemsHeader.Items;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public Vector<FieldData> GetRowItem(int nRow)
   {
      return this.LinesItems.m_lines.get(nRow).m_data;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public synchronized void UpdateRows(final Vector<List<String>> pQueue)
   {
     m_pWnd.runOnUiThread(new TaskUpdateRows(pQueue));
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public int GetID()
   {
      return this.m_nID;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void Update()
   {
      this.setMinimumHeight(this.Params.height - this.ItemsHeader.Params.height);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetScrollViewArea()
   {
      this.LinesItems.Params.height = this.Params.height + 8;
      this.LinesItems.setLayoutParams(this.LinesItems.Params);
      this.Invalidate(this.LinesItems, true);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void Clear()
   {
      this.LinesItems.m_lines.clear();
      this.LinesItems.Contaneir.removeAllViews();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetDataBackup(final Vector<TableWidget.DetailsArray> pData)
   {
      this.m_backup_data = pData;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public TableWidget.DetailsArray GetDataBackup(final String szTransactionId)
   {
      for(int i = 0; i < this.m_backup_data.size (); i++)
      {
         DetailsArray p = this.m_backup_data.get(i);

         if(p.TransactionId.trim().equals ( szTransactionId.trim () ))
         {
            return p;
         }
      }

      return null;
   }
   //-----------------------------------------------------------------------------------------------------------------//
}
