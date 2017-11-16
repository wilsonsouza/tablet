/*

   Sistema de Gestão de Pistas

   (C) 2016 TecSidel

   Created by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import br.com.tk.mcs.R;

/**
 * Created by wilsonsouza on 31/01/17.
 */

public class ScannerBarcode extends LinearHorizontal implements View.OnClickListener
{
   public  SurfaceView Data = null; //data
   private CameraSource m_camera = null;
   private BarcodeDetector m_barcode = null;
   private OnScannerBarcodeListener m_dispatch = null;
   //-----------------------------------------------------------------------------------------------------------------//
   public interface OnScannerBarcodeListener
   {
      public void OnHasData(SparseArray<Barcode> pData);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   class CameraSourcePictureCallback implements CameraSource.PictureCallback
   {
      @Override
      public void onPictureTaken(byte[] bytes)
      {
         try
         {
            final Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            final Frame f = new Frame.Builder().setBitmap(bmp).build();
            final SparseArray<Barcode> pData = m_barcode.detect(f);

            if (m_dispatch != null && pData.size() > 0)
            {
               new Thread(new Runnable()
               {
                  @Override
                  public void run()
                  {
                     m_dispatch.OnHasData(pData);
                  }
               }).start();
            }
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   class SurfaceHolderCallback implements SurfaceHolder.Callback
   {
      @Override
      public void surfaceCreated(SurfaceHolder holder)
      {
         try
         {
            m_camera.start(Data.getHolder());
         }
         catch (SecurityException | IOException e)
         {
            e.printStackTrace();
         }
      }
      @Override
      public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
      {

      }
      @Override
      public void surfaceDestroyed(SurfaceHolder holder)
      {
         try
         {
            m_camera.stop();
            m_camera.release();
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   class DetectorProcessor implements Detector.Processor<Barcode>
   {
      @Override
      public void release()
      {

      }
      @Override
      public void receiveDetections(Detector.Detections<Barcode> detections)
      {
         final SparseArray<Barcode> pQueue = detections.getDetectedItems();

         if(pQueue.size() > 0 && m_dispatch != null)
         {
            new Thread(new Runnable()
            {
               @Override
               public void run()
               {
                  m_dispatch.OnHasData(pQueue);
               }
            }).start();
         }
      }
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public ScannerBarcode(Context context, Point area, boolean bBorder)
   {
      super(context);
      this.Data = new SurfaceView(getContext());
      this.Data.setMinimumWidth(area.x);
      this.Data.setMinimumHeight(area.y);
      this.Create(area);
      this.addView(this.Data, this.Params);
      this.SetBorder(bBorder);
      this.Invalidate(true);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private void Create(Point p)
   {
      LinearHorizontal area = new LinearHorizontal(getContext());
      //use zero (0) to all formats supported by system.
      //final int nCode = Barcode.ALL_FORMATS;
      this.m_barcode = new BarcodeDetector.Builder(getContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build();

      if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
      {
         if (!this.m_barcode.isOperational())
         {
            new MessageBox(getContext(), R.string.ids_warning, R.string.scanner_is_not_ok, MessageBox.IDWARNING);
            return;
         }
      }
      //prepare all methods set camera area and frames per seconds
      Point point = new Point(area.Params.Display.widthPixels, area.Params.Display.heightPixels);
      {
         this.m_camera = new CameraSource.Builder(getContext(), m_barcode).
            setAutoFocusEnabled(true).
            setFacing(CameraSource.CAMERA_FACING_BACK).
            setRequestedPreviewSize(point.x, point.y).
            build();
      }
      this.Data.getHolder().addCallback(new SurfaceHolderCallback());
      this.Data.getHolder().setFixedSize(p.x, p.y);
      this.m_barcode.setProcessor(new DetectorProcessor());
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetOnScannerBarcodeListener(ScannerBarcode.OnScannerBarcodeListener pDispatch)
   {
      this.m_dispatch = pDispatch;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void onClick(View v)
   {
      if(v == this.Data)
      {
         this.m_camera.takePicture(null, new CameraSourcePictureCallback());
      }
   }
}
