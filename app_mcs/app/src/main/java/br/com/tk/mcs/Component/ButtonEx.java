package br.com.tk.mcs.Component;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.widget.Button;

import br.com.tk.mcs.Generic.ConfigDisplayMetrics;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 09/02/17.
 */

public class ButtonEx extends LinearHorizontal implements View.OnClickListener
{
   public Button Data = null;
   public final static int DEFAULT_RESOURCE = -1;
   public final static int IDOK = 0xf901;
   public final static int IDCANCEL = 0x765f;
   public final static int LEFT = 0xff01;
   public final static int RIGHT = 0xa001;
   public final static int UP = 0x9871;
   public final static int DOWN = 0x87f0;

   //-----------------------------------------------------------------------------------------------------------------//
   public static class IconDetail
   {
      public int ID = LEFT;
      public int IconID = IDOK;

      public IconDetail(final int nIconId, final int nId)
      {
         this.ID = nId;
         this.IconID = nIconId;
      }
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public ButtonEx(Context pWnd, int nID, boolean bEnabled, int nBackgroundResource, Rect padding, Rect margins)
   {
      super(pWnd, null, ConfigDisplayMetrics.ButtonStyle);
      this.Data = new Button(this.getContext());
      this.Data.setText(nID);
      this.Data.setId(nID);
      this.setId(nID);
      this.setEnabled(bEnabled);
      this.setBackgroundResource(nBackgroundResource);
      this.SetPadding(padding);
      this.SetMargins(margins);
      this.addView(this.Data, this.Params);
      this.setEnabled(false);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public ButtonEx(Context pWnd, String szCaption, boolean bEnabled, int nBackgroundResource, Rect padding, Rect margins)
   {
      super(pWnd, null, ConfigDisplayMetrics.ButtonStyle);
      this.Data = new Button(this.getContext());
      this.Data.setText(szCaption);
      this.setEnabled(bEnabled);
      this.setBackgroundResource(nBackgroundResource);
      this.SetPadding(padding);
      this.SetMargins(margins);
      this.addView(this.Data, this.Params);
      this.setEnabled(false);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void setEnabled(boolean bEnabled)
   {
      this.Data.setEnabled(bEnabled);
      super.setEnabled(bEnabled);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(View v)
   {

   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void setOnClickListener(View.OnClickListener pfnClick)
   {
      this.Data.setOnClickListener(pfnClick);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void setBackgroundResource(int nResourceId)
   {
      if (nResourceId != DEFAULT_RESOURCE)
      {
         this.Data.setBackgroundResource(nResourceId);
         super.setBackgroundResource(nResourceId);
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetIcon(ButtonEx.IconDetail pIconParam)
   {
      ImageViewEx pIcon = new ImageViewEx(this.getContext(), new android.graphics.Point(0x20, 0x20), -1, false, null, null);

      switch (pIconParam.IconID)
      {
         case IDOK:
            pIcon.SetImage(R.drawable.button_ok_3d);
            break;
         case IDCANCEL:
            pIcon.SetImage(R.drawable.button_cancel_3d);
            break;
         default:
            pIcon.SetImage(pIconParam.IconID);
            break;
      }

      switch (pIconParam.ID)
      {
         case LEFT:
            this.Data.setCompoundDrawables(pIcon.ToDrawable(), null, null, null);
            break;
         case DOWN:
            this.Data.setCompoundDrawables(null, null, null, pIcon.ToDrawable());
            break;
         case RIGHT:
            this.Data.setCompoundDrawables(null, null, pIcon.ToDrawable(), null);
            break;
         case UP:
            this.Data.setCompoundDrawables(null, pIcon.ToDrawable(), null, null);
            break;
      }
   }
}
