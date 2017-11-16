/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import br.com.tk.mcs.Component.ButtonEx;
import br.com.tk.mcs.Component.ChangeHomeIndicator;
import br.com.tk.mcs.Component.CustomerToolBar;
import br.com.tk.mcs.Component.DisplayMessage;
import br.com.tk.mcs.Component.EditTextEx;
import br.com.tk.mcs.Component.ImageViewEx;
import br.com.tk.mcs.Component.LinearHorizontal;
import br.com.tk.mcs.Component.LinearVertical;
import br.com.tk.mcs.Component.MessageBox;
import br.com.tk.mcs.Database.PersistenceController;
import br.com.tk.mcs.Drivers.DetectDeviceType;
import br.com.tk.mcs.Generic.Company;
import br.com.tk.mcs.Generic.ConfigDisplayMetrics;
import br.com.tk.mcs.Generic.DialogTableViewItem;
import br.com.tk.mcs.Generic.PhysicalPersonalCode;
import br.com.tk.mcs.Generic.Utils;
import br.com.tk.mcs.Lane.Lane;
import br.com.tk.mcs.Lane.Operations;
import br.com.tk.mcs.R;
import br.com.tk.mcs.Remote.Response.UserRequestResponse;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
   protected static int LENGTH = 0x1e;
   protected static int MIN_DISPLAY_RESOLUTION_ALLOWED = 0x220;
   //
   protected BuilderManager Manager = null;

   //-----------------------------------------------------------------------------------------------------------------//
   class BuilderMainLayout extends LinearVertical
   {
      public BuilderMainLayout(MainActivity pWnd)
      {
         super(pWnd);
         this.Params = this.Build(MATCH, MATCH);
         this.setGravity(Gravity.CENTER_VERTICAL);
         this.setBackgroundResource(R.drawable.login_ver);
         pWnd.setContentView(this, this.Params);
      }
   }

   //-----------------------------------------------------------------------------------------------------------------//
   class BuilderBodyLayout extends LinearVertical
   {
      public BuilderBodyLayout(MainActivity pWnd, BuilderMainLayout pMain)
      {
         super(pWnd);
         this.Params = this.Build(WRAP, WRAP);
         this.Params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
         pMain.addView(this, this.Params);
      }
   }

   //-----------------------------------------------------------------------------------------------------------------//
   class BuilderWallpaper extends ImageViewEx
   {
      public BuilderWallpaper(MainActivity pWnd, BuilderBodyLayout pBody)
      {
         super(pWnd, ConfigDisplayMetrics.WallpaperOffset, ConfigDisplayMetrics.Wallpaper, false, null, null);
         this.Params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
         /*
         logo of sp 99 tamoios
         this.setImageResource(R.drawable.logotamoios01);
          */
         /*
         logo of cro
          */
         pBody.addView(this, this.Params);
      }
   }

   //-----------------------------------------------------------------------------------------------------------------//
   class BuilderFieldUser extends EditTextEx implements TextWatcher
   {
      public BuilderFieldUser(MainActivity pWnd, BuilderBodyLayout pBody)
      {
         super(pWnd, -1, LENGTH, true, DEFAULT, 350, false);

         super.Params.gravity = Gravity.CENTER;
         this.Data.setHint(R.string.main_user);
         this.Data.setInputType(InputType.TYPE_CLASS_NUMBER);
         this.Data.addTextChangedListener(this);
         pBody.addView(this, this.Params);
      }
      //-----------------------------------------------------------------------------------------------------------------//
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after)
      {

      }
      //-----------------------------------------------------------------------------------------------------------------//
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count)
      {
         Manager.Actions.Clear.setEnabled(s.length() > 0);
      }
      //-----------------------------------------------------------------------------------------------------------------//
      @Override
      public void afterTextChanged(Editable s)
      {

      }
   }

   //-----------------------------------------------------------------------------------------------------------------//
   class BuilderFieldPassword extends EditTextEx implements TextWatcher
   {
      public BuilderFieldPassword(final MainActivity pWnd, BuilderBodyLayout pBody)
      {
         super(pWnd, -1, LENGTH, true, DEFAULT, 350, false);

         this.Params.gravity = Gravity.CENTER;
         this.Data.setHint(R.string.main_pass);
         this.Data.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
         this.Data.addTextChangedListener(this);
         pBody.addView(this, this.Params);
      }
      //-----------------------------------------------------------------------------------------------------------------//
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after)
      {

      }
      //-----------------------------------------------------------------------------------------------------------------//
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count)
      {
         Manager.Actions.Login.setEnabled(s.length() > 0 && Manager.User.GetData().length() > 0);
         Manager.Actions.Clear.setEnabled(s.length() > 0);
      }
      //-----------------------------------------------------------------------------------------------------------------//
      @Override
      public void afterTextChanged(Editable s)
      {

      }
   }

   //-----------------------------------------------------------------------------------------------------------------//
   class BuilderActions extends LinearHorizontal implements View.OnClickListener
   {
      public ButtonEx Login = null;
      public ButtonEx Clear = null;

      //-----------------------------------------------------------------------------------------------------------------//
      public BuilderActions(MainActivity pWnd, BuilderBodyLayout pBody)
      {
         super(pWnd);
         this.Params.gravity = Gravity.CENTER;
         this.SetMargins(new Rect(8, 8, 8, 0x10));

         this.Clear = new ButtonEx(getContext(), R.string.main_clear, true, R.drawable.button_selector, null, null);
         this.Clear.setOnClickListener(this);
         this.Login = new ButtonEx(getContext(), R.string.button_ok, false, R.drawable.button_selector, null, null);
         this.Login.setOnClickListener(pWnd);

         this.Clear.SetIcon(new ButtonEx.IconDetail(R.drawable.button_clear, ButtonEx.LEFT));
         this.Login.SetIcon(new ButtonEx.IconDetail(ButtonEx.IDOK, ButtonEx.LEFT));

         this.addView(this.Login, this.Params);
         this.addView(this.Clear, this.Params);
         pBody.addView(this, this.Params);
      }

      //-----------------------------------------------------------------------------------------------------------------//
      @Override
      public void onClick(View v)
      {
         if (v == Clear.Data)
         {
            ClearFields();
         }
      }
   }

   //-----------------------------------------------------------------------------------------------------------------//
   class LauncherLanes extends Thread implements Runnable
   {
      protected DisplayMessage m_pDlg = null;
      protected String m_szCaption = getString(R.string.main_login);
      protected String m_szText = getString(R.string.main_alert_login_sync);
      protected String m_szDisplay = new String();
      protected MainActivity m_pWnd = null;

      //-----------------------------------------------------------------------------------------------------------------//
      public LauncherLanes(MainActivity pWnd)
      {
         super();
         this.setName(this.getClass().getName());
         this.m_pWnd = pWnd;
         this.m_pDlg = new DisplayMessage(this.m_pWnd, this.m_szCaption, this.m_szText);
      }

      //-----------------------------------------------------------------------------------------------------------------//
      @Override
      public void run()
      {
         try
         {
            int i = 0;
            UserRequestResponse Result = UserRequestResponse.NoAuthorized;
            //
            runOnUiThread(new Runnable()
            {
               @Override
               public void run()
               {
                  m_pDlg.show();
               }
            });
            //
            for (final String szName : Manager.Db.getLanesNames())
            {
               /* put on message on main thread */
               runOnUiThread(new Runnable()
               {
                  @Override
                  public void run()
                  {
                     m_pDlg.setMessage(String.format(m_pWnd.getString(R.string.ids_syncronize), szName));
                  }
               });
               /**/
               try
               {
                  final String szAddress = Manager.Db.getLaneAddressByName(szName);
                  //Operations pOperation = new Operations("http://" + szAddress + ":8000/");
                  final String szUrl = String.format(m_pWnd.getString(R.string.ids_url_lane), szAddress);
                  final Lane pLane = new Lane(i++, szName, m_pWnd.GetUser(), new Operations(szUrl, m_pWnd.GetUser()));
               /**/
                  Log.i(this.getClass().getName(), "Lane Url " + szUrl);
                  Result = pLane.getOperations().userRequest(m_pWnd.GetUser(), m_pWnd.GetPassword());

                  if (Result == UserRequestResponse.Authorized)
                  {
                     break;
                  }
               }
               catch (Exception e)
               {
                  e.printStackTrace();
               }
            }
            //
            runOnUiThread(new Runnable()
            {
               @Override
               public void run()
               {
                  m_pDlg.dismiss();
               }
            });
            //
            if (Result != UserRequestResponse.Authorized)
            {
               if (Result == UserRequestResponse.NoAuthorized)
               {
                  m_szDisplay = getString(R.string.main_alert_login_error);
               }
               else
               {
                  m_szDisplay = getString(R.string.main_alert_login_no_communication);
               }
               //
               if (!m_szDisplay.isEmpty())
               {
                  runOnUiThread(new Runnable()
                  {
                     protected String m_caption = getString(R.string.ids_warning);

                     @Override
                     public void run()
                     {
                        new MessageBox(m_pWnd, this.m_caption, m_szDisplay, MessageBox.IDWARNING);
                     }
                  });
               }
            }
            else
            {
               Intent pWnd = new Intent(m_pWnd, ManageLanes.class); //ManagerActivity.class);
               {
                  //pWnd.putExtra(Utils.LANE, m_pLanes.toArray());
                  pWnd.putExtra(Utils.OPERATOR, m_pWnd.GetUser());
                  startActivity(pWnd);
               }
            }
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }

      //-----------------------------------------------------------------------------------------------------------------//
      public void start()
      {
         Log.i(this.getClass().getName(), getState().toString() + " isAlive " + this.isAlive() + ", " + this.isDaemon());
         if (!this.isAlive())
         {
            Log.i(this.getClass().getName(), "Starting...");
            super.start();
         }
      }
   }

   //-----------------------------------------------------------------------------------------------------------------//
   class BuilderManager implements View.OnClickListener
   {
      public BuilderMainLayout Main = null;
      public BuilderBodyLayout Body = null;
      public BuilderWallpaper Wallpaper = null;
      public BuilderFieldUser User = null;
      public BuilderFieldPassword Password = null;
      public BuilderActions Actions = null;
      public PersistenceController Db = null;

      //-----------------------------------------------------------------------------------------------------------------//
      public BuilderManager(MainActivity pWnd)
      {
         super();
         this.Db = new PersistenceController(pWnd);
         this.Main = new BuilderMainLayout(pWnd);
         this.Body = new BuilderBodyLayout(pWnd, this.Main);
         this.Wallpaper = new BuilderWallpaper(pWnd, this.Body);
         this.User = new BuilderFieldUser(pWnd, this.Body);
         this.Password = new BuilderFieldPassword(pWnd, this.Body);
         this.Actions = new BuilderActions(pWnd, this.Body);
         /* put logo */
         new ChangeHomeIndicator(pWnd, R.drawable.ic_company_web);
         new CustomerToolBar(pWnd, R.string.app_name).SetIcons(new int[]{R.drawable.cctv_camera_icon}, this);
         /* recalcule all components on screen */
         this.Main.Invalidate(true);
      }
      //-----------------------------------------------------------------------------------------------------------------//
      @Override
      public void onClick(View v)
      {
         int nId = v.getId();

         switch (nId)
         {
            case R.drawable.cctv_camera_icon:
               break;
         }
      }
   }

   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      /* put on my customer toolbar */
      //this.setTheme(R.style.AppTheme_NoActionBar);
      super.onCreate(savedInstanceState);
      /* define screen orientation */
      this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

      /* set density and display metrics */
      ConfigDisplayMetrics.Builder(this);
      /* create all controls on screen */
      this.Manager = new BuilderManager(this);
      Utils.hideSoftKeyboardOnCreate(MainActivity.this);

      /* detect that is the device type */
      if (!DetectDeviceType.IsTablet(this) || this.Manager.Main.Params.height < MIN_DISPLAY_RESOLUTION_ALLOWED)
      {
         int nMessageID = R.string.ids_device_not_supported;
         new MessageBox(this, R.string.ids_device_error, nMessageID, MessageBox.IDERROR, new View.OnClickListener()
         {
            @Override
            public void onClick(View view)
            {
               Log.e(this.getClass().getName(), getString(R.string.ids_device_not_supported));
               finish();
            }
         });
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   protected void onResume()
   {
      super.onResume();
   }

   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   protected void onDestroy()
   {
      super.onDestroy();
   }

   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(View v)
   {
      Utils.hideSoftKeyboard(MainActivity.this);

      if (this.GetUser().equals(Utils.ROOT) && this.GetPassword().equals(Utils.ROOT))
      {
         Intent pWnd = new Intent(this, ConfigurationActivity.class);
         startActivity(pWnd);
         return;
      }
      else
      {
         LauncherLanes pLauncher = new LauncherLanes(this);
         /* test new manager lanes window */
         if (this.GetUser().equals("1") && this.GetPassword().equals("2016"))
         {
            Intent pWnd = new Intent(this, ManageLanes.class);
            Manager.Password.Data.setText("00001");
            Company.IsDebug = true;
            pWnd.putExtra(Utils.OPERATOR, this.GetUser());
            startActivity(pWnd);
            return;
         }
         /* test dialog view item */
         if (this.GetUser().equals("3") && this.GetPassword().equals("2016"))
         {
            new DialogTableViewItem(MainActivity.this, null, null, null, null);
            return;
         }
         /* verify if physical personal code VerifyPersonalPhysicalCode*/
         if (this.GetUser().length() >= 0xb)
         {
            if (!PhysicalPersonalCode.IsOK(this.GetUser()))
            {
               new MessageBox(this, R.string.ids_warning, R.string.invalid_cpf, MessageBox.IDWARNING);
               return;
            }
         }
         /* verify if exists any lane registered */
         if (this.Manager.Db.getLanesCount() == 0)
         {
            new MessageBox(this, R.string.ids_warning, R.string.main_alert_login_no_lane, MessageBox.IDWARNING);
            return;
         }
         /* pass to connection with land and validation of user */
         pLauncher.start();
      }
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public String GetUser()
   {
      return this.Manager.User.GetData().trim();
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public String GetPassword()
   {
      return this.Manager.Password.GetData().trim();
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public void ClearFields()
   {
      this.Manager.Password.Data.getText().clear();
      this.Manager.User.Data.getText().clear();
      this.Manager.User.Data.requestFocus();
   }
}