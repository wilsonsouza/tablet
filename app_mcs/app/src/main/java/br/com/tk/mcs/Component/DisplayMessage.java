package br.com.tk.mcs.Component;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.text.Html;

import br.com.tk.mcs.Generic.ConfigDisplayMetrics;

/**
 * Created by wilsonsouza on 28/01/17.
 */
public class DisplayMessage extends ProgressDialog
{
   public DisplayMessage(Context pWnd, int nCaption, int nMsg)
   {
      super(pWnd, ConfigDisplayMetrics.DialogMessageStyle);

      this.SetTheme();
      this.setMessage(nMsg);
      this.setCancelable(false);
      this.setIndeterminate(true);
      this.setCaption(nCaption);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public DisplayMessage(Context pWnd, int nCaption, String szMsg)
   {
      super(pWnd, ConfigDisplayMetrics.DialogMessageStyle);

      this.SetTheme();
      this.setMessage(szMsg);
      this.setCancelable(false);
      this.setIndeterminate(true);
      this.setCaption(nCaption);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public DisplayMessage(Context pWnd, String szCaption, String szMsg)
   {
      super(pWnd, ConfigDisplayMetrics.DialogMessageStyle);

      this.SetTheme();
      this.setMessage(szMsg);
      this.setCancelable(false);
      this.setIndeterminate(true);
      this.setCaption(szCaption);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public <T> void setCaption(T caption)
   {
      if(caption.getClass().getName().equals("java.lang.String"))
      {
         if (caption.toString() != null)
         {
            this.setCustomTitle(new TextViewEx(getContext(), caption.toString(), TextViewEx.CAPTION, TextViewEx.DEFCOLOR, true));
         }
      }
      else if(caption.getClass().getName().equals("java.lang.Integer"))
      {
         int id = Integer.parseInt(caption.toString());

         if (id != -1)
         {
            this.setCustomTitle(new TextViewEx(getContext(), id, TextViewEx.CAPTION,TextViewEx.DEFCOLOR, true));
         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @SuppressWarnings("deprecated")
   @Override
   public void setMessage(CharSequence szMsg)
   {
      if(szMsg != null)
      {
         super.setMessage(Html.fromHtml(szMsg.toString()));
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void setMessage(int nID)
   {
      if(nID == -1)
      {
         return;
      }
      super.setMessage(Html.fromHtml(getContext().getString(nID)));
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void show()
   {
      super.show();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private void SetTheme()
   {
      if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
      {
         this.getContext().setTheme(ConfigDisplayMetrics.DialogStyle);
      }
   }
}