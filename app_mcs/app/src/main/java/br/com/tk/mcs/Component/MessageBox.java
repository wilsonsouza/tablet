/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Component;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import br.com.tk.mcs.Generic.ConfigDisplayMetrics;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 27/01/17.
 */

public class MessageBox extends Dialog implements View.OnClickListener
{
   public static int IDERROR = R.drawable.box_error;
   public static int IDOK = R.drawable.box_ok;
   public static int IDWARNING = R.drawable.box_warning;
   public static int IDQUESTION = R.drawable.box_question;
   private int[] m_buttons = new int[]{R.string.button_ok};

   public static final int LM_VERTICAL = 0x100;
   public static final int LM_HORIZONTAL = 0x101;
   public static final int LM_GRID = 0x102;
   public static final int LM_RELATION = 0x103;
   public static final int LM_FRAME = 0x104;
   public static final int LM_TABLE = 0x105;
   public static final int LM_WITHOUT = 0x200;
   protected static Point m_iconoffset = ConfigDisplayMetrics.DialogIcon;
   protected ViewGroup View = null;
   private LinearVertical m_body = null;
    //-----------------------------------------------------------------------------------------------------------------//
   public MessageBox(Context pWnd, int nID, int nMsg, int nIconID)
   {
      super(pWnd, ConfigDisplayMetrics.DialogStyle);
      this.SetLayoutMode(LM_HORIZONTAL);
      this.SetIcon(nIconID);
      this.SetMessage(nMsg);
      this.SetButtons(this.m_buttons);
      this.setTitle(nID);
      this.Create();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public MessageBox(Context pWnd, int nID, int nMsg, int nIconID, View.OnClickListener pfnClick)
   {
      super(pWnd, ConfigDisplayMetrics.DialogStyle);
      this.SetLayoutMode(LM_HORIZONTAL);
      this.SetIcon(nIconID);
      this.SetMessage(nMsg);
      this.SetButtons(this.m_buttons);
      this.setTitle(nID);
      this.GetControlById(this.m_buttons[0]).setOnClickListener(pfnClick);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public MessageBox(Context pWnd, String szCaption, String szMessage, int nIconID)
   {
      super(pWnd, ConfigDisplayMetrics.DialogStyle);
      this.SetLayoutMode(LM_HORIZONTAL);
      this.SetIcon(nIconID);
      this.SetMessage(szMessage);
      this.SetButtons(this.m_buttons);
      this.setTitle(szCaption);
      this.Create();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public MessageBox(Context pWnd, int nID, String szMessage, int nIconID)
   {
      super(pWnd, ConfigDisplayMetrics.DialogStyle);
      this.SetLayoutMode(LM_HORIZONTAL);
      this.SetIcon(nIconID);
      this.SetMessage(szMessage);
      this.SetButtons(this.m_buttons);
      this.setTitle(nID);
      this.Create();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public MessageBox(Context pWnd, int nID, Exception e, int nIconID)
   {
      super(pWnd, ConfigDisplayMetrics.DialogStyle);
      this.SetLayoutMode(LM_HORIZONTAL);
      this.SetIcon(nIconID);
      this.SetMessage(e.getMessage());
      this.SetButtons(this.m_buttons);
      this.setTitle(nID);
      this.Create();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public MessageBox(Context pWnd, String szCaption, Exception e, int nIconID)
   {
      super(pWnd, ConfigDisplayMetrics.DialogStyle);
      this.SetLayoutMode(LM_HORIZONTAL);
      this.SetIcon(nIconID);
      this.SetMessage(e.getMessage());
      this.SetButtons(this.m_buttons);
      this.setTitle(szCaption);
      this.Create();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onCreate(Bundle pSavedInstanceState)
   {
      super.onCreate(pSavedInstanceState);
      this.getWindow().setBackgroundDrawableResource(R.drawable.button_selector);
      this.setCancelable(false);
      this.setCanceledOnTouchOutside(false);
      this.setContentView(this.m_body, this.m_body.Params);
      this.m_body.SetMargins(new Rect(0, 8, 0, 8));
      this.m_body.SetBorder(true);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private MessageBox SetLayoutMode(int nLayoutMode)
   {
      this.m_body = new LinearVertical(getContext());

      switch (nLayoutMode)
      {
         case LM_FRAME:
            break;
         case LM_GRID:
            break;
         case LM_HORIZONTAL:
            this.View = new LinearHorizontal(this.getContext());
            this.m_body.addView(this.View);
            break;
         case LM_RELATION:
            break;
         case LM_TABLE:
            break;
         case LM_VERTICAL:
            this.View = new LinearVertical(this.getContext());
            this.m_body.addView(this.View);
            break;
      }
      return this;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetMessage(String szMessage)
   {
      TextViewEx pMessage = new TextViewEx(getContext(), szMessage, TextViewEx.DEFAULT, new int[]{0,0}, false);
      {
         pMessage.SetMargins(new Rect(8, 8, 8, 8));
         pMessage.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
      }
      View.addView(pMessage, pMessage.Params);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetMessage(int nID)
   {
      TextViewEx pMessage = new TextViewEx(getContext(), nID, TextViewEx.DEFAULT, new int[]{0,0}, false);
      {
         pMessage.SetMargins(new Rect(8, 8, 8, 8));
         pMessage.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
      }
      View.addView(pMessage, pMessage.Params);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void Create()
   {
      UpdateContent.Refresh(this.m_body, true);
      this.show();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetButtons(int[] aButtons)
   {
      LinearHorizontal p = new LinearHorizontal(getContext());
      /**/
      for(int nData: aButtons)
      {
         ButtonEx b = new ButtonEx(getContext(), nData, false, R.drawable.button_selector, null, null);
         /**/
         b.setEnabled(true);
         b.setOnClickListener(this);
         b.SetMargins(new Rect(8, 0, 8, 0));
         p.addView(b);
      }
      /* put horizontal layout on main view */
      p.Params.gravity = Gravity.CENTER;
      p.SetMargins(new Rect(8, 8, 8, 8));
      this.m_body.addView(p);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @TargetApi(Build.VERSION_CODES.KITKAT)
   protected void SetIcon(int nIconID)
   {
      ImageViewEx icon = new ImageViewEx(getContext(), m_iconoffset, nIconID, false, null, null);
      {
         icon.setGravity(Gravity.CENTER);
         icon.SetMargins(new Rect(8, 8, 8, 8));
         View.addView(icon, 0, icon.Params);
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(android.view.View v)
   {
      if(v.getId() == this.m_buttons[0])
      {
         this.dismiss();
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public View GetControlById(final int nID)
   {
      return this.m_body.findViewById(nID);
   }
}
