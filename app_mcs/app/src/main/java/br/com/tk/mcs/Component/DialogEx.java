/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Component;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import br.com.tk.mcs.Generic.ConfigDisplayMetrics;
import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 27/01/17.
 */

public class DialogEx extends Dialog implements View.OnClickListener
{
   public static final int LM_VERTICAL = 0x100;
   public static final int LM_HORIZONTAL = 0x101;
   public static final int LM_GRID = 0x102;
   public static final int LM_RELATION = 0x103;
   public static final int LM_FRAME = 0x104;
   public static final int LM_TABLE = 0x105;
   public static final int LM_WITHOUT = 0x200;
   protected static Point m_iconoffset = ConfigDisplayMetrics.DialogIcon;
   protected ViewGroup View = null;
   protected Activity m_pWnd = null;
   private LinearVertical m_body = null;
   //-----------------------------------------------------------------------------------------------------------------//
   public interface IRunnable
   {
      void run(int nWhich);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public DialogEx(Context pWnd, int nID, int nLayoutMode)
   {
      super(pWnd, ConfigDisplayMetrics.DialogStyle);
      this.m_pWnd = (Activity)pWnd;
      this.m_body = new LinearVertical(getContext());
      this.SetLayoutMode(nLayoutMode);
      this.setTitle(nID);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public DialogEx(Context pWnd, String szData, int nLayoutMode)
   {
      super(pWnd, ConfigDisplayMetrics.DialogStyle);
      this.m_pWnd = (Activity)pWnd;
      this.m_body = new LinearVertical(getContext());
      this.SetLayoutMode(nLayoutMode);
      this.setTitle(szData);
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
   @Override
   protected void onStart()
   {
      super.onStart();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   protected void onStop()
   {
      super.onStop();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private DialogEx SetLayoutMode(int nLayoutMode)
   {
      switch (nLayoutMode)
      {
         case LM_FRAME:
            break;
         case LM_GRID:
            break;
         case LM_HORIZONTAL:
            this.View = new LinearHorizontal(this.getContext());
            this.m_body.addView(this.View, 0);
            break;
         case LM_RELATION:
            break;
         case LM_TABLE:
            break;
         case LM_VERTICAL:
            this.View = new LinearVertical(this.getContext());
            this.m_body.addView(this.View, 0);
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
   public void Create() throws Exception
   {
       if(this.View != null)
      {
         UpdateContent.Refresh(this.m_body, true);
         this.show();
      }
      else
      {
         throw new Exception("Object DialogEx.View is null, operation aborted!");
      }
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
         if(nData == R.string.button_cancel || nData == R.string.manager_button_cancel)
         {
            b.setEnabled(true);
         }

         b.setOnClickListener(this);
         b.SetMargins(new Rect(8, 0, 8, 0));
         p.addView(b);
      }
      /* put horizontal layout on main view */
      p.Params.gravity = Gravity.CENTER;
      p.SetMargins(new Rect(8, 8, 8, 8));
      this.m_body.addView(p, 1);
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
   public void onClick(View v)
   {
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetKeyboard(boolean bEnabled)
   {
      if(bEnabled)
      {
         this.m_pWnd.runOnUiThread(new Runnable()
         {
            @Override
            public void run()
            {
               m_pWnd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
         });
      }
      else
      {
         this.m_pWnd.runOnUiThread(new Runnable()
         {
            @Override
            public void run()
            {
               m_pWnd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
         });
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetKeyboard(boolean bEnabled, View view)
   {
      this.SetKeyboard(bEnabled);

      if(bEnabled)
      {
         InputMethodManager imm = (InputMethodManager)this.m_pWnd.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
         {
            imm.showSoftInput(view, 0);
         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void ShowMsgBox(final int nCaption, final int nMessage, final int nKind)
   {
      this.m_pWnd.runOnUiThread(new Runnable()
      {
         @Override
         public void run()
         {
            new MessageBox(getContext(), nCaption, nMessage, nKind);
         }
      });
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void ShowMsgBox(final String szCaption, final String szMessage, final int nKind)
   {
      this.m_pWnd.runOnUiThread(new Runnable()
      {
         @Override
         public void run()
         {
            new MessageBox(getContext(), szCaption, szMessage, nKind);
         }
      });
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void ShowMsgBox(final int nCaption, final String szMessage, final int nKind)
   {
      this.m_pWnd.runOnUiThread(new Runnable()
      {
         @Override
         public void run()
         {
            new MessageBox(getContext(), nCaption, szMessage, nKind);
         }
      });
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void CloseDialog(final Dialog dialog)
   {
      this.m_pWnd.runOnUiThread(new Runnable()
      {
         @Override
         public void run()
         {
            dialog.dismiss();
         }
      });
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void ShowDialog(final Dialog dialog)
   {
      this.m_pWnd.runOnUiThread(new Runnable()
      {
         @Override
         public void run()
         {
            dialog.show();
         }
      });
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetMessageTask(final DisplayMessage dialog, final String szMsg)
   {
      this.m_pWnd.runOnUiThread(new Runnable()
      {
         @Override
         public void run()
         {
            dialog.setMessage(Html.fromHtml(szMsg));
         }
      });
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetMessageTask(final DisplayMessage dialog, final int nMsg)
   {
      this.m_pWnd.runOnUiThread(new Runnable()
      {
         @Override
         public void run()
         {
            dialog.setMessage(nMsg);
         }
      });
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public View GetControlById(final int nId)
   {
      return this.m_body.findViewById(nId);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetControlById(final int nId, final boolean bEnabled)
   {
      View p = this.m_body.findViewById(nId);

      if(p != null)
      {
         p.setEnabled(bEnabled);
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void dismiss()
   {
      this.SetKeyboard(false);
      super.dismiss();
   }
}
