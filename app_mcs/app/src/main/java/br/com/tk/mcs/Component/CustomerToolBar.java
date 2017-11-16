/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Component;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import br.com.tk.mcs.Generic.ConfigDisplayMetrics;

/**
 * Created by wilsonsouza on 17/02/17.
 */

public class CustomerToolBar extends LinearHorizontal
{
   public AppCompatActivity Instance = null;
   public ActionBar Data = null;
   public TextViewEx Caption = null;
   private LinearHorizontal m_Icons = null;
   //-----------------------------------------------------------------------------------------------------------------//
   class ChangeStateIcon implements Runnable
   {
      private int m_index = 0;
      private int m_id = 0;
      public ChangeStateIcon(final int nIndex, final int nIconId)
      {
         this.m_index = nIndex;
         this.m_id = nIconId;
      }
      @Override
      public void run()
      {
         ImageViewEx bmp = (ImageViewEx) m_Icons.getChildAt(this.m_index);

         if (bmp.getId() != this.m_id)
         {
            bmp.SetImage(this.m_id);
         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public CustomerToolBar(AppCompatActivity pWnd, int nCaption)
   {
      super(pWnd);
      this.Instance = pWnd;
      this.Data = pWnd.getSupportActionBar();
      this.Data.setDisplayShowCustomEnabled(true);
      this.Data.setDisplayShowTitleEnabled(false);
      this.Params = this.Build(MATCH, this.Params.GetActionBarHeight());
      this.Params.width -= 0x80;

      int[] anColors = new int[]{TextViewEx.CUSTOM, Color.WHITE};
      this.Caption = new TextViewEx(pWnd, nCaption, TextViewEx.CAPTION, anColors, false);
      this.Caption.Params.gravity = (Gravity.LEFT|Gravity.CENTER_VERTICAL);
      this.addView(this.Caption, this.Caption.Params);
      this.Caption.Invalidate(true);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetIcons(int[] icons, View.OnClickListener pOnClick)
   {
      Point area = ConfigDisplayMetrics.ToolbarIcon;
      LinearHorizontal pIcons = new LinearHorizontal(this.getContext());
      {
         pIcons.Params = this.Build(icons.length * area.x + 8, this.Params.GetActionBarHeight());
         pIcons.Params.gravity = (Gravity.RIGHT|Gravity.CENTER_VERTICAL);

         for (int icon : icons)
         {
            ImageViewEx bmp = new ImageViewEx(this.getContext(), area, icon, false, null, null);
            {
               bmp.setGravity(Gravity.CENTER);
               bmp.SetMargins(new Rect(8, 8, 8, 8));
               pIcons.addView(bmp, bmp.Params);
               bmp.Invalidate(true);
               bmp.setOnClickListener(pOnClick);
            }
         }

         this.addView(pIcons, pIcons.Params);
         this.Data.setCustomView(this);
         pIcons.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
         this.Invalidate(true);
         pIcons.Invalidate(true);
         pIcons.Params.width = this.CalculeWidth(pIcons);
         pIcons.setLayoutParams(pIcons.Params);
         this.m_Icons = pIcons;
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private int CalculeWidth(LinearLayoutImpl pIcons)
   {
      int data = this.Data.getCustomView().getWidth();
      int value = 0;
      int nFlags = ActionBar.DISPLAY_USE_LOGO & this.Data.getDisplayOptions();
      int n = pIcons.getWidth();

      switch (nFlags)
      {
         case ActionBar.DISPLAY_USE_LOGO:
            value = this.Params.width - data - 8;
            break;
         default:
            n -= (pIcons.getChildCount() * 8);
            n += ConfigDisplayMetrics.HomeIndicator.x - 8;
            value = (this.Params.width - data) + n;
            break;
      }
      return value;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetIcon(final int nIndex, final int nIconID)
   {
      /* run on main thread address */
      ((Activity) this.getContext()).runOnUiThread(new ChangeStateIcon(nIndex, nIconID));
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetVisible(final int index, final boolean enabled)
   {
      ((Activity) this.getContext()).runOnUiThread(new Runnable()
      {
         @Override
         public void run()
         {
            ImageViewEx p = (ImageViewEx)m_Icons.getChildAt(index);

            if(enabled)
            {
               p.setVisibility(VISIBLE);
            }
            else
            {
               p.setVisibility(INVISIBLE);
            }
         }
      });
   }
}
